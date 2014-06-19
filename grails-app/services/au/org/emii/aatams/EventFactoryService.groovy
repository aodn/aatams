package au.org.emii.aatams

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

class EventFactoryService 
{
    /**
     * Creates a receiver event given a map of parameters (which originate from a line
     * in a CSV upload file).
     */
    def newEvent(downloadFile, params) throws FileProcessingException 
    {
        def format = FileFormat.newFormat(downloadFile.type)        
        def nativeParams = format.parseRow(params)
        
        return initEvent(downloadFile, nativeParams)
    }
    
    private def initEvent(downloadFile, nativeParams)
    {
        def eventValidator = new EventValidator()
        assert(eventValidator)
        
        if (eventValidator.validate(downloadFile, nativeParams))
        {
            return createValidEvent(nativeParams +
                                    [receiverDownload: downloadFile,
                                     receiverDeployment: eventValidator.deployment])
        }
        else
        {
            return createInvalidEvent(nativeParams +
                                      [receiverDownload:downloadFile, 
                                       reason:eventValidator.invalidReason, 
                                       message:eventValidator.invalidMessage])
        }
    }
    
    protected def createValidEvent(params) 
    {
        return new ValidReceiverEvent(params).save()
    }
   
    protected def createInvalidEvent(params) 
    {
        return new InvalidReceiverEvent(params).save()
    }
}
