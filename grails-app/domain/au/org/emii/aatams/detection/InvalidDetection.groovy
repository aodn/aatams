package au.org.emii.aatams.detection

import au.org.emii.aatams.ReceiverDownloadFile
import au.org.emii.aatams.util.SqlUtils

import org.jooq.*
import org.jooq.conf.ParamType
import org.jooq.impl.DSL

import static org.jooq.impl.DSL.*

class InvalidDetection extends RawDetection
{
    InvalidDetectionReason reason
    String message

    static belongsTo = [receiverDownload:ReceiverDownloadFile]
    static transients = RawDetection.transients

    boolean isValid()
    {
        return false
    }

    static def toSqlInsert(det)
    {
        DSLContext create = DSL.using(SQLDialect.POSTGRES);

        def insert = create.insertInto(
            table('INVALID_DETECTION'),
            field('ID'),
            field('VERSION'),
            field('TIMESTAMP'),
            field('RECEIVER_DOWNLOAD_ID'),
            field('RECEIVER_NAME'),
            field('SENSOR_UNIT'),
            field('SENSOR_VALUE'),
            field('STATION_NAME'),
            field('TRANSMITTER_ID'),
            field('TRANSMITTER_NAME'),
            field('TRANSMITTER_SERIAL_NUMBER'),
            field('MESSAGE'),
            field('REASON')
        )
        .values(
            sequenceByName('hibernate_sequence').nextval(),
            0,
            formatTimestamp(det['timestamp'], 'yyyy-MM-dd HH:mm:ssZ'),
            det.receiverDownloadId,
            det.receiverName,
            det.sensorUnit,
            det.sensorValue,
            det.stationName,
            det.transmitterId,
            det.transmitterName,
            det.transmitterSerialNumber,
            det.message,
            det.reason
        )

        return insert.getSQL(ParamType.INLINED)
    }

    String toString()
    {
        StringBuilder buf = new StringBuilder(String.valueOf(reason))
        if (message)
        {
            buf.append(": ")
            buf.append(message)
        }

        return buf.toString()
    }
}
