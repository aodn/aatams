package au.org.emii.aatams.detection

import au.org.emii.aatams.*

import org.codehaus.groovy.grails.commons.ConfigurationHolder as GrailsConfig

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import org.joda.time.*
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet
import java.sql.SQLException
import java.util.Date;
import java.util.TimeZone

/**
 * Implementation of the detection/surgery matching algorithm as outlined here:
 * 
 * http://redmine.emii.org.au/issues/379
 * 
 * Essentially, input is a set of detection parameters (as recorded by a receiver
 * and exported with the VUE application), output is a detection record plus
 * 0 or more DetectionSurgeries.
 */
class DetectionFactoryService 
{
    static transactional = true
	
	def dataSource
	def sessionFactory

	private Map<String, List<Sensor>> sensorCache = new WeakHashMap<String, List<Sensor>>()
	
    /**
     * Creates a detection given a map of parameters (which originate from a line
     * in a CSV upload file).
     */
	def newDetection(downloadFile, params) throws FileProcessingException
    {
		def format = DetectionFormat.newFormat(downloadFile.type)		
		def nativeParams = format.parseRow(params)
        def detection = initDetection(downloadFile, nativeParams)
        assert(detection)
		
        if (!detection.valid)
        {
			;
        }
		else
		{
			matchToTags(detection)
		}
		
        return detection
    }
    
    Collection rescanForSurgery(Surgery surgery)
    {
        DeviceStatus retiredStatus = DeviceStatus.findByStatus(DeviceStatus.RETIRED, [cache:true])
        if (surgery.tag.status == retiredStatus)
        {
            return []
        }
        
        def newDetectionSurgeries = []
        
		surgery.tag.sensors.each
		{
			sensor ->

			def matchingDetections = ValidDetection.findAllByTransmitterId(sensor.transmitterId, [cache:true])
			def numRecords = matchingDetections.size()
			log.info("Rescanning for surgery on sensor: " + sensor + ", " + numRecords + " detections match tag.")
			int percentProgress = 0
			long startTime = System.currentTimeMillis()
			
			matchingDetections.eachWithIndex
			{
				detection, i ->
				
				if (surgery.isInWindow(detection.timestamp))
				{
					def newDetSurgery = createDetectionSurgery(surgery, sensor, detection)
					newDetectionSurgeries.add(newDetSurgery)
				}
				
				percentProgress = logProgress(i, numRecords, percentProgress, surgery, startTime)
			}
	
		}
		
		log.debug("Num new detection surgeries: " + newDetectionSurgeries.size())
        return newDetectionSurgeries
    }
	
	private String getDetectionsForDeploymentCondition(deployment)
	{
		def dateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis()
		return "receiver_name = '${deployment.receiver.name}' and timestamp between '${dateTimeFormatter.print(deployment.deploymentDateTime)}' AND '${dateTimeFormatter.print(deployment.recovery.recoveryDateTime)}'"
	}
	
	private String buildRescanDeploymentSql(deployment)
	{
		def condition = "reason <> 'DUPLICATE' and ${getDetectionsForDeploymentCondition(deployment)}"
		def validDetectionColumnNames = "id, version, location, receiver_deployment_id, receiver_download_id, receiver_name, sensor_unit, sensor_value, station_name, \
					 timestamp, transmitter_id, transmitter_name, transmitter_serial_number"
		def invalidDetectionSelectFields = "id, version, location, ${deployment.id}, receiver_download_id, receiver_name, sensor_unit, sensor_value, station_name, timestamp, transmitter_id, transmitter_name, transmitter_serial_number"
		def invalidDetectionGroupByClause = "receiver_name, transmitter_id, timestamp, id, version, location, message, reason, receiver_download_id, sensor_unit, sensor_value, station_name, transmitter_name, transmitter_serial_number"
		 
		return "insert into valid_detection \
					(${validDetectionColumnNames}) \
						( \
							select ${invalidDetectionSelectFields} from invalid_detection \
								where ${condition} \
								group by ${invalidDetectionGroupByClause} \
						); \
					delete from invalid_detection \
					where ${condition};"
	}
	
