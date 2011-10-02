package au.org.emii.aatams.detection

import au.org.emii.aatams.*

import org.grails.plugins.csv.CSVMapReader
import org.springframework.web.multipart.MultipartFile

/**
 * Process a file downloaded from receiver's VUE application.
 */
class VueDetectionFileProcessorService
{
    def detectionFactoryService
    
    static transactional = true

    def sessionFactory
    def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP
    
    void process(ReceiverDownloadFile downloadFile) throws FileProcessingException
    {
        downloadFile.status = FileProcessingStatus.PROCESSING
        downloadFile.save()
        
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
        def records = getRecords(downloadFile)
        def numRecords = records.size()

        // This has been tuned with a "suck-it-and-see" approach, with a dataset
        // of 3000 records.
        int batchSize = 50
        
        records.eachWithIndex
        {
            map, i ->
            
            log.debug("Processing record number: " + String.valueOf(i))
            
            RawDetection detection = detectionFactoryService.newDetection(downloadFile, map)
            downloadFile.addToDetections(detection)
            
            if ((i % batchSize) == 0)
            {
                cleanUpGorm()
            }
            
            log.debug("Successfully processed record number: " + String.valueOf(i))
        }
        
        long elapsedTime = System.currentTimeMillis() - startTimestamp
        log.info("Batch details, size: " + batchSize + ", time per record (ms) : " + (float)elapsedTime / numRecords)    
        log.info("Detections processed: (" + numRecords + ") in (ms): " +  elapsedTime)
            
        // Required to avoid hibernate exception, since session is flushed and cleared above.
        downloadFile = ReceiverDownloadFile.get(downloadFile.id)
        downloadFile.status = FileProcessingStatus.PROCESSED
        downloadFile.errMsg = ""
        downloadFile.save()
    } 
    
    List<Map<String, String>> getRecords(downloadFile)
    {
        return new File(downloadFile.path).toCsvMapReader().toList()
    }
    
    private void cleanUpGorm() 
    {
        def session = sessionFactory?.currentSession
        session?.flush()
        session?.clear()
        propertyInstanceMap?.get().clear()
    }
}
