package au.org.emii.aatams

import org.joda.time.DateTime

import grails.test.*

class ReceiverRecoveryTests extends GrailsUnitTestCase {

    // https://github.com/aodn/aatams/issues/127
    void testRecoveryDateBeforeDeploymentDate() {
         assertErrorExists(true)
    }

    void testRecoveryDateAfterDeploymentDate() {
        assertErrorExists(false)
    }

    void assertErrorExists(recoveryDateBeforeDeployment) {
        def deploymentDateTime = new DateTime()

        mockDomain(ReceiverDeployment)
        mockDomain(ReceiverRecovery)

        def deployment = new ReceiverDeployment(
            deploymentDateTime: deploymentDateTime
        )

        def recovery = new ReceiverRecovery(
            deployment: deployment,
            recoveryDateTime: recoveryDateBeforeDeployment ? deploymentDateTime.minusDays(1) : deploymentDateTime.plusDays(1)
        )

        recovery.save()

        if (recoveryDateBeforeDeployment) {
            assertNotNull(recovery.errors.recoveryDateTime)
            assertTrue(recovery.errors.recoveryDateTime.contains('invalid.beforeDeploymentDateTime'))
        }
        else {
            assertNull(recovery.errors.recoveryDateTime)
        }
    }
}
