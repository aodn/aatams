package au.org.emii.aatams.detection

import au.org.emii.aatams.FileFormat
import au.org.emii.aatams.FileFormatException

import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class VueDetectionFormat extends FileFormat {
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

    static final String DATE_AND_TIME_COLUMN = "Date and Time (UTC)"
    static final String RECEIVER_COLUMN = "Receiver"
    static final String TRANSMITTER_COLUMN = "Transmitter"
    static final String TRANSMITTER_NAME_COLUMN = "Transmitter Name"
    static final String TRANSMITTER_SERIAL_NUMBER_COLUMN = "Transmitter Serial"
    static final String SENSOR_VALUE_COLUMN = "Sensor Value"
    static final String SENSOR_UNIT_COLUMN = "Sensor Unit"
    static final String STATION_NAME_COLUMN = "Station Name"
    static final String LATITUDE_COLUMN = "Latitude"
    static final String LONGITUDE_COLUMN = "Longitude"

    static DateTimeFormatter TIMESTAMP_PARSER = DateTimeFormat.forPattern(DATE_FORMAT).withZone(DateTimeZone.UTC)

    Map parseRow(row) throws FileFormatException {

        def retMap = [
            timestamp: TIMESTAMP_PARSER.parseDateTime(row[DATE_AND_TIME_COLUMN]),
            receiverName: row[RECEIVER_COLUMN],
            transmitterId: row[TRANSMITTER_COLUMN],
            transmitterName: row[TRANSMITTER_NAME_COLUMN],
            transmitterSerialNumber: row[TRANSMITTER_SERIAL_NUMBER_COLUMN],
            sensorValue: getFloat(row[SENSOR_VALUE_COLUMN]),
            sensorUnit: row[SENSOR_UNIT_COLUMN],
            stationName: row[STATION_NAME_COLUMN]
        ]
    }

    private def getFloat(stringVal) {
        try {
            return Float.valueOf(stringVal)
        }
        catch (NullPointerException e) {
            return null
        }
        catch (NumberFormatException e) {
            return null
        }
    }
}
