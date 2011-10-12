package au.org.emii.aatams

/**
 *
 * @author jburgess
 */
abstract class AbstractBatchProcessor 
{
    def sessionFactory
    def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP
    
    // This has been tuned with a "suck-it-and-see" approach, with a dataset
    // of 3000 records.
    int batchSize = 50
    
    def searchableService
 
    protected void cleanUpGorm() 
    {
        def session = sessionFactory?.currentSession
        session?.flush()
        session?.clear()
        propertyInstanceMap?.get().clear()
    }
    
    void process(ReceiverDownloadFile downloadFile) throws FileProcessingException
    {
        try
        {
            searchableService.stopMirroring()
        
            downloadFile.status = FileProcessingStatus.PROCESSING
            downloadFile.save()

            def startTimestamp = System.currentTimeMillis()

            def records = getRecords(downloadFile)
            def numRecords = records.size()

            records.eachWithIndex
            {
                map, i ->

                log.debug("Processing record number: " + String.valueOf(i))

                processSingleRecord(downloadFile, map)

                if ((i % batchSize) == 0)
                {
                    cleanUpGorm()
                }

                log.debug("Successfully processed record number: " + String.valueOf(i))
            }

            long elapsedTime = System.currentTimeMillis() - startTimestamp
            log.info("Batch details, size: " + batchSize + ", time per record (ms) : " + (float)elapsedTime / numRecords)    
            log.info("Records processed: (" + numRecords + ") in (ms): " +  elapsedTime)

            // Required to avoid hibernate exception, since session is flushed and cleared above.
            downloadFile = ReceiverDownloadFile.get(downloadFile.id)
            downloadFile.status = FileProcessingStatus.PROCESSED
            downloadFile.errMsg = ""
            downloadFile.save(flush:true)
        }
        finally
        {
            searchableService.startMirroring()
        }
    }
    
    List<Map<String, String>> getRecords(downloadFile)
    {
        return new File(downloadFile.path).toCsvMapReader().toList()
    }
    
    abstract void processSingleRecord(downloadFile, map)
}

