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
			assertEquals("INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_DEPLOYMENT_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, CLASS, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','au.org.emii.aatams.detection.ValidDetection','','')", statementList[0])
			assertEquals("INSERT INTO DETECTION_SURGERY (ID, VERSION, DETECTION_ID, SURGERY_ID, SENSOR_ID)  VALUES(nextval('detection_surgery_sequence'),0,currval('hibernate_sequence'),1,1)", statementList[1])
			assertEquals("INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_DEPLOYMENT_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, CLASS, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','au.org.emii.aatams.detection.ValidDetection','','')", statementList[2])
			assertEquals("INSERT INTO DETECTION_SURGERY (ID, VERSION, DETECTION_ID, SURGERY_ID, SENSOR_ID)  VALUES(nextval('detection_surgery_sequence'),0,currval('hibernate_sequence'),1,1)", statementList[3])
			assertEquals("INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_DEPLOYMENT_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, CLASS, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,null,'AAA-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','au.org.emii.aatams.detection.InvalidDetection','Unknown receiver code name AAA-111','UNKNOWN_RECEIVER')", statementList[4])
			assertEquals("INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_DEPLOYMENT_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, CLASS, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,null,'BBB-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','au.org.emii.aatams.detection.InvalidDetection','Unknown receiver code name BBB-111','UNKNOWN_RECEIVER')", statementList[5])
		}
		else if (count == 1) {
			assertEquals("INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_DEPLOYMENT_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, CLASS, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:47:24.0',1,null,'BBB-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','au.org.emii.aatams.detection.InvalidDetection','Unknown receiver code name BBB-111','UNKNOWN_RECEIVER')", statementList[0])
			assertEquals("INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_DEPLOYMENT_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, CLASS, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2007-12-08 17:44:24.0',1,null,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','au.org.emii.aatams.detection.InvalidDetection','No deployment at time 2007-12-08 06:44:24 for receiver VR3UWM-354','NO_DEPLOYMENT_AT_DATE_TIME')", statementList[1])
			assertEquals("INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_DEPLOYMENT_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, CLASS, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2010-12-08 17:44:24.0',1,null,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','au.org.emii.aatams.detection.InvalidDetection','No recovery at time 2010-12-08 06:44:24 for receiver VR3UWM-354','NO_RECOVERY_AT_DATE_TIME')", statementList[2])
			assertEquals("INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_DEPLOYMENT_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, CLASS, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:50:24.0',1,1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','au.org.emii.aatams.detection.ValidDetection','','')", statementList[3])
			assertEquals("INSERT INTO DETECTION_SURGERY (ID, VERSION, DETECTION_ID, SURGERY_ID, SENSOR_ID)  VALUES(nextval('detection_surgery_sequence'),0,currval('hibernate_sequence'),1,1)", statementList[4])
		}

		count++
	}

	private void batchUpdate(String[] statementList) {
		assertEquals("INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_DEPLOYMENT_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, CLASS, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','au.org.emii.aatams.detection.ValidDetection','','')", statementList[0])
		assertEquals("INSERT INTO DETECTION_SURGERY (ID, VERSION, DETECTION_ID, SURGERY_ID, SENSOR_ID)  VALUES(nextval('detection_surgery_sequence'),0,currval('hibernate_sequence'),1,1)", statementList[1])
		assertEquals("INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_DEPLOYMENT_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, CLASS, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','au.org.emii.aatams.detection.ValidDetection','','')", statementList[2])
		assertEquals("INSERT INTO DETECTION_SURGERY (ID, VERSION, DETECTION_ID, SURGERY_ID, SENSOR_ID)  VALUES(nextval('detection_surgery_sequence'),0,currval('hibernate_sequence'),1,1)", statementList[3])
		assertEquals("INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_DEPLOYMENT_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, CLASS, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,null,'AAA-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','au.org.emii.aatams.detection.InvalidDetection','Unknown receiver code name AAA-111','UNKNOWN_RECEIVER')", statementList[4])
		assertEquals("INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_DEPLOYMENT_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, CLASS, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,null,'BBB-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','au.org.emii.aatams.detection.InvalidDetection','Unknown receiver code name BBB-111','UNKNOWN_RECEIVER')", statementList[5])
		assertEquals("INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_DEPLOYMENT_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, CLASS, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:47:24.0',1,null,'BBB-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','au.org.emii.aatams.detection.InvalidDetection','Unknown receiver code name BBB-111','UNKNOWN_RECEIVER')", statementList[6])
		assertEquals("INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_DEPLOYMENT_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, CLASS, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2007-12-08 17:44:24.0',1,null,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','au.org.emii.aatams.detection.InvalidDetection','No deployment at time 2007-12-08 06:44:24 for receiver VR3UWM-354','NO_DEPLOYMENT_AT_DATE_TIME')", statementList[7])
		assertEquals("INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_DEPLOYMENT_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, CLASS, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2010-12-08 17:44:24.0',1,null,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','au.org.emii.aatams.detection.InvalidDetection','No recovery at time 2010-12-08 06:44:24 for receiver VR3UWM-354','NO_RECOVERY_AT_DATE_TIME')", statementList[8])
		assertEquals("INSERT INTO RAW_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_DEPLOYMENT_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, CLASS, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:50:24.0',1,1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','au.org.emii.aatams.detection.ValidDetection','','')", statementList[9])
		assertEquals("INSERT INTO DETECTION_SURGERY (ID, VERSION, DETECTION_ID, SURGERY_ID, SENSOR_ID)  VALUES(nextval('detection_surgery_sequence'),0,currval('hibernate_sequence'),1,1)", statementList[10])
	}
}
