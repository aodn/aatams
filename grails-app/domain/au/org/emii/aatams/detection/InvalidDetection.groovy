package au.org.emii.aatams.detection

import au.org.emii.aatams.ReceiverDownloadFile
import au.org.emii.aatams.util.SqlUtils
import java.sql.PreparedStatement
import java.sql.Timestamp

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

	static PreparedStatement prepareInsertStatement(connection)
	{
		String sql = "INSERT INTO INVALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, MESSAGE, REASON) VALUES (nextval('hibernate_sequence'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
		return connection.prepareStatement(sql);
	}

	static void addToPreparedStatement(preparedStatement, detection)
	{
		preparedStatement.setLong     (1,  0)
		preparedStatement.setTimestamp(2,  new Timestamp(detection.timestamp.getTime()))
		preparedStatement.setLong     (3,  detection.receiverDownloadId)
		preparedStatement.setString   (4,  detection.receiverName)
		preparedStatement.setString   (5,  detection.sensorUnit)
		// sensorValue can be null
		preparedStatement.setFloat    (6,  detection.sensorValue ? Float.valueOf(detection.sensorValue) : 0f)
		preparedStatement.setString   (7,  detection.stationName)
		preparedStatement.setString   (8,  detection.transmitterId)
		preparedStatement.setString   (9,  detection.transmitterName)
		preparedStatement.setString   (10, detection.transmitterSerialNumber)
		preparedStatement.setString   (11, detection.message)
		preparedStatement.setString   (12, String.valueOf(detection.reason))
		preparedStatement.addBatch()
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
