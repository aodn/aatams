package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*
import grails.converters.JSON

class ReceiverDownloadFileControllerTests extends AbstractControllerUnitTestCase
{
    def user

    protected void setUp()
    {
        super.setUp()

        mockDomain(ReceiverDownloadFile)
        ReceiverDownloadFile.metaClass.validate = { true }

        mockDomain(ReceiverDownloadFileProgress)

        mockDomain(Person)
        user = new Person(username: "username")
        mockDomain(Person, [user])
        user.save()
    }

    protected void tearDown()
    {
        super.tearDown()
    }

    protected def getPrincipal()
    {
        return user?.id
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

    void testInitialiseForProcessing()
    {
        ReceiverDownloadFile download = new ReceiverDownloadFile(status: FileProcessingStatus.PROCESSING)
        download.initialiseForProcessing("the_filename")

        assertEquals("", download.errMsg)
        assertEquals("the_filename", download.name)
        assertEquals("username", download.requestingUser.username)
    }
}
