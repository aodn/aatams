package au.org.emii.aatams

import grails.converters.JSON

class AnimalMeasurementController {

    static allowedMethods = [save: "POST", update: "POST", delete: ["POST", "GET"]]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [animalMeasurementInstanceList: AnimalMeasurement.list(params), animalMeasurementInstanceTotal: AnimalMeasurement.count()]
    }

    def create = {
        def animalMeasurementInstance = new AnimalMeasurement()
        animalMeasurementInstance.properties = params
        return [animalMeasurementInstance: animalMeasurementInstance]
    }

    def save = {
        def animalMeasurementInstance = new AnimalMeasurement(params)
        if (animalMeasurementInstance.save(flush: true)) 
        {
            flash.message = "${message(code: 'default.updated.message', args: [message(code: 'animalMeasurement.label', default: 'AnimalMeasurement'), animalMeasurementInstance.toString()])}"
            render ([instance:animalMeasurementInstance, message:flash] as JSON)
        }
        else 
        {
            log.error(animalMeasurementInstance.errors)
            render ([errors:animalMeasurementInstance.errors] as JSON)
        }
    }

    def show = {
        def animalMeasurementInstance = AnimalMeasurement.get(params.id)
        if (!animalMeasurementInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animalMeasurement.label', default: 'AnimalMeasurement'), params.id])}"
            redirect(action: "list")
        }
        else {
            [animalMeasurementInstance: animalMeasurementInstance]
        }
    }

    def edit = {
        def animalMeasurementInstance = AnimalMeasurement.get(params.id)
        if (!animalMeasurementInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animalMeasurement.label', default: 'AnimalMeasurement'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [animalMeasurementInstance: animalMeasurementInstance]
        }
    }

    def update = {
        def animalMeasurementInstance = AnimalMeasurement.get(params.id)
        if (animalMeasurementInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (animalMeasurementInstance.version > version) {
                    
                    animalMeasurementInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'animalMeasurement.label', default: 'AnimalMeasurement')] as Object[], "Another user has updated this AnimalMeasurement while you were editing")
                    render(view: "edit", model: [animalMeasurementInstance: animalMeasurementInstance])
                    return
                }
            }
            animalMeasurementInstance.properties = params
            if (!animalMeasurementInstance.hasErrors() && animalMeasurementInstance.save(flush: true)) 
            {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'animalMeasurement.label', default: 'AnimalMeasurement'), animalMeasurementInstance.toString()])}"
                def release = animalMeasurementInstance?.release
                redirect(controller: "animalRelease", 
                         action: "edit", 
                         id: release?.id,
                         params: [projectId:release?.project?.id])
            }
            else {
                render(view: "edit", model: [animalMeasurementInstance: animalMeasurementInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animalMeasurement.label', default: 'AnimalMeasurement'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = 
    {
        def animalMeasurementInstance = AnimalMeasurement.get(params.id)
        if (animalMeasurementInstance) 
        {
            try 
            {
                animalMeasurementInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'animalMeasurement.label', default: 'AnimalMeasurement'), animalMeasurementInstance.toString()])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'animalMeasurement.label', default: 'AnimalMeasurement'), animalMeasurementInstance.toString()])}"
            }
            
            // Redirect to the "owning" animal release edit, since that is where
            // a normal user (i.e. non sysadmin) would have deleted from).
            def release = animalMeasurementInstance?.release
            log.debug("Redirecting to animalRelease, id: " + release?.id)
            redirect(controller: "animalRelease", 
                     action: "edit", 
                     id: release?.id,
                     params: [projectId:release?.project?.id])
        }
        else 
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animalMeasurement.label', default: 'AnimalMeasurement'), params.id])}"
            redirect(action: "list")
        }
    }
}
