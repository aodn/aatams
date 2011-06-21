package au.org.emii.aatams

import org.grails.plugins.csv.CSVMapReader
import org.springframework.web.multipart.MultipartFile

/**
 * Process a file downloaded from receiver's VUE application.
 */
class VueFileProcessorService
{
    def detectionFactoryService
    
    static transactional = true

    /**
     * Determine whether or not the given file is parseable by this processor.
     */
    boolean isParseable(ReceiverDownloadFile downloadFile)
    {
        
        // Just go by file suffix for now (might come up with a better way
        // of doing this.
        if (downloadFile.name.endsWith(".csv"))
        {
            log.debug("File " + downloadFile.name + " is parseable")
            return true
        }
        
        log.debug("File " + downloadFile.name + " is not parseable")
        return false
    }
    
    void process(ReceiverDownloadFile downloadFile) throws FileProcessingException
    {
        assert(isParseable(downloadFile)): "File is not parseable, downloadFile: " + String.valueOf(downloadFile)
        
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
