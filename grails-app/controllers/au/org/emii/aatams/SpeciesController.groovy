package au.org.emii.aatams

class SpeciesController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [speciesInstanceList: Species.list(params), speciesInstanceTotal: Species.count()]
    }

    def create = {
        def speciesInstance = new Species()
        speciesInstance.properties = params
        return [speciesInstance: speciesInstance]
    }

    def save = {
        def speciesInstance = new Species(params)
        if (speciesInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'species.label', default: 'Species'), speciesInstance.id])}"
            redirect(action: "show", id: speciesInstance.id)
        }
        else {
            render(view: "create", model: [speciesInstance: speciesInstance])
        }
    }

    def show = {
        def speciesInstance = Species.get(params.id)
        if (!speciesInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'species.label', default: 'Species'), params.id])}"
            redirect(action: "list")
        }
        else {
            [speciesInstance: speciesInstance]
        }
    }

    def edit = {
        def speciesInstance = Species.get(params.id)
        if (!speciesInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'species.label', default: 'Species'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [speciesInstance: speciesInstance]
        }
    }

    def update = {
        def speciesInstance = Species.get(params.id)
        if (speciesInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (speciesInstance.version > version) {
                    
                    speciesInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'species.label', default: 'Species')] as Object[], "Another user has updated this Species while you were editing")
                    render(view: "edit", model: [speciesInstance: speciesInstance])
                    return
                }
            }
            speciesInstance.properties = params
            if (!speciesInstance.hasErrors() && speciesInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'species.label', default: 'Species'), speciesInstance.id])}"
                redirect(action: "show", id: speciesInstance.id)
            }
            else {
                render(view: "edit", model: [speciesInstance: speciesInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'species.label', default: 'Species'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def speciesInstance = Species.get(params.id)
        if (speciesInstance) {
            try {
                speciesInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'species.label', default: 'Species'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'species.label', default: 'Species'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'species.label', default: 'Species'), params.id])}"
            redirect(action: "list")
        }
    }
}
