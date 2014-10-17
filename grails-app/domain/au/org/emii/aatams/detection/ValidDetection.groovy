package au.org.emii.aatams.detection

import au.org.emii.aatams.*
import au.org.emii.aatams.util.StringUtils
import au.org.emii.aatams.util.SqlUtils

import de.micromata.opengis.kml.v_2_2_0.Kml

class ValidDetection extends RawDetection implements Embargoable
{
    static belongsTo = [receiverDownload:ReceiverDownloadFile, receiverDeployment: ReceiverDeployment]
    static transients = RawDetection.transients +
        ['project', 'sensorIds', 'speciesNames', 'surgeries', 'placemark', 'release', 'mostRecentSurgery', 'mostRecentRelease']

    /**
     * This is a part of an optimisation for #2239.  All new detections are marked provisional.  Subsequently,
     * the set of provisional detections are used to update the 'detection_extract_view_mv' materialized view,
     * the 'detection_count_per_station_mv' materialized view and also the Statistics table.  After this, they
     * are no longer considered provisional.
     */
    boolean provisional = true

    static constraints = RawDetection.constraints

    static boolean isDuplicate(other)
    {
        boolean duplicate = false
        ValidDetection.findAllByTimestamp(other.timestamp).each
        {
            if (it.duplicate(other))
            {
                duplicate = true
                return
            }
            return false
        }

        return duplicate
    }

    private boolean duplicateProperty(property, other)
    {
        // null property is considered equal to empty string.
        (this[property] == other[property]) ||
        ((this[property] == null) && (other[property] == "")) ||
        ((this[property] == "") && (other[property] == null))
    }

    private boolean duplicate(other)
    {
        return (
            this.timestamp == other.timestamp
            && duplicateProperty("receiverName", other)
            && duplicateProperty("transmitterId", other)
            && duplicateProperty("transmitterName", other)
            && duplicateProperty("transmitterSerialNumber", other)
            && duplicateProperty("sensorUnit", other)
            && this.location == other.location
            && this.sensorValue == other.sensorValue)
    }

    String toString()
    {
        return timestamp.toString() + " " + String.valueOf(receiverDeployment?.receiver)
    }

    // Convenience method.
    Project getProject()
    {
        if (this.surgeries.isEmpty())
        {
            return null
        }

        return this.surgeries.last().project
    }

    def getSurgeries()
    {
        def sensors = Sensor.findAllByTransmitterId(this.transmitterId)
        if (!sensors)
        {
            return []
        }

        def surgeries = Surgery.findAllByTagInList(sensors*.tag, [sort: "timestamp"])

        return surgeries.grep {
            it.isInWindow(this.timestamp)
        }
    }

    def getMostRecentSurgery()
    {
        def theSurgeries = this.getSurgeries()
        return theSurgeries.isEmpty() ? null : theSurgeries.last()
    }

    def getReleases()
    {
        return this.getSurgeries()*.release
    }

    def getMostRecentRelease()
    {
        return this.getMostRecentSurgery()?.release
    }

    String getSensorIds()
    {
        return getSensorIds(surgeries)
    }

    private String getSensorIds(theSurgeries)
    {
        return StringUtils.removeSurroundingBrackets(theSurgeries*.tag.sensors*.transmitterId)
    }

    String getSpeciesNames()
    {
        return getSpeciesNames(surgeries)
    }

    private String getSpeciesNames(theSurgeries)
    {
        return StringUtils.removeSurroundingBrackets(theSurgeries*.release.animal.species.name)
    }

    static String toSqlInsert(detection)
    {
        StringBuilder detectionBuff = new StringBuilder(
            "INSERT INTO VALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, " +
            "STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, RECEIVER_DEPLOYMENT_ID, PROVISIONAL) " +
            " VALUES(")

        detectionBuff.append("nextval('hibernate_sequence'),")
        detectionBuff.append("0,")
        detectionBuff.append("'${formatTimestamp(detection['timestamp'], 'yyyy-MM-dd HH:mm:ssZ')}',")
        SqlUtils.appendIntegerParams(detectionBuff, detection, ["receiverDownloadId"])
        SqlUtils.appendStringParams(detectionBuff, detection, ["receiverName", "sensorUnit", "sensorValue", "stationName", "transmitterId", "transmitterName",
                                                               "transmitterSerialNumber"])
        SqlUtils.appendIntegerParams(detectionBuff, detection, ["receiverDeploymentId"])
        SqlUtils.appendBooleanParams(detectionBuff, detection, ["provisional"])
        SqlUtils.removeTrailingCommaAndAddBracket(detectionBuff)

        return detectionBuff.toString()
    }

    def applyEmbargo() {

        def anyReleaseEmbargoed = false

        // Return a temporary detection, with embargoed surgeries removed.
        def censoredDetection = new HashMap(this.properties)
        censoredDetection.surgeries = new HashSet<Surgery>()

        surgeries.each {

            if (it.surgery.release.isEmbargoed()) {
                anyReleaseEmbargoed = true
            }
            else {
                censoredDetection.detectionSurgeries.add(it)
            }
        }
        censoredDetection.metaClass.getSensorIds = { -> getSensorIds(censoredDetection.detectionSurgeries) }
        censoredDetection.metaClass.getSpeciesNames = { -> getSpeciesNames(censoredDetection.detectionSurgeries) }

        if (project.isProtected && anyReleaseEmbargoed) {
            return null
        }

        return censoredDetection
    }

    static Kml toKml(List<ValidDetection> detections, serverURL)
    {
        return new SensorTrackKml(detections, serverURL)
    }
}
