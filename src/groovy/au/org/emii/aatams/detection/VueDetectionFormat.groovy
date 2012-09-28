package au.org.emii.aatams.detection

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.Point
import java.util.Map;

class VueDetectionFormat extends DetectionFormat 
{
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
	
	static final String TRANSMITTER_ID_DELIM = "-"
	static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"

	Map parseRow(row) 
	{
		def timestamp = new Date().parse(DATE_FORMAT, row[DATE_AND_TIME_COLUMN] + " " + "UTC")
		
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
		if ((row[LATITUDE_COLUMN] != null) && (row[LONGITUDE_COLUMN] != null))
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
