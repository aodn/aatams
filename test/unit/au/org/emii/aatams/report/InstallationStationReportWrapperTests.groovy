package au.org.emii.aatams.report

import grails.test.*

import au.org.emii.aatams.*

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.Point

class InstallationStationReportWrapperTests extends GrailsUnitTestCase  {
    InstallationStation station

    def projectName = "The project"
    def installationName = "Bondi Line"
    def installationConfigType = "CURTAIN"

    def stationName = "NW1"
    def stationCurtainPosition = 3
    def stationLat = 42.45f
    def stationLon = 34.33f

    protected void setUp()  {
        super.setUp()

        def project = new Project(name:projectName)
        def installationConfig = new InstallationConfiguration(type:installationConfigType)
        def installation =
            new Installation(name:installationName,
                             configuration:installationConfig,
                             project:project)

        def stationLocation = new GeometryFactory().createPoint(new Coordinate(stationLon, stationLat))
        station =
            new InstallationStation(name:stationName,
                                    curtainPosition:stationCurtainPosition,
                                    location:stationLocation,
                                    installation:installation)
    }

    protected void tearDown()  {
        super.tearDown()
    }

    void testWrapper() {
        def wrapper = new InstallationStationReportWrapper(station)

        assertEquals(installationName, wrapper.installationName)
        assertEquals(installationConfigType, wrapper.installationConfigurationType)
        assertEquals(projectName, wrapper.projectName)
        assertEquals(stationName, wrapper.stationName)
        assertEquals(String.valueOf(stationCurtainPosition), wrapper.stationCurtainPosition)
        assertEquals(stationLat, wrapper.stationLatitude)
        assertEquals(stationLon, wrapper.stationLongitude)
    }

    void testWrapperNullCurtainPos() {
        station.curtainPosition = null
        def wrapper = new InstallationStationReportWrapper(station)

        assertEquals(InstallationStationReportWrapper.NULL_CURTAIN_POSITION, wrapper.stationCurtainPosition)
    }
}
