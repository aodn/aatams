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
	static hasOne = [progress: ReceiverDownloadFileProgress]
	
    static constraints =
    {
        type()
        path()
		progress(nullable: true)
    }
    
	static transients = ['knownSensors', 'uniqueTransmitterIds', 'percentComplete']
	
    static mapping =
    {
        errMsg type: 'text'
    }
    
    String toString()
    {
        return String.valueOf(path)
    }
	
	Integer getPercentComplete()
	{
		return progress?.percentComplete
	}
	
	void setPercentComplete(percentComplete)
	{
		if (!progress)
		{
			progress = new ReceiverDownloadFileProgress(receiverDownloadFile: this)
		}
		
		progress.percentComplete = percentComplete
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
	
	List<String> getUniqueTransmitterIds()
	{
		def uniqueTransmitterIds = 
			ValidDetection.executeQuery("select distinct det.transmitterId from ValidDetection det where receiverDownload = ? order by det.transmitterId", this)
		log.debug("Unique transmitter IDs: " + uniqueTransmitterIds)
		
		return uniqueTransmitterIds
	}
	
	List<Long> getKnownSensors()
	{
		return Sensor.findAllByTransmitterIdInList(getUniqueTransmitterIds())
	}
}
