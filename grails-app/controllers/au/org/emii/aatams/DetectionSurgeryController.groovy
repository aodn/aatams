package au.org.emii.aatams

class DetectionSurgeryController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
        [detectionSurgeryInstanceList: DetectionSurgery.list(params), detectionSurgeryInstanceTotal: DetectionSurgery.count()]
    }

    def create = {
        def detectionSurgeryInstance = new DetectionSurgery()
        detectionSurgeryInstance.properties = params
        return [detectionSurgeryInstance: detectionSurgeryInstance]
    }

    def save = {
        def detectionSurgeryInstance = new DetectionSurgery(params)
        if (detectionSurgeryInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'detectionSurgery.label', default: 'DetectionSurgery'), detectionSurgeryInstance.toString()])}"
            redirect(action: "show", id: detectionSurgeryInstance.id)
        }
        else {
            render(view: "create", model: [detectionSurgeryInstance: detectionSurgeryInstance])
        }
    }

    def show = {
        def detectionSurgeryInstance = DetectionSurgery.get(params.id)
        if (!detectionSurgeryInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'detectionSurgery.label', default: 'DetectionSurgery'), params.id])}"
            redirect(action: "list")
        }
        else {
            [detectionSurgeryInstance: detectionSurgeryInstance]
        }
    }

    def edit = {
        def detectionSurgeryInstance = DetectionSurgery.get(params.id)
        if (!detectionSurgeryInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'detectionSurgery.label', default: 'DetectionSurgery'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [detectionSurgeryInstance: detectionSurgeryInstance]
        }
    }

    def update = {
        def detectionSurgeryInstance = DetectionSurgery.get(params.id)
        if (detectionSurgeryInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (detectionSurgeryInstance.version > version) {
                    
                    detectionSurgeryInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'detectionSurgery.label', default: 'DetectionSurgery')] as Object[], "Another user has updated this DetectionSurgery while you were editing")
                    render(view: "edit", model: [detectionSurgeryInstance: detectionSurgeryInstance])
                    return
                }
            }
            detectionSurgeryInstance.properties = params
            if (!detectionSurgeryInstance.hasErrors() && detectionSurgeryInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'detectionSurgery.label', default: 'DetectionSurgery'), detectionSurgeryInstance.toString()])}"
                redirect(action: "show", id: detectionSurgeryInstance.id)
            }
            else {
                render(view: "edit", model: [detectionSurgeryInstance: detectionSurgeryInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'detectionSurgery.label', default: 'DetectionSurgery'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def detectionSurgeryInstance = DetectionSurgery.get(params.id)
        if (detectionSurgeryInstance) {
            try {
                detectionSurgeryInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'detectionSurgery.label', default: 'DetectionSurgery'), detectionSurgeryInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'detectionSurgery.label', default: 'DetectionSurgery'), detectionSurgeryInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'detectionSurgery.label', default: 'DetectionSurgery'), params.id])}"
            redirect(action: "list")
        }
    }
}
