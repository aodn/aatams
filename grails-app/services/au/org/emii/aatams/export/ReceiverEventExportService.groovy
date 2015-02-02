package au.org.emii.aatams.export

import au.org.emii.aatams.ValidReceiverEvent

class ReceiverEventExportService extends AbstractStreamingExporterService
{
    def queryService

    protected String getReportName()
    {
        return "receiverEvent"
    }

    protected def eachRow(filterParams, closure)
    {
        def queryResult = queryService.query(ValidReceiverEvent.class, filterParams)

        queryResult.results.each { row ->
            closure.call(row)
        }
    }

    protected def writeCsvRow(row, OutputStream out)
    {
        out << row.receiverDeployment.station.name << ","
        out << row.receiverName << ","
        out << row.formattedTimestamp << ","
        out << row.description << ","
        out << row.data << ","
        out << row.units << "\n"
    }

    protected void writeCsvHeader(OutputStream out)
    {
        out << "station name,receiver ID,timestamp,description,data,units\n"
    }
}
