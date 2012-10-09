package au.org.emii.aatams

import au.org.emii.aatams.bulk.BulkImportException
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
    
	long getNumRecords(downloadFile)
	{
		log.debug("Counting number of records in file...")
		long lineCount = 0
		
		new File(downloadFile.path).eachLine {
			lineCount++
		}
		
		log.debug("Records count: " + (lineCount - 1))
		return (lineCount - 1)	// -1 -> don't count the header.	
	}
	
    void process(ReceiverDownloadFile downloadFile) throws FileProcessingException
    {
		def recordCsvMapReader
        try
        {
            searchableService.stopMirroring()
        
            def startTimestamp = System.currentTimeMillis()

			recordCsvMapReader = getMapReader(downloadFile)
            def numRecords = getNumRecords(downloadFile)
			
			int percentProgress = -1
			
			long startTime = System.currentTimeMillis()
			
			downloadFile.discard()
			downloadFile?.progress?.discard()
			
			def context = [downloadFile: downloadFile]
			
			startBatch(context)
			
			File bulkDebugLog = new File("/tmp/bulkUpload.log")
			
            recordCsvMapReader.eachWithIndex
            {
                map, i ->

				if ((i != 0) && ((i % batchSize) == 0))
                {
                    endBatch(context)
					startBatch(context)
                }
				
				// TODO: should be getting exceptions - but it depends on reading in
				// all CSIRO imports (rather than ignoring some records).
				try
				{
	                processSingleRecord(downloadFile, map, context)
				}
				catch (Throwable e)	// should only have to catch BulkImportException, but that is being wrapped for some reason.
				{
//					log.warn("Exception reading record: ${map}")
					bulkDebugLog << "Exception reading record: ${map}\n${e.cause.message}\n"
				}
				finally
				{
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
						
						if ((percentProgress % 1) == 0)
						{
							updateProgress(downloadFile, percentProgress)
						}
					}
				}
            }

			endBatch(context)
			
            long elapsedTime = System.currentTimeMillis() - startTimestamp
            log.info("Batch details, size: " + batchSize + ", time per record (ms) : " + (float)elapsedTime / numRecords)    
            log.info("Records processed: (" + numRecords + ") in (ms): " +  elapsedTime)

            // Required to avoid hibernate exception, since session is flushed and cleared above.
            downloadFile = ReceiverDownloadFile.get(downloadFile.id)
			downloadFile.progress?.refresh()
            downloadFile.status = FileProcessingStatus.PROCESSED
            downloadFile.percentComplete = 100
			downloadFile.errMsg = ""
            downloadFile.save(flush:true)
        }
        finally
        {
            searchableService.startMirroring()
			recordCsvMapReader?.close()
        }
    }

	private void updateProgress(downloadFile, percentProgress) 
	{
		ReceiverDownloadFileProgress.withNewTransaction
		{
			ReceiverDownloadFile refreshedDownloadFile = ReceiverDownloadFile.read(downloadFile.id)
			ReceiverDownloadFileProgress progress = refreshedDownloadFile.progress
			
			log.debug("Updating progress, percentComplete: ${percentProgress}")
			progress?.percentComplete = percentProgress
			progress?.save(failOnError: true)
		}
	}
    
	Reader getReader(downloadFile)
	{
		log.debug("Instantiating stream reader...")
		// Wrap in BOMInputStream, to handle the byte-order marker present in VUE exports
		// (see http://stackoverflow.com/questions/1835430/byte-order-mark-screws-up-file-reading-in-java/7390288#7390288)
		def reader = new InputStreamReader(new BOMInputStream(new FileInputStream(new File(downloadFile.path))))
		log.debug("Stream reader instantiated")
		
		return reader
	}
	
    List<Map<String, String>> getRecords(downloadFile)
    {
		log.debug("Instantiating list of records...")
		def mapReader = getMapReader(downloadFile).toList()
		log.debug("List of records instantiated")
		return mapReader
    }
    
	protected CSVMapReader getMapReader(downloadFile)
	{
		log.debug("Instantiating CSV map reader...")
		def mapReader = new CSVMapReader(getReader(downloadFile))
		log.debug(" CSV map reader instantiated")
		return mapReader
	}
	
    abstract void processSingleRecord(downloadFile, map, context)
}

