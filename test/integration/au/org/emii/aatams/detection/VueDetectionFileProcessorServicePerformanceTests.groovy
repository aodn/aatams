package au.org.emii.aatams.detection

import au.org.emii.aatams.*
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase

import org.perf4j.StopWatch

class VueDetectionFileProcessorServicePerformanceTests extends AbstractGrailsUnitTestCase
{
    def vueDetectionFileProcessorService

    void testProcess()
    {
        ReceiverDownloadFile download

        try
        {
            download = new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV)
            download.initialiseForProcessing("thefilename")
            download.requestingUser = Person.findByUsername('jbloggs')
            download.save(failOnError: true, flush: true)

            download.metaClass.getPath = {
                "test/integration/au/org/emii/aatams/detection/Heron_15000.csv"
            }

            StopWatch stopWatch = new StopWatch();

            vueDetectionFileProcessorService.process(download)
            stopWatch.stop()

            def elapsedTimeMs = stopWatch.getElapsedTime()
            println "Elapsed time (ms): ${elapsedTimeMs}"
            assertTrue(elapsedTimeMs < 350000)
        }
        finally
        {
            download = ReceiverDownloadFile.read(download?.id)
            download?.delete()
        }
    }
}
