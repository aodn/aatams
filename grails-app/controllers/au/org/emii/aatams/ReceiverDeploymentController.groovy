package au.org.emii.aatams

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

class ReceiverDeploymentController {

    def candidateEntitiesService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
        [receiverDeploymentInstanceList: ReceiverDeployment.list(params), receiverDeploymentInstanceTotal: ReceiverDeployment.count()]
    }

    def create = {
        def receiverDeploymentInstance = new ReceiverDeployment()
        receiverDeploymentInstance.properties = params
        
        def model =
            [receiverDeploymentInstance: receiverDeploymentInstance] +
            [candidateStations:candidateEntitiesService.stations(),
             candidateReceivers:candidateEntitiesService.receivers()]
         
        return model
    }

    def save = 
    {
        def receiverDeploymentInstance = new ReceiverDeployment(params)

        if (!receiverDeploymentInstance.receiver?.canDeploy())
        {
            flash.message = "${message(code: 'default.invalidState.receiver', \
                                       args: [receiverDeploymentInstance?.receiver?.toString(), \
                                              receiverDeploymentInstance?.receiver?.status?.toString()])}"
            receiverDeploymentInstance.receiver = null
            def model =
                [receiverDeploymentInstance: receiverDeploymentInstance] +
                [candidateStations:candidateEntitiesService.stations(),
                 candidateReceivers:candidateEntitiesService.receivers()]

            render(view: "create", model: model)
            return
        }
        
        // Need to update that status of the receiver to DEPLOYED.
        receiverDeploymentInstance.receiver?.status = DeviceStatus.findByStatus('DEPLOYED')

        // Increment that station's number of deployments.
        if (receiverDeploymentInstance?.station?.numDeployments)
        {
            receiverDeploymentInstance?.station?.numDeployments =
                receiverDeploymentInstance?.station?.numDeployments + 1
            receiverDeploymentInstance?.station?.save()
        }
        
        // And record the deployment number against the actual deployment.
        receiverDeploymentInstance?.deploymentNumber = receiverDeploymentInstance?.station?.numDeployments
        
        if (receiverDeploymentInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'receiverDeployment.label', default: 'ReceiverDeployment'), receiverDeploymentInstance.toString()])}"
            redirect(action: "show", id: receiverDeploymentInstance.id)
        }
        else 
        {
            render(view: "create", model: [receiverDeploymentInstance: receiverDeploymentInstance])
        }
    }

    def show = {
        def receiverDeploymentInstance = ReceiverDeployment.get(params.id)
        if (!receiverDeploymentInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDeployment.label', default: 'ReceiverDeployment'), params.id])}"
            redirect(action: "list")
        }
        else {
            [receiverDeploymentInstance: receiverDeploymentInstance]
        }
    }

    def edit = {
        def receiverDeploymentInstance = ReceiverDeployment.get(params.id)
        if (!receiverDeploymentInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDeployment.label', default: 'ReceiverDeployment'), params.id])}"
            redirect(action: "list")
        }
        else {
            def model = [receiverDeploymentInstance: receiverDeploymentInstance]
            model.candidateStations = candidateEntitiesService.stations()
            model.candidateReceivers = candidateEntitiesService.receivers()
            return model
        }
    }

    def update = {
        
        log.debug("params: " + params)
        
        def receiverDeploymentInstance = ReceiverDeployment.get(params.id)
        if (receiverDeploymentInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (receiverDeploymentInstance.version > version) {
                    
                    receiverDeploymentInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'receiverDeployment.label', default: 'ReceiverDeployment')] as Object[], "Another user has updated this ReceiverDeployment while you were editing")
                    render(view: "edit", model: [receiverDeploymentInstance: receiverDeploymentInstance])
                    return
                }
            }
            receiverDeploymentInstance.properties = params
            
            if (!receiverDeploymentInstance.hasErrors() && receiverDeploymentInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'receiverDeployment.label', default: 'ReceiverDeployment'), receiverDeploymentInstance.toString()])}"
                redirect(action: "show", id: receiverDeploymentInstance.id)
            }
            else {
                render(view: "edit", model: [receiverDeploymentInstance: receiverDeploymentInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDeployment.label', default: 'ReceiverDeployment'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def receiverDeploymentInstance = ReceiverDeployment.get(params.id)
        if (receiverDeploymentInstance) {
            try {
                receiverDeploymentInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'receiverDeployment.label', default: 'ReceiverDeployment'), receiverDeploymentInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'receiverDeployment.label', default: 'ReceiverDeployment'), receiverDeploymentInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDeployment.label', default: 'ReceiverDeployment'), params.id])}"
            redirect(action: "list")
        }
    }
}
