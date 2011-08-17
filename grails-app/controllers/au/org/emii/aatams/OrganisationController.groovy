package au.org.emii.aatams

import org.apache.shiro.SecurityUtils
import org.codehaus.groovy.grails.commons.GrailsApplication
import grails.util.GrailsUtil

class OrganisationController
{
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = 
    {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        
        def organisationTotal = Organisation.count()
        def organisationList = Organisation.list(params)
        
        if (!SecurityUtils.getSubject().hasRole("SysAdmin"))
        {
            // Filter out non-ACTIVE organisations (only sys admin should see these).
            organisationList = organisationList.grep
            {
                return (it.status == EntityStatus.ACTIVE)
            }
            
            // Only count ACTIVE organisations.
            // TODO: why doesn't .count({}) work here?
            organisationTotal = 0
            Organisation.list().each
            {
                if (it.status == EntityStatus.ACTIVE)
                {
                    organisationTotal++
                }
            }
//            organisationTotal = 
//                Organisation.list().count
//                {
//                    it.status == EntityStatus.ACTIVE
//                }
        }
        
        [organisationInstanceList: organisationList, organisationInstanceTotal: organisationTotal]
    }

    def create = {
        def organisationInstance = new Organisation()
        organisationInstance.properties = params
        return [organisationInstance: organisationInstance]
    }

    def save = 
    {
        def streetAddress = new Address(params['streetAddress'])
        def postalAddress = new Address(params['postalAddress'])

        def organisationInstance = 
            new Organisation(params['organisation'])
        organisationInstance.streetAddress = streetAddress
        organisationInstance.postalAddress = postalAddress
        
        // If SysAdmin, then set Organisation's status to ACTIVE, otherwise,
        // set to PENDING.
        println("Principal: " + SecurityUtils.getSubject().getPrincipal())
        Person user = Person.findByUsername(SecurityUtils.getSubject().getPrincipal())
        organisationInstance.requestingUser = user
        
        if (SecurityUtils.getSubject().hasRole("SysAdmin"))
        {
            organisationInstance.status = EntityStatus.ACTIVE
        }
        else
        {
            organisationInstance.status = EntityStatus.PENDING
        }
        
        if (organisationInstance.save(flush: true)) 
        {
            if (SecurityUtils.getSubject().hasRole("SysAdmin"))
            {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'organisation.label', default: 'Organisation'), organisationInstance.id])}"
            }
            else
            {
                println "Organisation created by non-SysAdmin, id: " + organisationInstance.id
                sendCreationNotificationEmails(organisationInstance)
                flash.message = "${message(code: 'default.requested.message', args: [message(code: 'organisation.label', default: 'Organisation'), organisationInstance.id])}"
            }
            redirect(action: "show", id: organisationInstance.id)
        }
        else {
            println(organisationInstance.errors)
            render(view: "create", model: [organisationInstance: organisationInstance])
        }
    }

    def show = {
        def organisationInstance = Organisation.get(params.id)
        if (!organisationInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organisation.label', default: 'Organisation'), params.id])}"
            redirect(action: "list")
        }
        else {
            [organisationInstance: organisationInstance]
        }
    }

    def edit = {
        def organisationInstance = Organisation.get(params.id)
        if (!organisationInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organisation.label', default: 'Organisation'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [organisationInstance: organisationInstance]
        }
    }

    def update = {
        def organisationInstance = Organisation.get(params.id)
        
        boolean prevPending = (organisationInstance.status == EntityStatus.PENDING)
        
        if (organisationInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (organisationInstance.version > version) {
                    
                    organisationInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'organisation.label', default: 'Organisation')] as Object[], "Another user has updated this Organisation while you were editing")
                    render(view: "edit", model: [organisationInstance: organisationInstance])
                    return
                }
            }
            organisationInstance.properties = params
            if (!organisationInstance.hasErrors() && organisationInstance.save(flush: true)) {
                // Notify organisation activated.
                if (prevPending && (organisationInstance.status == EntityStatus.ACTIVE))
                {
                    sendActivatedNotificationEmails(organisationInstance)
                }
                
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'organisation.label', default: 'Organisation'), organisationInstance.id])}"
                redirect(action: "show", id: organisationInstance.id)
            }
            else {
                render(view: "edit", model: [organisationInstance: organisationInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organisation.label', default: 'Organisation'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def organisationInstance = Organisation.get(params.id)
        if (organisationInstance) {
            try {
                organisationInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'organisation.label', default: 'Organisation'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'organisation.label', default: 'Organisation'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organisation.label', default: 'Organisation'), params.id])}"
            redirect(action: "list")
        }
    }
    
    /**
     * Notification emails are sent when an organisation is created (by a non-
     * sys admin) to:
     *
     *  - the requesting user
     *  - AATAMS sys admins.
     */
    def sendCreationNotificationEmails(organisation)
    {
        if (!GrailsUtil.getEnvironment().equals(GrailsApplication.ENV_TEST))
        {
            sendMail 
            {  
                to organisation?.requestingUser?.emailAddress
                bcc grailsApplication.config.grails.mail.adminEmailAddress
                from grailsApplication.config.grails.mail.systemEmailAddress
                subject "${message(code: 'mail.request.organisation.create.subject', args: [organisation.name])}"     
                body "${message(code: 'mail.request.organisation.create.body', args: [organisation.name, createLink(action:'show', id:organisation.id, absolute:true)])}" 
            }
        }
    }
    
    def sendActivatedNotificationEmails(organisation)
    {
        if (!GrailsUtil.getEnvironment().equals(GrailsApplication.ENV_TEST))
        {
            sendMail 
            {     
                to organisation?.requestingUser?.emailAddress
                bcc grailsApplication.config.grails.mail.adminEmailAddress
                from grailsApplication.config.grails.mail.systemEmailAddress
                subject "${message(code: 'mail.request.organisation.activate.subject', args: [organisation.name])}"     
                body "${message(code: 'mail.request.organisation.activate.body', args: [organisation.name, createLink(action:'show', id:organisation.id, absolute:true)])}" 
            }
        }
    }
}
