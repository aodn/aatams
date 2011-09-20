package au.org.emii.aatams

class SensorDetectionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [sensorDetectionInstanceList: SensorDetection.list(params), sensorDetectionInstanceTotal: SensorDetection.count()]
    }

    def create = {
        def sensorDetectionInstance = new SensorDetection()
        sensorDetectionInstance.properties = params
        return [sensorDetectionInstance: sensorDetectionInstance]
    }

    def save = {
        def sensorDetectionInstance = new SensorDetection(params)
        if (sensorDetectionInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'sensorDetection.label', default: 'SensorDetection'), sensorDetectionInstance.toString()])}"
            redirect(action: "show", id: sensorDetectionInstance.id)
        }
        else {
            render(view: "create", model: [sensorDetectionInstance: sensorDetectionInstance])
        }
    }

    def show = {
        def sensorDetectionInstance = SensorDetection.get(params.id)
        if (!sensorDetectionInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sensorDetection.label', default: 'SensorDetection'), params.id])}"
            redirect(action: "list")
        }
        else {
            [sensorDetectionInstance: sensorDetectionInstance]
        }
    }

    def edit = {
        def sensorDetectionInstance = SensorDetection.get(params.id)
        if (!sensorDetectionInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sensorDetection.label', default: 'SensorDetection'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [sensorDetectionInstance: sensorDetectionInstance]
        }
    }

    def update = {
        def sensorDetectionInstance = SensorDetection.get(params.id)
        if (sensorDetectionInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (sensorDetectionInstance.version > version) {
                    
                    sensorDetectionInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'sensorDetection.label', default: 'SensorDetection')] as Object[], "Another user has updated this SensorDetection while you were editing")
                    render(view: "edit", model: [sensorDetectionInstance: sensorDetectionInstance])
                    return
                }
            }
            sensorDetectionInstance.properties = params
            if (!sensorDetectionInstance.hasErrors() && sensorDetectionInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'sensorDetection.label', default: 'SensorDetection'), sensorDetectionInstance.toString()])}"
                redirect(action: "show", id: sensorDetectionInstance.id)
            }
            else {
                render(view: "edit", model: [sensorDetectionInstance: sensorDetectionInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sensorDetection.label', default: 'SensorDetection'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def sensorDetectionInstance = SensorDetection.get(params.id)
        if (sensorDetectionInstance) {
            try {
                sensorDetectionInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'sensorDetection.label', default: 'SensorDetection'), sensorDetectionInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'sensorDetection.label', default: 'SensorDetection'), sensorDetectionInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sensorDetection.label', default: 'SensorDetection'), params.id])}"
            redirect(action: "list")
        }
    }
}
