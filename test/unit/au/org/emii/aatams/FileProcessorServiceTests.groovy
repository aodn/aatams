package au.org.emii.aatams

import grails.test.*
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;


class FileProcessorServiceTests extends GrailsUnitTestCase
{
	FileProcessorService fileProcessorService
	
    protected void setUp() 
	{
        super.setUp()
		
		mockLogging(FileProcessorService, true)
		fileProcessorService = new FileProcessorService()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

	void testProcessEventsEmptyFile()
	{
		ReceiverDownloadFile export = createExport()
		
		def emptyFile = 
			new MockMultipartFile("emptyFile", 
								  "emptyFile.csv", 
								  "text/csv",
								  new byte[0])
			
		try
		{
			fileProcessorService.process(export.id, emptyFile, "some link")
			fail("Exception should be thrown")
		}
		catch (FileProcessingException e)
		{
			assertEquals("File is empty", e.getMessage())
		} 
	}

	private ReceiverDownloadFile createExport() 
	{
		ReceiverDownloadFile export =
				new ReceiverDownloadFile(path:System.getProperty("java.io.tmpdir") + "fileProcessorServiceTests",
										 name:"filename.csv",
										 type:ReceiverDownloadFileType.EVENTS_CSV)
				
		mockDomain(ReceiverDownloadFile, [export])
		export.save()
		return export
	}
}
