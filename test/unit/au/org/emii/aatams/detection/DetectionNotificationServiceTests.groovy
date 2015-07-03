package au.org.emii.aatams.detection

import au.org.emii.aatams.ReceiverDownloadFile
import au.org.emii.aatams.ReceiverDownloadFileProgress;
import au.org.emii.aatams.Sensor
import grails.test.*
import org.springframework.context.support.DelegatingMessageSource

class DetectionNotificationServiceTests extends GrailsUnitTestCase {
     def messageSource
    def detectionNotificationService

    protected void setUp() {
        super.setUp()

        mockDomain(Sensor)
        mockLogging(DetectionNotificationService, true)
        detectionNotificationService = new DetectionNotificationService()
        messageSource = new DelegatingMessageSource()

        detectionNotificationService.messageSource = messageSource
    }

    void testNotificationEmailSent() {
        mockLogging(ReceiverDownloadFile)
        mockDomain(ReceiverDownloadFile)
        ReceiverDownloadFile.metaClass.getUniqueTransmitterIds = {

        }

        ReceiverDownloadFile download = new ReceiverDownloadFile()
        download.save()
        download.id = 123

        download.grailsApplication = [config: [fileimport: [path: "some path"]]]

        Sensor.metaClass.static.groupByOwningPI = {
            List<Sensor> sensors ->

            return [[name: 'adam', emailAddress: 'adam@aatams']: [[id: 1, transmitterId: 'a69_1303_1111'], [id:2, transmitterId: 'a69_1303_2222']],
                    [name: 'bruce', emailAddress: 'bruce@aatams']: [[id:3, transmitterId: 'a69_1303_1111']]]
        }

        int sendEmailToPersonCallCount = 0

        detectionNotificationService.metaClass.sendDetectionNotificationEmailToPerson = {
            recipient, sensors, downloadFile ->

            if (sendEmailToPersonCallCount == 0) {
                assertEquals('adam', recipient.name)
                assertContainsAll([1, 2], sensors*.id)
            }
            else if (sendEmailToPersonCallCount == 1) {
                assertEquals('bruce', recipient.name)
                assertContainsAll([3], sensors*.id)
            }
            else {
                fail()
            }

            sendEmailToPersonCallCount++
        }

        detectionNotificationService.sendDetectionNotificationEmails(download)

        assertEquals(2, sendEmailToPersonCallCount)
    }

    private assertContainsAll(listA, listB) {
        assertEquals(listA.size(), listB.size())
        assertTrue(listA.containsAll(listB))
        assertTrue(listB.containsAll(listA))
    }
}
