package au.org.emii.aatams

class ProjectRoleTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
        [projectRoleTypeInstanceList: ProjectRoleType.list(params), projectRoleTypeInstanceTotal: ProjectRoleType.count()]
    }

    def create = {
        def projectRoleTypeInstance = new ProjectRoleType()
        projectRoleTypeInstance.properties = params
        return [projectRoleTypeInstance: projectRoleTypeInstance]
    }

    def save = {
        def projectRoleTypeInstance = new ProjectRoleType(params)
        if (projectRoleTypeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'projectRoleType.label', default: 'ProjectRoleType'), projectRoleTypeInstance.toString()])}"
            redirect(action: "show", id: projectRoleTypeInstance.id)
        }
        else {
            render(view: "create", model: [projectRoleTypeInstance: projectRoleTypeInstance])
        }
    }

    def show = {
        def projectRoleTypeInstance = ProjectRoleType.get(params.id)
        if (!projectRoleTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'projectRoleType.label', default: 'ProjectRoleType'), params.id])}"
            redirect(action: "list")
        }
        else {
            [projectRoleTypeInstance: projectRoleTypeInstance]
        }
    }

    def edit = {
        def projectRoleTypeInstance = ProjectRoleType.get(params.id)
        if (!projectRoleTypeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'projectRoleType.label', default: 'ProjectRoleType'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [projectRoleTypeInstance: projectRoleTypeInstance]
        }
    }

    def update = {
        def projectRoleTypeInstance = ProjectRoleType.get(params.id)
        if (projectRoleTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (projectRoleTypeInstance.version > version) {

                    projectRoleTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'projectRoleType.label', default: 'ProjectRoleType')] as Object[], "Another user has updated this ProjectRoleType while you were editing")
                    render(view: "edit", model: [projectRoleTypeInstance: projectRoleTypeInstance])
                    return
                }
            }
            projectRoleTypeInstance.properties = params
            if (!projectRoleTypeInstance.hasErrors() && projectRoleTypeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'projectRoleType.label', default: 'ProjectRoleType'), projectRoleTypeInstance.toString()])}"
                redirect(action: "show", id: projectRoleTypeInstance.id)
            }
            else {
                render(view: "edit", model: [projectRoleTypeInstance: projectRoleTypeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'projectRoleType.label', default: 'ProjectRoleType'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def projectRoleTypeInstance = ProjectRoleType.get(params.id)
        if (projectRoleTypeInstance) {
            try {
                projectRoleTypeInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'projectRoleType.label', default: 'ProjectRoleType'), projectRoleTypeInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'projectRoleType.label', default: 'ProjectRoleType'), projectRoleTypeInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'projectRoleType.label', default: 'ProjectRoleType'), params.id])}"
            redirect(action: "list")
        }
    }
}
