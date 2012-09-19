package au.org.emii.aatams

import grails.test.*
import grails.converters.JSON

class ReceiverDownloadFileControllerTests extends ControllerUnitTestCase 
{
    protected void setUp() 
	{
        super.setUp()
		
		mockDomain(ReceiverDownloadFile)
		ReceiverDownloadFile.metaClass.validate = { true }
		
		mockDomain(ReceiverDownloadFileProgress)
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

	void testProgressInvalidDownload()
	{
		controller.params.id = 123l
		controller.status()
		
		def controllerResponse = controller.response.contentAsString
		def jsonResult = JSON.parse(controllerResponse)

		assertEquals(404, controller.response.status)
		
		assertEquals(1, jsonResult.size())
 		assertEquals('Unknown receiverDownloadFile id: 123', jsonResult.error.message)
	}
	
    void testProgressValidDownload() 
	{
		ReceiverDownloadFile download = new ReceiverDownloadFile(status: FileProcessingStatus.PROCESSING)
		download.save()
		
		ReceiverDownloadFileProgress progress = new ReceiverDownloadFileProgress(percentComplete: 73, receiverDownloadFile: download)
		progress.save()
		download.progress = progress
		
		controller.params.id = download.id
		controller.status()
		
		def controllerResponse = controller.response.contentAsString

		def jsonResult = JSON.parse(controllerResponse)

		assertEquals(2, jsonResult.size())

		assertEquals('PROCESSING', jsonResult.status.name)
		assertEquals(73, jsonResult.percentComplete)
    }
}
