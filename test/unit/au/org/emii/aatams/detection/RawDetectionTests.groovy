package au.org.emii.aatams.detection

import au.org.emii.aatams.*
import grails.test.*

import com.vividsolutions.jts.geom.*

class RawDetectionTests extends GrailsUnitTestCase 
{
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"
    
    protected void setUp()
    {
        super.setUp()
        
        mockDomain(RawDetection)
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testValid()
    {
        Point location = new GeometryFactory().createPoint(new Coordinate(45.1234f, -40.1234f))
        Date timestamp = new Date().parse(DATE_FORMAT, "2009-12-08 06:44:24 UTC")
        
        def rawDetection = 
            new RawDetection(receiverDownload:new ReceiverDownloadFile(),
                             timestamp:timestamp,
                             receiverName:"VR3UWM-354",
                             transmitterId:"A69-1303-62347",
                             transmitterName:"shark tag",
                             transmitterSerialNumber:"1234",
                             stationName:"Neptune SW 1",
                             location:location)
      
        rawDetection.save()
        assertFalse(rawDetection.hasErrors())
        assertTrue(rawDetection.validate())
    }

    void testValidSensor()
    {
        Point location = new GeometryFactory().createPoint(new Coordinate(45.1234f, -40.1234f))
        Date timestamp = new Date().parse(DATE_FORMAT, "2009-12-08 06:44:24 UTC")

        def rawDetection = 
            new RawDetection(receiverDownload:new ReceiverDownloadFile(),
                             timestamp:timestamp,
                                     receiverName:"VR3UWM-354",
                                     transmitterId:"A69-1303-62347",
                                     transmitterName:"shark tag",
                                     transmitterSerialNumber:"1234",
                                     sensorValue:123,
                                     sensorUnit:"ADC",
                                     stationName:"Neptune SW 1",
                                     location:location)

        rawDetection.save()
        assertFalse(rawDetection.hasErrors())
        assertTrue(rawDetection.validate())
    }

    void testMissingTimestamp()
    {
        Point location = new GeometryFactory().createPoint(new Coordinate(45.1234f, -40.1234f))
        Date timestamp = new Date().parse(DATE_FORMAT, "2009-12-08 06:44:24 UTC")

        def rawDetection = 
                new RawDetection(receiverDownload:new ReceiverDownloadFile(),
                             receiverName:"VR3UWM-354",
                                    transmitterId:"A69-1303-62347",
                                    transmitterName:"shark tag",
                                    transmitterSerialNumber:"1234",
                                    stationName:"Neptune SW 1",
                                    location:location)
                                
        rawDetection.save()
        assertTrue(rawDetection.hasErrors())
        assertFalse(rawDetection.validate())
    }

    void testMissingReceiverName()
    {
        Point location = new GeometryFactory().createPoint(new Coordinate(45.1234f, -40.1234f))
        Date timestamp = new Date().parse(DATE_FORMAT, "2009-12-08 06:44:24 UTC")

        def rawDetection = 
            new RawDetection(receiverDownload:new ReceiverDownloadFile(),
                             timestamp:timestamp,
                             transmitterId:"A69-1303-62347",
                             transmitterName:"shark tag",
                             transmitterSerialNumber:"1234",
                             sensorValue:123,
                             sensorUnit:"ADC",
                             stationName:"Neptune SW 1",
                             location:location)

        rawDetection.save()
        assertTrue(rawDetection.hasErrors())
        assertFalse(rawDetection.validate())
    }

    void testMissingTransmitterId()
    {
        Point location = new GeometryFactory().createPoint(new Coordinate(45.1234f, -40.1234f))
        Date timestamp = new Date().parse(DATE_FORMAT, "2009-12-08 06:44:24 UTC")

        def rawDetection = 
            new RawDetection(receiverDownload:new ReceiverDownloadFile(),
                             timestamp:timestamp,
                             receiverName:"VR3UWM-354",
                             transmitterName:"shark tag",
                             transmitterSerialNumber:"1234",
                             sensorValue:123,
                             sensorUnit:"ADC",
                             stationName:"Neptune SW 1",
                             location:location)

        rawDetection.save()
        assertTrue(rawDetection.hasErrors())
        assertFalse(rawDetection.validate())
    }

    void testMissingTransmitterName()
    {
        Point location = new GeometryFactory().createPoint(new Coordinate(45.1234f, -40.1234f))
        Date timestamp = new Date().parse(DATE_FORMAT, "2009-12-08 06:44:24 UTC")

        def rawDetection = 
            new RawDetection(receiverDownload:new ReceiverDownloadFile(),
                             timestamp:timestamp,
                             receiverName:"VR3UWM-354",
                             transmitterId:"A69-1303-62347",
                             transmitterSerialNumber:"1234",
                             sensorValue:123,
                             sensorUnit:"ADC",
                             stationName:"Neptune SW 1",
                             location:location)

        rawDetection.save()
        assertFalse(rawDetection.hasErrors())
        assertTrue(rawDetection.validate())
    }

    void testMissingTransmitterSerialNumber()
    {
        Point location = new GeometryFactory().createPoint(new Coordinate(45.1234f, -40.1234f))
        Date timestamp = new Date().parse(DATE_FORMAT, "2009-12-08 06:44:24 UTC")

        def rawDetection = 
            new RawDetection(receiverDownload:new ReceiverDownloadFile(),
                             timestamp:timestamp,
                             receiverName:"VR3UWM-354",
                             transmitterId:"A69-1303-62347",
                             transmitterName:"shark tag",
                             sensorValue:123,
                             sensorUnit:"ADC",
                             stationName:"Neptune SW 1",
                             location:location)

        rawDetection.save()
        assertFalse(rawDetection.hasErrors())
        assertTrue(rawDetection.validate())
    }

    void testMissingStationName()
    {
        Point location = new GeometryFactory().createPoint(new Coordinate(45.1234f, -40.1234f))
        Date timestamp = new Date().parse(DATE_FORMAT, "2009-12-08 06:44:24 UTC")

        def rawDetection = 
            new RawDetection(receiverDownload:new ReceiverDownloadFile(),
                             timestamp:timestamp,
                             receiverName:"VR3UWM-354",
                             transmitterId:"A69-1303-62347",
                             transmitterName:"shark tag",
                             transmitterSerialNumber:"1234",
                             sensorValue:123,
                             sensorUnit:"ADC",
                             location:location)

        rawDetection.save()
        assertFalse(rawDetection.hasErrors())
        assertTrue(rawDetection.validate())
    }

    void testMissingLocation()
    {
        Point location = new GeometryFactory().createPoint(new Coordinate(45.1234f, -40.1234f))
        Date timestamp = new Date().parse(DATE_FORMAT, "2009-12-08 06:44:24 UTC")

        def rawDetection = 
            new RawDetection(receiverDownload:new ReceiverDownloadFile(),
                             timestamp:timestamp,
                             receiverName:"VR3UWM-354",
                             transmitterId:"A69-1303-62347",
                             transmitterName:"shark tag",
                             transmitterSerialNumber:"1234",
                             sensorValue:123,
                             sensorUnit:"ADC",
                             stationName:"Neptune SW 1")

        rawDetection.save()
        assertFalse(rawDetection.hasErrors())
        assertTrue(rawDetection.validate())
    }
}
