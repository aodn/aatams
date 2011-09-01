package au.org.emii.aatams

import org.grails.plugins.csv.CSVMapReader
import org.springframework.web.multipart.MultipartFile

/**
 * Process a file downloaded from receiver's VUE application.
 */
class VueDetectionFileProcessorService
{
    def cachedDetectionFactoryService
    def detectionFactoryService
    
    static transactional = true

    def sessionFactory
    def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP
    
    void process(ReceiverDownloadFile downloadFile) throws FileProcessingException
    {
        def detectionService = cachedDetectionFactoryService
//        def detectionService = detectionFactoryService
    
        downloadFile.status = FileProcessingStatus.PROCESSING
        downloadFile.save()
        
        detectionService.clearCache()
        
        def startTimestamp = System.currentTimeMillis()
        
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
        def records = new File(downloadFile.path).toCsvMapReader().toList()
        def numRecords = records.size()
        
        // Provide more frequent updates when there are many records to process.
        int percentComplete = 0
        int percentCompleteInc = (numRecords > 1000) ? 1 : 10
        
        records.eachWithIndex{ map, i ->
            
            log.debug("Processing record number: " + String.valueOf(i))
            
            Detection detection = detectionService.newDetection(map)
            
            def exactPercentComplete = i * 100 / numRecords
            if (exactPercentComplete > (percentComplete + percentCompleteInc))
            {
                String percentCompleteMsg = percentComplete + "% complete"
                log.info("Receiver export: " + String.valueOf(downloadFile.name) + ", " + percentCompleteMsg)
                percentComplete = exactPercentComplete / percentCompleteInc * percentCompleteInc
            }
            
            log.debug("Successfully processed record number: " + String.valueOf(i))
        }
        
        log.info("Detections processed: (" + numRecords + ") in " + (System.currentTimeMillis() - startTimestamp) / 1000 + "s.")
            
        downloadFile.status = FileProcessingStatus.PROCESSED
        downloadFile.errMsg = ""
        downloadFile.save()
    }   
}
