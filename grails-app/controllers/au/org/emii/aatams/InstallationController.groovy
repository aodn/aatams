package au.org.emii.aatams

import au.org.emii.aatams.report.ReportController
import grails.converters.JSON

class InstallationController extends ReportController {
    def candidateEntitiesService
    def grailsApplication

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list =  {
        doList("installation")
    }

    def export = {
        doExport("installation")
    }

    def create = {
        def installationInstance = new Installation()
        installationInstance.properties = params

        def model =
            [installationInstance: installationInstance] + [candidateProjects:candidateEntitiesService.projects()]
        return model
    }

    def save = {
        def installationInstance = new Installation(params)
        if (installationInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'installation.label', default: 'Installation'), installationInstance.toString()])}"
            redirect(action: "show", id: installationInstance.id)
        }
        else {
            def model =
                [installationInstance: installationInstance] + [candidateProjects:candidateEntitiesService.projects()]
            render(view: "create", model: model)
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
        else  {
            def model = [installationInstance: installationInstance]
            model.candidateProjects = candidateEntitiesService.projects()
            return model
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
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'installation.label', default: 'Installation'), installationInstance.toString()])}"
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
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'installation.label', default: 'Installation'), installationInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'installation.label', default: 'Installation'), installationInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'installation.label', default: 'Installation'), params.id])}"
            redirect(action: "list")
        }
    }

    def lookupByName = {
        def matches = Installation.findAllByNameIlike('%' + params.term + '%')
        render(matches as JSON)
    }
}
