package au.org.emii.aatams

import au.org.emii.aatams.report.ReportController
import grails.converters.JSON

class InstallationStationController  extends ReportController
{

    def candidateEntitiesService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = 
    {
        doList("installationStation")
    }

    def export =
    {
        doExport("installationStation")
    }
    
    def create = {
        def installationStationInstance = new InstallationStation()
        installationStationInstance.properties = params
        
        def model =
            [installationStationInstance: installationStationInstance] + [candidateInstallations:candidateEntitiesService.installations()]
        return model
    }

    def save = {
        def installationStationInstance = new InstallationStation(params)
        if (installationStationInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'installationStation.label', default: 'InstallationStation'), installationStationInstance.toString()])}"
            redirect(action: "show", id: installationStationInstance.id)
        }
        else {
            def model =
                [installationStationInstance: installationStationInstance] + [candidateInstallations:candidateEntitiesService.installations()]
            render(view: "create", model: model)
        }
    }

    def show = {
        log.debug("params: " + params)
        
        def installationStationInstance = InstallationStation.get(params.id)
        if (!installationStationInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'installationStation.label', default: 'InstallationStation'), params.id])}"
            redirect(action: "list")
        }
        else 
        {
            if (params.encoding && (params.encoding == 'json'))
            {
                render([installationStationInstance: installationStationInstance] as JSON)
            }
            else
            {
                [installationStationInstance: installationStationInstance]
            }
        }
    }

    def edit = {
        def installationStationInstance = InstallationStation.get(params.id)
        if (!installationStationInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'installationStation.label', default: 'InstallationStation'), params.id])}"
            redirect(action: "list")
        }
        else {
            def model =[installationStationInstance: installationStationInstance]
            model.candidateInstallations = candidateEntitiesService.installations()
            return model
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
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'installationStation.label', default: 'InstallationStation'), installationStationInstance.toString()])}"
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
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'installationStation.label', default: 'InstallationStation'), installationStationInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'installationStation.label', default: 'InstallationStation'), installationStationInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'installationStation.label', default: 'InstallationStation'), params.id])}"
            redirect(action: "list")
        }
    }

    def lookupByName =
    {
        def matches = InstallationStation.findAllByNameIlike('%' + params.term + '%')
        render(matches as JSON) 
    }
}
