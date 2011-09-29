package au.org.emii.aatams

class InstallationConfigurationController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
        [installationConfigurationInstanceList: InstallationConfiguration.list(params), installationConfigurationInstanceTotal: InstallationConfiguration.count()]
    }

    def create = {
        def installationConfigurationInstance = new InstallationConfiguration()
        installationConfigurationInstance.properties = params
        return [installationConfigurationInstance: installationConfigurationInstance]
    }

    def save = {
        def installationConfigurationInstance = new InstallationConfiguration(params)
        if (installationConfigurationInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'installationConfiguration.label', default: 'InstallationConfiguration'), installationConfigurationInstance.toString()])}"
            redirect(action: "show", id: installationConfigurationInstance.id)
        }
        else {
            render(view: "create", model: [installationConfigurationInstance: installationConfigurationInstance])
        }
    }

    def show = {
        def installationConfigurationInstance = InstallationConfiguration.get(params.id)
        if (!installationConfigurationInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'installationConfiguration.label', default: 'InstallationConfiguration'), params.id])}"
            redirect(action: "list")
        }
        else {
            [installationConfigurationInstance: installationConfigurationInstance]
        }
    }

    def edit = {
        def installationConfigurationInstance = InstallationConfiguration.get(params.id)
        if (!installationConfigurationInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'installationConfiguration.label', default: 'InstallationConfiguration'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [installationConfigurationInstance: installationConfigurationInstance]
        }
    }

    def update = {
        def installationConfigurationInstance = InstallationConfiguration.get(params.id)
        if (installationConfigurationInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (installationConfigurationInstance.version > version) {
                    
                    installationConfigurationInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'installationConfiguration.label', default: 'InstallationConfiguration')] as Object[], "Another user has updated this InstallationConfiguration while you were editing")
                    render(view: "edit", model: [installationConfigurationInstance: installationConfigurationInstance])
                    return
                }
            }
            installationConfigurationInstance.properties = params
            if (!installationConfigurationInstance.hasErrors() && installationConfigurationInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'installationConfiguration.label', default: 'InstallationConfiguration'), installationConfigurationInstance.toString()])}"
                redirect(action: "show", id: installationConfigurationInstance.id)
            }
            else {
                render(view: "edit", model: [installationConfigurationInstance: installationConfigurationInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'installationConfiguration.label', default: 'InstallationConfiguration'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def installationConfigurationInstance = InstallationConfiguration.get(params.id)
        if (installationConfigurationInstance) {
            try {
                installationConfigurationInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'installationConfiguration.label', default: 'InstallationConfiguration'), installationConfigurationInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'installationConfiguration.label', default: 'InstallationConfiguration'), installationConfigurationInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'installationConfiguration.label', default: 'InstallationConfiguration'), params.id])}"
            redirect(action: "list")
        }
    }
}
