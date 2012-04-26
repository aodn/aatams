package au.org.emii.aatams

import org.hibernate.criterion.CriteriaSpecification
import org.hibernate.criterion.Restrictions
import org.joda.time.*

import com.vividsolutions.jts.geom.Point

class ReceiverRecoveryController extends AbstractController
{
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def candidateEntitiesService
    def sessionFactory
	
    def index = {
        redirect(action: "list", params: params)
    }
	
    def list = 
	{
		doList("receiverRecovery") + [readableProjects:candidateEntitiesService.readableProjects()]
	}
	
    def create = 
    {
        ReceiverDeployment deployment = ReceiverDeployment.get(params.deploymentId)
        
        def receiverRecoveryInstance = new ReceiverRecovery()
        receiverRecoveryInstance.properties = params
        receiverRecoveryInstance.deployment = deployment
		
		receiverRecoveryInstance.location = determineDefaultLocation(deployment)
		
        return [receiverRecoveryInstance: receiverRecoveryInstance]
    }

	private Point determineDefaultLocation(deployment)
	{
		assert(deployment)
		
		if (deployment.location)
		{
			return deployment.location
		}
		
		return deployment.station?.location
	}
	
    def save = 
    {
        ReceiverDeployment deployment = ReceiverDeployment.get(params.deploymentId)
		deployment.properties = params.deployment
		
        log.debug("deployment: " + deployment)
		
        def receiverRecoveryInstance = new ReceiverRecovery(params)
        receiverRecoveryInstance.deployment = deployment

        deployment?.recovery = receiverRecoveryInstance
        
        if (deployment?.save(flush: true)) 
        {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'receiverRecovery.label', default: 'ReceiverRecovery'), receiverRecoveryInstance.toString()])}"
            redirect(action: "show", id: receiverRecoveryInstance.id)
        }
        else 
		{
            render(view: "create", model: [receiverRecoveryInstance: receiverRecoveryInstance])
        }
    }

    def show = {
        def receiverRecoveryInstance = ReceiverRecovery.get(params.id)
        if (!receiverRecoveryInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverRecovery.label', default: 'ReceiverRecovery'), params.id])}"
            redirect(action: "list")
        }
        else {
            [receiverRecoveryInstance: receiverRecoveryInstance]
        }
    }

    def edit = {
        def receiverRecoveryInstance = ReceiverRecovery.get(params.id)
        if (!receiverRecoveryInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverRecovery.label', default: 'ReceiverRecovery'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [receiverRecoveryInstance: receiverRecoveryInstance]
        }
    }

    def update = {
        def receiverRecoveryInstance = ReceiverRecovery.get(params.id)
        if (receiverRecoveryInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (receiverRecoveryInstance.version > version) {
                    
                    receiverRecoveryInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'receiverRecovery.label', default: 'ReceiverRecovery')] as Object[], "Another user has updated this ReceiverRecovery while you were editing")
                    render(view: "edit", model: [receiverRecoveryInstance: receiverRecoveryInstance])
                    return
                }
            }
			
            receiverRecoveryInstance.properties = params
			receiverRecoveryInstance.deployment.properties = params.deployment
			
            if (   !receiverRecoveryInstance.deployment.hasErrors() 
				&& !receiverRecoveryInstance.hasErrors() 
				&& receiverRecoveryInstance.deployment.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'receiverRecovery.label', default: 'ReceiverRecovery'), receiverRecoveryInstance.toString()])}"
                redirect(action: "show", id: receiverRecoveryInstance.id)
            }
            else {
                render(view: "edit", model: [receiverRecoveryInstance: receiverRecoveryInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverRecovery.label', default: 'ReceiverRecovery'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def receiverRecoveryInstance = ReceiverRecovery.get(params.id)
        if (receiverRecoveryInstance) {
            try 
            {
                def deployment = receiverRecoveryInstance.deployment
                deployment.recovery = null
                deployment.save()
                
                receiverRecoveryInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'receiverRecovery.label', default: 'ReceiverRecovery'), receiverRecoveryInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'receiverRecovery.label', default: 'ReceiverRecovery'), receiverRecoveryInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverRecovery.label', default: 'ReceiverRecovery'), params.id])}"
            redirect(action: "list")
        }
    }
}
