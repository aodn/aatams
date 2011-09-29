package au.org.emii.aatams

class SurgeryTreatmentTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
        [surgeryTreatmentTypeInstanceList: SurgeryTreatmentType.list(params), surgeryTreatmentTypeInstanceTotal: SurgeryTreatmentType.count()]
    }

    def create = {
        def surgeryTreatmentTypeInstance = new SurgeryTreatmentType()
        surgeryTreatmentTypeInstance.properties = params
        return [surgeryTreatmentTypeInstance: surgeryTreatmentTypeInstance]
    }

    def save = {
        def surgeryTreatmentTypeInstance = new SurgeryTreatmentType(params)
        if (surgeryTreatmentTypeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'surgeryTreatmentType.label', default: 'SurgeryTreatmentType'), surgeryTreatmentTypeInstance.toString()])}"
            redirect(action: "show", id: surgeryTreatmentTypeInstance.id)
        }
        else {
            render(view: "create", model: [surgeryTreatmentTypeInstance: surgeryTreatmentTypeInstance])
        }
    }

    def show = {
        def surgeryTreatmentTypeInstance = SurgeryTreatmentType.get(params.id)
        if (!surgeryTreatmentTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'surgeryTreatmentType.label', default: 'SurgeryTreatmentType'), params.id])}"
            redirect(action: "list")
        }
        else {
            [surgeryTreatmentTypeInstance: surgeryTreatmentTypeInstance]
        }
    }

    def edit = {
        def surgeryTreatmentTypeInstance = SurgeryTreatmentType.get(params.id)
        if (!surgeryTreatmentTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'surgeryTreatmentType.label', default: 'SurgeryTreatmentType'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [surgeryTreatmentTypeInstance: surgeryTreatmentTypeInstance]
        }
    }

    def update = {
        def surgeryTreatmentTypeInstance = SurgeryTreatmentType.get(params.id)
        if (surgeryTreatmentTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (surgeryTreatmentTypeInstance.version > version) {
                    
                    surgeryTreatmentTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'surgeryTreatmentType.label', default: 'SurgeryTreatmentType')] as Object[], "Another user has updated this SurgeryTreatmentType while you were editing")
                    render(view: "edit", model: [surgeryTreatmentTypeInstance: surgeryTreatmentTypeInstance])
                    return
                }
            }
            surgeryTreatmentTypeInstance.properties = params
            if (!surgeryTreatmentTypeInstance.hasErrors() && surgeryTreatmentTypeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'surgeryTreatmentType.label', default: 'SurgeryTreatmentType'), surgeryTreatmentTypeInstance.toString()])}"
                redirect(action: "show", id: surgeryTreatmentTypeInstance.id)
            }
            else {
                render(view: "edit", model: [surgeryTreatmentTypeInstance: surgeryTreatmentTypeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'surgeryTreatmentType.label', default: 'SurgeryTreatmentType'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def surgeryTreatmentTypeInstance = SurgeryTreatmentType.get(params.id)
        if (surgeryTreatmentTypeInstance) {
            try {
                surgeryTreatmentTypeInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'surgeryTreatmentType.label', default: 'SurgeryTreatmentType'), surgeryTreatmentTypeInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'surgeryTreatmentType.label', default: 'SurgeryTreatmentType'), surgeryTreatmentTypeInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'surgeryTreatmentType.label', default: 'SurgeryTreatmentType'), params.id])}"
            redirect(action: "list")
        }
    }
}
