package au.org.emii.aatams

import org.apache.shiro.SecurityUtils
import org.apache.shiro.crypto.hash.Sha256Hash

import org.joda.time.DateTimeZone

class PersonController {

    def permissionUtilsService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST", updatePassword: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)

        def personTotal = Person.count()
        def personList = Person.list(params)


        if (!SecurityUtils.getSubject().hasRole("SysAdmin")) {
            // Filter out non-ACTIVE people (only sys admin should see these).
            personTotal = Person.findAllByStatus(EntityStatus.ACTIVE).size();
            personList = Person.findAllByStatus(EntityStatus.ACTIVE, params);
        }

        [personInstanceList: personList, personInstanceTotal: personTotal]
    }

    def create = {
        def personInstance = new Person()
        personInstance.properties = params
        return [personInstance: personInstance]
    }

    def save = {
        PersonCreateCommand createPersonCmd ->

        if (createPersonCmd.validate()) {
            def personInstance = createPersonCmd.createPerson()

            // If a PI then set Person's status to ACTIVE, otherwise,
            // set to PENDING.
            // (Use "personWriteAny" permission for now, as my understanding
            // of shiro permission wildcards was back-to-front, so the commented
            // out way of doing things doesn't work.
            if (   !SecurityUtils.getSubject().isAuthenticated()
                || !SecurityUtils.getSubject().isPermitted(permissionUtilsService.buildPersonWriteAnyPermission())) {
                personInstance.status = EntityStatus.PENDING
            }
            else {
                personInstance.status = EntityStatus.ACTIVE
            }

            if (createPersonCmd.unlistedOrganisationName) {
                personInstance.organisation = createUnlistedOrganisation(createPersonCmd, personInstance)
            }

            assert(personInstance.organisation)
            personInstance.organisation.addToPeople(personInstance)

            if (personInstance.organisation.save(flush: true)) {
                if (createPersonCmd.unlistedOrganisationName) {
                    sendMail {
                        to grailsApplication.config.grails.mail.adminEmailAddress
                        from grailsApplication.config.grails.mail.systemEmailAddress
                        subject "${message(code: 'mail.unlisted.organisation.create.subject', args: [personInstance.organisation.name])}"
                        body "${message(code: 'mail.unlisted.organisation.create.body', args: [personInstance.organisation.name, createLink(controller:'organisation', action:'show', id:personInstance.organisation.id, absolute:true)])}"
                    }
                }

                if (   !SecurityUtils.getSubject().isAuthenticated()
                    || !SecurityUtils.getSubject().isPermitted(permissionUtilsService.buildPersonWriteAnyPermission())) {
                    if (personInstance.status == EntityStatus.PENDING) {
                        sendCreationNotificationEmails(personInstance)
                    }

                    flash.message = "${message(code: 'default.requested.message', args: [message(code: 'person.label', default: 'Person'), personInstance.toString()])}"
                }
                else {
                    flash.message = "${message(code: 'default.created.message', args: [message(code: 'person.label', default: 'Person'), personInstance.toString()])}"
                }
                redirect(action: "show", id: personInstance.id)
            }
            else {
                render(view: "create", model: [createPersonCmd:createPersonCmd, organisation: personInstance.organisation])
            }
        }
        else {
            render(view: "create", model: [createPersonCmd:createPersonCmd])
        }
    }

    def createUnlistedOrganisation(createPersonCommand, requester) {
        String name = createPersonCommand.unlistedOrganisationName
        log.debug("Creating unlisted organisation, name: " + name)

        def addressParams =
            [streetAddress:name,
             suburbTown:name,
             state:name,
             postcode:name,
             country:name]
        Address unlistedPostalAddress = new Address(addressParams).save()
        Address unlistedStreetAddress = new Address(addressParams).save()

        Request request = new Request(requester:requester)
        requester.addToRequests(request)
        return new Organisation(name:createPersonCommand.unlistedOrganisationName,
                             department:name,
                             phoneNumber:name,
                             faxNumber:name,
                             streetAddress:unlistedStreetAddress,
                             postalAddress:unlistedPostalAddress,
                             request:request,
                             status:EntityStatus.PENDING)
    }

    def show = {
        def personInstance = Person.get(params.id)
        if (!personInstance) {
            log.debug("Person not found, id: " + params.id)

            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
            redirect(action: "list")
        }
        else {
            log.debug("Person found, id: " + params.id)

            def canEdit = false

            if (!SecurityUtils.subject.isAuthenticated()) {
                // canEdit = false
            }
            else if (SecurityUtils.subject.hasRole("SysAdmin")) {
                canEdit = true
            }
            // A person can edit their own record
            else if (personInstance.id == SecurityUtils.subject.principal) {
                canEdit = true
            }
            else {
                // Determine if the subject is a PI on any of this person's projects.
                for (ProjectRole personInstanceRole : personInstance.projectRoles) {
                    Project project = personInstanceRole?.project

                    // If the principal is a PI on this project, then they can edit.
                    if (SecurityUtils.subject.isPermitted(permissionUtilsService.buildPrincipalInvestigatorPermission(project?.id))) {
                        canEdit = true
                    }
                }
            }

            [personInstance: personInstance, canEdit:canEdit]
        }
    }

    def edit = {
        def personInstance = Person.get(params.id)
        if (!personInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [personInstance: personInstance]
        }
    }

    def update = {
        def personInstance = Person.get(params.id)
        boolean prevPending = (personInstance.status == EntityStatus.PENDING)

        if (personInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (personInstance.version > version) {

                    personInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'person.label', default: 'Person')] as Object[], "Another user has updated this Person while you were editing")
                    render(view: "edit", model: [personInstance: personInstance])
                    return
                }
            }

            params.username = params.username.toLowerCase()

            personInstance.properties = params
            if (!personInstance.hasErrors() && personInstance.save(flush: true)) {
                // Notify organisation activated.
                if (prevPending && (personInstance.status == EntityStatus.ACTIVE)) {
                    sendActivatedNotificationEmails(personInstance)
                }

                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'person.label', default: 'Person'), personInstance.toString()])}"
                redirect(action: "show", id: personInstance.id)
            }
            else {
                render(view: "edit", model: [personInstance: personInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
            redirect(action: "list")
        }
    }

    def updatePassword = {
        PersonUpdatePasswordCommand updatePwdCmd ->

        if (   updatePwdCmd.validate()
            && updatePwdCmd.updatePassword()) {
            flash.message = "${message(code: 'person.password.update', default: 'Password changed')}"
            redirect(action: "show", id: updatePwdCmd.id)
        }
        else {
            def personInstance = Person.get(updatePwdCmd.id)
            if (!personInstance) {
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
                redirect(action: "list")
            }
            else {
                return [personInstance: personInstance]
            }
        }

    }
    def delete = {
        def personInstance = Person.get(params.id)
        if (personInstance) {
            try {
                personInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'person.label', default: 'Person'), personInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'person.label', default: 'Person'), personInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
            redirect(action: "list")
        }
    }

    /**
     * Notification emails are sent when a person is created (by a non-
     * PI) to:
     *
     *  - the requesting user
     *  - AATAMS sys admins.
     */
    def sendCreationNotificationEmails(person) {
        sendMail {
            to person?.emailAddress
            bcc grailsApplication.config.grails.mail.adminEmailAddress
            from grailsApplication.config.grails.mail.systemEmailAddress
            subject "${message(code: 'mail.request.person.create.subject', args: [person.name])}"
            body "${message(code: 'mail.request.person.create.body', args: [person.name, createLink(action:'show', id:person.id, absolute:true)])}"
        }
    }

    def sendActivatedNotificationEmails(person) {
        sendMail {
            to person?.emailAddress
            bcc grailsApplication.config.grails.mail.adminEmailAddress
            from grailsApplication.config.grails.mail.systemEmailAddress
            subject "${message(code: 'mail.request.person.activate.subject', args: [person.name])}"
            body "${message(code: 'mail.request.person.activate.body', args: [person.name, createLink(controller:'auth', action:'login', absolute:true), createLink(controller:'gettingStarted', action:'index', absolute:true), grailsApplication.config.grails.mail.adminEmailAddress])}"
        }
    }
}

