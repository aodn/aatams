package au.org.emii.aatams

import java.text.DateFormat;
import java.text.SimpleDateFormat

import au.org.emii.aatams.util.SqlUtils

/**
 * Receivers (or more correctly deployments) have a number of (usually daily)
 * events associated with them.
 *
 * e.g. initialisation time (once off), daily battery level, daily pings etc.
 */
class ReceiverEvent
{
    /**
     * UTC timestamp.
     */
    Date timestamp

	String receiverName

    static belongsTo = [receiverDownload:ReceiverDownloadFile]

    String description

    String data

    String units

    static constraints =
    {
        timestamp()
        description()
		receiverName()
        data(nullable:true, blank:true)
        units(nullable:true, blank:true)
    }

    static mapping =
    {
        cache true
    }

	static transients = ['formattedTimestamp']

	static String toSqlInsert(Map event)
	{
		StringBuilder eventBuff = new StringBuilder(
				"INSERT INTO RECEIVER_EVENT (ID, VERSION, TIMESTAMP, RECEIVER_DEPLOYMENT_ID, RECEIVER_DOWNLOAD_ID, DATA, DESCRIPTION, RECEIVER_NAME, UNITS, CLASS, MESSAGE, REASON)" +
				" VALUES(")

		eventBuff.append("nextval('hibernate_sequence'),")
		eventBuff.append("0,")
		eventBuff.append("'" + new java.sql.Timestamp(event["timestamp"].getTime()) + "',")

		SqlUtils.appendIntegerParams(eventBuff, event, ["receiverDeploymentId", "receiverDownloadId"])
		SqlUtils.appendStringParams(eventBuff, event, ["data", "description", "receiverName", "units", "clazz", "message", "reason"])
		SqlUtils.removeTrailingCommaAndAddBracket(eventBuff)

		return eventBuff.toString()
	}

	static DateFormat formatter

	String getFormattedTimestamp()
	{
		if (timestamp)
		{
			if (!formatter)
			{
				formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			}

			return formatter.format(timestamp)
		}

		return null
	}
}
