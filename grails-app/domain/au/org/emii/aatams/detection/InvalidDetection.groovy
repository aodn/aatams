package au.org.emii.aatams.detection

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

	static PreparedStatement prepareInsertStatement(connection)
	{
		String sql = "INSERT INTO INVALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, MESSAGE, REASON) VALUES (nextval('hibernate_sequence'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
		return connection.prepareStatement(sql);
	}

	static void addToPreparedStatement(preparedStatement, detection)
	{
		preparedStatement.setInt     (1,  0)
		preparedStatement.setString  (2,  detection.timestamp.getTime())
		preparedStatement.setInt     (3,  detection.receiverDownloadId)
		preparedStatement.setString  (4,  detection.receiverName)
		preparedStatement.setString  (5,  detection.sensorUnit)
		preparedStatement.setString  (6,  detection.sensorValue)
		preparedStatement.setString  (7,  detection.stationName)
		preparedStatement.setString  (8,  detection.transmitterId)
		preparedStatement.setString  (9,  detection.transmitterName)
		preparedStatement.setString  (10, detection.transmitterSerialNumber)
		preparedStatement.setInt     (11, detection.message)
		preparedStatement.setBoolean (12, detection.reason)
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
