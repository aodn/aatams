package au.org.emii.aatams

import java.text.DateFormat;
import java.text.SimpleDateFormat

import au.org.emii.aatams.util.SqlUtils

import org.jooq.*
import org.jooq.conf.ParamType
import org.jooq.impl.DSL

import static org.jooq.impl.DSL.*

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

    static String toSqlInsert(event)
    {
        DSLContext create = DSL.using(SQLDialect.POSTGRES);

        def insert = create.insertInto(
            table('RECEIVER_EVENT'),
            field('ID'),
            field('VERSION'),
            field('TIMESTAMP'),
            field('RECEIVER_DEPLOYMENT_ID'),
            field('RECEIVER_DOWNLOAD_ID'),
            field('DATA'),
            field('DESCRIPTION'),
            field('RECEIVER_NAME'),
            field('UNITS'),
            field('CLASS'),
            field('MESSAGE'),
            field('REASON')
        )
        .values(
            sequenceByName('hibernate_sequence').nextval(),
            0,
            formatTimestamp(event['timestamp'], 'yyyy-MM-dd HH:mm:ssZ'),
            event.receiverDeploymentId,
            event.receiverDownloadId,
            event.data,
            event.description,
            event.receiverName,
            event.units,
            event.clazz,
            event.message,
            event.reason
        )

        return insert.getSQL(ParamType.INLINED)
    }

    static DateFormat formatter

    String getFormattedTimestamp()
    {
        return formatTimestamp(timestamp, "yyyy-MM-dd HH:mm:ss")
    }

    static formatTimestamp(timestamp, format) {
        return SqlUtils.formatTimestamp(timestamp, format)
    }
}
