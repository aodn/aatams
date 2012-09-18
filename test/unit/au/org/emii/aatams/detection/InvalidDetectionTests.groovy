package au.org.emii.aatams.detection

import grails.test.*

import au.org.emii.aatams.*
import com.vividsolutions.jts.geom.*

class InvalidDetectionTests extends GrailsUnitTestCase 
{
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"
    
    protected void setUp()
    {
        super.setUp()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testInvalidDetection() 
    {
        mockDomain(InvalidDetection)

        Point location = new GeometryFactory().createPoint(new Coordinate(45.1234f, -40.1234f))
        Date timestamp = new Date().parse(DATE_FORMAT, "2009-12-08 06:44:24 UTC")
        
        def invalidDetection = 
            new InvalidDetection(
                             receiverDownload:new ReceiverDownloadFile(),
                             timestamp:timestamp,
                             receiverName:"VR3UWM-354",
                             transmitterId:"A69-1303-62347",
                             transmitterName:"shark tag",
                             transmitterSerialNumber:"1234",
                             stationName:"Neptune SW 1",
                             location:location)

        invalidDetection.save()
        assertTrue(invalidDetection.hasErrors())
        assertFalse(invalidDetection.validate())

        invalidDetection.message = "Duplicate"
        invalidDetection.reason = InvalidDetectionReason.DUPLICATE

        invalidDetection.save(failOnError: true)
        assertFalse(invalidDetection.hasErrors())
        assertTrue(invalidDetection.validate())
        assertEquals(InvalidDetectionReason.DUPLICATE, invalidDetection.reason)
        assertEquals("Duplicate", invalidDetection.message)
    }
}
