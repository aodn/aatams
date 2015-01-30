package au.org.emii.aatams.export

import au.org.emii.aatams.ValidReceiverEvent

class ReceiverEventExportService extends AbstractStreamingExporterService
{
    def queryService

    protected String getReportName()
    {
        return "receiverEvent"
    }

    protected def readData(filterParams)
    {
        def queryResult = queryService.queryWithoutCount(ValidReceiverEvent.class, filterParams)
        return [results: queryResult.results, rowCount: queryResult.results.size()]
    }

    protected def writeCsvChunk(resultList, OutputStream out)
    {
        resultList.each
        {
            row ->

            out << row.receiverDeployment.station.name << ","
            out << row.receiverName << ","
            out << row.formattedTimestamp << ","
            out << row.description << ","
            out << row.data << ","
            out << row.units << "\n"
        }

        return resultList.size()
    }

    protected void writeCsvHeader(OutputStream out)
    {
        out << "station name,receiver ID,timestamp,description,data,units\n"
    }
}
