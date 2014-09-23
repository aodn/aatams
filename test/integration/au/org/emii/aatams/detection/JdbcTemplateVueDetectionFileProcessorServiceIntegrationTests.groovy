package au.org.emii.aatams.detection

import au.org.emii.aatams.*
import au.org.emii.aatams.test.*

import grails.test.*
import groovy.sql.*
import groovy.lang.MetaClass

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
        def origMatViewCount = sql.firstRow('select count(*) from detection_extract_view_mv;').count
        def origValidDetCount = sql.firstRow('select count(*) from valid_detection;').count
        def origInvalidDetCount = InvalidDetection.count()
        assertEquals(origMatViewCount, origValidDetCount)

        def origStatisticsNumValidDetCount = Statistics.findByKey('numValidDetections')?.value

        def detRows = ['2011-05-17 03:54:05,VR2W-101336,A69-1303-12345','2011-05-17 04:54:05,VR2W-101336,A69-1303-12345', '2011-05-17 05:54:05,VR2W-101336,A69-1303-12345']
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
    }

    private def getRefreshedExport(export)
    {
        return ReceiverDownloadFile.read(export?.id)
    }
}
