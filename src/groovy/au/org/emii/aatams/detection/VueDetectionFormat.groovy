package au.org.emii.aatams.detection

import au.org.emii.aatams.FileFormat;
import au.org.emii.aatams.bulk.FileFormatException
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.Point

class VueDetectionFormat extends FileFormat
{
    static final String DATE_AND_TIME_COLUMN = "Date and Time (UTC)"
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"
    static final String RECEIVER_COLUMN = "Receiver"
    static final String TRANSMITTER_COLUMN = "Transmitter"
    static final String TRANSMITTER_NAME_COLUMN = "Transmitter Name"
    static final String TRANSMITTER_SERIAL_NUMBER_COLUMN = "Transmitter Serial"
    static final String SENSOR_VALUE_COLUMN = "Sensor Value"
    static final String SENSOR_UNIT_COLUMN = "Sensor Unit"
    static final String STATION_NAME_COLUMN = "Station Name"
    static final String LATITUDE_COLUMN = "Latitude"
    static final String LONGITUDE_COLUMN = "Longitude"

    Map parseRow(row) throws FileFormatException
    {

        def timestamp = getUtcDate(row, DATE_AND_TIME_COLUMN, DATE_FORMAT)

        def retMap =
               [timestamp:timestamp,
                receiverName:row[RECEIVER_COLUMN],
                transmitterId:row[TRANSMITTER_COLUMN],
                transmitterName:row[TRANSMITTER_NAME_COLUMN],
                transmitterSerialNumber:row[TRANSMITTER_SERIAL_NUMBER_COLUMN],
                sensorValue:getFloat(row[SENSOR_VALUE_COLUMN]),
                sensorUnit:row[SENSOR_UNIT_COLUMN],
                stationName:row[STATION_NAME_COLUMN]]

        // Latitude and longitude are optional.
        if ((row[LATITUDE_COLUMN]) && (row[LONGITUDE_COLUMN]))
        {
            Point location = new GeometryFactory().createPoint(new Coordinate(getFloat(row[LONGITUDE_COLUMN]), getFloat(row[LATITUDE_COLUMN])))
            location.setSRID(4326)
            retMap.location = location
        }
        else
        {
            retMap.location = null
        }

        return retMap
    }

    private def getFloat(stringVal)
    {
        try
        {
            return Float.valueOf(stringVal)
        }
        catch (NullPointerException e)
        {
            return null
        }
        catch (NumberFormatException e)
        {
            return null
        }
    }
}
