package au.org.emii.aatams

class ProjectRoleController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [projectRoleInstanceList: ProjectRole.list(params), projectRoleInstanceTotal: ProjectRole.count()]
    }

    def create = {
        def projectRoleInstance = new ProjectRole()
        projectRoleInstance.properties = params
        return [projectRoleInstance: projectRoleInstance]
    }

    def save = {
        def projectRoleInstance = new ProjectRole(params)
        if (projectRoleInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'projectRole.label', default: 'ProjectRole'), projectRoleInstance.id])}"
            redirect(action: "show", id: projectRoleInstance.id)
        }
        else {
            render(view: "create", model: [projectRoleInstance: projectRoleInstance])
        }
    }

    def show = {
        def projectRoleInstance = ProjectRole.get(params.id)
        if (!projectRoleInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'projectRole.label', default: 'ProjectRole'), params.id])}"
            redirect(action: "list")
        }
        else {
            [projectRoleInstance: projectRoleInstance]
        }
    }

    def edit = {
        def projectRoleInstance = ProjectRole.get(params.id)
        if (!projectRoleInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'projectRole.label', default: 'ProjectRole'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [projectRoleInstance: projectRoleInstance]
        }
    }

    def update = {
        def projectRoleInstance = ProjectRole.get(params.id)
        if (projectRoleInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (projectRoleInstance.version > version) {
                    
                    projectRoleInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'projectRole.label', default: 'ProjectRole')] as Object[], "Another user has updated this ProjectRole while you were editing")
                    render(view: "edit", model: [projectRoleInstance: projectRoleInstance])
                    return
                }
            }
            projectRoleInstance.properties = params
            if (!projectRoleInstance.hasErrors() && projectRoleInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'projectRole.label', default: 'ProjectRole'), projectRoleInstance.id])}"
                redirect(action: "show", id: projectRoleInstance.id)
            }
            else {
                render(view: "edit", model: [projectRoleInstance: projectRoleInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'projectRole.label', default: 'ProjectRole'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def projectRoleInstance = ProjectRole.get(params.id)
        if (projectRoleInstance) {
            try {
                projectRoleInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'projectRole.label', default: 'ProjectRole'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'projectRole.label', default: 'ProjectRole'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'projectRole.label', default: 'ProjectRole'), params.id])}"
            redirect(action: "list")
        }
    }
}
