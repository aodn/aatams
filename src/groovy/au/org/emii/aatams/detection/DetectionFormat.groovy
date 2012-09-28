package au.org.emii.aatams.detection

import au.org.emii.aatams.ReceiverDownloadFileType

/**
 * Format subclasses know how to parse particular detection file formats, e.g. Vue, CSIRO.
 * 
 * @author jburgess
 *
 */
abstract class DetectionFormat 
{
	abstract Map parseRow(row)
	
	static DetectionFormat newFormat(type)
	{
		if (type == ReceiverDownloadFileType.DETECTIONS_CSV)
		{
			return new VueDetectionFormat()
		}
		
		assert(false): "Unknown detection format: " + type
	}
}
