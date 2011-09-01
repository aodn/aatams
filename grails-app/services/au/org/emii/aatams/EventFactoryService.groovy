package au.org.emii.aatams

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

class EventFactoryService 
{
    static final String DATE_AND_TIME_COLUMN = "Date/Time"
    static final String RECEIVER_COLUMN = "Receiver"
    static final String DESCRIPTION_COLUMN = "Description"
    static final String DATA_COLUMN = "Data"
    static final String UNITS_COLUMN = "Units"
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"
    
    /**
     * Creates a receiver event given a map of parameters (which originate from a line
     * in a CSV upload file).
     */
    ReceiverEvent newEvent(eventParams) throws FileProcessingException 
    {
        String dateString = eventParams[DATE_AND_TIME_COLUMN] + " " + "UTC"
        log.debug("Parsing date string: " + dateString)
        Date eventDate = new Date().parse(DATE_FORMAT, dateString)
        
        Receiver receiver = Receiver.findByCodeName(eventParams[RECEIVER_COLUMN])
        String errMsg = "Unknown receiver name: " + eventParams[RECEIVER_COLUMN]
        //assert(receiver != null): errMsg
        if (receiver == null)
        {
            throw new FileProcessingException(errMsg)
        }
        
        // Find the appropriate receiver deployment (based on the timestamp of
        // the detection and the deployment/recovery timestamps.
        ReceiverDeployment deployment = findReceiverDeployment(receiver, eventDate)

        ReceiverEvent event = 
            new ReceiverEvent(receiverDeployment:deployment,
                              timestamp:eventDate)
       
        String description = eventParams[DESCRIPTION_COLUMN]
        event.description = description
        
        String data = eventParams[DATA_COLUMN]
        event.data = data

        String units = eventParams[UNITS_COLUMN]
        event.units = units
        
        deployment.save()
        
        return event
    }
   
    ReceiverDeployment findReceiverDeployment(receiver, eventDate)
    {
        List<ReceiverDeployment> deployments = receiver.deployments.grep(
        {
            // Check that the receiver was deployed before the event and
            // that there is a valid recovery and that the recovery date is
            // after the detection.
            if (   (it.deploymentDateTime.toDate() <= eventDate)
                && (it?.recovery.recoveryDateTime?.toDate() >= eventDate))
            {
                return true
            }
            else
            {
                return false
            }
        }).sort{it.deploymentDateTime}
        
        // There should be one and only one matching deployment.
        if (deployments.size() != 1)
        {
            log.warn("There are not exactly one matching deployment for receiver: " + receiver + ", event date: " + eventDate)
        }
        
        return deployments?.first()
    }
}
