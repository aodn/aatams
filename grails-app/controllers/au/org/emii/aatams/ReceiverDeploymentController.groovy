package au.org.emii.aatams

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

class ReceiverDeploymentController extends AbstractController
{
    def candidateEntitiesService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = 
	{
		doList("receiverDeployment")
	}
	
    def create = {
        def receiverDeploymentInstance = new ReceiverDeployment()
        receiverDeploymentInstance.properties = params
        
        renderCreateWithDefaultModel(receiverDeploymentInstance)
    }

    def save = 
    {
        def receiverDeploymentInstance = new ReceiverDeployment(params)

		if (receiverDeploymentInstance.receiver)
		{
	        if (!receiverDeploymentInstance.receiver?.canDeploy())
	        {
	            flash.message = "${message(code: 'default.invalidState.receiver', \
	                                       args: [receiverDeploymentInstance?.receiver?.toString(), \
	                                              receiverDeploymentInstance?.receiver?.status?.toString()])}"
	            receiverDeploymentInstance.receiver = null
	            renderCreateWithDefaultModel(receiverDeploymentInstance)
	            return
	        }
	        
			incNumDeployments(receiverDeploymentInstance)
		}
		
        if (receiverDeploymentInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'receiverDeployment.label', default: 'ReceiverDeployment'), receiverDeploymentInstance.toString()])}"
            redirect(action: "show", id: receiverDeploymentInstance.id)
        }
        else 
        {
            renderCreateWithDefaultModel(receiverDeploymentInstance)
        }
    }

	private void incNumDeployments(ReceiverDeployment deployment) 
	{
		if (deployment?.station?.numDeployments != null)
		{
			deployment?.station?.numDeployments =
					deployment?.station?.numDeployments + 1
			deployment?.station?.save()
		}

		// And record the deployment number against the actual deployment.
		deployment?.deploymentNumber = deployment?.station?.numDeployments
		
		addReceiverToStation(deployment)
	}

	private addReceiverToStation(ReceiverDeployment deployment) 
	{
		deployment.station.addToReceivers(deployment.receiver)
		
		// Need to update that status of the receiver to DEPLOYED.
		deployment.receiver?.status = DeviceStatus.findByStatus('DEPLOYED')
	}

	private removeReceiverFromStation(ReceiverDeployment deployment) 
	{
		deployment.station.removeFromReceivers(deployment.receiver)
		deployment.receiver.status = DeviceStatus.findByStatus("NEW")
		deployment.receiver.save()
	}

	private renderCreateWithDefaultModel(ReceiverDeployment receiverDeploymentInstance) 
	{
		def model = renderDefaultModel(receiverDeploymentInstance)

		render(view: "create", model: model)
	}

	private Map renderDefaultModel(ReceiverDeployment receiverDeploymentInstance) 
	{
		def model =
				[receiverDeploymentInstance: receiverDeploymentInstance] +
				[candidateStations:candidateEntitiesService.stations(),
				 candidateReceivers:getCandidateReceivers(receiverDeploymentInstance)]
		return model
	}
	
	private Collection<Receiver> getCandidateReceivers(deployment)
	{
		def retList = candidateEntitiesService.receivers()
		if (deployment.receiver)
		{
			retList += deployment.receiver
		}
		
		return retList.unique({ a, b -> a.id <=> b.id })
		
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
			renderDefaultModel(receiverDeploymentInstance)
        }
    }

    def update = {
        
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
			
			removeReceiverFromStation(receiverDeploymentInstance)
            receiverDeploymentInstance.properties = params
			addReceiverToStation(receiverDeploymentInstance)
			
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
