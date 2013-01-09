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
		
		mockConfig("bulkimport.path = \"" + System.getProperty("java.io.tmpdir") + "\"")
		registerMetaClass InstallationLoader
		registerMetaClass ReceiverLoader
		registerMetaClass ReceiverDeploymentLoader
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
			List<InputStream> streams ->
			
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
	
	void testBulkImportInstallationsCalled()
	{
		boolean loadCalled = false
		
		def groupingDetailText = '''"GRP_ID","GRP_NAME","GRP_DESCRIPTION"
1,"60 nm closure","The fishery closure area off Port Lincoln SA. Closed the upper-slope fisheries to protect gulper shark population. Study area for gulper tagging and tracking experiment"
2,"GAB","Great Australian Bight"
'''
		
		def groupingsText = '''"GRP_ID","STA_ID"
1,69
1,70
1,71
'''
		def stationsText = '''"STA_ID","STA_SITE_NAME","STA_CONTACT_NAME","STA_DESCRIPTION","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
1,"MB11C","Richard Pillans","Old Name: MB11B",1/10/2010 15:05:31,"TAG",1/10/2010 15:05:31,"TAG"
2,"MB12A","Richard Pillans",,1/10/2010 15:11:07,"TAG",1/10/2010 15:11:26,"TAG"
'''
		
		InstallationLoader.metaClass.load =
		{
			Map context,
			List<InputStream> streams ->
			
			assertNotNull(context.bulkImport)
			loadCalled = true
		}
		
		bulkImportService.process(
			bulkImport.id,
			new MockMultipartFile(
				"testBulkImportInstallations.zip",
				"testBulkImportInstallations.zip",
				null,
				createZipStream(["GROUPINGDETAIL.csv": groupingDetailText,
								 "GROUPINGS.csv": groupingsText,
								 "STATIONS.csv": stationsText])))
		
		assertTrue(loadCalled)
		
		assertEquals(BulkImportStatus.SUCCESS, bulkImport.status)
		assertNotNull(bulkImport.importFinishDate)
	}
	
	void testBulkImportReceiverDeploymentsCalled()
	{
		boolean loadCalled = false
		
		def receiverDeploymentsText = '''"RCD_ID","RCV_ID","STA_ID","RCD_LATITUDE","RCD_LONGITUDE","RCD_MOORING_DEPTH","RCD_DATETIME_IN","RCD_DATETIME_OUT","RCD_FIRMWARE_VER","RCD_PC_DATETIME_AT_UPLOAD","RCD_PC_TIMEZONE_AT_UPLOAD","RCD_PC_TIMEZONE_AT_INIT","RCD_INIT_DATETIME","RCD_LOG_START_DATETIME","RCD_LOG_END_DATETIME","RCD_UPLO
AD_DATETIME","RCD_CODE_MAP_CODE","RCD_BLANKING_INT","RCD_BATTERY_UPTIME","RCD_FILENAME","RCD_PERF_ST
ATS","RCD_GAIN_MODE_CODE","RCD_GAIN","RCD_STRUCTURE_FAIL_YN","RCD_BATTERY_FAIL_YN","RCD_SOFTWARE_FAIL_YN","RCD_LOST_YN","RCD_OTHER_FAILURE","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
256,108,,,,,,,,,,,1/1/1990 0:00:00,,,,,,,,,,,-1,,,,,31/3/2009 15:05:22,"TAG",27/9/2010 10:29:27,"TAG"
257,109,,,,,,,,,,,1/1/1990 0:00:00,,,,,,,,,,,,,,,,31/3/2009 15:17:10,"TAG",27/9/2010 10:29:26,"TAG"
'''
		
		ReceiverDeploymentLoader.metaClass.load =
		{
			Map context,
			List<InputStream> streams ->
			
			assertNotNull(context.bulkImport)
			loadCalled = true
		}
		
		bulkImportService.process(
			bulkImport.id,
			new MockMultipartFile(
				"testBulkImportReceiverDeployments.zip",
				"testBulkImportReceiverDeployments.zip",
				null,
				createZipStream(["RECEIVERDEPLOYMENTS.csv":receiverDeploymentsText])))
		
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
