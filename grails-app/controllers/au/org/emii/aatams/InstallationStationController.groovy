package au.org.emii.aatams

class InstallationStationController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [installationStationInstanceList: InstallationStation.list(params), installationStationInstanceTotal: InstallationStation.count()]
    }

    def create = {
        def installationStationInstance = new InstallationStation()
        installationStationInstance.properties = params
        return [installationStationInstance: installationStationInstance]
    }

    def save = {
        def installationStationInstance = new InstallationStation(params)
        if (installationStationInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'installationStation.label', default: 'InstallationStation'), installationStationInstance.id])}"
            redirect(action: "show", id: installationStationInstance.id)
        }
        else {
            render(view: "create", model: [installationStationInstance: installationStationInstance])
        }
    }

    def show = {
        def installationStationInstance = InstallationStation.get(params.id)
        if (!installationStationInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'installationStation.label', default: 'InstallationStation'), params.id])}"
            redirect(action: "list")
        }
        else {
            [installationStationInstance: installationStationInstance]
        }
    }

    def edit = {
        def installationStationInstance = InstallationStation.get(params.id)
        if (!installationStationInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'installationStation.label', default: 'InstallationStation'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [installationStationInstance: installationStationInstance]
        }
    }

    def update = 
    {
        log.debug("params: " + params)
        
        def installationStationInstance = InstallationStation.get(params.id)
        if (installationStationInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (installationStationInstance.version > version) {
                    
                    installationStationInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'installationStation.label', default: 'InstallationStation')] as Object[], "Another user has updated this InstallationStation while you were editing")
                    render(view: "edit", model: [installationStationInstance: installationStationInstance])
                    return
                }
            }
            installationStationInstance.properties = params
            if (!installationStationInstance.hasErrors() && installationStationInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'installationStation.label', default: 'InstallationStation'), installationStationInstance.id])}"
                redirect(action: "show", id: installationStationInstance.id)
            }
            else {
                render(view: "edit", model: [installationStationInstance: installationStationInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'installationStation.label', default: 'InstallationStation'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def installationStationInstance = InstallationStation.get(params.id)
        if (installationStationInstance) {
            try {
                installationStationInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'installationStation.label', default: 'InstallationStation'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'installationStation.label', default: 'InstallationStation'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'installationStation.label', default: 'InstallationStation'), params.id])}"
            redirect(action: "list")
        }
    }
}
