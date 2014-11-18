package au.org.emii.aatams

import grails.converters.JSON

import org.joda.time.format.DateTimeFormat

class SurgeryController extends AbstractController {

    static allowedMethods = [save: "POST", update: "POST", delete: ["POST", "GET"]]

    def detectionFactoryService
    def jdbcTemplateDetectionFactoryService
    def tagFactoryService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        doList("surgery")
    }

    def create = {
        def surgeryInstance = new Surgery()
        surgeryInstance.properties = params
        return [surgeryInstance: surgeryInstance]
    }

    def save =
    {
        def surgeryInstance = new Surgery(params)

        // Lookup or create tag (after inserting some required parameters)...
        params.tag['project'] = Project.get(params.projectId)
        params.tag['status'] = DeviceStatus.findByStatus('DEPLOYED')
        params.tag['transmitterType'] = TransmitterType.findByTransmitterTypeName('PINGER')

        def tag = null
        try
        {
            tag = tagFactoryService.lookupOrCreate(params.tag)
        }
        catch (IllegalArgumentException e)
        {
            log.error(e)
            render ([errors:[localizedMessage:"Invalid code name for tag: " + params.tag?.codeName]] as JSON)
            return
        }

        assert(tag): "tag cannot be null"
        surgeryInstance.tag = tag
        tag.addToSurgeries(surgeryInstance)

        if (surgeryInstance.save(flush: true))
        {
            flash.message = "${message(code: 'default.updated.message', args: [message(code: 'tagging.label', default: 'Tagging'), surgeryInstance.toString()])}"
            render ([instance:surgeryInstance, message:flash] as JSON)
        }
        else
        {
            log.error(surgeryInstance.errors)
            render ([errors:surgeryInstance.errors] as JSON)
        }
    }

    def show = {
        def surgeryInstance = Surgery.get(params.id)
        if (!surgeryInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'surgery.label', default: 'Surgery'), params.id])}"
            redirect(action: "list")
        }
        else {
            [surgeryInstance: surgeryInstance]
        }
    }

    def edit = {
        def surgeryInstance = Surgery.get(params.id)
        if (!surgeryInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'surgery.label', default: 'Surgery'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [surgeryInstance: surgeryInstance]
        }
    }

    def update = {
        def surgeryInstance = Surgery.get(params.id)
        if (surgeryInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (surgeryInstance.version > version) {

                    surgeryInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'surgery.label', default: 'Surgery')] as Object[], "Another user has updated this Surgery while you were editing")
                    render(view: "edit", model: [surgeryInstance: surgeryInstance])
                    return
                }
            }
            surgeryInstance.properties = params
            if (!surgeryInstance.hasErrors() && surgeryInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'surgery.label', default: 'Surgery'), surgeryInstance.toString()])}"
                def release = surgeryInstance?.release
                redirect(controller: "animalRelease", action: "edit", id: release?.id, params: [projectId:release?.project?.id])
            }
            else {
                render(view: "edit", model: [surgeryInstance: surgeryInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'surgery.label', default: 'Surgery'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def surgeryInstance = Surgery.get(params.id)
        if (surgeryInstance) {
            try {
                surgeryInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'surgery.label', default: 'Surgery'), surgeryInstance.toString()])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'surgery.label', default: 'Surgery'), surgeryInstance.toString()])}"
            }

            def release = surgeryInstance?.release
            redirect(controller: "animalRelease", action: "edit", id: release?.id, params: [projectId:release?.project?.id])
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'surgery.label', default: 'Surgery'), params.id])}"
            redirect(action: "list")
        }
    }
}
