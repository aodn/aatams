package au.org.emii.aatams

class DeviceModelController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [deviceModelInstanceList: DeviceModel.list(params), deviceModelInstanceTotal: DeviceModel.count()]
    }

    def create = {
        def deviceModelInstance = new DeviceModel()
        deviceModelInstance.properties = params
        return [deviceModelInstance: deviceModelInstance]
    }

    def save = {
        def deviceModelInstance = new DeviceModel(params)
        if (deviceModelInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'deviceModel.label', default: 'DeviceModel'), deviceModelInstance.id])}"
            redirect(action: "show", id: deviceModelInstance.id)
        }
        else {
            render(view: "create", model: [deviceModelInstance: deviceModelInstance])
        }
    }

    def show = {
        def deviceModelInstance = DeviceModel.get(params.id)
        if (!deviceModelInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'deviceModel.label', default: 'DeviceModel'), params.id])}"
            redirect(action: "list")
        }
        else {
            [deviceModelInstance: deviceModelInstance]
        }
    }

    def edit = {
        def deviceModelInstance = DeviceModel.get(params.id)
        if (!deviceModelInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'deviceModel.label', default: 'DeviceModel'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [deviceModelInstance: deviceModelInstance]
        }
    }

    def update = {
        def deviceModelInstance = DeviceModel.get(params.id)
        if (deviceModelInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (deviceModelInstance.version > version) {
                    
                    deviceModelInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'deviceModel.label', default: 'DeviceModel')] as Object[], "Another user has updated this DeviceModel while you were editing")
                    render(view: "edit", model: [deviceModelInstance: deviceModelInstance])
                    return
                }
            }
            deviceModelInstance.properties = params
            if (!deviceModelInstance.hasErrors() && deviceModelInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'deviceModel.label', default: 'DeviceModel'), deviceModelInstance.id])}"
                redirect(action: "show", id: deviceModelInstance.id)
            }
            else {
                render(view: "edit", model: [deviceModelInstance: deviceModelInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'deviceModel.label', default: 'DeviceModel'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def deviceModelInstance = DeviceModel.get(params.id)
        if (deviceModelInstance) {
            try {
                deviceModelInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'deviceModel.label', default: 'DeviceModel'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'deviceModel.label', default: 'DeviceModel'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'deviceModel.label', default: 'DeviceModel'), params.id])}"
            redirect(action: "list")
        }
    }
}
