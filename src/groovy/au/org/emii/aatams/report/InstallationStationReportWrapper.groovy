package au.org.emii.aatams.report

import au.org.emii.aatams.InstallationStation
import au.org.emii.aatams.Sensor
import au.org.emii.aatams.Species
import au.org.emii.aatams.Tag

import com.vividsolutions.jts.geom.*

/*
 * Wrapper bean for InstallationStation for use as a data source element in
 * Jasper Reports.
 *
 * The main purpose is to expose indirect properties (e.g. properties of owning
 * installation, project etc) as bean methods.
 *
 * TODO: can we add the methods dynamically?
 * TODO: can groovy classes act as data source beans?
 *
 * @author jburgess
 */
class InstallationStationReportWrapper  {
    /**
     * The wrapped domain object.
     */
    private final station

    public static final String NULL_CURTAIN_POSITION = "-"

    public InstallationStationReportWrapper(InstallationStation station) {
        this.station = station
    }

    public String getInstallationName() {
        return station.installation.name
    }

    public String getInstallationConfigurationType() {
        return station.installation.configuration.type
    }

    public String getProjectName() {
        return station.installation.project.name
    }

    public String getStationName() {
        return station.name
    }

    public String getStationCurtainPosition() {
        Integer curtainPos = station.curtainPosition

        if (curtainPos == null) {
            return NULL_CURTAIN_POSITION
        }

        return String.valueOf(curtainPos)
    }

    public double getStationLatitude() {
        return station.location.coordinate.y
    }

    public double getStationLongitude() {
        return station.location.coordinate.x
    }

    /**
     * This is here just to keep iReport happy.
     */
    public static Collection<InstallationStationReportWrapper> list() {
        return Collections.EMPTY_LIST
    }

    boolean hasDetections() {
        return (detectionCount() != 0)
    }

    long getDetectionCount() {
        return 73
    }
}

