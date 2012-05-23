package au.org.emii.aatams.detection

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat;

import au.org.emii.aatams.*
import grails.test.*
import com.vividsolutions.jts.geom.*

import de.micromata.opengis.kml.v_2_2_0.Document
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.TimeStamp;
import de.micromata.opengis.kml.v_2_2_0.gx.Track;

class ValidDetectionTests extends GrailsUnitTestCase 
{
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"
    
    protected void setUp() 
    {
        super.setUp()
        
        mockDomain(ValidDetection)
    }

    void testIsDuplicateTrue() 
    {
        Point location = new GeometryFactory().createPoint(new Coordinate(45.1234f, -40.1234f))
        Date timestamp = new Date().parse(DATE_FORMAT, "2009-12-08 06:44:24 UTC")
        def params = 
                [receiverDownload:new ReceiverDownloadFile(),
                 timestamp:timestamp,
                 receiverName:"VR3UWM-354",
                 transmitterId:"A69-1303-62347",
                 transmitterName:"shark tag",
                 transmitterSerialNumber:"1234",
                 sensorValue:123,
                 sensorUnit:"ADC",
                 stationName:"Neptune SW 1",
                 location:location,
                 receiverDeployment:new ReceiverDeployment()]
             
        ValidDetection validDet = new ValidDetection(params)
        validDet.save()
        assertFalse(validDet.hasErrors())
        assertTrue(validDet.validate())
        
        assertTrue(ValidDetection.isDuplicate(params))
        assertTrue(validDet.isDuplicate(params))
        
        params.receiverName = "XYZ"
        assertFalse(ValidDetection.isDuplicate(params))
    }
}
