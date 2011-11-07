package au.org.emii.aatams.detection

import au.org.emii.aatams.*

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Detection validation utilities.
 * 
 * Stores some state to minimise queries.
 * 
 * @author jburgess
 */
class DetectionValidator
{
    def params
    
    ReceiverDownloadFile receiverDownload
    
    Receiver receiver
    Collection<ReceiverDeployment> deployments
    Collection<ReceiverRecovery> recoveries
    
    ReceiverDeployment deployment
    
    static DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    
    static
    {
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT:00"))
    }
    
    private boolean isDuplicate()
    {
        return ValidDetection.isDuplicate(params)
    }
    
    private boolean isUnknownReceiver()
    {
        receiver = Receiver.findByCodeName(params.receiverName, [cache:true])
        return (receiver == null)
    }
    
    
    private boolean hasNoDeploymentsAtDateTime()
    {
        assert(receiver)
        
        deployments = receiver.deployments?.grep
        {
            deployment ->
            
            if (!deployment)
            {
                return false
            }
            
            if (deployment.deploymentDateTime.toDate().after(params.timestamp))
            {
                return false
            }
            
            return true
        }
        
        return (!deployments || deployments.isEmpty())
    }
    
    private boolean hasNoRecoveriesAtDateTime()
    {
        assert(receiver)
        assert(deployments)
        assert(!deployments.isEmpty())
        
        recoveries = deployments*.recovery.grep
        {
            recovery ->

            if (!recovery)
            {
                return false
            }
            
            if (recovery.recoveryDateTime.toDate().before(params.timestamp))
            {
                return false
            }
        
            deployment = recovery.deployment
            return true
        }
        
        return (!recoveries || recoveries.isEmpty())
    }

    RawDetection validate()
    {
        def validator = this
        
        if (validator.isDuplicate())
        {
            return new InvalidDetection(params + [receiverDownload:receiverDownload, reason:InvalidDetectionReason.DUPLICATE])
        }
        
        if (validator.isUnknownReceiver())
        {
            return new InvalidDetection(params + 
                                        [receiverDownload:receiverDownload, 
                                         reason:InvalidDetectionReason.UNKNOWN_RECEIVER, 
                                         message:"Unknown receiver code name " + params.receiverName])
        }
        
        if (validator.hasNoDeploymentsAtDateTime())
        {
            return new InvalidDetection(params + 
                                        [receiverDownload:receiverDownload, 
                                         reason:InvalidDetectionReason.NO_DEPLOYMENT_AT_DATE_TIME, 
                                         message:"No deployment at time " + simpleDateFormat.format(params.timestamp) + " for receiver " + params.receiverName])
        }

        if (validator.hasNoRecoveriesAtDateTime())
        {
            return new InvalidDetection(params + 
                                        [receiverDownload:receiverDownload, 
                                         reason:InvalidDetectionReason.NO_RECOVERY_AT_DATE_TIME, 
                                         message:"No recovery at time " + simpleDateFormat.format(params.timestamp) + " for receiver " + params.receiverName])
        }
        
        def validDetection = new ValidDetection(params + 
                                                [receiverDownload:receiverDownload, 
                                                receiverDeployment:validator.deployment]).save()
        
        receiver.addToDetections(validDetection)
        receiver.save()
        
        deployment.addToDetections(validDetection)
        deployment.save()
        
        return validDetection
    }
}

