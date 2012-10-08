package au.org.emii.aatams.detection

import au.org.emii.aatams.ReceiverDownloadFileType
import grails.test.*

class DetectionFormatTests extends GrailsUnitTestCase 
{
    protected void setUp() 
	{
        super.setUp()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testDetectionsCSV() 
	{
		assertTrue(DetectionFormat.newFormat(ReceiverDownloadFileType.DETECTIONS_CSV) instanceof VueDetectionFormat)
    }
	
	void testCsiroDetectionsCSV()
	{
		assertTrue(DetectionFormat.newFormat(ReceiverDownloadFileType.CSIRO_DETECTIONS_CSV) instanceof CsiroDetectionFormat)
	}
	
	void testUnknownDetectionFormat()
	{
		try
		{
			DetectionFormat.newFormat("XYZ")
			fail()
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Unknown detection format: XYZ", e.message)
		}
	}
}
