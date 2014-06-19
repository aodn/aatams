package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractGrailsUnitTestCase
import grails.test.*
import org.joda.time.*

class TimeZoneTests extends AbstractGrailsUnitTestCase
{
    def specifiedTZ = DateTimeZone.forID("Australia/Adelaide")
    def userTZ = DateTimeZone.forID("Australia/Perth")

    def person

    protected void setUp()
    {
        super.setUp()

        permitted = true

        person = new Person(
            username:"person",
            organisation:new Organisation(),
            defaultTimeZone:userTZ)

        mockDomain(Person, [person])
        person.save()

    }

    protected void tearDown()
    {
        super.tearDown()
    }

    protected def getPrincipal()
    {
        return person.id
    }

    void testAnimalRelease()
    {
        AnimalRelease releaseNoCaptureOrReleaseDateTime = new AnimalRelease()
        AnimalRelease releaseCaptureDateTime = new AnimalRelease(captureDateTime:new DateTime(specifiedTZ))
        AnimalRelease releaseReleaseDateTime = new AnimalRelease(releaseDateTime:new DateTime(specifiedTZ))

        def releaseList = [releaseNoCaptureOrReleaseDateTime, releaseCaptureDateTime, releaseReleaseDateTime]
        mockDomain(AnimalRelease, releaseList)
        releaseList.each { it.save() }

//        assertEquals(userTZ, releaseNoCaptureOrReleaseDateTime.captureDateTime.zone)
//        assertEquals(userTZ, releaseNoCaptureOrReleaseDateTime.releaseDateTime.zone)
//
//        assertEquals(specifiedTZ, releaseCaptureDateTime.captureDateTime.zone)
//        assertEquals(userTZ, releaseCaptureDateTime.releaseDateTime.zone)
//
//        assertEquals(userTZ, releaseReleaseDateTime.captureDateTime.zone)
//        assertEquals(specifiedTZ, releaseReleaseDateTime.releaseDateTime.zone)
    }

    void testReceiverDeployment()
    {
        ReceiverDeployment deploymentNotSpec = new ReceiverDeployment()
        ReceiverDeployment deploymentSpec = new ReceiverDeployment(deploymentDateTime:new DateTime(specifiedTZ))

        def deploymentList = [deploymentNotSpec, deploymentSpec]
        mockDomain(ReceiverDeployment, deploymentList)
        deploymentList.each { it.save() }

//        assertEquals(specifiedTZ, deploymentSpec.deploymentDateTime.zone)
//        assertEquals(userTZ, deploymentNotSpec.deploymentDateTime.zone)
    }

    void testReceiverRecovery()
    {
        ReceiverRecovery recoveryNotSpec = new ReceiverRecovery()
        ReceiverRecovery recoverySpec = new ReceiverRecovery(recoveryDateTime:new DateTime(specifiedTZ))

        def recoveryList = [recoveryNotSpec, recoverySpec]
        mockDomain(ReceiverRecovery, recoveryList)
        recoveryList.each { it.save() }

//        assertEquals(specifiedTZ, recoverySpec.recoveryDateTime.zone)
//        assertEquals(userTZ, recoveryNotSpec.recoveryDateTime.zone)
    }

    void testSurgery()
    {
        Surgery surgeryNotSpec = new Surgery()
        Surgery surgerySpec = new Surgery(timestamp:new DateTime(specifiedTZ))

        def surgeryList = [surgeryNotSpec, surgerySpec]
        mockDomain(Surgery, surgeryList)
        surgeryList.each { it.save() }

//        assertEquals(specifiedTZ, surgerySpec.timestamp.zone)
//        assertEquals(userTZ, surgeryNotSpec.timestamp.zone)
    }
}
