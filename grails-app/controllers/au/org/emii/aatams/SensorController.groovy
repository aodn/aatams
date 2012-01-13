package au.org.emii.aatams

import grails.converters.JSON

class SensorController extends AbstractController 
{
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
        return [sensorInstance: sensorInstance]
    }

    def save = 
	{
		def tag = tagFactoryService.lookupOrCreate(params.tag)
		assert(tag)
		
		params.tag = tag
		
        def sensorInstance = new Sensor(params)
        
		if (sensorInstance.save(flush: true)) 
        {
			sensorInstance.save()
			
            flash.message = "${message(code: 'default.updated.message', args: [message(code: 'sensor.label', default: 'Sensor'), sensorInstance.toString()])}"
//            render ([instance:sensorInstance, message:flash] as JSON)
            redirect(controller: "tag", action: "show", id: sensorInstance.tag?.id)
        }
        else 
        {
			println(tag.errors)
            log.error(sensorInstance.errors)
            render ([errors:sensorInstance.errors] as JSON)
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
            return [sensorInstance: sensorInstance]
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
}
