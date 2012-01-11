package au.org.emii.aatams.detection

import au.org.emii.aatams.DetectionSurgery
import au.org.emii.aatams.ReceiverDownloadFile
import org.joda.time.DateTime
import grails.test.*

class JdbcTemplateDetectionFactoryServiceTests extends AbstractDetectionFactoryServiceTests 
{
    protected void setUp() 
	{
        super.setUp()
		
		mockLogging(DetectionFactoryService, true)
		detectionFactoryService = new JdbcTemplateDetectionFactoryService()
		detectionFactoryService.metaClass.batchUpdate = { String[] statements -> batchUpdate(statements) }
		
		mockLogging(DetectionValidatorService, true)
		detectionFactoryService.detectionValidatorService = new DetectionValidatorService()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

	void testRescan()
	{
		def detection = new ValidDetection(transmitterId:"A69-1303-62347", timestamp:(new DateTime("2009-12-08T06:44:24Z")).toDate())
		mockDomain(ValidDetection, [detection])

		setupData()

		Collection newDetSurgeries = detectionFactoryService.rescanForSurgery(surgery)
	}
	
	void batchUpdate(String[] statements)
	{
		assertEquals('''INSERT INTO DETECTION_SURGERY (ID, VERSION, DETECTION_ID, SURGERY_ID, SENSOR_ID)  VALUES(nextval('detection_surgery_sequence'),0,1,1,2)''',
					 statements[0])
	}
}
