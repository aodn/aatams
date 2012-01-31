package au.org.emii.aatams.detection

import org.joda.time.DateTime;

import grails.test.*

class DetectionExtractServiceTests extends GrailsUnitTestCase 
{
	def detectionExtractService
	
    protected void setUp() 
	{
        super.setUp()
		
		mockLogging(DetectionExtractService, true)
		detectionExtractService = new DetectionExtractService()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testConstructQueryNoFilterParams() 
	{
		assertEquals('''select * from detection_extract_view limit 10000 offset 0''', detectionExtractService.constructQuery([:], 10000, 0))
    }

	void testConstructQueryOneProject()
	{
		assertEquals('''select * from detection_extract_view where project in ('Whales') limit 10000 offset 0''',
					 detectionExtractService.constructQuery([in:[receiverDeployment:[station:[installation:[project:[name:"Whales, "]]]]]], 10000, 0))
	}

	void testConstructQueryTwoProjects()
	{
		assertEquals('''select * from detection_extract_view where project in ('Whales', 'Sharks') limit 10000 offset 0''',
					 detectionExtractService.constructQuery([in:[receiverDeployment:[station:[installation:[project:[name:"Whales, Sharks, "]]]]]], 10000, 0))
	}

	void testConstructQueryOneProjectOneInstallation()
	{
		assertEquals('''select * from detection_extract_view where project in ('Whales') and installation in ('Bondi') limit 10000 offset 0''',
					 detectionExtractService.constructQuery([in:[receiverDeployment:[station:[installation:[project:[name:"Whales, "], name:"Bondi"]]]]], 10000, 0))
	}

	void testConstructQueryTwoProjectsOneInstallation()
	{
		assertEquals('''select * from detection_extract_view where project in ('Whales', 'Sharks') and installation in ('Bondi') limit 10000 offset 0''',
					 detectionExtractService.constructQuery([in:[receiverDeployment:[station:[installation:[project:[name:"Whales, Sharks, "], name:"Bondi"]]]]], 10000, 0))
	}

	void testConstructQueryOneInstallation()
	{
		assertEquals('''select * from detection_extract_view where installation in ('Bondi') limit 10000 offset 0''',
					 detectionExtractService.constructQuery([in:[receiverDeployment:[station:[installation:[name:"Bondi, "]]]]], 10000, 0))
	}

    void testConstructQueryOneStation() 
	{
		assertEquals('''select * from detection_extract_view where station in ('CTBAR East') limit 10000 offset 0''', 
					 detectionExtractService.constructQuery([in:[receiverDeployment:[station:[name:"CTBAR East, "]]]], 10000, 0))
    }
	
    void testConstructQueryOneTagID() 
	{
		assertEquals('''select * from detection_extract_view where sensor_id in ('A69-1303-12345') limit 10000 offset 0''', 
					 detectionExtractService.constructQuery([in:[detectionSurgeries:[tag:[codeName:"A69-1303-12345, "]]]], 10000, 0))
    }
	
    void testConstructQueryOneSpecies() 
	{
		assertEquals('''select * from detection_extract_view where spcode in ('12345') limit 10000 offset 0''', 
					 detectionExtractService.constructQuery([in:[detectionSurgeries:[surgery:[release:[animal:[species:[spcode:"12345, "]]]]]]], 10000, 0))
    }

    void testConstructQueryTimestampRange() 
	{
		DateTime startTime = new DateTime("2010-01-01T12:34:56")
		DateTime endTime = new DateTime("2010-01-01T17:00:01")
		
		assertEquals('''select * from detection_extract_view where timestamp between '2010-01-01 12:34:56.0' and '2010-01-01 17:00:01.0' limit 10000 offset 0''', 
					 detectionExtractService.constructQuery([between:[timestamp:[startTime.toDate(), endTime.toDate()]]], 10000, 0))
    }
	
    void testConstructQueryTimestampRangeOneProject() 
	{
		DateTime startTime = new DateTime("2010-01-01T12:34:56")
		DateTime endTime = new DateTime("2010-01-01T17:00:01")
		
		assertEquals('''select * from detection_extract_view where project in ('Whales') and timestamp between '2010-01-01 12:34:56.0' and '2010-01-01 17:00:01.0' limit 10000 offset 0''', 
					 detectionExtractService.constructQuery([between:[timestamp:[startTime.toDate(), endTime.toDate()]], in:[receiverDeployment:[station:[installation:[project:[name:"Whales, "]]]]]], 10000, 0))
    }
}
