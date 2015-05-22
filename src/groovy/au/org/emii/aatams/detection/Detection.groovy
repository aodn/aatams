package au.org.emii.aatams.detection

import au.org.emii.aatams.ReceiverDownloadFile

import groovy.sql.Sql

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.ISODateTimeFormat

import org.jooq.*
import org.jooq.conf.ParamType
import org.jooq.impl.DSL

import static org.jooq.impl.DSL.*

class Detection {

    static def ISO8601_TIMESTAMP_FORMATTER = ISODateTimeFormat.dateTime()
    static def CSV_TIMESTAMP_FORMATTER = DateTimeFormat.forPattern('yyyy-MM-dd HH:mm:ss')

    Long id

    DateTime timestamp
    String receiverName
    String transmitterId
    String transmitterName
    String transmitterSerialNumber
    Double sensorValue
    String sensorUnit
    String stationName
    Double latitude
    Double longitude
    Long receiverDownloadId

    String toString() {
        return "${timestamp.withZone(DateTimeZone.UTC).toString()}, ${receiverName}, ${transmitterId}"
    }

    def toSqlInsertInlined() {
        toSqlInsert(this)
    }

    static def toSqlInsert(det) {
        DSLContext create = DSL.using(SQLDialect.POSTGRES);

        def insert = create.insertInto(
            table('DETECTION'),
            field('ID'),
            field('TIMESTAMP'),
            field('RECEIVER_NAME'),
            field('TRANSMITTER_ID'),
            field('TRANSMITTER_NAME'),
            field('TRANSMITTER_SERIAL_NUMBER'),
            field('SENSOR_VALUE'),
            field('SENSOR_UNIT'),
            field('STATION_NAME'),
            field('LATITUDE'),
            field('LONGITUDE'),
            field('RECEIVER_DOWNLOAD_ID')
        )
        .values(
            sequenceByName('hibernate_sequence').nextval(),
            ISO8601_TIMESTAMP_FORMATTER.print(det.timestamp),
            det.receiverName,
            det.transmitterId,
            det.transmitterName,
            det.transmitterSerialNumber,
            det.sensorValue,
            det.sensorUnit,
            det.stationName,
            det.latitude,
            det.longitude,
            det.receiverDownloadId
        )

        return insert.getSQL(ParamType.INLINED)
    }

    static def fromSqlRow(def row) {
        def detection = [
            id: row.detection_id,
            timestamp: new DateTime(row.timestamp).withZone(DateTimeZone.UTC),
            receiverName: row.receiver_name,
            transmitterId: row.transmitter_id,
            transmitterName: row.transmitter_name,
            transmitterSerialNumber: row.transmitter_serial_number,
            sensorValue: row.sensor_value,
            sensorUnit: row.sensor_unit,
            stationName: row.station_name,
            latitude: row.latitude,
            longitude: row.longitude,
            receiverDownloadId: row.receiver_download_id
        ]

        return detection
    }

    String getCsvFormattedTimestamp() {
        return CSV_TIMESTAMP_FORMATTER.print(timestamp)
    }

    ReceiverDownloadFile getReceiverDownload() {
        return ReceiverDownloadFile.get(receiverDownloadId)
    }

    boolean equals(other) {
        if (!other) {
            return false
        }

        return this.timestamp == other.timestamp &&
               this.receiverName == other.receiverName &&
               this.transmitterId == other.transmitterId
    }
}
