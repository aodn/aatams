package au.org.emii.aatams

import groovy.sql.Sql

import au.org.emii.aatams.detection.Detection
import grails.test.*
import org.joda.time.DateTime

class ReceiverDownloadFileTests extends GroovyTestCase {

    def dataSource

    void testGetUniqueTransmitterIds() {
        ReceiverDownloadFile export =
            new ReceiverDownloadFile(type:ReceiverDownloadFileType.DETECTIONS_CSV,
                                     name:"export.csv",
                                     importDate:new DateTime("2013-05-17T12:54:56").toDate(),
                                     status:FileProcessingStatus.PROCESSED,
                                     errMsg:"",
                                     requestingUser:Person.findByName("Joe Bloggs"))


        export.save(flush: true)

        def sql = new Sql(dataSource)

        [ 'A69-1303-1111', 'A69-1303-5555', 'A69-1303-1111', 'A69-1303-3333' ].each {
            transmitterId ->

            int i = 0

            Detection detection =
                new Detection(
                    timestamp:new DateTime("2011-05-17T02:54:00+00:00").plusSeconds(i++),
                    receiverName: Receiver.list()[0].name,
                    transmitterId: transmitterId,
                    receiverDownloadId: export.id
                )

            sql.execute(detection.toSqlInsertInlined())
        }

        assertEquals(['A69-1303-1111', 'A69-1303-3333', 'A69-1303-5555'], export.getUniqueTransmitterIds())

        sql.execute("DELETE FROM detection WHERE receiver_download_id = ?", export.id)
        export.delete()
    }
}
