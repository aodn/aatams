package au.org.emii.aatams

class DeviceManufacturerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
        [deviceManufacturerInstanceList: DeviceManufacturer.list(params), deviceManufacturerInstanceTotal: DeviceManufacturer.count()]
    }

    def create = {
        def deviceManufacturerInstance = new DeviceManufacturer()
        deviceManufacturerInstance.properties = params
        return [deviceManufacturerInstance: deviceManufacturerInstance]
    }

    def save = {
        def deviceManufacturerInstance = new DeviceManufacturer(params)
        if (deviceManufacturerInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'deviceManufacturer.label', default: 'DeviceManufacturer'), deviceManufacturerInstance.toString()])}"
            redirect(action: "show", id: deviceManufacturerInstance.id)
        }
        else {
            render(view: "create", model: [deviceManufacturerInstance: deviceManufacturerInstance])
        }
    }

    def show = {
        def deviceManufacturerInstance = DeviceManufacturer.get(params.id)
        if (!deviceManufacturerInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'deviceManufacturer.label', default: 'DeviceManufacturer'), params.id])}"
            redirect(action: "list")
        }
        else {
            [deviceManufacturerInstance: deviceManufacturerInstance]
        }
    }

    def edit = {
        def deviceManufacturerInstance = DeviceManufacturer.get(params.id)
        if (!deviceManufacturerInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'deviceManufacturer.label', default: 'DeviceManufacturer'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [deviceManufacturerInstance: deviceManufacturerInstance]
        }
    }

    def update = {
        def deviceManufacturerInstance = DeviceManufacturer.get(params.id)
        if (deviceManufacturerInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (deviceManufacturerInstance.version > version) {

                    deviceManufacturerInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'deviceManufacturer.label', default: 'DeviceManufacturer')] as Object[], "Another user has updated this DeviceManufacturer while you were editing")
                    render(view: "edit", model: [deviceManufacturerInstance: deviceManufacturerInstance])
                    return
                }
            }
            deviceManufacturerInstance.properties = params
            if (!deviceManufacturerInstance.hasErrors() && deviceManufacturerInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'deviceManufacturer.label', default: 'DeviceManufacturer'), deviceManufacturerInstance.toString()])}"
                redirect(action: "show", id: deviceManufacturerInstance.id)
            }
            else {
                render(view: "edit", model: [deviceManufacturerInstance: deviceManufacturerInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'deviceManufacturer.label', default: 'DeviceManufacturer'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def deviceManufacturerInstance = DeviceManufacturer.get(params.id)
        if (deviceManufacturerInstance) {
            try {
                deviceManufacturerInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'deviceManufacturer.label', default: 'DeviceManufacturer'), deviceManufacturerInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'deviceManufacturer.label', default: 'DeviceManufacturer'), deviceManufacturerInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'deviceManufacturer.label', default: 'DeviceManufacturer'), params.id])}"
            redirect(action: "list")
        }
    }
}
