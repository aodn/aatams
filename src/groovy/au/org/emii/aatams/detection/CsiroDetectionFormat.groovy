package au.org.emii.aatams.detection

import java.text.DateFormat
import java.text.SimpleDateFormat;

import au.org.emii.aatams.Receiver
import au.org.emii.aatams.Tag
import au.org.emii.aatams.bulk.BulkImportException
import au.org.emii.aatams.bulk.BulkImportRecord;

class CsiroDetectionFormat extends DetectionFormat 
{
	static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z")
	
	Map parseRow(row) throws BulkImportException
	{
		BulkImportRecord tagRecord = BulkImportRecord.findByDstClassAndSrcPk(String.valueOf(Tag.class), Long.valueOf(row['ACO_ID']))
		if (!tagRecord)
		{
			throw new BulkImportException("No bulk import record for tag with ACO_ID = ${row['ACO_ID']}")
		}
		
		Tag tag = Tag.get(tagRecord.dstPk)
		if (!tag)
		{
			throw new BulkImportException("No tag corresponding to bulk import record with ACO_ID = ${row['ACO_ID']}")
		}

		BulkImportRecord receiverRecord = BulkImportRecord.findByDstClassAndSrcPk(String.valueOf(Receiver.class), Long.valueOf(row['RCD_ID']))
		if (!receiverRecord)
		{
			throw new BulkImportException("No bulk import record for receiver with RCD_ID = ${row['RCD_ID']}")
		}
		
		Receiver receiver = Receiver.get(receiverRecord.dstPk)
		if (!receiver)
		{
			throw new BulkImportException("No receiver corresponding to bulk import record with RCD_ID = ${row['RCD_ID']}")
		}

		return [timestamp: DATE_FORMAT.parse(row['DET_DATETIME'] + " UTC"),
			    receiverName: receiver.name,
				transmitterId: tag.sensors.toList()[0].transmitterId]
		
//				transmitterName:row[TRANSMITTER_NAME_COLUMN],
//				transmitterSerialNumber:row[TRANSMITTER_SERIAL_NUMBER_COLUMN],
//				sensorValue:getFloat(row[SENSOR_VALUE_COLUMN]),
//				sensorUnit:row[SENSOR_UNIT_COLUMN],
//				stationName:row[STATION_NAME_COLUMN]]
//
	}
}
