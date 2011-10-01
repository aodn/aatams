package au.org.emii.aatams.detection

class InvalidDetectionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [invalidDetectionInstanceList: InvalidDetection.list(params), invalidDetectionInstanceTotal: InvalidDetection.count()]
    }

    def create = {
        def invalidDetectionInstance = new InvalidDetection()
        invalidDetectionInstance.properties = params
        return [invalidDetectionInstance: invalidDetectionInstance]
    }

    def save = {
        def invalidDetectionInstance = new InvalidDetection(params)
        if (invalidDetectionInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'invalidDetection.label', default: 'InvalidDetection'), invalidDetectionInstance.id])}"
            redirect(action: "show", id: invalidDetectionInstance.id)
        }
        else {
            render(view: "create", model: [invalidDetectionInstance: invalidDetectionInstance])
        }
    }

    def show = {
        def invalidDetectionInstance = InvalidDetection.get(params.id)
        if (!invalidDetectionInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'invalidDetection.label', default: 'InvalidDetection'), params.id])}"
            redirect(action: "list")
        }
        else {
            [invalidDetectionInstance: invalidDetectionInstance]
        }
    }

    def edit = {
        def invalidDetectionInstance = InvalidDetection.get(params.id)
        if (!invalidDetectionInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'invalidDetection.label', default: 'InvalidDetection'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [invalidDetectionInstance: invalidDetectionInstance]
        }
    }

    def update = {
        def invalidDetectionInstance = InvalidDetection.get(params.id)
        if (invalidDetectionInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (invalidDetectionInstance.version > version) {
                    
                    invalidDetectionInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'invalidDetection.label', default: 'InvalidDetection')] as Object[], "Another user has updated this InvalidDetection while you were editing")
                    render(view: "edit", model: [invalidDetectionInstance: invalidDetectionInstance])
                    return
                }
            }
            invalidDetectionInstance.properties = params
            if (!invalidDetectionInstance.hasErrors() && invalidDetectionInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'invalidDetection.label', default: 'InvalidDetection'), invalidDetectionInstance.id])}"
                redirect(action: "show", id: invalidDetectionInstance.id)
            }
            else {
                render(view: "edit", model: [invalidDetectionInstance: invalidDetectionInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'invalidDetection.label', default: 'InvalidDetection'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def invalidDetectionInstance = InvalidDetection.get(params.id)
        if (invalidDetectionInstance) {
            try {
                invalidDetectionInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'invalidDetection.label', default: 'InvalidDetection'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'invalidDetection.label', default: 'InvalidDetection'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'invalidDetection.label', default: 'InvalidDetection'), params.id])}"
            redirect(action: "list")
        }
    }
}
