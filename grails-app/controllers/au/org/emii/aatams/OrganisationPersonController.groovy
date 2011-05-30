package au.org.emii.aatams

class OrganisationPersonController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [organisationPersonInstanceList: OrganisationPerson.list(params), organisationPersonInstanceTotal: OrganisationPerson.count()]
    }

    def create = {
        def organisationPersonInstance = new OrganisationPerson()
        organisationPersonInstance.properties = params
        return [organisationPersonInstance: organisationPersonInstance]
    }

    def save = {
        def organisationPersonInstance = new OrganisationPerson(params)
        if (organisationPersonInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'organisationPerson.label', default: 'OrganisationPerson'), organisationPersonInstance.id])}"
            redirect(action: "show", id: organisationPersonInstance.id)
        }
        else {
            render(view: "create", model: [organisationPersonInstance: organisationPersonInstance])
        }
    }

    def show = {
        def organisationPersonInstance = OrganisationPerson.get(params.id)
        if (!organisationPersonInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organisationPerson.label', default: 'OrganisationPerson'), params.id])}"
            redirect(action: "list")
        }
        else {
            [organisationPersonInstance: organisationPersonInstance]
        }
    }

    def edit = {
        def organisationPersonInstance = OrganisationPerson.get(params.id)
        if (!organisationPersonInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organisationPerson.label', default: 'OrganisationPerson'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [organisationPersonInstance: organisationPersonInstance]
        }
    }

    def update = {
        def organisationPersonInstance = OrganisationPerson.get(params.id)
        if (organisationPersonInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (organisationPersonInstance.version > version) {
                    
                    organisationPersonInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'organisationPerson.label', default: 'OrganisationPerson')] as Object[], "Another user has updated this OrganisationPerson while you were editing")
                    render(view: "edit", model: [organisationPersonInstance: organisationPersonInstance])
                    return
                }
            }
            organisationPersonInstance.properties = params
            if (!organisationPersonInstance.hasErrors() && organisationPersonInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'organisationPerson.label', default: 'OrganisationPerson'), organisationPersonInstance.id])}"
                redirect(action: "show", id: organisationPersonInstance.id)
            }
            else {
                render(view: "edit", model: [organisationPersonInstance: organisationPersonInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organisationPerson.label', default: 'OrganisationPerson'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def organisationPersonInstance = OrganisationPerson.get(params.id)
        if (organisationPersonInstance) {
            try {
                organisationPersonInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'organisationPerson.label', default: 'OrganisationPerson'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'organisationPerson.label', default: 'OrganisationPerson'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organisationPerson.label', default: 'OrganisationPerson'), params.id])}"
            redirect(action: "list")
        }
    }
}
