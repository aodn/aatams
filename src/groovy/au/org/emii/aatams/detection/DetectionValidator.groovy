package au.org.emii.aatams.detection

import au.org.emii.aatams.*

//import com.vividsolutions.jts.geom.Coordinate;
//import com.vividsolutions.jts.geom.GeometryFactory;
//import com.vividsolutions.jts.geom.Point;
//
//import org.joda.time.*
//import java.text.DateFormat
//import java.text.SimpleDateFormat
//import java.util.TimeZone

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
    
    Receiver receiver
    Collection<ReceiverDeployment> deployments
    Collection<ReceiverRecovery> recoveries
    
    ReceiverDeployment deployment
    
    boolean isDuplicate()
    {
        return ValidDetection.isDuplicate(params)
    }
    
    boolean isUnknownReceiver()
    {
        receiver = Receiver.findByCodeName(params.receiverName)
        return (receiver == null)
    }
    
    
    boolean hasNoDeploymentsAtDateTime()
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
    
    boolean hasNoRecoveriesAtDateTime()
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
}

