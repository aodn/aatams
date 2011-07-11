package au.org.emii.aatams

import grails.converters.deep.JSON

class OrganisationProjectController {

    static allowedMethods = [saveOrganisationToProject: "POST", save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [organisationProjectInstanceList: OrganisationProject.list(params), organisationProjectInstanceTotal: OrganisationProject.count()]
    }

    def create = {
        def organisationProjectInstance = new OrganisationProject()
        organisationProjectInstance.properties = params
        return [organisationProjectInstance: organisationProjectInstance]
    }
    
    def save = {
        def organisationProjectInstance = new OrganisationProject(params)
        if (organisationProjectInstance.save(flush: true)) 
        {
            render organisationProjectInstance as JSON
        }
        else {
            render(view: "create", model: [organisationProjectInstance: organisationProjectInstance])
        }
    }
    
    def show = {
        def organisationProjectInstance = OrganisationProject.get(params.id)
        if (!organisationProjectInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organisationProject.label', default: 'OrganisationProject'), params.id])}"
            redirect(action: "list")
        }
        else {
            [organisationProjectInstance: organisationProjectInstance]
        }
    }

    def edit = {
        def organisationProjectInstance = OrganisationProject.get(params.id)
        if (!organisationProjectInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organisationProject.label', default: 'OrganisationProject'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [organisationProjectInstance: organisationProjectInstance]
        }
    }

    def update = {
        def organisationProjectInstance = OrganisationProject.get(params.id)
        if (organisationProjectInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (organisationProjectInstance.version > version) {
                    
                    organisationProjectInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'organisationProject.label', default: 'OrganisationProject')] as Object[], "Another user has updated this OrganisationProject while you were editing")
                    render(view: "edit", model: [organisationProjectInstance: organisationProjectInstance])
                    return
                }
            }
            organisationProjectInstance.properties = params
            if (!organisationProjectInstance.hasErrors() && organisationProjectInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'organisationProject.label', default: 'OrganisationProject'), organisationProjectInstance.id])}"
                redirect(action: "show", id: organisationProjectInstance.id)
            }
            else {
                render(view: "edit", model: [organisationProjectInstance: organisationProjectInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organisationProject.label', default: 'OrganisationProject'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def organisationProjectInstance = OrganisationProject.get(params.id)
        if (organisationProjectInstance) {
            try {
                organisationProjectInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'organisationProject.label', default: 'OrganisationProject'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'organisationProject.label', default: 'OrganisationProject'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organisationProject.label', default: 'OrganisationProject'), params.id])}"
            redirect(action: "list")
        }
    }
}