	void rescanForDeployment(ReceiverDeployment deployment)
	{
		assert(deployment.recovery) : "Deployment must have associated recovery"
		
		log.info("Rescanning invalid detections, deployment: ${deployment}...")
		JdbcTemplate rescanInvalid = new JdbcTemplate(dataSource)
		rescanInvalid.execute(buildRescanDeploymentSql(deployment))
		log.info("Rescan complete, deployment: ${deployment}")
		
		log.info("Rescanning surgeries...")
		JdbcTemplate rescanSurgeries = new JdbcTemplate(dataSource)
		def rows = 
			rescanSurgeries.query(
				"select id, timestamp, transmitter_id from valid_detection where ${getDetectionsForDeploymentCondition(deployment)}", 
				new RowMapper()
				{
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map retRow = [:]
						
						retRow.id = rs.getLong("id")
						retRow.timestamp = rs.getDate("timestamp")
						retRow.transmitter_id = rs.getString("transmitter_id")
						return retRow
					}
				})
		
		log.info("Rescanning surgeries for ${rows.size()} detections...")
		
		int progress = 0
		int prevProgress = -1
				
		rows.eachWithIndex 
		{
			detection, index ->
			
			if (DetectionSurgery.findByDetection(ValidDetection.get(detection.id)))
			{
				// duplicate
			}
			else
			{
				def sensor = Sensor.findByTransmitterId(detection.transmitter_id, [cache: true])
				
				Surgery.findAllByTag(sensor.tag).each 
				{
					surgery ->
					
					if (surgery.isInWindow(detection.timestamp))
					{
						createDetectionSurgery(surgery, sensor, ValidDetection.get(detection.id))
					}
				}
			}
			
			progress = ((float) index / rows.size()) * 100
			if (progress != prevProgress)
			{
				prevProgress = progress
				log.info("${progress}% of surgeries rescanned")
			}
		}
	}
    
	private int logProgress(i, numRecords, percentProgress, surgery, startTime)
	{
		float progress = (float)i/numRecords * 100
		if ((int)progress > percentProgress)
		{
			percentProgress = (int)progress
			
			String progressMsg =
				"Surgery processing, id: " + surgery.id +
				", progress: " + percentProgress +
				"%, average time per record: " + (float)(System.currentTimeMillis() - startTime) / (i + 1) + "ms"
				
			if ((percentProgress % 10) == 0)
			{
				log.info(progressMsg)
			}
			else
			{
				log.debug(progressMsg)
			}
		}
		
		return percentProgress
	}
	
    private def initDetection(downloadFile, nativeParams)
    {
		def detectionValidator = new DetectionValidator()
        assert(detectionValidator)
		
		if (detectionValidator.validate(downloadFile, nativeParams))
		{
			return createValidDetection(nativeParams + 
					   	                [receiverDownload:downloadFile, 
										 receiverDeployment:detectionValidator.deployment,
										 receiver:detectionValidator.receiver])
		}
		else
		{
			return createInvalidDetection(nativeParams + 
                                          [receiverDownload:downloadFile, 
                                           reason:detectionValidator.invalidReason, 
                                           message:detectionValidator.invalidMessage])
		}
    }
    
    private void matchToTags(detection)
    {
        assert(detection)

		def sensors = findSensors(detection.transmitterId)
        sensors.each
        {
            sensor -> sensor.tag.surgeries.each
            {
                surgery ->

                if (!surgery.isInWindow(detection.timestamp))
                {
                    return
                }
                
				createDetectionSurgery(surgery, sensor, detection)
            }
        }
    }
	
	private List<Sensor> findSensors(transmitterId)
	{
		if (!sensorCache.containsKey(transmitterId))
		{
			def sensorsWithId = 
				Sensor.findAllByTransmitterId(transmitterId, [cache:true])
				
			sensorsWithId = sensorsWithId.grep
			{
				it.tag.status != DeviceStatus.findByStatus(DeviceStatus.RETIRED, [cache:true])
			}
			
			sensorCache.put(transmitterId, 
					  	    sensorsWithId)
		}
		
		return sensorCache[transmitterId]
	}
	
    protected def createDetectionSurgery(surgery, sensor, detection)
    {
		return new DetectionSurgery(
			surgery:surgery,
			sensor:sensor,
			detection:detection).save()
    }
	
	protected def createValidDetection(params)
	{
		return new ValidDetection(params).save()
	}
	
	protected def createInvalidDetection(params)
	{
		return new InvalidDetection(params).save()
	}
}
