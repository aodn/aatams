package au.org.emii.aatams

class SurgeryTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
        [surgeryTypeInstanceList: SurgeryType.list(params), surgeryTypeInstanceTotal: SurgeryType.count()]
    }

    def create = {
        def surgeryTypeInstance = new SurgeryType()
        surgeryTypeInstance.properties = params
        return [surgeryTypeInstance: surgeryTypeInstance]
    }

    def save = {
        def surgeryTypeInstance = new SurgeryType(params)
        if (surgeryTypeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'surgeryType.label', default: 'SurgeryType'), surgeryTypeInstance.toString()])}"
            redirect(action: "show", id: surgeryTypeInstance.id)
        }
        else {
            render(view: "create", model: [surgeryTypeInstance: surgeryTypeInstance])
        }
    }

    def show = {
        def surgeryTypeInstance = SurgeryType.get(params.id)
        if (!surgeryTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'surgeryType.label', default: 'SurgeryType'), params.id])}"
            redirect(action: "list")
        }
        else {
            [surgeryTypeInstance: surgeryTypeInstance]
        }
    }

    def edit = {
        def surgeryTypeInstance = SurgeryType.get(params.id)
        if (!surgeryTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'surgeryType.label', default: 'SurgeryType'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [surgeryTypeInstance: surgeryTypeInstance]
        }
    }

    def update = {
        def surgeryTypeInstance = SurgeryType.get(params.id)
        if (surgeryTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (surgeryTypeInstance.version > version) {

                    surgeryTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'surgeryType.label', default: 'SurgeryType')] as Object[], "Another user has updated this SurgeryType while you were editing")
                    render(view: "edit", model: [surgeryTypeInstance: surgeryTypeInstance])
                    return
                }
            }
            surgeryTypeInstance.properties = params
            if (!surgeryTypeInstance.hasErrors() && surgeryTypeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'surgeryType.label', default: 'SurgeryType'), surgeryTypeInstance.toString()])}"
                redirect(action: "show", id: surgeryTypeInstance.id)
            }
            else {
                render(view: "edit", model: [surgeryTypeInstance: surgeryTypeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'surgeryType.label', default: 'SurgeryType'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def surgeryTypeInstance = SurgeryType.get(params.id)
        if (surgeryTypeInstance) {
            try {
                surgeryTypeInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'surgeryType.label', default: 'SurgeryType'), surgeryTypeInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'surgeryType.label', default: 'SurgeryType'), surgeryTypeInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'surgeryType.label', default: 'SurgeryType'), params.id])}"
            redirect(action: "list")
        }
    }
}
