package au.org.emii.aatams

import org.apache.commons.io.input.BOMInputStream;

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

			int percentProgress = 0
			
			long startTime = System.currentTimeMillis()
			
            records.eachWithIndex
            {
                map, i ->

                log.debug("Processing record number: " + String.valueOf(i))

                processSingleRecord(downloadFile, map)

                if ((i % batchSize) == 0)
                {
                    cleanUpGorm()
					log.debug(String.valueOf(i) + " records processed, average time per record: " + (float)(System.currentTimeMillis() - startTime) / (i + 1) + "ms")
                }
				
				float progress = (float)i/numRecords * 100
				if ((int)progress > percentProgress)
				{
					percentProgress = (int)progress
					log.debug("Progress: " + percentProgress + "%")
				}

//				if (i == 1000)
//				{
//					throw new RuntimeException("test finished")
//				}
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
		// Wrap in BOMInputStream, to handle the byte-order marker present in VUE exports
		// (see http://stackoverflow.com/questions/1835430/byte-order-mark-screws-up-file-reading-in-java/7390288#7390288)
        return new InputStreamReader(new BOMInputStream(new FileInputStream(new File(downloadFile.path)))).toCsvMapReader().toList()
    }
    
    abstract void processSingleRecord(downloadFile, map)
}

