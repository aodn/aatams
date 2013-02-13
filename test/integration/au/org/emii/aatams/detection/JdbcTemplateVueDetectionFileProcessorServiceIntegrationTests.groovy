package au.org.emii.aatams.detection

import au.org.emii.aatams.DetectionSurgery;
import au.org.emii.aatams.FileProcessingStatus
import au.org.emii.aatams.Person
import au.org.emii.aatams.ReceiverDownloadFile
import au.org.emii.aatams.ReceiverDownloadFileType

import au.org.emii.aatams.test.*

import grails.test.*

class JdbcTemplateVueDetectionFileProcessorServiceIntegrationTests extends AbstractGrailsUnitTestCase 
{
    static transactional = false
    
	def jdbcTemplateVueDetectionFileProcessorService
    ReceiverDownloadFile export
	def exportFile
    
    protected void setUp() 
	{
        super.setUp()
        
        export =
            new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV,
            name: "duplicate",
            importDate: new Date(),
            status: FileProcessingStatus.PROCESSING,
            errMsg: "",
            requestingUser: null).save(failOnError:true)
										 
            export.initialiseForProcessing("duplicate.csv")
            export.requestingUser = Person.findByUsername('jbloggs')
			export.save(flush: true, failOnError: true)					
		
		export = ReceiverDownloadFile.read(export.id)
		
		exportFile = new File(export.path)
		exportFile.getParentFile().mkdirs()
		exportFile << '''Date and Time (UTC),Receiver,Transmitter,Transmitter Name,Transmitter Serial,Sensor Value,Sensor Unit,Station Name,Latitude,Longitude
'''
    }

    protected void tearDown() 
	{
        exportFile?.delete()
        getRefreshedExport(export)?.delete()
        
        super.tearDown()
    }
	
	// Test for #2055
	void testNothing()
	{
		
	}

    void testNoDetectionSurgeryForDuplicateDetection() 
	{
		exportFile << '''2011-05-17 02:54:05,VR2W-101336,A69-1303-62339
'''
		
        def detSurgeryCount = DetectionSurgery.count()
        jdbcTemplateVueDetectionFileProcessorService.process(export)
        assertEquals(detSurgeryCount, DetectionSurgery.count())
    }

    void testNewDetectionsAreProvisional()
    {
		exportFile << '''2011-05-17 03:54:05,VR2W-101336,A69-1303-62339
2011-05-17 04:54:05,VR2W-101336,A69-1303-62339
2011-05-17 05:54:05,VR2W-101336,A69-1303-62339
'''

        jdbcTemplateVueDetectionFileProcessorService.process(export)

        assertEquals(3, getRefreshedExport(export)?.validDetections.size())
        getRefreshedExport(export)?.validDetections.each {
            assertTrue(it.isProvisional())
        }
    }

    private def getRefreshedExport(export)
    {
        return ReceiverDownloadFile.read(export?.id)
    }
}
