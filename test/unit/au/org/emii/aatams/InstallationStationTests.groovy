package au.org.emii.aatams

import grails.test.*

import org.joda.time.*

class InstallationStationTests extends GrailsUnitTestCase
{
    void testHasDeployment()
    {
        ReceiverRecovery recovery = new ReceiverRecovery(recoveryDateTime:tomorrow())
        mockDomain(ReceiverRecovery, [recovery])

        ReceiverDeployment deployment = new ReceiverDeployment(deploymentDateTime:yesterday(), recovery:recovery)
        mockDomain(ReceiverDeployment, [deployment])

        InstallationStation stationNoDeployment = new InstallationStation()
        InstallationStation stationWithDeployment = new InstallationStation()
        def stationList = [stationNoDeployment, stationWithDeployment]
        mockDomain(InstallationStation, stationList)
        stationWithDeployment.addToDeployments(deployment)
        stationList.each { it.save() }

        assertFalse(stationNoDeployment.isActive())
        assertTrue(stationWithDeployment.isActive())
    }

    DateTime yesterday()
    {
        return new DateTime().minusDays(1)
    }

    DateTime tomorrow()
    {
        return new DateTime().plusDays(1)
    }
}
