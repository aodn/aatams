package au.org.emii.aatams

class ReceiverRecoveryController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def fileProcessorService
    
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
//        [receiverRecoveryInstanceList: ReceiverRecovery.list(params), receiverRecoveryInstanceTotal: ReceiverRecovery.count()]

        // We actually want to display a list of deployments (some with and some
        // without associated recoveries).
        // TODO: sort(hasRecovery, date)
        def receiverDeploymentList = ReceiverDeployment.list(params)
                                   
        [receiverDeploymentInstanceList: receiverDeploymentList, receiverDeploymentInstanceTotal: ReceiverDeployment.count()]
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
        deployment.receiver.status = receiverRecoveryInstance.status
        deployment.receiver.save(flush: true)
        
        deployment.recovery = receiverRecoveryInstance
        
        if (deployment.save(flush: true)) 
        {
            // Create a new receiver download an associate it with the recovery
            // TODO: may need some extra controls in view to populate download 
            // fields.
            ReceiverDownload download = 
                new ReceiverDownload()
                
            download.receiverRecovery = receiverRecoveryInstance
            download.downloadDate = new Date()
            download.save(flush:true, failOnErrors:true)
            assert(download != null): "download cannot be null"
            
            def fileMap = request.getFileMap()
            
            for (e in fileMap)
            {
                log.debug("key: " + e.key)
                
                // Kick off processing here.
//                runAsync
//                {
                    fileProcessorService.process(download, e.value)
//                }
            }

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
            try {
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
