package au.org.emii.aatams.detection

import au.org.emii.aatams.*
import au.org.emii.aatams.util.SqlUtils

import de.micromata.opengis.kml.v_2_2_0.Kml

class ValidDetection extends RawDetection implements Embargoable
{
    static belongsTo = [receiverDownload:ReceiverDownloadFile, receiverDeployment: ReceiverDeployment]
    static transients = RawDetection.transients +
        ['project', 'sensorIds', 'speciesNames', 'surgeries', 'placemark', 'release', 'mostRecentSurgery', 'mostRecentRelease', 'sanitised']

    /**
     * All new detections are marked provisional being upload processing is completed.
     * At the end of upload processing, the count of provisional detections is used to update the Statistics table.
     * After this, they are no longer considered provisional.
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
        return "${timestamp.toString()}, ${String.valueOf(receiverDeployment?.receiver)}, ${transmitterId}"
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

    static String getSensorIds(theSurgeries)
    {
        return theSurgeries*.tag.sensors*.transmitterId.flatten().join(", ")
    }

    String getSpeciesNames()
    {
        return getSpeciesNames(surgeries)
    }

    static String getSpeciesNames(theSurgeries)
    {
        return theSurgeries*.release.animal.species.name.flatten().join(", ")
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

    def applyEmbargo(allowSanitised = true) {

        def anyReleaseEmbargoed = false

        // Return a temporary detection, with embargoed surgeries removed.
        def censoredDetection = new HashMap(this.properties)
        censoredDetection.surgeries = new HashSet<Surgery>()

        surgeries.each {

            if (it.release.isEmbargoed()) {
                anyReleaseEmbargoed = true
            }
            else {
                censoredDetection.surgeries.add(it)
            }
        }

        censoredDetection.sensorIds = getSensorIds(censoredDetection.surgeries)
        censoredDetection.speciesNames = getSpeciesNames(censoredDetection.surgeries)
        censoredDetection.isSanitised = { -> true }

        def protectionRequired = project.isProtected && anyReleaseEmbargoed

        def hideFromResults = anyReleaseEmbargoed && !allowSanitised

        if (protectionRequired || hideFromResults) {
            return null
        }

        return censoredDetection
    }

    def isSanitised() {
        !sensorIds && !speciesNames
    }

    static Kml toKml(List<ValidDetection> detections, serverURL)
    {
        return new SensorTrackKml(detections, serverURL)
    }

    static def toPresentationFormat(detectionRow) {

        def validDetection = ValidDetection.get(detectionRow.detection_id)

        def dto = [:]

        [ 'id', 'project', 'timestamp', 'receiverName', 'receiverDeployment', 'transmitterId', 'transmitterName',
          'transmitterSerialNumber', 'stationName', 'receiverDownload'
        ].each {
            dto[it] = validDetection[it]
        }

        dto.speciesNames = detectionRow.species_name
        dto.sensorIds = detectionRow.sensor_id

        return dto
    }
}
