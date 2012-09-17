package au.org.emii.aatams.detection

import org.hibernatespatial.GeometryUserType

import au.org.emii.aatams.ReceiverDownloadFile
import au.org.emii.aatams.util.GeometryUtils
import au.org.emii.aatams.util.SqlUtils

import com.vividsolutions.jts.geom.*

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat;
import java.util.Map


/**
 * A 1:1 mapping of a single CSV record from receiver export.
 */
class RawDetection 
{
    /**
     * UTC timestamp.
     */
    Date timestamp
    
    String receiverName
    
    String stationName
    
    /**
     * Record the actual ID transmitted by the tag.
     */
    String transmitterId
    
    /**
     * May be different (as with station name above).
     */
    String transmitterName
    
    String transmitterSerialNumber
    
    /**
     * May be different (as with station name above).
     */
    Point location

    Float sensorValue
    String sensorUnit
    
    static transients = ['scrambledLocation', 'valid', 'formattedTimestamp']
    
    static constraints = 
    {
        transmitterName(nullable:true, blank:true)
        transmitterSerialNumber(nullable:true, blank:true)
        sensorValue(nullable:true)
        sensorUnit(nullable:true, blank:true)
        stationName(nullable:true, blank:true)
        location(nullable:true)
		
		// Workaround for problem where jenkins build is failing - not sure why.
		// Getting "ValidationException" when trying to save ValidDetection.
		scrambledLocation(nullable:true)
		formattedTimestamp(nullable:true)
    }

    static belongsTo = [receiverDownload:ReceiverDownloadFile]
    
    static mapping =
    {
        timestamp index:'timestamp_index'
        transmitterId index:'transmitterId_index'
        receiverName index:'receiverName_index'
        cache true
		location type: GeometryUserType
		
		// Partition valid/invalid detections for performance improvement.
		tablePerHierarchy false
    }
    
	static List<String> toSqlInsert(Map detection)
	{
		def retList = []
		
		StringBuilder detectionBuff = new StringBuilder(
				"INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, " + /* LOCATION, */ "RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, " +
				"STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER) " +
				" VALUES(")

		detectionBuff.append("nextval('hibernate_sequence'),")
		detectionBuff.append("0,")
		detectionBuff.append("'" + new java.sql.Timestamp(detection["timestamp"].getTime()) + "',")
		SqlUtils.appendIntegerParams(detectionBuff, detection, ["receiverDownloadId"])
		SqlUtils.appendStringParams(detectionBuff, detection, ["receiverName", "sensorUnit", "sensorValue", "stationName", "transmitterId", "transmitterName",
			"transmitterSerialNumber"])
		SqlUtils.removeTrailingCommaAndAddBracket(detectionBuff)
		
		retList.add(detectionBuff.toString())

		if (detection["clazz"] == "au.org.emii.aatams.detection.ValidDetection")
		{
			detectionBuff = new StringBuilder(
					"INSERT INTO VALID_DETECTION (ID, RECEIVER_DEPLOYMENT_ID) " +
					" VALUES(")
	
			detectionBuff.append("currval('hibernate_sequence'),")
			SqlUtils.appendIntegerParams(detectionBuff, detection, ["receiverDeploymentId"])
			SqlUtils.removeTrailingCommaAndAddBracket(detectionBuff)
		}
		else if (detection["clazz"] == "au.org.emii.aatams.detection.InvalidDetection")
		{
			detectionBuff = new StringBuilder(
					"INSERT INTO INVALID_DETECTION (ID, MESSAGE, REASON) " +
					" VALUES(")
	
			detectionBuff.append("currval('hibernate_sequence'),")
			SqlUtils.appendStringParams(detectionBuff, detection, ["message", "reason"])
			SqlUtils.removeTrailingCommaAndAddBracket(detectionBuff)
		}
		else
		{
			assert(false): "Unknown detection class: " + detection["clazz"]
		}

		
		retList.add(detectionBuff.toString())

		return retList
	}

    boolean isValid()
    {
        return true
    }
    
    /**
     * Non-authenticated users can only see scrambled locations.
     */
    Point getScrambledLocation()
    {
        return GeometryUtils.scrambleLocation(location)
    }
	
	static DateFormat formatter
	
	String getFormattedTimestamp()
	{
		if (timestamp)
		{
			if (!formatter)
			{
				formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			}
			
			return formatter.format(timestamp)
		}
		
		return null
	}
}
