package au.org.emii.aatams.detection

import java.text.DateFormat
import java.text.SimpleDateFormat;

import au.org.emii.aatams.Receiver
import au.org.emii.aatams.ReceiverDeployment
import au.org.emii.aatams.Tag
import au.org.emii.aatams.bulk.BulkImportException
import au.org.emii.aatams.bulk.BulkImportRecord;

class CsiroDetectionFormat extends DetectionFormat 
{
	static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z")
	
	Map parseRow(row) throws BulkImportException
	{
		BulkImportRecord tagRecord = BulkImportRecord.findByDstClassAndSrcPk("au.org.emii.aatams.Tag", Long.valueOf(row['ACO_ID']))
		
		if (!tagRecord)
		{
			throw new BulkImportException("No bulk import record for tag with ACO_ID = ${row['ACO_ID']}")
		}
		
		Tag tag = Tag.get(tagRecord.dstPk)
		if (!tag)
		{
			throw new BulkImportException("No tag corresponding to bulk import record with ACO_ID = ${row['ACO_ID']}")
		}

		BulkImportRecord receiverDeploymentRecord = BulkImportRecord.findByDstClassAndSrcPk("au.org.emii.aatams.ReceiverDeployment", Long.valueOf(row['RCD_ID']))
		if (!receiverDeploymentRecord)
		{
			throw new BulkImportException("No bulk import record for receiver with RCD_ID = ${row['RCD_ID']}")
		}
		
		Receiver receiver = ReceiverDeployment.get(receiverDeploymentRecord.dstPk)?.receiver
		if (!receiver)
		{
			throw new BulkImportException("No receiver corresponding to bulk import record with RCD_ID = ${row['RCD_ID']}")
		}

		return [timestamp: DATE_FORMAT.parse(row['DET_DATETIME'] + " UTC"),
			    receiverName: receiver.name,
				transmitterId: tag.sensors.toList()[0].transmitterId]
	}
}
