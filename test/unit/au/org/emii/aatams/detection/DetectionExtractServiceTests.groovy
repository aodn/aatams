package au.org.emii.aatams.detection

import org.joda.time.DateTime;

import au.org.emii.aatams.PermissionUtilsService;
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase

import grails.test.*

class DetectionExtractServiceTests extends AbstractGrailsUnitTestCase 
{
	def detectionExtractService
	def permissionUtilsService
	
    protected void setUp() 
	{
        super.setUp()
		
		mockLogging(DetectionExtractService, true)
		detectionExtractService = new DetectionExtractService()
		
		mockLogging(PermissionUtilsService, true)
		detectionExtractService.permissionUtilsService = new PermissionUtilsService()
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
		assertEquals('''select * from detection_extract_view where trim(project) in ('Whales') limit 10000 offset 0''',
					 detectionExtractService.constructQuery([receiverDeployment:[station:[installation:[project:[in: ["name", "Whales, "]]]]]], 10000, 0))
	}

	void testConstructQueryTwoProjects()
	{
		assertEquals('''select * from detection_extract_view where trim(project) in ('Whales', 'Sharks') limit 10000 offset 0''',
					 detectionExtractService.constructQuery([receiverDeployment:[station:[installation:[project:[in:["name", "Whales, Sharks, "]]]]]], 10000, 0))
	}

	void testConstructQueryOneProjectOneInstallation()
	{
		assertEquals('''select * from detection_extract_view where trim(project) in ('Whales') and trim(installation) in ('Bondi') limit 10000 offset 0''',
					 detectionExtractService.constructQuery([receiverDeployment:[station:[installation:[project:[in:["name", "Whales, "]], in:["name", "Bondi"]]]]], 10000, 0))
	}

	void testConstructQueryTwoProjectsOneInstallation()
	{
		assertEquals('''select * from detection_extract_view where trim(project) in ('Whales', 'Sharks') and trim(installation) in ('Bondi') limit 10000 offset 0''',
					 detectionExtractService.constructQuery([receiverDeployment:[station:[installation:[project:[in:["name", "Whales, Sharks, "]], in:["name", "Bondi"]]]]], 10000, 0))
	}

	void testConstructQueryOneInstallation()
	{
		assertEquals('''select * from detection_extract_view where trim(installation) in ('Bondi') limit 10000 offset 0''',
					 detectionExtractService.constructQuery([receiverDeployment:[station:[installation:[in:["name", "Bondi, "]]]]], 10000, 0))
	}

    void testConstructQueryOneStation() 
	{
		assertEquals('''select * from detection_extract_view where trim(station) in ('CTBAR East') limit 10000 offset 0''', 
					 detectionExtractService.constructQuery([receiverDeployment:[station:[in:["name", "CTBAR East, "]]]], 10000, 0))
    }
	
    void testConstructQueryOneTagID() 
	{
		assertEquals('''select * from detection_extract_view where trim(sensor_id) in ('A69-1303-12345') limit 10000 offset 0''', 
					 detectionExtractService.constructQuery([detectionSurgeries:[tag:[in:["codeName", "A69-1303-12345, "]]]], 10000, 0))
    }
	
    void testConstructQueryOneSpecies() 
	{
		assertEquals('''select * from detection_extract_view where trim(spcode) in ('12345') limit 10000 offset 0''', 
					 detectionExtractService.constructQuery([detectionSurgeries:[surgery:[release:[animal:[species:[in:["spcode", "12345, "]]]]]]], 10000, 0))
    }

    void testConstructQueryTimestampRange() 
	{
		DateTime startTime = new DateTime("2010-01-01T12:34:56")
		DateTime endTime = new DateTime("2010-01-01T17:00:01")
		
		assertEquals('''select * from detection_extract_view where timestamp between '2010-01-01 12:34:56.0' and '2010-01-01 17:00:01.0' limit 10000 offset 0''', 
					 detectionExtractService.constructQuery([between:["timestamp", startTime.toDate(), endTime.toDate()]], 10000, 0))
    }
	
    void testConstructQueryTimestampRangeOneProject() 
	{
		DateTime startTime = new DateTime("2010-01-01T12:34:56")
		DateTime endTime = new DateTime("2010-01-01T17:00:01")
		
		assertEquals('''select * from detection_extract_view where trim(project) in ('Whales') and timestamp between '2010-01-01 12:34:56.0' and '2010-01-01 17:00:01.0' limit 10000 offset 0''', 
					 detectionExtractService.constructQuery([between:["timestamp", startTime.toDate(), endTime.toDate()], 
						 									 receiverDeployment:[station:[installation:[project:[in:["name", "Whales, "]]]]]], 10000, 0))
    }
}