/**
 * The main purpose of this command object (on top of Person domain object)
 * is to provide validation that the two given passwords match, and to hash
 * the given password when creating a new Person instance.
 */
class PersonCreateCommand {
    String name
    String username
    String password         // Plain-text password.
    String passwordConfirm
    Organisation organisation
    String unlistedOrganisationName
    String phoneNumber
    String emailAddress
    DateTimeZone defaultTimeZone

    static constraints = {
        name(blank:false)
        username(blank:false)
        password(blank:false, validator:{val, obj ->
            if (val != obj.passwordConfirm) {
                return "person.password.mismatch"
            }
        })

        passwordConfirm(blank:false)
        organisation(validator: {
            val, obj ->

            return !(val && obj.unlistedOrganisationName)
        })
        unlistedOrganisationName(validator: {
            val, obj ->

            return !(val && obj.organisation)
        })

        phoneNumber(blank:false)
        emailAddress(email:true)
        defaultTimeZone(nullable:false)
    }

    Person createPerson() {
        def person = new Person(username:username.toLowerCase(),
                                passwordHash:new Sha256Hash(password).toHex(),
                                name:name,
                                organisation:organisation,
                                phoneNumber:phoneNumber,
                                emailAddress:emailAddress,
                                defaultTimeZone:defaultTimeZone)

        return person
    }
}

class PersonUpdatePasswordCommand {
    Integer id
    String password
    String passwordConfirm

    static constraints = {
        id(validator:{val ->
            if (!Person.get(val)) {
                return "person.id.invalid"
            }
        })

        password(blank:false, validator:{val, obj ->
            if (val != obj.passwordConfirm) {
                return "person.password.mismatch"
            }
        })
        passwordConfirm(blank:false)
    }

    Person updatePassword() {
        def person = Person.get(id)
        person.setPasswordHash(new Sha256Hash(password).toHex())
        if (!person.save(flush:true)) {
            return null
        }

        return person
    }
}
