package au.org.emii.aatams.bulk

import java.util.zip.ZipEntry

import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import au.org.emii.aatams.Organisation
import grails.test.*

import org.apache.commons.io.FileUtils;
import org.springframework.mock.web.MockMultipartFile
import org.joda.time.DateTime

class BulkImportServiceTests extends GrailsUnitTestCase 
{
	BulkImport bulkImport
	BulkImportService bulkImportService
	
    protected void setUp() 
	{
        super.setUp()
		
		mockLogging(BulkImportService, true)
		bulkImportService = new BulkImportService()

		bulkImport = new BulkImport(organisation: new Organisation(), importStartDate: new DateTime(), status: BulkImportStatus.IN_PROGRESS)
		mockDomain(BulkImport, [bulkImport])
		bulkImport.save()
		
		mockConfig("bulkimport.path = \"" + FileUtils.getTempDirectory().getPath() + "\"")
		registerMetaClass ReceiverLoader
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
	
	void testInvalidZipFile()
	{
		try
		{
			bulkImportService.process(bulkImport.id, new MockMultipartFile("testBulkImportSaveFile.zip", "testBulkImportSaveFile.zip", null, new ByteArrayInputStream("foo".bytes)))
			fail()
		}
		catch (BulkImportException e)
		{
			assertEquals(BulkImportStatus.ERROR, bulkImport.status)
			assertNotNull(bulkImport.importFinishDate)
			assertEquals("Invalid zip file", e.message)
			assertEquals(ZipException.class, e.cause.class)
		}
	}
	
	void testBulkImportWriteFileToDisk()
	{
		bulkImportService.process(bulkImport.id, new MockMultipartFile("testBulkImportSaveFile.zip", "testBulkImportSaveFile.zip", null, createZipStream(["foo":"foo"])))
		
		try
		{
			FileInputStream bulkIn = new FileInputStream(bulkImport.path)
			assertEquals(BulkImportStatus.SUCCESS, bulkImport.status)
			assertNotNull(bulkImport.importFinishDate)
		}
		catch (Exception e)
		{
			fail()
		}
	}
	
	void testBulkImportReceiversCalled()
	{
		boolean loadCalled = false
		
		def receiversText = '''"RCV_ID","RCV_SERIAL_NO","RCV_MODEL_CODE","RCV_OWNER","RCV_COMMENTS","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
		1,1661,"VR2",,,15/5/2008 16:01:55,"TAG",15/5/2008 16:01:55,"TAG"
		2,1663,"VR2",,,15/5/2008 16:01:56,"TAG",15/5/2008 16:01:56,"TAG"
'''
		
		ReceiverLoader.metaClass.load =
		{
			Map context,
			InputStream receiverStream ->
			
			assertNotNull(context.bulkImport)
			loadCalled = true
		}
		
		bulkImportService.process(
			bulkImport.id, 
			new MockMultipartFile(
				"testBulkImportReceivers.zip", 
				"testBulkImportReceivers.zip", 
				null, 
				createZipStream(["RECEIVERS.csv":receiversText])))
		
		assertTrue(loadCalled)
		
		assertEquals(BulkImportStatus.SUCCESS, bulkImport.status)
		assertNotNull(bulkImport.importFinishDate)
	}
	
	private InputStream createZipStream(Map entries)
	{
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream()
		ZipOutputStream zipOut = new ZipOutputStream(byteOut)
		
		entries.each 
		{
			k, v ->
			
			ZipEntry receiversEntry = new ZipEntry(k)
			zipOut.putNextEntry(receiversEntry)
			zipOut.write(v.bytes)
			zipOut.closeEntry()
		}
		
		zipOut.close()
		
		return new ByteArrayInputStream(byteOut.toByteArray())
	}
}
