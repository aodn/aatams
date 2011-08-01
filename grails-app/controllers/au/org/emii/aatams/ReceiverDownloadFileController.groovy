package au.org.emii.aatams

class ReceiverDownloadFileController {

    def fileProcessorService
    
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [receiverDownloadFileInstanceList: ReceiverDownloadFile.list(params), receiverDownloadFileInstanceTotal: ReceiverDownloadFile.count()]
    }

    def create = 
    {
        def receiverDownloadFileInstance = new ReceiverDownloadFile()
        receiverDownloadFileInstance.properties = params
        receiverDownloadFileInstance.receiverDownload = ReceiverDownload.get(params.downloadId)
        
        return [receiverDownloadFileInstance: receiverDownloadFileInstance]
    }

    def save = 
    {
        def receiverDownloadFileInstance = new ReceiverDownloadFile(params)
        receiverDownloadFileInstance.receiverDownload = ReceiverDownload.get(params.downloadId)
        receiverDownloadFileInstance.errMsg = ""
        receiverDownloadFileInstance.importDate = new Date()
        receiverDownloadFileInstance.status = FileProcessingStatus.PENDING
        
        def fileMap = request.getFileMap()
        
        // Should only be one file in file map.
        for (e in fileMap)
        {
            fileProcessorService.process(receiverDownloadFileInstance, e.value)
        }
        
        flash.message = "${message(code: 'default.created.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), receiverDownloadFileInstance.id])}"

        // Go back to receiver recovery edit screen.
        redirect(controller: "receiverRecovery", action: "edit", id: receiverDownloadFileInstance?.receiverDownload?.receiverRecovery?.id)
    }

    def show = {
        def receiverDownloadFileInstance = ReceiverDownloadFile.get(params.id)
        if (!receiverDownloadFileInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), params.id])}"
            redirect(action: "list")
        }
        else {
            [receiverDownloadFileInstance: receiverDownloadFileInstance]
        }
    }

    def edit = {
        def receiverDownloadFileInstance = ReceiverDownloadFile.get(params.id)
        if (!receiverDownloadFileInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [receiverDownloadFileInstance: receiverDownloadFileInstance]
        }
    }

    def update = {
        def receiverDownloadFileInstance = ReceiverDownloadFile.get(params.id)
        if (receiverDownloadFileInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (receiverDownloadFileInstance.version > version) {
                    
                    receiverDownloadFileInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile')] as Object[], "Another user has updated this ReceiverDownloadFile while you were editing")
                    render(view: "edit", model: [receiverDownloadFileInstance: receiverDownloadFileInstance])
                    return
                }
            }
            receiverDownloadFileInstance.properties = params
            if (!receiverDownloadFileInstance.hasErrors() && receiverDownloadFileInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), receiverDownloadFileInstance.id])}"
                redirect(action: "show", id: receiverDownloadFileInstance.id)
            }
            else {
                render(view: "edit", model: [receiverDownloadFileInstance: receiverDownloadFileInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def receiverDownloadFileInstance = ReceiverDownloadFile.get(params.id)
        if (receiverDownloadFileInstance) {
            try {
                receiverDownloadFileInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), params.id])}"
            redirect(action: "list")
        }
    }
}
