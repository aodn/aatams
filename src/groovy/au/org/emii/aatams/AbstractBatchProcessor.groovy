package au.org.emii.aatams

import org.apache.commons.io.input.BOMInputStream;
import org.grails.plugins.csv.CSVMapReader

/**
 *
 * @author jburgess
 */
abstract class AbstractBatchProcessor 
{
    def sessionFactory
    def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP
    
    def searchableService
 
	protected int getBatchSize()
	{
	    // This has been  tuned with a "suck-it-and-see" approach, with a dataset
	    // of 3000 records.
		return 30
	}
	
	protected void startBatch(context)
	{
		
	}
	
    protected void endBatch(context) 
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
			
			def context = [:]
			startBatch(context)
			
            records.eachWithIndex
            {
                map, i ->

				if ((i != 0) && ((i % batchSize) == 0))
                {
                    endBatch(context)
					startBatch(context)
                }
				
                processSingleRecord(downloadFile, map, context)

				float progress = (float)i/numRecords * 100
				if ((int)progress > percentProgress)
				{
					percentProgress = (int)progress
					
					String progressMsg = 
						"Download file processing, id: " + downloadFile.id +
						", progress: " + percentProgress +
						"%, average time per record: " + (float)(System.currentTimeMillis() - startTime) / (i + 1) + "ms"
						
					if ((percentProgress % 10) == 0)
					{
						log.info(progressMsg)
					}
					else
					{
						log.debug(progressMsg)
					}
				}
            }

			endBatch(context)
			
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
    
	Reader getReader(downloadFile)
	{
		// Wrap in BOMInputStream, to handle the byte-order marker present in VUE exports
		// (see http://stackoverflow.com/questions/1835430/byte-order-mark-screws-up-file-reading-in-java/7390288#7390288)
		return new InputStreamReader(new BOMInputStream(new FileInputStream(new File(downloadFile.path))))
	}
	
    List<Map<String, String>> getRecords(downloadFile)
    {
		return getMapReader(downloadFile).toList()
    }
    
	protected CSVMapReader getMapReader(downloadFile)
	{
		return new CSVMapReader(getReader(downloadFile))
	}
	
    abstract void processSingleRecord(downloadFile, map, context)
}

