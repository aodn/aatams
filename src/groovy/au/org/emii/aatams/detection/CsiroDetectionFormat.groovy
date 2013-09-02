package au.org.emii.aatams.detection

import au.org.emii.aatams.bulk.FileFormatException

import java.text.DateFormat
import java.text.ParseException
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
        return [timestamp: timestamp(row),
			    receiverName: receiver(row).name,
				transmitterId: tag(row).sensors.toList()[0].transmitterId]
	}

    def tagRecord(row) throws BulkImportException {
        BulkImportRecord tagRecord = BulkImportRecord.findByDstClassAndSrcPk("au.org.emii.aatams.Tag", Long.valueOf(row['ACO_ID']))

        if (!tagRecord)
        {
            throw new BulkImportException("No bulk import record for tag with ACO_ID = ${row['ACO_ID']}")
        }

        return tagRecord
    }

    def tag(row) throws BulkImportException {
        Tag tag = Tag.get(tagRecord(row).dstPk)

        if (!tag)
        {
            throw new BulkImportException("No tag corresponding to bulk import record with ACO_ID = ${row['ACO_ID']}")
        }

        return tag
    }

    def receiverDeployment(row) throws BulkImportException {
        BulkImportRecord receiverDeploymentRecord = BulkImportRecord.findByDstClassAndSrcPk("au.org.emii.aatams.ReceiverDeployment", Long.valueOf(row['RCD_ID']))
        if (!receiverDeploymentRecord)
        {
            throw new BulkImportException("No bulk import record for receiver with RCD_ID = ${row['RCD_ID']}")
        }

        return receiverDeploymentRecord
    }

    def receiver(row) throws BulkImportException {
        Receiver receiver = ReceiverDeployment.get(receiverDeployment(row).dstPk)?.receiver
        if (!receiver)
        {
            throw new BulkImportException("No receiver corresponding to bulk import record with RCD_ID = ${row['RCD_ID']}")
        }

        return receiver
    }

    def timestamp(row) throws FileFormatException {
        def dateTimeColumnName = 'DET_DATETIME'
        try {
            return DATE_FORMAT.parse(row[dateTimeColumnName] + " UTC")
        }
        catch (NullPointerException npe) {
            throw new FileFormatException("Missing $dateTimeColumnName value", npe)
        }
        catch (ParseException pe) {
            throw new FileFormatException("Incorrect format for $dateTimeColumnName expected ${DATE_FORMAT.toPattern()}", pe)
        }
    }

}
