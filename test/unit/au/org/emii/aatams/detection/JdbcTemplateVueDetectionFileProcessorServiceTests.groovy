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

        registerMetaClass AbstractBatchProcessor
        AbstractBatchProcessor.metaClass.flushSession = {  }

        vueDetectionFileProcessorService = new JdbcTemplateVueDetectionFileProcessorService()
        vueDetectionFileProcessorService.jdbcTemplateDetectionFactoryService = jdbcTemplateDetectionFactoryService
        vueDetectionFileProcessorService.detectionNotificationService = new DetectionNotificationService()
        vueDetectionFileProcessorService.detectionNotificationService.metaClass.sendDetectionNotificationEmails =
        {
            downloadFile ->
        }

        vueDetectionFileProcessorService.searchableService = searchableService
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

        download = new ReceiverDownloadFile(
            type: ReceiverDownloadFileType.DETECTIONS_CSV,
            importDate: new Date(),
            name: "My Detections File",
            status: FileProcessingStatus.PENDING,
            errMsg: ""
        )

        mockDomain(ReceiverDownloadFile, [download])
        download.save()

        mockDomain(Statistics)
        new Statistics(key: 'numValidDetections', value: 0).save(failOnError: true)
    }

    protected void tearDown() {
        super.tearDown()
        AbstractBatchProcessor.metaClass.getReader = null
        VueDetectionFileProcessorService.metaClass.getReader = null
        JdbcTemplateVueDetectionFileProcessorService.metaClass.getReader = null
        vueDetectionFileProcessorService.metaClass.getReader = null
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

    void testNumValidDetectionsUpdated() {
        vueDetectionFileProcessorService.metaClass.batchUpdate = { String[] statements -> batchUpdate(statements) }
        ValidDetection.metaClass.static.countByReceiverDownload = { 5 }

        vueDetectionFileProcessorService.process(download)

        def numValidDetectionsAfter = Statistics.findByKey("numValidDetections").value
        println numValidDetectionsAfter
        assertEquals(5, numValidDetectionsAfter)
    }

    /*
     * This does not call into the correct getReader method, i.e. getBadDateFormatReader never gets called
     * so the dates pass
     */
//    void testInvalidDateFormat()
//    {
//        AbstractBatchProcessor.metaClass.getReader = { getBadDateFormatReader(it) }
//        VueDetectionFileProcessorService.metaClass.getReader = { getBadDateFormatReader(it) }
//        JdbcTemplateVueDetectionFileProcessorService.metaClass.getReader = { getBadDateFormatReader(it) }
//        vueDetectionFileProcessorService.metaClass.getReader = { getBadDateFormatReader(it) }
//
//        registerMetaClass AbstractBatchProcessor
//        registerMetaClass VueDetectionFileProcessorService
//        registerMetaClass JdbcTemplateVueDetectionFileProcessorService
//        try
//        {
//            vueDetectionFileProcessorService.metaClass.batchUpdate = { String[] statements -> batchUpdate(statements) }
//            vueDetectionFileProcessorService.metaClass.getBatchSize = { 10000 }
//            vueDetectionFileProcessorService.process(download)
//
//            fail("Date format should have thrown a FileProcessingException")
//        }
//        catch (FileProcessingException fpe)
//        {
//            assert true
//        }
//        catch (Throwable t)
//        {
//            fail("Expected FileProcessingException but caught ${t.toString()}")
//        }
//    }

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

    protected String getBadDateFormatData() {
        println "getBadDateFormatData"
        return super.getBadDateFormatData() + '''
06/09/12 2:18,VR3UWM-354,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
06/09/12 2:12,VR3UWM-354,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
06/09/12 2:12,AAA-111,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
06/09/12 2:12,BBB-111,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
06/09/12 2:15,BBB-354,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
06/09/12 2:12,VR3UWM-354,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
06/09/12 2:12,VR3UWM-354,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
'''
    }

    static int count = 0

    private void batchUpdateFirst(String[] statementList) {
        if (count == 0) {
            assertEach(
            [
                "INSERT INTO VALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, RECEIVER_DEPLOYMENT_ID)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234',1)",
                "INSERT INTO VALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, RECEIVER_DEPLOYMENT_ID)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234',1)",
                "INSERT INTO INVALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,'AAA-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','Unknown receiver code name AAA-111','UNKNOWN_RECEIVER')",
                "INSERT INTO INVALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,'BBB-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','Unknown receiver code name BBB-111','UNKNOWN_RECEIVER')",
            ], statementList)
        }
        else if (count == 1) {
            assertEach([
                "INSERT INTO INVALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:47:24.0',1,'BBB-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','Unknown receiver code name BBB-111','UNKNOWN_RECEIVER')",
                "INSERT INTO INVALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2007-12-08 17:44:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','No deployment at time 2007-12-08 06:44:24 for receiver VR3UWM-354','NO_DEPLOYMENT_AT_DATE_TIME')",
                "INSERT INTO INVALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2010-12-08 17:44:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','No recovery at time 2010-12-08 06:44:24 for receiver VR3UWM-354','NO_RECOVERY_AT_DATE_TIME')",
                "INSERT INTO VALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, RECEIVER_DEPLOYMENT_ID)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:50:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234',1)",
            ], statementList)
        }

        count++
    }

    private void batchUpdate(String[] statementList) {

        assertEach([
            "INSERT INTO VALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, RECEIVER_DEPLOYMENT_ID)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234',1)",
            "INSERT INTO VALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, RECEIVER_DEPLOYMENT_ID)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234',1)",
            "INSERT INTO INVALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,'AAA-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','Unknown receiver code name AAA-111','UNKNOWN_RECEIVER')",
            "INSERT INTO INVALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:44:24.0',1,'BBB-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','Unknown receiver code name BBB-111','UNKNOWN_RECEIVER')",
            "INSERT INTO INVALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:47:24.0',1,'BBB-111','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','Unknown receiver code name BBB-111','UNKNOWN_RECEIVER')",
            "INSERT INTO INVALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2007-12-08 17:44:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','No deployment at time 2007-12-08 06:44:24 for receiver VR3UWM-354','NO_DEPLOYMENT_AT_DATE_TIME')",
            "INSERT INTO INVALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, MESSAGE, REASON)  VALUES(nextval('hibernate_sequence'),0,'2010-12-08 17:44:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234','No recovery at time 2010-12-08 06:44:24 for receiver VR3UWM-354','NO_RECOVERY_AT_DATE_TIME')",
            "INSERT INTO VALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, RECEIVER_DEPLOYMENT_ID)  VALUES(nextval('hibernate_sequence'),0,'2009-12-08 17:50:24.0',1,'VR3UWM-354','',null,'Neptune SW 1','A69-1303-62347','shark tag','1234',1)"
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

    public Reader getBadDateFormatReader(downloadFile)
    {
        return new StringReader(getBadDateFormatData())
    }
}
