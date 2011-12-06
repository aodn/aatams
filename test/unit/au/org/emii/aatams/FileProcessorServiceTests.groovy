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
		fileProcessorService.metaClass.saveToDiskAndProcess = { ReceiverDownloadFile receiverDownloadFile, MultipartFile file, showLink -> }
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

	void testValidateEventsFilename()
	{
		assertInvalidFilenames(["mockFile.rld", "mockFile.vrl", "mockFile.xyz", "asdf"], ReceiverDownloadFileType.EVENTS_CSV)
		assertValidFilenames(["mockFile.csv"], ReceiverDownloadFileType.EVENTS_CSV)
	}
	
	void testValidateDetectionsCSVFilename()
	{
		assertInvalidFilenames(["mockFile.rld", "mockFile.vrl", "mockFile.xyz", "asdf"], ReceiverDownloadFileType.DETECTIONS_CSV)
		assertValidFilenames(["mockFile.csv"], ReceiverDownloadFileType.DETECTIONS_CSV)
	}

	void testValidateDetectionsVRLFilename()
	{
		assertInvalidFilenames(["mockFile.csv", "mockFile.rld", "mockFile.xyz", "asdf"], ReceiverDownloadFileType.VRL)
		assertValidFilenames(["mockFile.vrl"], ReceiverDownloadFileType.VRL)
	}

	void testValidateDetectionsRLDFilename()
	{
		assertInvalidFilenames(["mockFile.csv", "mockFile.vrl", "mockFile.xyz", "asdf"], ReceiverDownloadFileType.RLD)
		assertValidFilenames(["mockFile.rld"], ReceiverDownloadFileType.RLD)
	}

	private void assertValidFilenames(filenames, fileType)
	{
		filenames.each
		{
			assertNoFileProcessingException(it,
											"asdfasdf".getBytes(),
											fileType)
		}
	}

	private void assertInvalidFilenames(filenames, fileType)
	{
		filenames.each
		{
			def errMsg = "Invalid " + ReceiverDownloadFileType.getCategory(fileType) + " filename (" + it + ") - must have extension \"." + ReceiverDownloadFileType.getExtension(fileType) + "\"."
			
			assertFileProcessingException(it,
				"asdfasdf".getBytes(),
				fileType,
				errMsg)
		}
	}
	
	void testProcessEventsEmptyFile()
	{
		def emptyFile =
			new MockMultipartFile("emptyFile",
								  "emptyFile.csv",
								  "text/csv",
								  new byte[0])

		assertFileProcessingException("emptyFile.csv", new byte[0], ReceiverDownloadFileType.EVENTS_CSV, "File is empty")
	}
	
	private void assertFileProcessingException(filename, data, downloadType, errMsg)
	{
		def mockFile = 
			new MockMultipartFile("mockFile", 
								  filename, 
								  "text/csv",
								  data)
			
		ReceiverDownloadFile export = createExport(filename, downloadType)
			
		try
		{
			fileProcessorService.process(export.id, mockFile, "some link")
			fail("Exception should be thrown")
		}
		catch (FileProcessingException e)
		{
			assertEquals(errMsg, e.getMessage())
		} 
	}

	private void assertNoFileProcessingException(filename, data, downloadType)
	{
		def mockFile = 
			new MockMultipartFile("mockFile", 
								  filename, 
								  "text/csv",
								  data)
			
		ReceiverDownloadFile export = createExport(filename, downloadType)
			
		try
		{
			fileProcessorService.process(export.id, mockFile, "some link")
		}
		catch (FileProcessingException e)
		{
			fail("Exception should not be thrown")
		} 
	}

	private ReceiverDownloadFile createExport(filename, downloadType) 
	{
		ReceiverDownloadFile export =
				new ReceiverDownloadFile(path:System.getProperty("java.io.tmpdir") + "fileProcessorServiceTests",
										 name:filename,
										 type:downloadType)
				
		mockDomain(ReceiverDownloadFile, [export])
		export.save()
		return export
	}
}
