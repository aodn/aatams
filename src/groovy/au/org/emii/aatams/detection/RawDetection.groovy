package au.org.emii.aatams.detection

import org.hibernatespatial.GeometryUserType

import au.org.emii.aatams.util.GeometryUtils
import au.org.emii.aatams.util.SqlUtils

import com.vividsolutions.jts.geom.*

/**
 * A 1:1 mapping of a single CSV record from receiver export.
 */
abstract class RawDetection
{
    /**
     * UTC timestamp.
     */
    Date timestamp

    String receiverName

    String stationName

    /**
     * Record the actual ID transmitted by the tag.
     */
    String transmitterId

    /**
     * May be different (as with station name above).
     */
    String transmitterName

    String transmitterSerialNumber

    /**
     * May be different (as with station name above).
     */
    Point location

    Float sensorValue
    String sensorUnit

    static transients = ['scrambledLocation', 'valid', 'formattedTimestamp']

    static constraints =
    {
        transmitterName(nullable:true, blank:true)
        transmitterSerialNumber(nullable:true, blank:true)
        sensorValue(nullable:true)
        sensorUnit(nullable:true, blank:true)
        stationName(nullable:true, blank:true)
        location(nullable:true)

        // Workaround for problem where jenkins build is failing - not sure why.
        // Getting "ValidationException" when trying to save ValidDetection.
        scrambledLocation(nullable:true)
        formattedTimestamp(nullable:true)
    }

//    static belongsTo = [receiverDownload:ReceiverDownloadFile]

    static mapping =
    {
//        timestamp index:'timestamp_index'
//        transmitterId index:'transmitterId_index'
//        receiverName index:'receiverName_index'
        cache true
        location type: GeometryUserType
    }

    boolean isValid()
    {
        return true
    }

    /**
     * Non-authenticated users can only see scrambled locations.
     */
    Point getScrambledLocation()
    {
        return GeometryUtils.scrambleLocation(location)
    }

    String getFormattedTimestamp()
    {
        return formatTimestamp(timestamp, "yyyy-MM-dd HH:mm:ss")
    }

    static formatTimestamp(timestamp, format) {
        return SqlUtils.formatTimestamp(timestamp, format)
    }
}
