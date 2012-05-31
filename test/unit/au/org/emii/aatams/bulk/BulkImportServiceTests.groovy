package au.org.emii.aatams.bulk

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream;

import au.org.emii.aatams.Organisation
import grails.test.*

import org.apache.commons.io.FileUtils;
import org.springframework.mock.web.MockMultipartFile

class BulkImportServiceTests extends GrailsUnitTestCase 
{
	BulkImport bulkImport
	BulkImportService bulkImportService
	
    protected void setUp() 
	{
        super.setUp()
		
		mockLogging(BulkImportService, true)
		bulkImportService = new BulkImportService()

		bulkImport = new BulkImport(organisation: new Organisation(), importStartDate: new Date(), status: BulkImportStatus.IN_PROGRESS)
		mockDomain(BulkImport, [bulkImport])
		bulkImport.save()
		
		mockConfig("bulkimport.path = \"" + FileUtils.getTempDirectory().getPath() + "\"")
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testInvalidBulkImportId() 
	{
		try
		{
			bulkImportService.process(12, new MockMultipartFile("file1.zip", "foo".bytes))
			fail()
		}
		catch (BulkImportException e)
		{
			assertEquals("Invalid bulk import id: 12", e.message)
		}
    }
	
	void testBulkImportWriteFileToDisk()
	{
		bulkImportService.process(bulkImport.id, new MockMultipartFile("testBulkImportSaveFile.zip", "testBulkImportSaveFile.zip", null, new ByteArrayInputStream("foo".bytes)))
		
		try
		{
			FileInputStream bulkIn = new FileInputStream(bulkImport.path)
		}
		catch (Exception e)
		{
			fail()
		}
		
	}
	
	void testBulkImportReceivers()
	{
		boolean processReceiversCalled = false
		
		def receiversText = '''"RCV_ID","RCV_SERIAL_NO","RCV_MODEL_CODE","RCV_OWNER","RCV_COMMENTS","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
		1,1661,"VR2",,,15/5/2008 16:01:55,"TAG",15/5/2008 16:01:55,"TAG"
		2,1663,"VR2",,,15/5/2008 16:01:56,"TAG",15/5/2008 16:01:56,"TAG"
'''
		
		bulkImportService.metaClass.processReceivers =
		{
			InputStream receiverStream ->
			
			processReceiversCalled = true
		}
		
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream()
		ZipOutputStream zipOut = new ZipOutputStream(byteOut)
		
		ZipEntry receiversEntry = new ZipEntry("RECEIVERS.csv")
		zipOut.putNextEntry(receiversEntry)
		zipOut.write(receiversText.bytes)
		zipOut.closeEntry()
		zipOut.close()
		
		bulkImportService.process(bulkImport.id, new MockMultipartFile("testBulkImportReceivers.zip", "testBulkImportReceivers.zip", null, new ByteArrayInputStream(byteOut.toByteArray())))
		assertTrue(processReceiversCalled)
	}
}
