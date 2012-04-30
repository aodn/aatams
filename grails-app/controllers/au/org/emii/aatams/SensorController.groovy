package au.org.emii.aatams

import au.org.emii.aatams.report.ReportController;
import grails.converters.JSON

class SensorController extends ReportController
{
	def candidateEntitiesService
    def tagFactoryService
	
    static allowedMethods = [save: "POST", update: "POST", delete: ["POST", "GET"]]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = 
	{
		doList("sensor")
	}

    def create = {
        def sensorInstance = new Sensor()
        sensorInstance.properties = params
        return [sensorInstance: sensorInstance, 
                candidateProjects:candidateEntitiesService.projects()]
    }

    def save = 
	{
		def tag = tagFactoryService.lookupOrCreate(params.tag)
		assert(tag)
		
		params.tag = tag
		
        def sensorInstance = new Sensor(params)
		
		// Workaround for http://jira.grails.org/browse/GRAILS-3783
		tag.addToSensors(sensorInstance)

		if (sensorInstance.save(flush: true)) 
        {
			flash.message = "${message(code: 'default.updated.message', args: [message(code: 'sensor.label', default: 'Tag'), sensorInstance.toString()])}"
			
			if (params.responseType == 'json')
			{
				render ([instance:sensorInstance, message:flash, tag:[id: tag.id]] as JSON)
			}
			else
			{
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'sensor.label', default: 'Tag'), sensorInstance.toString()])}"
				redirect(controller: "tag", action: "show", id: sensorInstance.tag?.id)
			}
        }
        else 
        {
            log.error(sensorInstance.errors)
			
			if (params.responseType == 'json')
			{
				render ([errors:sensorInstance.errors] as JSON)
			}
			else
			{
				def model =  [sensorInstance: sensorInstance,
							  candidateProjects:candidateEntitiesService.projects()]
				render(view: "create", model: model)
			}
        }
    }

    def show = {
        def sensorInstance = Sensor.get(params.id)
        if (!sensorInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sensor.label', default: 'Sensor'), params.id])}"
            redirect(action: "list")
        }
        else {
            [sensorInstance: sensorInstance]
        }
    }

    def edit = {
        def sensorInstance = Sensor.get(params.id)
        if (!sensorInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sensor.label', default: 'Sensor'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [sensorInstance: sensorInstance, candidateProjects: candidateEntitiesService.projects()]
        }
    }

    def update = {
        def sensorInstance = Sensor.get(params.id)
        if (sensorInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (sensorInstance.version > version) {
                    
                    sensorInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'sensor.label', default: 'Sensor')] as Object[], "Another user has updated this Sensor while you were editing")
                    render(view: "edit", model: [sensorInstance: sensorInstance])
                    return
                }
            }
            sensorInstance.properties = params
            if (!sensorInstance.hasErrors() && sensorInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'sensor.label', default: 'Sensor'), sensorInstance.toString()])}"
                def tagId = sensorInstance?.tag?.id
                redirect(controller: "tag", action: "edit", id: tagId, params: [projectId:sensorInstance?.project?.id])
                
            }
            else {
                render(view: "edit", model: [sensorInstance: sensorInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sensor.label', default: 'Sensor'), params.id])}"
            redirect(action: "list")
        }
	}

    def delete = {
        def sensorInstance = Sensor.get(params.id)
        if (sensorInstance) {
            try {
                sensorInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'sensor.label', default: 'Sensor'), sensorInstance.toString()])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'sensor.label', default: 'Sensor'), sensorInstance.toString()])}"
            }
            
            def tagId = sensorInstance?.tag?.id
            redirect(controller: "tag", action: "edit", id: tagId, params: [projectId:sensorInstance?.project?.id])
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sensor.label', default: 'Sensor'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def lookupByTransmitterId =
	{
		def sensors = Sensor.findAllByTransmitterIdIlike(params.term + "%", [sort: "transmitterId"])
		
		// Limit so that all results fit on screen.
		if (sensors?.size() > 20)
		{
			sensors = sensors[0..19]
		}
		
		render sensors as JSON
	}
}
