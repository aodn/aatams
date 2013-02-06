package au.org.emii.aatams.detection

import au.org.emii.aatams.*
import grails.test.*
import groovy.sql.Sql
import org.joda.time.DateTime

class DetectionFactoryServiceIntegrationTests extends GroovyTestCase {

    def dataSource
	def detectionFactoryService
    
    protected void setUp() 
	{
        super.setUp()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

	
	// Test for #1751
	void testRescanForDeployment()
	{
		def initValidCount = 31
		def initInvalidCount = 0
		def initDetSurgeryCount = 6

        def sql = new Sql(dataSource)
        ValidDetection.metaClass.static.count = {

            return sql.firstRow('select count(*) from valid_detection').count
        }
        
		Tag releasedTag = Tag.findBySerialNumber('46601')
		assertNotNull(releasedTag)
		Sensor pinger46601 = Sensor.findByTagAndPingCode(releasedTag, 46601)
		assertNotNull(pinger46601)

		assertEquals(6, DetectionSurgery.findAllBySensor(pinger46601).size())
		
        assertEquals(initValidCount, ValidDetection.count())
		assertEquals(initInvalidCount, InvalidDetection.count())
		
		// 1) invaliddetection - inside time range - no deployment
		InvalidDetection insideTimeRangeUnknownReceiver = createInvalidDetection(reason: InvalidDetectionReason.UNKNOWN_RECEIVER)
		InvalidDetection insideTimeRangeNoDeployment = createInvalidDetection(reason: InvalidDetectionReason.NO_DEPLOYMENT_AT_DATE_TIME)
		InvalidDetection insideTimeRangeNoRecovery = createInvalidDetection(reason: InvalidDetectionReason.NO_RECOVERY_AT_DATE_TIME)
		
		// 2) invaliddetection - outside time range
		InvalidDetection beforeTimeRange = createInvalidDetection(timestamp: new DateTime("2009-01-01T00:00:00").toDate(), reason: InvalidDetectionReason.UNKNOWN_RECEIVER)
		InvalidDetection afterTimeRange = createInvalidDetection(timestamp: new DateTime("2025-01-01T00:00:00").toDate(), reason: InvalidDetectionReason.UNKNOWN_RECEIVER)
		
		// 3) invaliddetection - other reason
		InvalidDetection insideTimeRangeDuplicate = createInvalidDetection(reason: InvalidDetectionReason.DUPLICATE)
		InvalidDetection insideTimeRangeDifferentReceiver = createInvalidDetection(receiverName: "VR2W-5678", reason: InvalidDetectionReason.NO_RECOVERY_AT_DATE_TIME)

		assertEquals(initValidCount + 0, ValidDetection.count())
		assertEquals(initInvalidCount + 7, InvalidDetection.count())
		
		ReceiverDeployment deploymentBondi1 =
			ReceiverDeployment.findByStation(InstallationStation.findByName("Bondi SW1"))

		detectionFactoryService.rescanForDeployment(deploymentBondi1)

		assertEquals(initValidCount + 3, ValidDetection.count())
		assertEquals(initInvalidCount + 4, InvalidDetection.count())
		
		// assert that DetectionSurgeries have been created.
		assertEquals(initDetSurgeryCount + 3, DetectionSurgery.findAllBySensor(pinger46601).size())
		
		// These should all still be invalid.
		[beforeTimeRange, afterTimeRange, insideTimeRangeDuplicate, insideTimeRangeDifferentReceiver].each {
			assertNotNull(InvalidDetection.get(it.id))
		}
		
		// TODO: cleanup
	}
	
	
	private def createInvalidDetection(params)
	{
		def timestamp = params.timestamp ?: new DateTime("2012-01-01T00:00:00").toDate()
		def receiverName = params.receiverName ?: "VR2W-101336"
		
		def det = 
			new InvalidDetection(
				timestamp: timestamp, 
				receiverName: receiverName, 
				reason: params.reason, 
				message: "some message", 
				transmitterId: "A69-1303-46601", 
				receiverDownload: ReceiverDownloadFile.list()[0])
			
		det.save(failOnError: true)
		
		return det
	}
}
