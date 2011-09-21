package au.org.emii.aatams

import grails.test.*

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.ParseException
import com.vividsolutions.jts.io.WKTReader

class InstallationStationTests extends GrailsUnitTestCase 
{
    protected void setUp() 
    {
        super.setUp()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testUpdateSRID() 
    {
        //2011-09-21 20:30:49,251 [http-8080-10] DEBUG grails.app.controller.au.org.emii.aatams.InstallationStationController - params: [id:49, location_lat:30.1234, project.id:11, project:[id:11], curtainPosition:1, location:POINT(30.1234 30.1234) , 28348, location_srid:28348, pointInputTextField:30.1234¬∞N 30.1234¬∞ E (datum:GDA94/ MGA zone 48), name:Bondi SW1, _action_update:Update, location_lon:30.1234, installation.id:47, installation:[id:47], version:0, action:index, controller:installationStation]

//POINT(30.1234 30.1234) , 28348
//            installationStationInstance.properties = params

        WKTReader reader = new WKTReader();
        Point location = (Point)reader.read("POINT(10.1234 10.1234)")
        location.setSRID(4326)
        InstallationStation station = new InstallationStation(name:"Bondi SW1", location:location, installation:new Installation())
        mockDomain(InstallationStation, [station])
        station.save()
        
        InstallationStation savedStation = InstallationStation.findByName("Bondi SW1")
        assertNotNull(savedStation)
        assertEquals(location.x, savedStation.location.x)
        assertEquals(location.y, savedStation.location.y)
        assertEquals(location.SRID, savedStation.location.SRID)
        
        def params = 
            [id:49, 
             location_lat:50.1234, 
             "project.id":11, 
             project:[id:11], 
             curtainPosition:1, 
//             location:"POINT(30.1234 30.1234) , 28348", 
             location:"POINT(30.1234 50.1234) , 28348", 
             location_srid:28348, 
             pointInputTextField:"30.1234¬∞N 30.1234¬∞ E (datum:GDA94/ MGA zone 48)", 
             name:"Bondi SW1", 
             _action_update:"Update", 
             location_lon:30.1234, 
             "installation.id":47, 
             installation:[id:47], 
             version:0, 
             action:"index", 
             controller:"installationStation"]
        
        savedStation.properties = params
        savedStation.save()
        

        savedStation = InstallationStation.findByName("Bondi SW1")
        assertNotNull(savedStation)
//        assertEquals(location.x, savedStation.location.x)
        assertEquals(location.y, savedStation.location.y)
        assertEquals(28348, savedStation.location.SRID)
        
    }
}
