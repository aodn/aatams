package au.org.emii.aatams.upload

import au.org.emii.aatams.Detection

import com.lucastex.grails.fileuploader.UFile
import org.grails.plugins.csv.CSVMapReader

/**
 * Process a file downloaded from receiver's VUE application.
 */
class VueFileProcessorService extends AbstractFileProcessorService
{
    def detectionFactoryService
    
    static transactional = true

    /**
     * Determine whether or not the given file is parseable by this processor.
     */
    boolean isParseable(fileId)
    {
        UFile file = getUFile(fileId)
        
        // Just go by file suffix for now (might come up with a better way
        // of doing this.
        if (file.name.endsWith(".csv"))
        {
            log.debug("File " + file.name + " is parseable")
            return true
        }
        
        log.debug("File " + file.name + " is not parseable")
        return false
    }
    
    void process(fileId) throws FileProcessingException
    {
        assert(isParseable(fileId)): "File is not parseable, file ID: " + fileId
        ProcessedUploadFile file = getFile(fileId)
        file.status = FileProcessingStatus.PROCESSING
        file.save()
        
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
        new File(file.uFile.path).toCsvMapReader().eachWithIndex{ map, i ->
            
            log.debug("Processing record number: " + String.valueOf(i))
            
            Detection detection = detectionFactoryService.newDetection(map)
            
            log.debug("Successfully processed record number: " + String.valueOf(i))
        }

        file.status = FileProcessingStatus.PROCESSED
        file.save()
    }   
}
