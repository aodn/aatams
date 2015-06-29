package au.org.emii.aatams

import au.org.emii.aatams.ReceiverDownloadFileProgressUpdater
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
        flushSession()
        propertyInstanceMap?.get().clear()
    }

    void flushSession()
    {
        def session = sessionFactory.currentSession
        session.flush()
        session.clear()
    }

    long getNumRecords(downloadFile)
    {
        log.debug("Counting number of records in file...")
        long lineCount = 0

        new File(downloadFile.path).eachLine {
            lineCount++
        }

        log.debug("Records count: " + (lineCount - 1))
        return (lineCount - 1)    // -1 -> don't count the header.
    }

    void process(ReceiverDownloadFile downloadFile) throws FileProcessingException
    {
        def recordCsvMapReader
        try
        {
            searchableService.stopMirroring()
            recordCsvMapReader = getMapReader(downloadFile)
            batchProcess(downloadFile, recordCsvMapReader)
            downloadFileProcessed(downloadFile)
        }
        catch (Throwable t)
        {
            log.error t

            downloadFileError(downloadFile, t)
            throw t
        }
        finally
        {
            downloadFile.save(flush: true, failOnError: true)
            flushSession()
            searchableService.startMirroring()
            recordCsvMapReader?.close()
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

    private boolean isEndOfBatch(itemNumber)
    {
        return itemNumber != 0 && itemNumber % batchSize == 0
    }

    private void batchProcess(downloadFile, recordCsvMapReader) throws FileProcessingException
    {
        def progress = new ReceiverDownloadFileProgressUpdater(downloadFile, getNumRecords(downloadFile))
        progress.start()

        def context = [downloadFile: downloadFile]
        try
        {
            startBatch(context)
            recordCsvMapReader.eachWithIndex
            {   map, i ->

                if (isEndOfBatch(i))
                {
                    endBatch(context)
                    startBatch(context)
                }

                try
                {
                    processSingleRecord(downloadFile, map, context)
                }
                catch (FileProcessingException e)
                {
                    log.error("Exception reading record: ${map}", e)
                    throw e
                }
                finally
                {
                    progress.updateProgress(i + 1)
                }
            }
        }
        finally
        {
            endBatch(context)
        }
        // Only log statistics if the upload is successful
        progress.logStatistics(batchSize)
    }

    private void finishDownloadFile(downloadFile, errMsg, status)
    {
        downloadFile.errMsg = errMsg
        downloadFile.status = status
    }

    private void downloadFileProcessed(downloadFile)
    {
        finishDownloadFile(downloadFile, "", FileProcessingStatus.PROCESSED)
    }

    private void downloadFileError(downloadFile, throwable)
    {
        finishDownloadFile(downloadFile, downloadFileErrorMessage(throwable), FileProcessingStatus.ERROR)
    }

    private def downloadFileErrorMessage(throwable)
    {
        def message = throwable.toString()
        if (throwable.message)
        {
            message = throwable.message
        }
        else if (throwable.cause)
        {
            message = throwable.cause.message
        }
        else if (throwable.undeclaredThrowable)
        {
            message = throwable.undeclaredThrowable.message
        }

        return message
    }

    abstract void processSingleRecord(downloadFile, map, context) throws FileProcessingException
}
