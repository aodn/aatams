package au.org.emii.aatams

import org.joda.time.DateTime

import grails.test.*

class ReceiverRecoveryTests extends GrailsUnitTestCase {

    void setUp() {
        super.setUp()

        mockDomain(ReceiverDeployment)
        mockDomain(ReceiverRecovery)
    }

    // https://github.com/aodn/aatams/issues/127
    void testRecoveryDateBeforeDeploymentDate() {
        def recovery = newRecoveryWithDeployment(recoveryDateIsBeforeDeploymentDate: true)

        assertNotNull(recovery.errors.recoveryDateTime)
        assertTrue(recovery.errors.recoveryDateTime.contains('invalid.beforeDeploymentDateTime'))
    }

    void testRecoveryDateAfterDeploymentDate() {
        def recovery = newRecoveryWithDeployment(recoveryDateIsBeforeDeploymentDate: false)

        assertNull(recovery.errors.recoveryDateTime)
    }

    def newRecoveryWithDeployment(params) {
        def deploymentDateTime = new DateTime()
        def recoveryDateTime =
            params.recoveryDateIsBeforeDeploymentDate ? deploymentDateTime.minusDays(1) : deploymentDateTime.plusDays(1)

        def deployment = new ReceiverDeployment(
            deploymentDateTime: deploymentDateTime
        )

        def recovery = new ReceiverRecovery(
            deployment: deployment,
            recoveryDateTime: recoveryDateTime
        )

        recovery.save()

        return recovery
    }
}
