package au.org.emii.aatams

class InstallationController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [installationInstanceList: Installation.list(params), installationInstanceTotal: Installation.count()]
    }

    def create = {
        def installationInstance = new Installation()
        installationInstance.properties = params
        return [installationInstance: installationInstance]
    }

    def save = {
        def installationInstance = new Installation(params)
        if (installationInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'installation.label', default: 'Installation'), installationInstance.id])}"
            redirect(action: "show", id: installationInstance.id)
        }
        else {
            render(view: "create", model: [installationInstance: installationInstance])
        }
    }

    def show = {
        def installationInstance = Installation.get(params.id)
        if (!installationInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'installation.label', default: 'Installation'), params.id])}"
            redirect(action: "list")
        }
        else {
            [installationInstance: installationInstance]
        }
    }

    def edit = {
        def installationInstance = Installation.get(params.id)
        if (!installationInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'installation.label', default: 'Installation'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [installationInstance: installationInstance]
        }
    }

    def update = {
        def installationInstance = Installation.get(params.id)
        if (installationInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (installationInstance.version > version) {
                    
                    installationInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'installation.label', default: 'Installation')] as Object[], "Another user has updated this Installation while you were editing")
                    render(view: "edit", model: [installationInstance: installationInstance])
                    return
                }
            }
            installationInstance.properties = params
            if (!installationInstance.hasErrors() && installationInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'installation.label', default: 'Installation'), installationInstance.id])}"
                redirect(action: "show", id: installationInstance.id)
            }
            else {
                render(view: "edit", model: [installationInstance: installationInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'installation.label', default: 'Installation'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def installationInstance = Installation.get(params.id)
        if (installationInstance) {
            try {
                installationInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'installation.label', default: 'Installation'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'installation.label', default: 'Installation'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'installation.label', default: 'Installation'), params.id])}"
            redirect(action: "list")
        }
    }
}
