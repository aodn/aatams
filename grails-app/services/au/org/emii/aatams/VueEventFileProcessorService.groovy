package au.org.emii.aatams

import org.grails.plugins.csv.CSVMapReader
import org.springframework.web.multipart.MultipartFile

/**
 * Process an event file downloaded from receiver's VUE application.
 */
class VueEventFileProcessorService
{
    def eventFactoryService
    
    void process(ReceiverDownloadFile downloadFile) throws FileProcessingException
    {
        downloadFile.status = FileProcessingStatus.PROCESSING
        downloadFile.save()
        
        //
        // Expects csv files with the following columns (including a header row).
        //
        //      Date/Time
        //      Receiver
        //      Description
        //      Data
        //      Units
        //
        new File(downloadFile.path).toCsvMapReader().eachWithIndex{ map, i ->
            
            log.debug("Processing event record number: " + String.valueOf(i))
            
            ReceiverEvent event = eventFactoryService.newEvent(map).save(failOnError: true)
            
            log.debug("Successfully event processed record number: " + String.valueOf(i))
        }

        downloadFile.status = FileProcessingStatus.PROCESSED
        downloadFile.save()
    }   
}
