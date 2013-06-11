package au.org.emii.aatams.detection

import au.org.emii.aatams.*
import au.org.emii.aatams.test.*

import org.perf4j.StopWatch

class JdbcTemplateVueDetectionFileProcessorServicePerformanceTests extends AbstractJdbcTemplateVueDetectionFileProcessorServiceIntegrationTests
{
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
            
            jdbcTemplateVueDetectionFileProcessorService.process(download)
            stopWatch.stop()

            def elapsedTimeMs = stopWatch.getElapsedTime()
            println "Elapsed time (ms): ${elapsedTimeMs}"
            assertTrue(elapsedTimeMs < 200000)
        }
        finally
        {
            download = ReceiverDownloadFile.read(download?.id)
            download?.delete()
        }
    }
}