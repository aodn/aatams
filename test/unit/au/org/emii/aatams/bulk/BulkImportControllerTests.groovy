package au.org.emii.aatams.bulk

import au.org.emii.aatams.Organisation
import grails.test.*

import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.grails.plugins.testing.GrailsMockHttpServletRequest
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest
import org.springframework.web.multipart.MultipartFile;

class BulkImportControllerTests extends ControllerUnitTestCase 
{
    protected void setUp() 
	{
        super.setUp()
		
		controller.request.metaClass.mixin(MockMultipartHttpServletRequest)
		Organisation csiroCmarHobart = new Organisation(name: 'CSIRO', department: 'CMAR Hobart')
		mockDomain(Organisation, [csiroCmarHobart])
		csiroCmarHobart.save()
		
		mockDomain(BulkImport)
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testSaveNoFile() 
	{
		controller.save()
		
		assertEquals("Number of posted files must be exactly one, you posted: 0", controller.flash.error)
		assertEquals("create", controller.renderArgs.view)
    }
	
	void testSaveTwoFiles()
	{
		controller.request.addFile(new MockMultipartFile("file1.zip", "foo".bytes))
		controller.request.addFile(new MockMultipartFile("file2.zip", "foo".bytes))

		controller.save()
		
		assertEquals("Number of posted files must be exactly one, you posted: 2", controller.flash.error)
		assertEquals("create", controller.renderArgs.view)
	}

	void testSaveOneFile()
	{
		boolean triggerNowCalled = false
		def bulkImportIdInJob
		
		BulkImportJob.metaClass.static.triggerNow = 
		{
			params ->
			
			triggerNowCalled = true
			bulkImportIdInJob = params.bulkImportId
		}
		
		controller.request.addFile(new MockMultipartFile("file1.zip", "foo".bytes))

		controller.save()
		
		assertEquals("show", controller.redirectArgs.action)
		assertNotNull(BulkImport.get(controller.redirectArgs.id))
		assertTrue(triggerNowCalled)
		assertEquals(controller.redirectArgs.id, bulkImportIdInJob)
	}	
}
