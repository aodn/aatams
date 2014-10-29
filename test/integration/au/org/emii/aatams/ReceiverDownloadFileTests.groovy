package au.org.emii.aatams

import au.org.emii.aatams.detection.ValidDetection
import grails.test.*
import org.joda.time.DateTime

class ReceiverDownloadFileTests extends GroovyTestCase
{
    void testGetUniqueTransmitterIds()
    {
        Person.build(name: 'Joe Bloggs')
        def receiver = Receiver.build()
        def deployment = ReceiverDeployment.build(receiver: receiver)

        ReceiverDownloadFile export =
            new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV,
                                     name: "export.csv",
                                     importDate: new DateTime("2013-05-17T12:54:56").toDate(),
                                     status: FileProcessingStatus.PROCESSED,
                                     errMsg: "",
                                     requestingUser: Person.findByName("Joe Bloggs"))

        ['A69-1303-1111', 'A69-1303-5555', 'A69-1303-1111', 'A69-1303-3333'].each
        {
            transmitterId ->

            int i = 0

            ValidDetection detection =
                new ValidDetection(receiverDeployment: deployment,
                                   timestamp: new DateTime("2011-05-17T02:54:00+00:00").plusSeconds(i++).toDate(),
                                   receiverName: Receiver.list()[0].name,
                                   transmitterId: transmitterId,
                                   receiverDownload: export)
            export.addToDetections(detection)
        }

        export.save()

        assertEquals(['A69-1303-1111', 'A69-1303-3333', 'A69-1303-5555'], export.getUniqueTransmitterIds())

        export.delete()
    }
}
