package au.org.emii.aatams.bulk

import au.org.emii.aatams.FileProcessingStatus
import au.org.emii.aatams.ReceiverDownloadFile
import au.org.emii.aatams.ReceiverDownloadFileProgress
import au.org.emii.aatams.ReceiverDownloadFileType
import grails.test.*

class ReceiverDownloadFileProgressUpdaterTests extends GrailsUnitTestCase {

    def progress
    def downloadFile

    protected void setUp()
    {
        super.setUp()
        mockLogging(ReceiverDownloadFileProgressUpdater)
        mockDomain(ReceiverDownloadFile)
        mockDomain(ReceiverDownloadFileProgress)
        ReceiverDownloadFileProgress.metaClass.static.withNewTransaction = {  }

        downloadFile = new ReceiverDownloadFile(
            type: ReceiverDownloadFileType.DETECTIONS_CSV,
            importDate: new Date(),
            name: "My Detections File",
            status: FileProcessingStatus.PENDING,
            errMsg: "")


        downloadFile.progress = new ReceiverDownloadFileProgress();
        downloadFile.save()

        progress = new ReceiverDownloadFileProgressUpdater(downloadFile, 100)
    }

    protected void tearDown()
    {
        super.tearDown()
    }

    void testStart()
    {
        progress.start()

        assertNotNull(progress.startTime)
        assertNotNull(progress.percentComplete)
    }

    void testUpdateProgress()
    {
        progress.start()
        progress.updateProgress(10)
        assertEquals(10, progress.percentComplete)
    }

    void testAverageTimePerRecord()
    {
        progress.start()

        def tenSecondsAgo = System.currentTimeMillis() - 10000
        progress.startTime = tenSecondsAgo
        def avgTime = progress.averageTimePerRecord(20)
        assert avgTime >= (10000 / 20)
    }

    void testLogStatistics()
    {
        try
        {
            progress.start()
            progress.logStatistics(10)
        }
        catch (Throwable t)
        {
            fail("Log statistics should not throw exception")
        }

    }
}
