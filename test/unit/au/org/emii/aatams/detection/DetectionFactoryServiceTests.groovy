package au.org.emii.aatams.detection

import au.org.emii.aatams.*

import org.joda.time.DateTime

import grails.test.*

class DetectionFactoryServiceTests extends GrailsUnitTestCase {

    void testNewDetection() {
        def dfs = new DetectionFactoryService()
        def download = new ReceiverDownloadFile()
        download.type = ReceiverDownloadFileType.DETECTIONS_CSV

        def csvParams = [
            (VueDetectionFormat.DATE_AND_TIME_COLUMN): '2013-04-05 08:09:10',
            (VueDetectionFormat.RECEIVER_COLUMN): 'VR2W-4567',
            (VueDetectionFormat.TRANSMITTER_COLUMN): 'A69-1303-7856'
        ]

        assertEquals(
            new Detection(
                timestamp: new DateTime('2013-04-05T08:09:10Z'),
                receiverName: 'VR2W-4567',
                transmitterId: 'A69-1303-7856'
            ),
            dfs.newDetection(download, csvParams)
        )
    }
}
