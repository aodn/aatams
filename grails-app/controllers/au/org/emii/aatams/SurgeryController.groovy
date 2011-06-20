package au.org.emii.aatams

//import grails.converters.deep.JSON
import grails.converters.JSON

class SurgeryController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [surgeryInstanceList: Surgery.list(params), surgeryInstanceTotal: Surgery.count()]
    }

    def create = {
        def surgeryInstance = new Surgery()
        surgeryInstance.properties = params
        return [surgeryInstance: surgeryInstance]
    }

    def save = 
    {
        def surgeryInstance = new Surgery(params)
        if (surgeryInstance.save(flush: true)) 
        {
            // Deep rendering of object not working due to geometry type
            // Need to use custom object marshaller.
            JSON.registerObjectMarshaller(Surgery.class)
            {
                def returnArray = [:]
                returnArray['id'] = it.id
                returnArray['timestamp'] = it.timestamp
                returnArray['tag'] = it.tag
                returnArray['type'] = it.type
                returnArray['sutures'] = it.sutures
                returnArray['treatmentType'] = it.treatmentType
                returnArray['surgeon'] = it.surgeon
                returnArray['comments'] = it.comments
                
                return returnArray
            }
            
            render surgeryInstance as JSON
        }
        else 
        {
            log.error(surgeryInstance.errors)
            render(view: "create", model: [surgeryInstance: surgeryInstance])
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
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'surgery.label', default: 'Surgery'), surgeryInstance.id])}"
                redirect(action: "show", id: surgeryInstance.id)
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
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'surgery.label', default: 'Surgery'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'surgery.label', default: 'Surgery'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'surgery.label', default: 'Surgery'), params.id])}"
            redirect(action: "list")
        }
    }
}
