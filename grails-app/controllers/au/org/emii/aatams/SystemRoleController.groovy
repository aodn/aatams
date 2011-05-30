package au.org.emii.aatams

class SystemRoleController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [systemRoleInstanceList: SystemRole.list(params), systemRoleInstanceTotal: SystemRole.count()]
    }

    def create = {
        def systemRoleInstance = new SystemRole()
        systemRoleInstance.properties = params
        return [systemRoleInstance: systemRoleInstance]
    }

    def save = {
        def systemRoleInstance = new SystemRole(params)
        if (systemRoleInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'systemRole.label', default: 'SystemRole'), systemRoleInstance.id])}"
            redirect(action: "show", id: systemRoleInstance.id)
        }
        else {
            render(view: "create", model: [systemRoleInstance: systemRoleInstance])
        }
    }

    def show = {
        def systemRoleInstance = SystemRole.get(params.id)
        if (!systemRoleInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'systemRole.label', default: 'SystemRole'), params.id])}"
            redirect(action: "list")
        }
        else {
            [systemRoleInstance: systemRoleInstance]
        }
    }

    def edit = {
        def systemRoleInstance = SystemRole.get(params.id)
        if (!systemRoleInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'systemRole.label', default: 'SystemRole'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [systemRoleInstance: systemRoleInstance]
        }
    }

    def update = {
        def systemRoleInstance = SystemRole.get(params.id)
        if (systemRoleInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (systemRoleInstance.version > version) {
                    
                    systemRoleInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'systemRole.label', default: 'SystemRole')] as Object[], "Another user has updated this SystemRole while you were editing")
                    render(view: "edit", model: [systemRoleInstance: systemRoleInstance])
                    return
                }
            }
            systemRoleInstance.properties = params
            if (!systemRoleInstance.hasErrors() && systemRoleInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'systemRole.label', default: 'SystemRole'), systemRoleInstance.id])}"
                redirect(action: "show", id: systemRoleInstance.id)
            }
            else {
                render(view: "edit", model: [systemRoleInstance: systemRoleInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'systemRole.label', default: 'SystemRole'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def systemRoleInstance = SystemRole.get(params.id)
        if (systemRoleInstance) {
            try {
                systemRoleInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'systemRole.label', default: 'SystemRole'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'systemRole.label', default: 'SystemRole'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'systemRole.label', default: 'SystemRole'), params.id])}"
            redirect(action: "list")
        }
    }
}
