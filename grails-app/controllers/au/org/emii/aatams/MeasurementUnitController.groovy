package au.org.emii.aatams

class MeasurementUnitController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
        [measurementUnitInstanceList: MeasurementUnit.list(params), measurementUnitInstanceTotal: MeasurementUnit.count()]
    }

    def create = {
        def measurementUnitInstance = new MeasurementUnit()
        measurementUnitInstance.properties = params
        return [measurementUnitInstance: measurementUnitInstance]
    }

    def save = {
        def measurementUnitInstance = new MeasurementUnit(params)
        if (measurementUnitInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'measurementUnit.label', default: 'MeasurementUnit'), measurementUnitInstance.toString()])}"
            redirect(action: "show", id: measurementUnitInstance.id)
        }
        else {
            render(view: "create", model: [measurementUnitInstance: measurementUnitInstance])
        }
    }

    def show = {
        def measurementUnitInstance = MeasurementUnit.get(params.id)
        if (!measurementUnitInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'measurementUnit.label', default: 'MeasurementUnit'), params.id])}"
            redirect(action: "list")
        }
        else {
            [measurementUnitInstance: measurementUnitInstance]
        }
    }

    def edit = {
        def measurementUnitInstance = MeasurementUnit.get(params.id)
        if (!measurementUnitInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'measurementUnit.label', default: 'MeasurementUnit'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [measurementUnitInstance: measurementUnitInstance]
        }
    }

    def update = {
        def measurementUnitInstance = MeasurementUnit.get(params.id)
        if (measurementUnitInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (measurementUnitInstance.version > version) {

                    measurementUnitInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'measurementUnit.label', default: 'MeasurementUnit')] as Object[], "Another user has updated this MeasurementUnit while you were editing")
                    render(view: "edit", model: [measurementUnitInstance: measurementUnitInstance])
                    return
                }
            }
            measurementUnitInstance.properties = params
            if (!measurementUnitInstance.hasErrors() && measurementUnitInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'measurementUnit.label', default: 'MeasurementUnit'), measurementUnitInstance.toString()])}"
                redirect(action: "show", id: measurementUnitInstance.id)
            }
            else {
                render(view: "edit", model: [measurementUnitInstance: measurementUnitInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'measurementUnit.label', default: 'MeasurementUnit'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def measurementUnitInstance = MeasurementUnit.get(params.id)
        if (measurementUnitInstance) {
            try {
                measurementUnitInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'measurementUnit.label', default: 'MeasurementUnit'), measurementUnitInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'measurementUnit.label', default: 'MeasurementUnit'), measurementUnitInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'measurementUnit.label', default: 'MeasurementUnit'), params.id])}"
            redirect(action: "list")
        }
    }
}
