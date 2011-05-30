package au.org.emii.aatams

class ReceiverDownloadController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [receiverDownloadInstanceList: ReceiverDownload.list(params), receiverDownloadInstanceTotal: ReceiverDownload.count()]
    }

    def create = {
        def receiverDownloadInstance = new ReceiverDownload()
        receiverDownloadInstance.properties = params
        return [receiverDownloadInstance: receiverDownloadInstance]
    }

    def save = {
        def receiverDownloadInstance = new ReceiverDownload(params)
        if (receiverDownloadInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'receiverDownload.label', default: 'ReceiverDownload'), receiverDownloadInstance.id])}"
            redirect(action: "show", id: receiverDownloadInstance.id)
        }
        else {
            render(view: "create", model: [receiverDownloadInstance: receiverDownloadInstance])
        }
    }

    def show = {
        def receiverDownloadInstance = ReceiverDownload.get(params.id)
        if (!receiverDownloadInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDownload.label', default: 'ReceiverDownload'), params.id])}"
            redirect(action: "list")
        }
        else {
            [receiverDownloadInstance: receiverDownloadInstance]
        }
    }

    def edit = {
        def receiverDownloadInstance = ReceiverDownload.get(params.id)
        if (!receiverDownloadInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDownload.label', default: 'ReceiverDownload'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [receiverDownloadInstance: receiverDownloadInstance]
        }
    }

    def update = {
        def receiverDownloadInstance = ReceiverDownload.get(params.id)
        if (receiverDownloadInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (receiverDownloadInstance.version > version) {
                    
                    receiverDownloadInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'receiverDownload.label', default: 'ReceiverDownload')] as Object[], "Another user has updated this ReceiverDownload while you were editing")
                    render(view: "edit", model: [receiverDownloadInstance: receiverDownloadInstance])
                    return
                }
            }
            receiverDownloadInstance.properties = params
            if (!receiverDownloadInstance.hasErrors() && receiverDownloadInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'receiverDownload.label', default: 'ReceiverDownload'), receiverDownloadInstance.id])}"
                redirect(action: "show", id: receiverDownloadInstance.id)
            }
            else {
                render(view: "edit", model: [receiverDownloadInstance: receiverDownloadInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDownload.label', default: 'ReceiverDownload'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def receiverDownloadInstance = ReceiverDownload.get(params.id)
        if (receiverDownloadInstance) {
            try {
                receiverDownloadInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'receiverDownload.label', default: 'ReceiverDownload'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'receiverDownload.label', default: 'ReceiverDownload'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDownload.label', default: 'ReceiverDownload'), params.id])}"
            redirect(action: "list")
        }
    }
}
