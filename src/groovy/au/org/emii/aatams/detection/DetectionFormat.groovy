package au.org.emii.aatams.detection

import au.org.emii.aatams.ReceiverDownloadFileType
import au.org.emii.aatams.bulk.BulkImportException;

/**
 * Format subclasses know how to parse particular detection file formats, e.g. Vue, CSIRO.
 * 
 * @author jburgess
 *
 */
abstract class DetectionFormat 
{
	abstract Map parseRow(row) throws BulkImportException
	
	static DetectionFormat newFormat(type)
	{
		if (type == ReceiverDownloadFileType.DETECTIONS_CSV)
		{
			return new VueDetectionFormat()
		}
		else if (type == ReceiverDownloadFileType.CSIRO_DETECTIONS_CSV)
		{
			return new CsiroDetectionFormat()
		}

		throw new IllegalArgumentException("Unknown detection format: " + type)
	}
}
