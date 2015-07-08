package au.org.emii.aatams

class MooringTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
        [mooringTypeInstanceList: MooringType.list(params), mooringTypeInstanceTotal: MooringType.count()]
    }

    def create = {
        def mooringTypeInstance = new MooringType()
        mooringTypeInstance.properties = params
        return [mooringTypeInstance: mooringTypeInstance]
    }

    def save = {
        def mooringTypeInstance = new MooringType(params)
        if (mooringTypeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'mooringType.label', default: 'MooringType'), mooringTypeInstance.toString()])}"
            redirect(action: "show", id: mooringTypeInstance.id)
        }
        else {
            render(view: "create", model: [mooringTypeInstance: mooringTypeInstance])
        }
    }

    def show = {
        def mooringTypeInstance = MooringType.get(params.id)
        if (!mooringTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mooringType.label', default: 'MooringType'), params.id])}"
            redirect(action: "list")
        }
        else {
            [mooringTypeInstance: mooringTypeInstance]
        }
    }

    def edit = {
        def mooringTypeInstance = MooringType.get(params.id)
        if (!mooringTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mooringType.label', default: 'MooringType'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [mooringTypeInstance: mooringTypeInstance]
        }
    }

    def update = {
        def mooringTypeInstance = MooringType.get(params.id)
        if (mooringTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (mooringTypeInstance.version > version) {

                    mooringTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'mooringType.label', default: 'MooringType')] as Object[], "Another user has updated this MooringType while you were editing")
                    render(view: "edit", model: [mooringTypeInstance: mooringTypeInstance])
                    return
                }
            }
            mooringTypeInstance.properties = params
            if (!mooringTypeInstance.hasErrors() && mooringTypeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'mooringType.label', default: 'MooringType'), mooringTypeInstance.toString()])}"
                redirect(action: "show", id: mooringTypeInstance.id)
            }
            else {
                render(view: "edit", model: [mooringTypeInstance: mooringTypeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mooringType.label', default: 'MooringType'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def mooringTypeInstance = MooringType.get(params.id)
        if (mooringTypeInstance) {
            try {
                mooringTypeInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'mooringType.label', default: 'MooringType'), mooringTypeInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'mooringType.label', default: 'MooringType'), mooringTypeInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mooringType.label', default: 'MooringType'), params.id])}"
            redirect(action: "list")
        }
    }
}
