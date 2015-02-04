package au.org.emii.aatams.export

import au.org.emii.aatams.ReceiverEventQueryBuilder
import au.org.emii.aatams.ValidReceiverEvent

class ReceiverEventExportService extends AbstractStreamingExporterService
{
    def queryService

    protected String getReportName()
    {
        return "receiverEvent"
    }

    protected def eachRow(params, closure)
    {
        def startTime = System.currentTimeMillis()
        def query =  new ReceiverEventQueryBuilder().constructQuery(params)
        int count = 0

        params.sql.eachRow(query.getSQL(), query.getBindValues()) { row ->
            closure.call(row)
            count++
        }

        def endTime = System.currentTimeMillis()
        log.debug("Export finished, num results: ${count}, elapsed time (ms): ${endTime - startTime}, query: ${query}")
    }

    protected def writeCsvRow(row, OutputStream out)
    {
        out << row.station << ","
        out << row.receiver_name << ","
        out << row.formatted_timestamp << ","
        out << row.description << ","
        out << row.data << ","
        out << row.units << "\n"
    }

    protected void writeCsvHeader(OutputStream out)
    {
        out << "station name,receiver ID,timestamp,description,data,units\n"
    }
}
