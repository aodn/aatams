package au.org.emii.aatams

import java.text.ParseException
import java.text.SimpleDateFormat

import au.org.emii.aatams.ReceiverDownloadFileType
import au.org.emii.aatams.bulk.BulkImportException
import au.org.emii.aatams.bulk.FileFormatException
import au.org.emii.aatams.detection.CsiroDetectionFormat;
import au.org.emii.aatams.detection.VueDetectionFormat;
import au.org.emii.aatams.event.EventFormat

/**
 * Format subclasses know how to parse particular file formats, e.g. Vue Detections, CSIRO detections, Events.
 * 
 * @author jburgess
 *
 */
abstract class FileFormat
{
    abstract Map parseRow(row) throws FileFormatException
    
    static FileFormat newFormat(type)
    {
        if (type == ReceiverDownloadFileType.DETECTIONS_CSV)
        {
            return new VueDetectionFormat()
        }
        else if (type == ReceiverDownloadFileType.CSIRO_DETECTIONS_CSV)
        {
            return new CsiroDetectionFormat()
        }
        else if (type == ReceiverDownloadFileType.EVENTS_CSV)
        {
            return new EventFormat()
        }

        throw new IllegalArgumentException("Unknown detection format: " + type)
    }

    protected def getUtcDate(row, timestampColumn, dateFormat)
    {
        try
        {
            return new SimpleDateFormat(dateFormat).parse(row[timestampColumn] + " " + "UTC")
        }
        catch (NullPointerException npe) {
            throw new FileFormatException("Missing $timestampColumn value", npe)
        }
        catch (ParseException pe) {
            throw new FileFormatException("Incorrect format for $timestampColumn expected ${dateFormat}", pe)
        }
    }
}
