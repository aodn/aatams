package au.org.emii.aatams

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
    
    static belongsTo = [receiverDeployment: ReceiverDeployment, receiverDownload:ReceiverDownloadFile]
    
    String description
    
    String data
    
    String units
    
    static constraints = 
    {
        timestamp()
        description()
        data(nullable:true, blank:true)
        units(nullable:true, blank:true)
    }
    
    static searchable =
    {
        root(false)
        receiverDeployment(component:true)
    }
	
	static String toSqlInsert(Map event)
	{
		StringBuilder eventBuff = new StringBuilder(
				"INSERT INTO RECEIVER_EVENT (ID, VERSION, TIMESTAMP, RECEIVER_DEPLOYMENT_ID, RECEIVER_DOWNLOAD_ID, DATA, DESCRIPTION, UNITS)" +
				" VALUES(")

		eventBuff.append("nextval('hibernate_sequence'),")
		eventBuff.append("0,")
		eventBuff.append("'" + new java.sql.Timestamp(event["timestamp"].getTime()) + "',")
		
		SqlUtils.appendIntegerParams(eventBuff, event, ["receiverDeploymentId", "receiverDownloadId"])
		SqlUtils.appendStringParams(eventBuff, event, ["data", "description", "units"])
		SqlUtils.removeTrailingCommaAndAddBracket(eventBuff)
		
		return eventBuff.toString()
	}
}
