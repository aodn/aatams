package au.org.emii.aatams

class DeviceStatusController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
        [deviceStatusInstanceList: DeviceStatus.list(params), deviceStatusInstanceTotal: DeviceStatus.count()]
    }

    def create = {
        def deviceStatusInstance = new DeviceStatus()
        deviceStatusInstance.properties = params
        return [deviceStatusInstance: deviceStatusInstance]
    }

    def save = {
        def deviceStatusInstance = new DeviceStatus(params)
        if (deviceStatusInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'deviceStatus.label', default: 'DeviceStatus'), deviceStatusInstance.toString()])}"
            redirect(action: "show", id: deviceStatusInstance.id)
        }
        else {
            render(view: "create", model: [deviceStatusInstance: deviceStatusInstance])
        }
    }

    def show = {
        def deviceStatusInstance = DeviceStatus.get(params.id)
        if (!deviceStatusInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'deviceStatus.label', default: 'DeviceStatus'), params.id])}"
            redirect(action: "list")
        }
        else {
            [deviceStatusInstance: deviceStatusInstance]
        }
    }

    def edit = {
        def deviceStatusInstance = DeviceStatus.get(params.id)
        if (!deviceStatusInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'deviceStatus.label', default: 'DeviceStatus'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [deviceStatusInstance: deviceStatusInstance]
        }
    }

    def update = {
        def deviceStatusInstance = DeviceStatus.get(params.id)
        if (deviceStatusInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (deviceStatusInstance.version > version) {
                    
                    deviceStatusInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'deviceStatus.label', default: 'DeviceStatus')] as Object[], "Another user has updated this DeviceStatus while you were editing")
                    render(view: "edit", model: [deviceStatusInstance: deviceStatusInstance])
                    return
                }
            }
            deviceStatusInstance.properties = params
            if (!deviceStatusInstance.hasErrors() && deviceStatusInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'deviceStatus.label', default: 'DeviceStatus'), deviceStatusInstance.toString()])}"
                redirect(action: "show", id: deviceStatusInstance.id)
            }
            else {
                render(view: "edit", model: [deviceStatusInstance: deviceStatusInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'deviceStatus.label', default: 'DeviceStatus'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def deviceStatusInstance = DeviceStatus.get(params.id)
        if (deviceStatusInstance) {
            try {
                deviceStatusInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'deviceStatus.label', default: 'DeviceStatus'), deviceStatusInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'deviceStatus.label', default: 'DeviceStatus'), deviceStatusInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'deviceStatus.label', default: 'DeviceStatus'), params.id])}"
            redirect(action: "list")
        }
    }
}
