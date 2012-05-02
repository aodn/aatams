package au.org.emii.aatams

import au.org.emii.aatams.report.ReportController

class ReceiverEventController extends ReportController
{
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = 
	{
		doList("receiverEvent")
	}
	
	def export =
	{
		doExport("receiverEvent")
	}

    def create = {
        redirect(controller:"receiverDownloadFile", 
                 action:"createEvents") 
    }

    def save = {
        def receiverEventInstance = new ReceiverEvent(params)
        if (receiverEventInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'receiverEvent.label', default: 'ReceiverEvent'), receiverEventInstance.toString()])}"
            redirect(action: "show", id: receiverEventInstance.id)
        }
        else {
            render(view: "create", model: [receiverEventInstance: receiverEventInstance])
        }
    }

    def show = {
        def receiverEventInstance = ReceiverEvent.get(params.id)
        if (!receiverEventInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverEvent.label', default: 'ReceiverEvent'), params.id])}"
            redirect(action: "list")
        }
        else {
            [receiverEventInstance: receiverEventInstance]
        }
    }

    def edit = {
        def receiverEventInstance = ReceiverEvent.get(params.id)
        if (!receiverEventInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverEvent.label', default: 'ReceiverEvent'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [receiverEventInstance: receiverEventInstance]
        }
    }

    def update = {
        def receiverEventInstance = ReceiverEvent.get(params.id)
        if (receiverEventInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (receiverEventInstance.version > version) {
                    
                    receiverEventInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'receiverEvent.label', default: 'ReceiverEvent')] as Object[], "Another user has updated this ReceiverEvent while you were editing")
                    render(view: "edit", model: [receiverEventInstance: receiverEventInstance])
                    return
                }
            }
            receiverEventInstance.properties = params
            if (!receiverEventInstance.hasErrors() && receiverEventInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'receiverEvent.label', default: 'ReceiverEvent'), receiverEventInstance.toString()])}"
                redirect(action: "show", id: receiverEventInstance.id)
            }
            else {
                render(view: "edit", model: [receiverEventInstance: receiverEventInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverEvent.label', default: 'ReceiverEvent'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def receiverEventInstance = ReceiverEvent.get(params.id)
        if (receiverEventInstance) {
            try {
                receiverEventInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'receiverEvent.label', default: 'ReceiverEvent'), receiverEventInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'receiverEvent.label', default: 'ReceiverEvent'), receiverEventInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverEvent.label', default: 'ReceiverEvent'), params.id])}"
            redirect(action: "list")
        }
    }
}
