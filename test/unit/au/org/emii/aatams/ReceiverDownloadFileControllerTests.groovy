package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*
import grails.converters.JSON
import grails.converters.XML

class ReceiverDownloadFileControllerTests extends AbstractControllerUnitTestCase {
    Person user

    protected void setUp() {
        super.setUp()

        mockDomain(ReceiverDownloadFile)
        ReceiverDownloadFile.metaClass.validate = { true }

        mockDomain(ReceiverDownloadFileProgress)

        mockDomain(Person)
        user = new Person(username: "username")
        mockDomain(Person, [user])
        user.save()
    }

    protected def getPrincipal() {
        return user?.id
    }

    void testProgressInvalidDownload() {
        controller.params.id = 123l
        controller.status()

        def controllerResponse = controller.response.contentAsString
        def jsonResult = JSON.parse(controllerResponse)

        assertEquals(404, controller.response.status)

        assertEquals(1, jsonResult.size())
         assertEquals('Unknown receiverDownloadFile id: 123', jsonResult.error.message)
    }

    void testProgressValidDownload() {
        def download = createDownload()

        controller.params.id = download.id
        controller.status()

        def controllerResponse = controller.response.contentAsString

        def jsonResult = JSON.parse(controllerResponse)

        assertEquals(2, jsonResult.size())

        assertEquals('PROCESSING', jsonResult.status.name)
        assertEquals(73, jsonResult.percentComplete)
    }

    void testInitialiseForProcessing() {
        ReceiverDownloadFile download = new ReceiverDownloadFile(status: FileProcessingStatus.PROCESSING)
        download.initialiseForProcessing("the_filename")

        assertEquals("", download.errMsg)
        assertEquals("the_filename", download.name)
        assertEquals("username", download.requestingUser.username)
    }

    void testSaveXmlSuccessRedirectToShow() {
        controller.request.format = 'xml'
        controller.params.type = 'VUE_XML_ZIPPED'
        controller.params.'example.zip' = '@example.zip'

        controller.request.metaClass.getFileMap = {
            ['example.zip': []]
        }
        controller.metaClass._processRxrDownload = {
        }

        controller.save()

        assertEquals('show', controller.redirectArgs.action)
        assertEquals(controller.params, controller.redirectArgs.params)
    }

    void testSaveXmlBadRequest() {
        controller.request.format = 'xml'
        controller.params.type = 'VUE_XML_ZIPPED'
        controller.params.'example.zip' = '@example.zip'

        // No file attached.
        controller.request.metaClass.getFileMap = {
            [:]
        }

        controller.save()

        def decodedResponse = new XmlParser().parseText(controller.response.contentAsString)

        assertEquals(400, controller.response.status)
        assertEquals('No file uploaded', decodedResponse.'@errMsg')
    }

    void testShowXml() {
        controller.request.format = 'xml'

        def download = createDownload()
        controller.params.id = download.id

        download.metaClass.toXml = {
            "some xml"
        }

        controller.show()

        assertEquals(200, controller.response.status)
        assertEquals("some xml", controller.response.contentAsString)
    }

    def createDownload()  {
        ReceiverDownloadFile download = new ReceiverDownloadFile(status: FileProcessingStatus.PROCESSING)
        download.save()

        ReceiverDownloadFileProgress progress = new ReceiverDownloadFileProgress(percentComplete: 73, receiverDownloadFile: download)
        progress.save()
        download.progress = progress

        return download
    }
}
