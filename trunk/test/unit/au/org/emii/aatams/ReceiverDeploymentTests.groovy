package au.org.emii.aatams

import java.util.Date
import java.util.Set

import org.joda.time.DateTime

import au.org.emii.aatams.detection.ValidDetection

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.WKTReader

import grails.test.*

class ReceiverDeploymentTests extends GrailsUnitTestCase 
{
    protected void setUp() 
	{
        super.setUp()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testScheduledRecoveryDateBeforeDeploymentDate() 
	{
		WKTReader reader = new WKTReader()
		
		ReceiverDeployment deployment = 
			new ReceiverDeployment(station: new InstallationStation(),
								   receiver: new Receiver(),
								   deploymentNumber: 1,
								   deploymentDateTime:new DateTime(),
								   recoveryDate:new DateTime().minusHours(1).toDate(),
								   acousticReleaseID:"1234",
								   mooringType:new MooringType(),
								   mooringDescriptor:"concrete",
								   bottomDepthM:23.3f,
								   depthBelowSurfaceM:12.3f,
								   receiverOrientation:ReceiverOrientation.UP,
								   location:(Point)reader.read("POINT(30.1234 30.1234)"),
								   batteryLifeDays:60,
								   comments:"some comment")
			
		mockDomain(ReceiverDeployment, [deployment])
		deployment.save()
		
		assertTrue(deployment.hasErrors())
    }
}
