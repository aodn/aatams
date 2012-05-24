package au.org.emii.aatams

import au.org.emii.aatams.detection.InvalidDetection
import au.org.emii.aatams.detection.InvalidDetectionReason
import au.org.emii.aatams.detection.RawDetection
import au.org.emii.aatams.detection.ValidDetection

/**
 * Index (and meta-data) to a file which has been downloaded from a receiver as
 * part of the recovery process.
 */
class ReceiverDownloadFile 
{
    ReceiverDownloadFileType type
    
    /**
     * Path (including filename) to file.
     */
    String path
    
    String name
    Date importDate
    
    FileProcessingStatus status
    
    String errMsg
    
    Person requestingUser
    
    Set<RawDetection> detections = new HashSet<RawDetection>()
    Set<ReceiverEvent> events = new HashSet<ReceiverEvent>()
    
    static hasMany = [detections:RawDetection, events:ReceiverEvent]
    
    static constraints =
    {
        type()
        path()
    }
    
	static transients = ['knownSensors']
	
    static mapping =
    {
        errMsg type: 'text'
    }
    
    String toString()
    {
        return String.valueOf(path)
    }
    
	def totalDetectionCount()
	{
		return executeCountCriteria(RawDetection)
	}
	
    def validDetectionCount()
    {
		return executeCountCriteria(ValidDetection)
    }
    
    def invalidDetectionCount()
    {
		return executeCountCriteria(InvalidDetection)
    }

    def invalidDetectionCount(reason)
    {
		return executeCountCriteria(InvalidDetection, 
		{
			eq("reason", reason)
		})
    }
	
	def unknownReceivers()
	{
		def c = InvalidDetection.createCriteria()
		def results = c.list
		{
			receiverDownload
			{
				eq("id", Long.valueOf(id))
			}

			eq("reason", InvalidDetectionReason.UNKNOWN_RECEIVER)
			
			projections
			{
				distinct("receiverName")
			}
		}
		
		return results
	}
	
	private def executeCountCriteria(clazz)
	{
		executeCountCriteria(clazz, null)
	}
	
	private def executeCountCriteria(clazz, customRestriction)
	{
		def c = clazz.createCriteria()
		def count = c.get()
		{
			projections
			{
				rowCount()
			}

			receiverDownload
			{
				eq("id", Long.valueOf(id))
			}
			
			if (customRestriction)
			{
				customRestriction.delegate = delegate
				customRestriction.call()
			}
		}
		
		return count
	}
	
	List<Long> getKnownSensors()
	{
		def validDetections = ValidDetection.findAllByReceiverDownload(this)
		def uniqueTransmitterIds = validDetections*.transmitterId.unique()
		return Sensor.findAllByTransmitterIdInList(uniqueTransmitterIds)
	}
}
