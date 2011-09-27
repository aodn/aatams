package au.org.emii.aatams

class DetectionController {

    def candidateEntitiesService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [detectionInstanceList: Detection.list(params), detectionInstanceTotal: Detection.count()]
    }

    def create = {
        redirect(controller:"receiverDownloadFile", action:"create")
    }

    def save = {
        def detectionInstance = new Detection(params)
        if (detectionInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'detection.label', default: 'Detection'), detectionInstance.toString()])}"
            redirect(action: "show", id: detectionInstance.id)
        }
        else {
            def model =
                [detectionInstance: detectionInstance] + [candidateDeployments:candidateEntitiesService.deployments()]
            render(view: "create", model: model)
        }
    }

    def show = {
        def detectionInstance = Detection.get(params.id)
        if (!detectionInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'detection.label', default: 'Detection'), params.id])}"
            redirect(action: "list")
        }
        else {
            [detectionInstance: detectionInstance]
        }
    }

    def edit = {
        def detectionInstance = Detection.get(params.id)
        if (!detectionInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'detection.label', default: 'Detection'), params.id])}"
            redirect(action: "list")
        }
        else 
        {
            def model = [detectionInstance: detectionInstance]
            model.candidateDeployments = candidateEntitiesService.deployments()
            return model
        }
    }

    def update = {
        def detectionInstance = Detection.get(params.id)
        if (detectionInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (detectionInstance.version > version) {
                    
                    detectionInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'detection.label', default: 'Detection')] as Object[], "Another user has updated this Detection while you were editing")
                    render(view: "edit", model: [detectionInstance: detectionInstance])
                    return
                }
            }
            detectionInstance.properties = params
            if (!detectionInstance.hasErrors() && detectionInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'detection.label', default: 'Detection'), detectionInstance.toString()])}"
                redirect(action: "show", id: detectionInstance.id)
            }
            else {
                render(view: "edit", model: [detectionInstance: detectionInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'detection.label', default: 'Detection'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def detectionInstance = Detection.get(params.id)
        if (detectionInstance) {
            try {
                detectionInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'detection.label', default: 'Detection'), detectionInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'detection.label', default: 'Detection'), detectionInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'detection.label', default: 'Detection'), params.id])}"
            redirect(action: "list")
        }
    }
}
