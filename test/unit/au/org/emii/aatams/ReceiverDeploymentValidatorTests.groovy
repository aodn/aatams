package au.org.emii.aatams

import org.joda.time.DateTime
import org.joda.time.Interval

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.WKTReader

import grails.test.*


class ReceiverDeploymentValidatorTests extends GrailsUnitTestCase {
    static def now = new DateTime()

    void setUp() {
        super.setUp()

        JodaOverrides.apply()
        mockDomain(ReceiverDeployment)
        mockDomain(ReceiverRecovery)
        mockDomain(Receiver)
    }

    void tearDown() {
        JodaOverrides.unmock()
        super.tearDown()
    }

    /*
        Xavier says,
        GUI logic should be,
          Within each record (row) --> initialisation_date < deployment_date < recovery_date
          Across records (row)     --> deployment_date (t+1) > recovery_date (t)
    */

    void testSingleDeploymentTimeValidation() {

        // init < deployment -> valid
        assertValidDeployment(now, now.plusDays(1), null, DeviceStatus.RECOVERED)

        // init == deployment -> valid
        assertValidDeployment(now, now, null, DeviceStatus.RECOVERED)

        // init > deployment -> invalid
        assertInvalidDeployment(now.plusDays(1), now, null, DeviceStatus.RECOVERED)

        // deployment date == initialisation time, deployment date < recovery_date -> valid
        assertValidDeployment(now, now, now.plusDays(1), DeviceStatus.RECOVERED)

        // deployment date == initialisation time, deployment date == recovery_date -> valid
        assertValidDeployment(now, now, now, DeviceStatus.RECOVERED)

        // deployment date == initialisation time, deployment date > recovery date -> invalid
        assertInvalidDeployment(now, now, now.minusDays(1), DeviceStatus.RECOVERED)

        // deployment date == initialisation time, deployment date > recovery date -> invalid
        assertInvalidDeployment(now, now, now.minusDays(1), DeviceStatus.RECOVERED)
    }

    void testMultipleDeploymentTimeValidation1() {

        // test unassociated deployments are not otherwise invalid when validated independently
        def deployment1 = createDeploymentWithReceiver(new Receiver(), 1, now.minusDays(2), now.minusDays(2), now, DeviceStatus.RECOVERED)
        assertTrue( deployment1.validate())

        def deployment2 = createDeploymentWithReceiver(new Receiver(), 2, now.minusDays(2), now.minusDays(1), now, DeviceStatus.RECOVERED)
        assertTrue(deployment2.validate())
    }

    void testMultipleDeploymentTimeValidation2() {

        // deployment_date (t+1) => recovery_date (t) -> valid
        def receiver = new Receiver()
        def deployment1 = createDeploymentWithReceiver(receiver, 1, now.minusDays(2), now.minusDays(2), now, DeviceStatus.RECOVERED)
        def deployment2 = createDeploymentWithReceiver(receiver, 2, now.minusDays(2), now,              now, DeviceStatus.RECOVERED)

        receiver.addToDeployments(deployment1) 
        receiver.addToDeployments(deployment2) 

        assertEquals(receiver.deployments.size(), 2)

        assertTrue(deployment1.validate())
    }

    void testMultipleDeploymentTimeValidation3() {

        // deployment_date (t+1) < recovery_date (t) -> invalid
        def receiver = new Receiver()
        def deployment1 = createDeploymentWithReceiver(receiver, 1, now.minusDays(2), now.minusDays(2), now, DeviceStatus.RECOVERED)
        def deployment2 = createDeploymentWithReceiver(receiver, 2, now.minusDays(2), now.minusDays(1), now, DeviceStatus.RECOVERED)

        receiver.addToDeployments(deployment1) 
        receiver.addToDeployments(deployment2) 

        assertFalse(deployment1.validate())
    }

    def createDeployment(initialisationDateTime, deploymentDateTime, recoveryDateTime, recoveryStatus) {

        createDeploymentWithReceiver(new Receiver(), 1, initialisationDateTime, deploymentDateTime, recoveryDateTime, recoveryStatus)
    }

    def createDeploymentWithReceiver(receiver, id, initialisationDateTime, deploymentDateTime, recoveryDateTime, recoveryStatus) {

        def deployment = new ReceiverDeployment(
            id: id,
            receiver: receiver,
            station: new InstallationStation(),
            mooringType: new MooringType(),
            initialisationDateTime: initialisationDateTime,
            deploymentDateTime: deploymentDateTime
        )

        if(recoveryDateTime) {

            def recovery = new ReceiverRecovery(
                deployment: deployment,
                recoveryDateTime: recoveryDateTime,
                status:  recoveryStatus,
                location: new WKTReader().read('POINT(1 2)'),
                recoverer: new ProjectRole()
            )
            deployment.recovery = recovery
        }

        deployment
    }

    void assertValidDeployment(initialisationDateTime, deploymentDateTime, recoveryDateTime, recoveryStatus) {

        def deployment = createDeployment(initialisationDateTime, deploymentDateTime, recoveryDateTime, recoveryStatus)
        assertTrue(deployment.validate())
    }

   void assertInvalidDeployment(initialisationDateTime, deploymentDateTime, recoveryDateTime, recoveryStatus) {

        def deployment = createDeployment(initialisationDateTime, deploymentDateTime, recoveryDateTime, recoveryStatus)
        assertFalse(deployment.validate())
    }
}
