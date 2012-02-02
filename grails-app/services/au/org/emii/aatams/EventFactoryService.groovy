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
	static final List UNITS_DETAILS_COLUMNS = ["Units detail 1", "Units detail 2", "Units detail 3"]

    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"
    
	def eventValidatorService
	
    /**
     * Creates a receiver event given a map of parameters (which originate from a line
     * in a CSV upload file).
     */
    def newEvent(downloadFile, params) throws FileProcessingException 
    {
		def nativeParams = toNativeParams(params)
		
		return initEvent(downloadFile, nativeParams)
    }
	
	private def initEvent(downloadFile, nativeParams)
	{
		assert(eventValidatorService)
		
		if (eventValidatorService.validate(downloadFile, nativeParams))
		{
			return createValidEvent(nativeParams +
									[receiverDownload: downloadFile,
									 receiverDeployment: eventValidatorService.deployment])
		}
		else
		{
			return createInvalidEvent(nativeParams +
									  [receiverDownload:downloadFile, 
                                       reason:eventValidatorService.invalidReason, 
                                       message:eventValidatorService.invalidMessage])
		}
	}
	
	private static Map toNativeParams(params)
	{
		def timestamp = new Date().parse(DATE_FORMAT, params[DATE_AND_TIME_COLUMN] + " " + "UTC")
		
		// nullable string properties which are blank are saved as null, so do that here too
		// so that the comparison when checking for duplicates works properly.
		def data = params[DATA_COLUMN] == "" ? null : params[DATA_COLUMN]
		def units = params[UNITS_COLUMN] == "" ? null : params[UNITS_COLUMN]
		
		UNITS_DETAILS_COLUMNS.each
		{
			columnName ->

			if (params[columnName])
			{
				units += "," + params[columnName]
			}
		}
		
		return [timestamp: timestamp,
			 	receiverName: params[RECEIVER_COLUMN],
				description: params[DESCRIPTION_COLUMN],
				data: data,
				units: units]
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
