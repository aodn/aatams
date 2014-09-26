package au.org.emii.aatams.detection

import java.util.Map;

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

import au.org.emii.aatams.*
import au.org.emii.aatams.util.StringUtils
import au.org.emii.aatams.util.SqlUtils

import com.vividsolutions.jts.geom.Point

import de.micromata.opengis.kml.v_2_2_0.Feature
import de.micromata.opengis.kml.v_2_2_0.Kml
import de.micromata.opengis.kml.v_2_2_0.Placemark
import de.micromata.opengis.kml.v_2_2_0.TimeStamp

class ValidDetection extends RawDetection implements Embargoable
{
    static belongsTo = [receiverDownload:ReceiverDownloadFile, receiverDeployment: ReceiverDeployment]
    static transients = RawDetection.transients +
        ['project', 'sensorIds', 'speciesNames', 'surgeries', 'placemark', 'release', 'mostRecentSurgery', 'mostRecentRelease']

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
            "STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, RECEIVER_DEPLOYMENT_ID) " +
            " VALUES(")

        detectionBuff.append("nextval('hibernate_sequence'),")
        detectionBuff.append("0,")
        detectionBuff.append("'" + new java.sql.Timestamp(detection["timestamp"].getTime()) + "',")
        SqlUtils.appendIntegerParams(detectionBuff, detection, ["receiverDownloadId"])
        SqlUtils.appendStringParams(detectionBuff, detection, ["receiverName", "sensorUnit", "sensorValue", "stationName", "transmitterId", "transmitterName",
                                                               "transmitterSerialNumber"])
        SqlUtils.appendIntegerParams(detectionBuff, detection, ["receiverDeploymentId"])
        SqlUtils.removeTrailingCommaAndAddBracket(detectionBuff)

        return detectionBuff.toString()
    }

    def applyEmbargo()
    {
        boolean isEmbargoed = false

        surgeries.each
        {
            if (it.release.isEmbargoed())
            {
                isEmbargoed = true
            }
        }

        return isEmbargoed ? null : this
    }

    static Kml toKml(List<ValidDetection> detections, serverURL)
    {
        return new SensorTrackKml(detections, serverURL)
    }
}
