package au.org.emii.aatams.upload

class ProcessedUploadFileController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [processedUploadFileInstanceList: ProcessedUploadFile.list(params), processedUploadFileInstanceTotal: ProcessedUploadFile.count()]
    }

    def create = {
        def processedUploadFileInstance = new ProcessedUploadFile()
        processedUploadFileInstance.properties = params
        return [processedUploadFileInstance: processedUploadFileInstance]
    }

    def save = {
        def processedUploadFileInstance = new ProcessedUploadFile(params)
        if (processedUploadFileInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'processedUploadFile.label', default: 'ProcessedUploadFile'), processedUploadFileInstance.id])}"
            redirect(action: "show", id: processedUploadFileInstance.id)
        }
        else {
            render(view: "create", model: [processedUploadFileInstance: processedUploadFileInstance])
        }
    }

    def show = {
        def processedUploadFileInstance = ProcessedUploadFile.get(params.id)
        if (!processedUploadFileInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'processedUploadFile.label', default: 'ProcessedUploadFile'), params.id])}"
            redirect(action: "list")
        }
        else {
            [processedUploadFileInstance: processedUploadFileInstance]
        }
    }

    def edit = {
        def processedUploadFileInstance = ProcessedUploadFile.get(params.id)
        if (!processedUploadFileInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'processedUploadFile.label', default: 'ProcessedUploadFile'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [processedUploadFileInstance: processedUploadFileInstance]
        }
    }

    def update = {
        def processedUploadFileInstance = ProcessedUploadFile.get(params.id)
        if (processedUploadFileInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (processedUploadFileInstance.version > version) {
                    
                    processedUploadFileInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'processedUploadFile.label', default: 'ProcessedUploadFile')] as Object[], "Another user has updated this ProcessedUploadFile while you were editing")
                    render(view: "edit", model: [processedUploadFileInstance: processedUploadFileInstance])
                    return
                }
            }
            processedUploadFileInstance.properties = params
            if (!processedUploadFileInstance.hasErrors() && processedUploadFileInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'processedUploadFile.label', default: 'ProcessedUploadFile'), processedUploadFileInstance.id])}"
                redirect(action: "show", id: processedUploadFileInstance.id)
            }
            else {
                render(view: "edit", model: [processedUploadFileInstance: processedUploadFileInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'processedUploadFile.label', default: 'ProcessedUploadFile'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def processedUploadFileInstance = ProcessedUploadFile.get(params.id)
        if (processedUploadFileInstance) {
            try {
                processedUploadFileInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'processedUploadFile.label', default: 'ProcessedUploadFile'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'processedUploadFile.label', default: 'ProcessedUploadFile'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'processedUploadFile.label', default: 'ProcessedUploadFile'), params.id])}"
            redirect(action: "list")
        }
    }
}
