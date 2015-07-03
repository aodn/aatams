package au.org.emii.aatams

class ReceiverDeviceModelController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [receiverDeviceModelInstanceList: ReceiverDeviceModel.list(params), receiverDeviceModelInstanceTotal: ReceiverDeviceModel.count()]
    }

    def create = {
        def receiverDeviceModelInstance = new ReceiverDeviceModel()
        receiverDeviceModelInstance.properties = params
        return [receiverDeviceModelInstance: receiverDeviceModelInstance]
    }

    def save = {
        def receiverDeviceModelInstance = new ReceiverDeviceModel(params)
        if (receiverDeviceModelInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'receiverDeviceModel.label', default: 'ReceiverDeviceModel'), receiverDeviceModelInstance.id])}"
            redirect(action: "show", id: receiverDeviceModelInstance.id)
        }
        else {
            render(view: "create", model: [receiverDeviceModelInstance: receiverDeviceModelInstance])
        }
    }

    def show = {
        def receiverDeviceModelInstance = ReceiverDeviceModel.get(params.id)
        if (!receiverDeviceModelInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDeviceModel.label', default: 'ReceiverDeviceModel'), params.id])}"
            redirect(action: "list")
        }
        else {
            [receiverDeviceModelInstance: receiverDeviceModelInstance]
        }
    }

    def edit = {
        def receiverDeviceModelInstance = ReceiverDeviceModel.get(params.id)
        if (!receiverDeviceModelInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDeviceModel.label', default: 'ReceiverDeviceModel'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [receiverDeviceModelInstance: receiverDeviceModelInstance]
        }
    }

    def update = {
        def receiverDeviceModelInstance = ReceiverDeviceModel.get(params.id)
        if (receiverDeviceModelInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (receiverDeviceModelInstance.version > version) {

                    receiverDeviceModelInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'receiverDeviceModel.label', default: 'ReceiverDeviceModel')] as Object[], "Another user has updated this ReceiverDeviceModel while you were editing")
                    render(view: "edit", model: [receiverDeviceModelInstance: receiverDeviceModelInstance])
                    return
                }
            }
            receiverDeviceModelInstance.properties = params
            if (!receiverDeviceModelInstance.hasErrors() && receiverDeviceModelInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'receiverDeviceModel.label', default: 'ReceiverDeviceModel'), receiverDeviceModelInstance.id])}"
                redirect(action: "show", id: receiverDeviceModelInstance.id)
            }
            else {
                render(view: "edit", model: [receiverDeviceModelInstance: receiverDeviceModelInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDeviceModel.label', default: 'ReceiverDeviceModel'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def receiverDeviceModelInstance = ReceiverDeviceModel.get(params.id)
        if (receiverDeviceModelInstance) {
            try {
                receiverDeviceModelInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'receiverDeviceModel.label', default: 'ReceiverDeviceModel'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'receiverDeviceModel.label', default: 'ReceiverDeviceModel'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDeviceModel.label', default: 'ReceiverDeviceModel'), params.id])}"
            redirect(action: "list")
        }
    }
}
