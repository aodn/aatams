package au.org.emii.aatams

import org.joda.time.*

class ReceiverRecoveryController 
{

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def candidateEntitiesService
    
    def index = {
        redirect(action: "list", params: params)
    }
    
    def passesFilter(it, params)
    {
        // Filter by project (if specified).
        def projectId = params.filter?.project?.id
        if (projectId)
        {
            projectId = Integer.valueOf(projectId)  // Not sure why it's a String initially?
            
            if (it.station?.installation?.project?.id != projectId)
            {
                // Wrong project, filter out.
                return false
            }
        }

        // Filter by recovery status (if specified).
        def unrecoveredOnly = params.filter?.unrecoveredOnly
        if (unrecoveredOnly && it.recovery)
        {
            // This deployment has been recovered, filter out.
            return false
        }

        return true
    }
    
    def filter = 
    {
        log.debug("Filter parameters: " + params.filter)

        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        // We actually want to display a list of deployments (some with and some
        // without associated recoveries).
        // TODO: sort(hasRecovery, date)
        def receiverDeploymentList = ReceiverDeployment.list(params)

        // Filter...
        receiverDeploymentList = receiverDeploymentList.grep
        {
            return passesFilter(it, params)
        }
        
        // Determine total matching deployments.
        def receiverDeploymentInstanceTotal = 0
        ReceiverDeployment.list().each
        {
            if (passesFilter(it, params))
            {
                receiverDeploymentInstanceTotal++
            }
        }
        
        render(view:"list", model:
        [receiverDeploymentInstanceList: receiverDeploymentList, 
         receiverDeploymentInstanceTotal: receiverDeploymentInstanceTotal,
         readableProjects:candidateEntitiesService.readableProjects(),
         selectedProjectId:params.filter?.project?.id,
         unrecoveredOnly:params.filter?.unrecoveredOnly])
        
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
//        [receiverRecoveryInstanceList: ReceiverRecovery.list(params), receiverRecoveryInstanceTotal: ReceiverRecovery.count()]

        // We actually want to display a list of deployments (some with and some
        // without associated recoveries).
        // TODO: sort(hasRecovery, date)
        def receiverDeploymentList = ReceiverDeployment.list(params)
                                   
        [receiverDeploymentInstanceList: receiverDeploymentList, 
         receiverDeploymentInstanceTotal: ReceiverDeployment.count(),
         readableProjects:candidateEntitiesService.readableProjects()]
    }

    def create = 
    {
        ReceiverDeployment deployment = ReceiverDeployment.get(params.deploymentId)
        
        def receiverRecoveryInstance = new ReceiverRecovery()
        receiverRecoveryInstance.properties = params
        receiverRecoveryInstance.deployment = deployment
        
        return [receiverRecoveryInstance: receiverRecoveryInstance]
    }

    def save = 
    {
        ReceiverDeployment deployment = ReceiverDeployment.get(params.deploymentId)
        log.debug("deployment: " + deployment)
        
        def receiverRecoveryInstance = new ReceiverRecovery(params)
        receiverRecoveryInstance.deployment = deployment

        // Set the receiver's status to that of the recovery's.
        deployment?.receiver?.status = receiverRecoveryInstance.status
        deployment?.receiver?.save(flush: true)
        
        deployment?.recovery = receiverRecoveryInstance
        
        // Create a new receiver download an associate it with the recovery
        // TODO: may need some extra controls in view to populate download 
        // fields.
        ReceiverDownload download = 
            new ReceiverDownload()

        download.receiverRecovery = receiverRecoveryInstance
        download.downloadDateTime = new DateTime()
//        download.save(flush:true, failOnErrors:true)
        
        deployment?.recovery?.download = download
        
//        assert(download != null): "download cannot be null"

        if (deployment?.save(flush: true)) 
        {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'receiverRecovery.label', default: 'ReceiverRecovery'), receiverRecoveryInstance.id])}"
            redirect(action: "show", id: receiverRecoveryInstance.id)
        }
        else {
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
            if (!receiverRecoveryInstance.hasErrors() && receiverRecoveryInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'receiverRecovery.label', default: 'ReceiverRecovery'), receiverRecoveryInstance.id])}"
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
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'receiverRecovery.label', default: 'ReceiverRecovery'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'receiverRecovery.label', default: 'ReceiverRecovery'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverRecovery.label', default: 'ReceiverRecovery'), params.id])}"
            redirect(action: "list")
        }
    }
}
