package au.org.emii.aatams.detection

import org.joda.time.format.ISODateTimeFormat

import au.org.emii.aatams.ReceiverDownloadFile
import au.org.emii.aatams.util.SqlUtils

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

    static String toSqlInsert(detection)
    {
        StringBuilder detectionBuff = new StringBuilder(
                "INSERT INTO INVALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, " +
                "STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, MESSAGE, REASON) " +
                " VALUES(")

        detectionBuff.append("nextval('hibernate_sequence'),")
        detectionBuff.append("0,")
        detectionBuff.append("'${ISODateTimeFormat.basicDateTime().print(detection['timestamp'].getTime())}',")
        SqlUtils.appendIntegerParams(detectionBuff, detection, ["receiverDownloadId"])
        SqlUtils.appendStringParams(detectionBuff, detection, ["receiverName", "sensorUnit", "sensorValue", "stationName", "transmitterId", "transmitterName",
            "transmitterSerialNumber", "message", "reason"])
        SqlUtils.removeTrailingCommaAndAddBracket(detectionBuff)

        return detectionBuff.toString()
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
