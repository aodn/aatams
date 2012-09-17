package au.org.emii.aatams.detection

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import au.org.emii.aatams.*
import grails.test.*

import org.springframework.jdbc.core.JdbcTemplate

class JdbcTemplateVueDetectionFileProcessorServiceTests extends AbstractVueDetectionFileProcessorServiceTests {
	ReceiverDownloadFile download

	protected void setUp() {
		super.setUp()

		mockLogging(JdbcTemplateDetectionFactoryService, true)
		def jdbcTemplateDetectionFactoryService = new JdbcTemplateDetectionFactoryService()
		mockLogging(DetectionValidator, true)

		mockLogging(VueDetectionFileProcessorService, true)

		vueDetectionFileProcessorService = new JdbcTemplateVueDetectionFileProcessorService()
		vueDetectionFileProcessorService.jdbcTemplateDetectionFactoryService = jdbcTemplateDetectionFactoryService
		vueDetectionFileProcessorService.detectionNotificationService = new DetectionNotificationService()
		vueDetectionFileProcessorService.detectionNotificationService.metaClass.sendDetectionNotificationEmails =
		{
			donwloadFile ->
		}
		
		vueDetectionFileProcessorService.searchableService = searchableService
		vueDetectionFileProcessorService.metaClass.getReader = { getReader(it) }
		vueDetectionFileProcessorService.metaClass.markDuplicates = { }
		vueDetectionFileProcessorService.metaClass.getNumRecords = { 14 }
		
		DeviceStatus status = new DeviceStatus(status: "DEPLOYED")
		mockDomain(DeviceStatus, [status])
		status.save()

		CodeMap a69_1303 = new CodeMap(codeMap: "A69-1303")
		mockDomain(CodeMap, [a69_1303])
		a69_1303.save()

		Tag tag = new Tag(codeMap: a69_1303, status:status)
		mockDomain(Tag, [tag])

		Sensor sensor = new Sensor(tag: tag, pingCode: 62347)
		mockDomain(Sensor, [sensor])
		sensor.save()

		tag.addToSensors(sensor)

		AnimalRelease release = new AnimalRelease(releaseDateTime:new DateTime("2009-12-07T06:50:24"))

		Surgery surgery = new Surgery(tag:tag, timestamp:new DateTime("2009-12-07T06:50:24"), release:release)
		mockDomain(Surgery, [surgery])
		surgery.save()

		tag.addToSurgeries(surgery)
		tag.save()

		download = new ReceiverDownloadFile()
		mockDomain(ReceiverDownloadFile, [download])
		download.save()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testProcessSingleBatch() {
		vueDetectionFileProcessorService.metaClass.batchUpdate = { String[] statements -> batchUpdate(statements) }
		vueDetectionFileProcessorService.metaClass.getBatchSize = { 10000 }
		vueDetectionFileProcessorService.process(download)
	}

	void testProcessMultipleBatches() {
		vueDetectionFileProcessorService.metaClass.batchUpdate = { String[] statements -> batchUpdateFirst(statements) }
		vueDetectionFileProcessorService.metaClass.getBatchSize = { 4 }
		vueDetectionFileProcessorService.process(download)
	}

	protected String getData() {
		return super.getData() + '''
2009-12-08 06:50:24,VR3UWM-354,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
2009-12-08 06:44:24,VR3UWM-354,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
2009-12-08 06:44:24,AAA-111,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
2009-12-08 06:44:24,BBB-111,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
2009-12-08 06:47:24,BBB-354,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
2007-12-08 06:44:24,VR3UWM-354,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
2010-12-08 06:44:24,VR3UWM-354,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
'''
	}

	static int count = 0

	private void batchUpdateFirst(String[] statementList) {
		if (count == 0) {
			
			assertEach(
			[
				"INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234')",
				"INSERT INTO VALID_DETECTION (ID, RECEIVER_DEPLOYMENT_ID)  VALUES(currval('hibernate_sequence'),1)",
				"INSERT INTO DETECTION_SURGERY (ID, VERSION, DETECTION_ID, SURGERY_ID, SENSOR_ID)  VALUES(nextval('detection_surgery_sequence'),0,currval('hibernate_sequence'),1,1)",
				"INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234')",
				"INSERT INTO VALID_DETECTION (ID, RECEIVER_DEPLOYMENT_ID)  VALUES(currval('hibernate_sequence'),1)",
				"INSERT INTO DETECTION_SURGERY (ID, VERSION, DETECTION_ID, SURGERY_ID, SENSOR_ID)  VALUES(nextval('detection_surgery_sequence'),0,currval('hibernate_sequence'),1,1)",
				"INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,'AAA-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234')",
				"INSERT INTO INVALID_DETECTION (ID, MESSAGE, REASON)  VALUES(currval('hibernate_sequence'),'Unknown receiver code name AAA-111','UNKNOWN_RECEIVER')",
				"INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,'BBB-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234')",
				"INSERT INTO INVALID_DETECTION (ID, MESSAGE, REASON)  VALUES(currval('hibernate_sequence'),'Unknown receiver code name BBB-111','UNKNOWN_RECEIVER')",
			], statementList)
		}
		else if (count == 1) {
			
			assertEach([
				"INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:47:24.0',1,'BBB-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234')",
				"INSERT INTO INVALID_DETECTION (ID, MESSAGE, REASON)  VALUES(currval('hibernate_sequence'),'Unknown receiver code name BBB-111','UNKNOWN_RECEIVER')",
				"INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER)  VALUES(nextval('hibernate_sequence'),0,'2007-12-08 17:44:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234')",
				"INSERT INTO INVALID_DETECTION (ID, MESSAGE, REASON)  VALUES(currval('hibernate_sequence'),'No deployment at time 2007-12-08 06:44:24 for receiver VR3UWM-354','NO_DEPLOYMENT_AT_DATE_TIME')",
				"INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER)  VALUES(nextval('hibernate_sequence'),0,'2010-12-08 17:44:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234')",
				"INSERT INTO INVALID_DETECTION (ID, MESSAGE, REASON)  VALUES(currval('hibernate_sequence'),'No recovery at time 2010-12-08 06:44:24 for receiver VR3UWM-354','NO_RECOVERY_AT_DATE_TIME')",
				"INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:50:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234')",
				"INSERT INTO VALID_DETECTION (ID, RECEIVER_DEPLOYMENT_ID)  VALUES(currval('hibernate_sequence'),1)",
				"INSERT INTO DETECTION_SURGERY (ID, VERSION, DETECTION_ID, SURGERY_ID, SENSOR_ID)  VALUES(nextval('detection_surgery_sequence'),0,currval('hibernate_sequence'),1,1)",
			], statementList)
		}

		count++
	}

	private void batchUpdate(String[] statementList) {
		
		assertEach([
			"INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234')",
			"INSERT INTO VALID_DETECTION (ID, RECEIVER_DEPLOYMENT_ID)  VALUES(currval('hibernate_sequence'),1)",
			"INSERT INTO DETECTION_SURGERY (ID, VERSION, DETECTION_ID, SURGERY_ID, SENSOR_ID)  VALUES(nextval('detection_surgery_sequence'),0,currval('hibernate_sequence'),1,1)",
			"INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234')",
			"INSERT INTO VALID_DETECTION (ID, RECEIVER_DEPLOYMENT_ID)  VALUES(currval('hibernate_sequence'),1)",
			"INSERT INTO DETECTION_SURGERY (ID, VERSION, DETECTION_ID, SURGERY_ID, SENSOR_ID)  VALUES(nextval('detection_surgery_sequence'),0,currval('hibernate_sequence'),1,1)",
			"INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,'AAA-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234')",
			"INSERT INTO INVALID_DETECTION (ID, MESSAGE, REASON)  VALUES(currval('hibernate_sequence'),'Unknown receiver code name AAA-111','UNKNOWN_RECEIVER')",
			"INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,'BBB-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234')",
			"INSERT INTO INVALID_DETECTION (ID, MESSAGE, REASON)  VALUES(currval('hibernate_sequence'),'Unknown receiver code name BBB-111','UNKNOWN_RECEIVER')",
			"INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:47:24.0',1,'BBB-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234')",
			"INSERT INTO INVALID_DETECTION (ID, MESSAGE, REASON)  VALUES(currval('hibernate_sequence'),'Unknown receiver code name BBB-111','UNKNOWN_RECEIVER')",
			"INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER)  VALUES(nextval('hibernate_sequence'),0,'2007-12-08 17:44:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234')",
			"INSERT INTO INVALID_DETECTION (ID, MESSAGE, REASON)  VALUES(currval('hibernate_sequence'),'No deployment at time 2007-12-08 06:44:24 for receiver VR3UWM-354','NO_DEPLOYMENT_AT_DATE_TIME')",
			"INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER)  VALUES(nextval('hibernate_sequence'),0,'2010-12-08 17:44:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234')",
			"INSERT INTO INVALID_DETECTION (ID, MESSAGE, REASON)  VALUES(currval('hibernate_sequence'),'No recovery at time 2010-12-08 06:44:24 for receiver VR3UWM-354','NO_RECOVERY_AT_DATE_TIME')",
			"INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:50:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234')",
			"INSERT INTO VALID_DETECTION (ID, RECEIVER_DEPLOYMENT_ID)  VALUES(currval('hibernate_sequence'),1)",
			"INSERT INTO DETECTION_SURGERY (ID, VERSION, DETECTION_ID, SURGERY_ID, SENSOR_ID)  VALUES(nextval('detection_surgery_sequence'),0,currval('hibernate_sequence'),1,1)"
		], statementList)
	}
	
	private void assertEach(expected, actual)
	{
		expected.eachWithIndex {
			val, i ->
			
			if (val != actual[i])
			{
				println "Failure: val: " + val + ", actual[" + i + "]: " + actual[i]
			}
			
			assertEquals(val, actual[i])
		}
	}
}
