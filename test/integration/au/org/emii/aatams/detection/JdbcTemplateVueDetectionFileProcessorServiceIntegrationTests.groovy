package au.org.emii.aatams.detection

import au.org.emii.aatams.DetectionSurgery;
import au.org.emii.aatams.FileProcessingStatus
import au.org.emii.aatams.ReceiverDownloadFile
import au.org.emii.aatams.ReceiverDownloadFileType

import grails.test.*

class JdbcTemplateVueDetectionFileProcessorServiceIntegrationTests extends GroovyTestCase 
{
	def jdbcTemplateVueDetectionFileProcessorService
	
    protected void setUp() 
	{
        super.setUp()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }
	
	// Test for #2055
	void testNothing()
	{
		
	}
/* Broken because of mocked method in previous unit tests.
 * TODO: fix	

    void testNoDetectionSurgeryForDuplicateDetection() 
	{
		ReceiverDownloadFile export
		
		ReceiverDownloadFile.withNewTransaction
		{
			export =
				new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV,
										 name: "duplicate",
										 importDate: new Date(),
										 status: FileProcessingStatus.PROCESSING,
										 errMsg: "",
										 requestingUser: null).save(failOnError:true)
										 
			export.initialiseForProcessing("duplicate.csv")		
			export.save(flush: true, failOnError: true)					
		}
		
		export = ReceiverDownloadFile.read(export.id)
		
		def exportFile = new File(export.path)
		exportFile.getParentFile().mkdirs()
		exportFile << '''Date and Time (UTC),Receiver,Transmitter,Transmitter Name,Transmitter Serial,Sensor Value,Sensor Unit,Station Name,Latitude,Longitude
2011-05-17 02:54:05,VR2W-101336,A69-1303-62339
'''
		
		try
		{		
			def detSurgeryCount = DetectionSurgery.count()
			jdbcTemplateVueDetectionFileProcessorService.process(export)
			assertEquals(detSurgeryCount, DetectionSurgery.count())
		}
		finally
		{
			exportFile.delete()
		}
    }
*/    
}
