package au.org.emii.aatams

class SystemRoleTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [systemRoleTypeInstanceList: SystemRoleType.list(params), systemRoleTypeInstanceTotal: SystemRoleType.count()]
    }

    def create = {
        def systemRoleTypeInstance = new SystemRoleType()
        systemRoleTypeInstance.properties = params
        return [systemRoleTypeInstance: systemRoleTypeInstance]
    }

    def save = {
        def systemRoleTypeInstance = new SystemRoleType(params)
        if (systemRoleTypeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'systemRoleType.label', default: 'SystemRoleType'), systemRoleTypeInstance.id])}"
            redirect(action: "show", id: systemRoleTypeInstance.id)
        }
        else {
            render(view: "create", model: [systemRoleTypeInstance: systemRoleTypeInstance])
        }
    }

    def show = {
        def systemRoleTypeInstance = SystemRoleType.get(params.id)
        if (!systemRoleTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'systemRoleType.label', default: 'SystemRoleType'), params.id])}"
            redirect(action: "list")
        }
        else {
            [systemRoleTypeInstance: systemRoleTypeInstance]
        }
    }

    def edit = {
        def systemRoleTypeInstance = SystemRoleType.get(params.id)
        if (!systemRoleTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'systemRoleType.label', default: 'SystemRoleType'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [systemRoleTypeInstance: systemRoleTypeInstance]
        }
    }

    def update = {
        def systemRoleTypeInstance = SystemRoleType.get(params.id)
        if (systemRoleTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (systemRoleTypeInstance.version > version) {
                    
                    systemRoleTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'systemRoleType.label', default: 'SystemRoleType')] as Object[], "Another user has updated this SystemRoleType while you were editing")
                    render(view: "edit", model: [systemRoleTypeInstance: systemRoleTypeInstance])
                    return
                }
            }
            systemRoleTypeInstance.properties = params
            if (!systemRoleTypeInstance.hasErrors() && systemRoleTypeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'systemRoleType.label', default: 'SystemRoleType'), systemRoleTypeInstance.id])}"
                redirect(action: "show", id: systemRoleTypeInstance.id)
            }
            else {
                render(view: "edit", model: [systemRoleTypeInstance: systemRoleTypeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'systemRoleType.label', default: 'SystemRoleType'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def systemRoleTypeInstance = SystemRoleType.get(params.id)
        if (systemRoleTypeInstance) {
            try {
                systemRoleTypeInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'systemRoleType.label', default: 'SystemRoleType'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'systemRoleType.label', default: 'SystemRoleType'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'systemRoleType.label', default: 'SystemRoleType'), params.id])}"
            redirect(action: "list")
        }
    }
}
