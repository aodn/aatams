package au.org.emii.aatams

class AnimalMeasurementTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [animalMeasurementTypeInstanceList: AnimalMeasurementType.list(params), animalMeasurementTypeInstanceTotal: AnimalMeasurementType.count()]
    }

    def create = {
        def animalMeasurementTypeInstance = new AnimalMeasurementType()
        animalMeasurementTypeInstance.properties = params
        return [animalMeasurementTypeInstance: animalMeasurementTypeInstance]
    }

    def save = {
        def animalMeasurementTypeInstance = new AnimalMeasurementType(params)
        if (animalMeasurementTypeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'animalMeasurementType.label', default: 'AnimalMeasurementType'), animalMeasurementTypeInstance.toString()])}"
            redirect(action: "show", id: animalMeasurementTypeInstance.id)
        }
        else {
            render(view: "create", model: [animalMeasurementTypeInstance: animalMeasurementTypeInstance])
        }
    }

    def show = {
        def animalMeasurementTypeInstance = AnimalMeasurementType.get(params.id)
        if (!animalMeasurementTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animalMeasurementType.label', default: 'AnimalMeasurementType'), params.id])}"
            redirect(action: "list")
        }
        else {
            [animalMeasurementTypeInstance: animalMeasurementTypeInstance]
        }
    }

    def edit = {
        def animalMeasurementTypeInstance = AnimalMeasurementType.get(params.id)
        if (!animalMeasurementTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animalMeasurementType.label', default: 'AnimalMeasurementType'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [animalMeasurementTypeInstance: animalMeasurementTypeInstance]
        }
    }

    def update = {
        def animalMeasurementTypeInstance = AnimalMeasurementType.get(params.id)
        if (animalMeasurementTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (animalMeasurementTypeInstance.version > version) {
                    
                    animalMeasurementTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'animalMeasurementType.label', default: 'AnimalMeasurementType')] as Object[], "Another user has updated this AnimalMeasurementType while you were editing")
                    render(view: "edit", model: [animalMeasurementTypeInstance: animalMeasurementTypeInstance])
                    return
                }
            }
            animalMeasurementTypeInstance.properties = params
            if (!animalMeasurementTypeInstance.hasErrors() && animalMeasurementTypeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'animalMeasurementType.label', default: 'AnimalMeasurementType'), animalMeasurementTypeInstance.toString()])}"
                redirect(action: "show", id: animalMeasurementTypeInstance.id)
            }
            else {
                render(view: "edit", model: [animalMeasurementTypeInstance: animalMeasurementTypeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animalMeasurementType.label', default: 'AnimalMeasurementType'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def animalMeasurementTypeInstance = AnimalMeasurementType.get(params.id)
        if (animalMeasurementTypeInstance) {
            try {
                animalMeasurementTypeInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'animalMeasurementType.label', default: 'AnimalMeasurementType'), animalMeasurementTypeInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'animalMeasurementType.label', default: 'AnimalMeasurementType'), animalMeasurementTypeInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animalMeasurementType.label', default: 'AnimalMeasurementType'), params.id])}"
            redirect(action: "list")
        }
    }
}
