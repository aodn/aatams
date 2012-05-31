package au.org.emii.aatams.bulk

import au.org.emii.aatams.Organisation
import grails.test.*
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile;

class BulkImportJobTests extends GrailsUnitTestCase 
{
	BulkImportService bulkImportService
	BulkImport bulkImport
	BulkImportJob bulkImportJob
	
	boolean processCalled
	Long expectedId
	
    protected void setUp() 
	{
        super.setUp()
		
		mockLogging(BulkImportService, true)
		bulkImportService = new BulkImportService()
		bulkImportService.metaClass.process =
		{
			Long bulkImportId, MultipartFile multipartFile ->
			
			processCalled = true
			assertEquals(expectedId, bulkImportId)
		}
		
		bulkImportJob = new BulkImportJob()
		bulkImportJob.bulkImportService = bulkImportService
		
		bulkImport = new BulkImport(organisation: new Organisation(), importStartDate: new Date(), status: BulkImportStatus.IN_PROGRESS)
		mockDomain(BulkImport, [bulkImport])
		bulkImport.save()
		
		processCalled = false
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testExecute() 
	{
		expectedId = 23
		bulkImportJob.execute([mergedJobDataMap:[bulkImportId: 23, multipartFile: new MockMultipartFile("file1.zip", "foo".bytes)]])
		
		assertTrue(processCalled)
    }
}
