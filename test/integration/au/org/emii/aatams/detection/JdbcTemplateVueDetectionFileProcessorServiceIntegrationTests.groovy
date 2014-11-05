package au.org.emii.aatams.detection

import org.joda.time.*

import au.org.emii.aatams.*

class JdbcTemplateVueDetectionFileProcessorServiceIntegrationTests extends AbstractJdbcTemplateVueDetectionFileProcessorServiceIntegrationTests

{
    ReceiverDownloadFile export
    def exportFile

    // Required since the file processor commits the transaction.
    def itemsToDelete = []

    def jbloggs

    protected void setUp()
    {
        super.setUp()

        jbloggs = Person.build(username: 'jbloggs')
        itemsToDelete << jbloggs

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
        itemsToDelete << export

        exportFile = new File(export.path)
        exportFile.getParentFile().mkdirs()
        exportFile << '''Date and Time (UTC),Receiver,Transmitter,Transmitter Name,Transmitter Serial,Sensor Value,Sensor Unit,Station Name,Latitude,Longitude
'''
        itemsToDelete << exportFile
    }

    protected void tearDown()
    {
        itemsToDelete.each {
            it.delete()
        }

        super.tearDown()
    }

    // This test will have no use when https://github.com/aodn/aatams/tree/remove_det_surgeries_take_2 is merged.
    //void testNoDetectionSurgeryForDuplicateDetection()

    void testPromoteProvisional()
    {
        def origMatViewCount = sql.firstRow('select count(*) from detection_extract_view_mv;').count
        def origValidDetCount = sql.firstRow('select count(*) from valid_detection;').count
        def origInvalidDetCount = InvalidDetection.count()
        assertEquals(origMatViewCount, origValidDetCount)

        def origStatisticsNumValidDetCount = Statistics.findByKey('numValidDetections')?.value

        def rxr = TestUtils.buildReceiver('VR2W-101336')
        def deployment = ReceiverDeployment.build(receiver: rxr,
                                                  initialisationDateTime: new DateTime('2010-01-01T00:00'),
                                                  deploymentDateTime: new DateTime('2010-01-01T00:00'))
        def role = ProjectRole.build(person: jbloggs)
        def recovery = ReceiverRecovery.build(recoveryDateTime: new DateTime('2012-01-01T00:00'),
                                              deployment: deployment,
                                              recoverer: role).save(flush: true)
        deployment.recovery = recovery
        deployment.save(flush: true)

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

        def finalMatViewCount = sql.firstRow('select count(*) from detection_extract_view_mv;').count
        def finalValidDetCount = sql.firstRow('select count(*) from valid_detection;').count
        def finalProvDetCount = ValidDetection.findAllByProvisional(true).size()
        def finalStatisticsNumValidDetCount = Statistics.getStatistic('numValidDetections')

        assertEquals(origInvalidDetCount, InvalidDetection.count())

        assertEquals(0, finalProvDetCount)
        assertEquals(origMatViewCount + numNewDets, finalMatViewCount)
        assertEquals(origValidDetCount + numNewDets, finalValidDetCount)
        assertEquals(origStatisticsNumValidDetCount + numNewDets, finalStatisticsNumValidDetCount)

        // Cleanup (because the transaction has been comitted this won't happen automatically)
        def ts = [timestamp1, timestamp2, timestamp3]
        ValidDetection.findAllWhere(receiverName: testReceiver, transmitterId: testTransmitter).findAll{ ts.contains(it.formattedTimestamp) }*.delete(flush: true)
    }

    private def getRefreshedExport(export)
    {
        return ReceiverDownloadFile.read(export?.id)
    }
}
