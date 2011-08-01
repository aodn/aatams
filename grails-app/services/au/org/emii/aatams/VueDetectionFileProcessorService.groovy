package au.org.emii.aatams

import org.grails.plugins.csv.CSVMapReader
import org.springframework.web.multipart.MultipartFile

/**
 * Process a file downloaded from receiver's VUE application.
 */
class VueDetectionFileProcessorService
{
    def detectionFactoryService
    
    static transactional = true

    void process(ReceiverDownloadFile downloadFile) throws FileProcessingException
    {
        downloadFile.status = FileProcessingStatus.PROCESSING
        downloadFile.save()
        
        //
        // Expects csv files with the following columns (including a header row).
        //
        //      ÔªøDate and Time (UTC)  // what's with the funny characters??
        //      Receiver
        //      Transmitter	
        //      Transmitter Name	
        //      Transmitter Serial	
        //      Sensor Value	
        //      Sensor Unit	
        //      Station Name	
        //      Latitude	
        //      Longitude
        //
        new File(downloadFile.path).toCsvMapReader().eachWithIndex{ map, i ->
            
            log.debug("Processing record number: " + String.valueOf(i))
            
            Detection detection = detectionFactoryService.newDetection(map).save(failOnError: true)
            
            log.debug("Successfully processed record number: " + String.valueOf(i))
        }

        downloadFile.status = FileProcessingStatus.PROCESSED
        downloadFile.save()
    }   
}
