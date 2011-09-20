package au.org.emii.aatams

class TransmitterTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [transmitterTypeInstanceList: TransmitterType.list(params), transmitterTypeInstanceTotal: TransmitterType.count()]
    }

    def create = {
        def transmitterTypeInstance = new TransmitterType()
        transmitterTypeInstance.properties = params
        return [transmitterTypeInstance: transmitterTypeInstance]
    }

    def save = {
        def transmitterTypeInstance = new TransmitterType(params)
        if (transmitterTypeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'transmitterType.label', default: 'TransmitterType'), transmitterTypeInstance.toString()])}"
            redirect(action: "show", id: transmitterTypeInstance.id)
        }
        else {
            render(view: "create", model: [transmitterTypeInstance: transmitterTypeInstance])
        }
    }

    def show = {
        def transmitterTypeInstance = TransmitterType.get(params.id)
        if (!transmitterTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'transmitterType.label', default: 'TransmitterType'), params.id])}"
            redirect(action: "list")
        }
        else {
            [transmitterTypeInstance: transmitterTypeInstance]
        }
    }

    def edit = {
        def transmitterTypeInstance = TransmitterType.get(params.id)
        if (!transmitterTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'transmitterType.label', default: 'TransmitterType'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [transmitterTypeInstance: transmitterTypeInstance]
        }
    }

    def update = {
        def transmitterTypeInstance = TransmitterType.get(params.id)
        if (transmitterTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (transmitterTypeInstance.version > version) {
                    
                    transmitterTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'transmitterType.label', default: 'TransmitterType')] as Object[], "Another user has updated this TransmitterType while you were editing")
                    render(view: "edit", model: [transmitterTypeInstance: transmitterTypeInstance])
                    return
                }
            }
            transmitterTypeInstance.properties = params
            if (!transmitterTypeInstance.hasErrors() && transmitterTypeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'transmitterType.label', default: 'TransmitterType'), transmitterTypeInstance.toString()])}"
                redirect(action: "show", id: transmitterTypeInstance.id)
            }
            else {
                render(view: "edit", model: [transmitterTypeInstance: transmitterTypeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'transmitterType.label', default: 'TransmitterType'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def transmitterTypeInstance = TransmitterType.get(params.id)
        if (transmitterTypeInstance) {
            try {
                transmitterTypeInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'transmitterType.label', default: 'TransmitterType'), transmitterTypeInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'transmitterType.label', default: 'TransmitterType'), transmitterTypeInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'transmitterType.label', default: 'TransmitterType'), params.id])}"
            redirect(action: "list")
        }
    }
}
