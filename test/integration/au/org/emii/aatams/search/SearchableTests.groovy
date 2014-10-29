package au.org.emii.aatams.search

import au.org.emii.aatams.*

import org.joda.time.*
import grails.test.*

import java.text.SimpleDateFormat

class SearchableTests extends GrailsUnitTestCase
{
    void testSearchOrganisationByName()
    {
        ['CSIRO', 'IMOS'].each {
            def org = Organisation.build(name: it)
            org.index()
        }

        def searchResult = Organisation.search("CSIRO")

        assertTrue(searchResult.results*.name.contains('CSIRO'))
        assertFalse(searchResult.results*.name.contains('IMOS'))
    }

    void testSearchProjects()
    {
        def sealCount = Project.build(name: 'Seal Count')
        sealCount.index()

        def searchResult = Project.search(sealCount.name)

        assertTrue(searchResult.results*.id.contains(sealCount.id))
    }

    void testSearchDeploymentsByReceiver()
    {
        def rx1 = TestUtils.buildReceiver('VR2W-101336')
        def rx2 = TestUtils.buildReceiver('VR2W-101337')

        def rx1Bondi = ReceiverDeployment.build(receiver: rx1)
        def rx2Bondi = ReceiverDeployment.build(receiver: rx2)

        [ rx1, rx2, rx1Bondi, rx2Bondi ].each { it.index() }

        def searchResult = ReceiverDeployment.search(rx1.name)

        assertTrue(searchResult.results*.id.contains(rx1Bondi.id))
        assertFalse(searchResult.results*.id.contains(rx2Bondi.id))
    }

    void testSearchDeploymentsByStations()
    {
        def bondiSW1 = InstallationStation.build(name: 'Bondi SW1')
        def bondiSW2 = InstallationStation.build(name: 'Bondi SW2')

        def rx1Bondi = ReceiverDeployment.build(station: bondiSW1)
        def rx2Bondi = ReceiverDeployment.build(station: bondiSW2)

        [ bondiSW1, bondiSW2, rx1Bondi, rx2Bondi ].each { it.index() }

        def searchResult = ReceiverDeployment.search(bondiSW1.name)

        assertTrue(searchResult.results*.id.contains(rx1Bondi.id))
        assertFalse(searchResult.results*.id.contains(rx2Bondi.id))
    }
}
