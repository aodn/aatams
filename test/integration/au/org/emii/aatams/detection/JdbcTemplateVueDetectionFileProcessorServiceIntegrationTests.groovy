package au.org.emii.aatams.detection

import au.org.emii.aatams.*

class JdbcTemplateVueDetectionFileProcessorServiceIntegrationTests extends AbstractJdbcTemplateVueDetectionFileProcessorServiceIntegrationTests
{
    ReceiverDownloadFile export
    def exportFile

    protected void setUp()
    {
        super.setUp()

        export =
            new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV,
            name: "duplicate",
            importDate: new Date(),
            status: FileProcessingStatus.PROCESSING,
            errMsg: "",
            requestingUser: null).save(failOnError:true)

            export.initialiseForProcessing("duplicate.csv")
            export.requestingUser = Person.findByUsername('jbloggs')
            export.save(flush: true, failOnError: true)

        export = ReceiverDownloadFile.read(export.id)

        exportFile = new File(export.path)
        exportFile.getParentFile().mkdirs()
        exportFile << '''Date and Time (UTC),Receiver,Transmitter,Transmitter Name,Transmitter Serial,Sensor Value,Sensor Unit,Station Name,Latitude,Longitude
'''
    }

    protected void tearDown()
    {
        exportFile?.delete()
        getRefreshedExport(export)?.delete()

        super.tearDown()
    }

    // Test for #2055
    void testNothing()
    {
    }

    void testPromoteProvisional()
    {
        def origDetectionViewCount = getDetectionViewCount()
        def origValidDetCount = getValidDetectionCount()
        def origInvalidDetCount = InvalidDetection.count()
        assertEquals(origDetectionViewCount, origValidDetCount)

        def origStatisticsNumValidDetCount = Statistics.findByKey('numValidDetections')?.value

        def timestamp1 = '2011-05-17 03:54:05'
        def timestamp2 = '2011-05-17 04:54:05'
        def timestamp3 = '2011-05-17 05:54:05'
        def testReceiver = 'VR2W-101336'
        def testTransmitter = 'A69-1303-12345'
        def detRows = [
            "$timestamp1,$testReceiver,$testTransmitter",
            "$timestamp2,$testReceiver,$testTransmitter",
            "$timestamp3,$testReceiver,$testTransmitter"
        ]
        def numNewDets = detRows.size()

        exportFile << detRows.join('\n')

        jdbcTemplateVueDetectionFileProcessorService.process(export)

        def finalDetectionViewCount = getDetectionViewCount()
        def finalValidDetCount = getValidDetectionCount()
        def finalProvDetCount = ValidDetection.findAllByProvisional(true).size()
        def finalStatisticsNumValidDetCount = Statistics.getStatistic('numValidDetections')

        assertEquals(origInvalidDetCount, InvalidDetection.count())
        assertEquals(0, finalProvDetCount)
        assertEquals(origDetectionViewCount + numNewDets, finalDetectionViewCount)
        assertEquals(origValidDetCount + numNewDets, finalValidDetCount)
        assertEquals(origStatisticsNumValidDetCount + numNewDets, finalStatisticsNumValidDetCount)

        // Cleanup (because the transaction has been comitted this won't happen automatically)
        def ts = [timestamp1, timestamp2, timestamp3]
        ValidDetection.findAllWhere(receiverName: testReceiver, transmitterId: testTransmitter).findAll{ ts.contains(it.formattedTimestamp) }*.delete(flush: true)
    }

    private def getDetectionViewCount() {
        return sql.firstRow(String.valueOf("select count(*) from ${QueryBuilder.getViewName()};")).count
    }

    private def getValidDetectionCount() {
        return sql.firstRow('select count(*) from valid_detection;').count
    }

    private def getRefreshedExport(export)
    {
        return ReceiverDownloadFile.read(export?.id)
    }
}
