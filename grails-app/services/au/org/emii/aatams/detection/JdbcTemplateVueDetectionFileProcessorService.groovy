package au.org.emii.aatams.detection

import groovy.sql.Sql

import org.springframework.jdbc.core.JdbcTemplate

import au.org.emii.aatams.FileProcessingException
import au.org.emii.aatams.ReceiverDownloadFile
import au.org.emii.aatams.Statistics

/**
 *  Uses JDBC template (rather than GORM) to persist detection records.
 *
 * @author jburgess
 *
 */
class JdbcTemplateVueDetectionFileProcessorService extends VueDetectionFileProcessorService
{
    static transactional = true

    def dataSource
    def grailsApplication
    def jdbcTemplateDetectionFactoryService

    protected int getBatchSize()
    {
        return 500
    }

    void process(ReceiverDownloadFile downloadFile) throws FileProcessingException
    {
        super.process(downloadFile)

        promoteProvisional()
    }

    protected void startBatch(context)
    {
        log.debug("Start batch")

        // Create lists
        context.detectionBatch = new ArrayList<Map>()
    }

    protected void endBatch(context)
    {
        if (context.detectionBatch.isEmpty())
        {
            log.warn("Detection batch empty.")
        }
        else
        {
            log.debug("Start mark duplicates...")
            markDuplicates(context)
            log.debug("End mark duplicates.")

            log.debug("End batch, inserting detections...")
            insertDetections(context)
        }

        super.endBatch(context)
    }

    private void markDuplicates(context)
    {
        context.detectionBatch.each
        {
            newDetection ->

            def c = ValidDetection.createCriteria()
            def duplicateCount = c.get()
            {
                projections
                {
                    rowCount()
                }

                and
                {
                    eq("receiverName", newDetection.receiverName)
                    eq("timestamp", newDetection.timestamp)
                    eq("transmitterId", newDetection.transmitterId)
                }
            }

            if (duplicateCount > 0)
            {
                newDetection["clazz"] = "au.org.emii.aatams.detection.InvalidDetection"
                newDetection["message"] = "Invalid detection: duplicate"
                newDetection["reason"] = InvalidDetectionReason.DUPLICATE
            }
        }
    }

    private void insertDetections(context)
    {
        def insertStatementList = []

        context.detectionBatch.each
        {
            if (it.clazz == "au.org.emii.aatams.detection.ValidDetection")
            {
                insertStatementList += ValidDetection.toSqlInsert(it)
            }
            else if (it.clazz == "au.org.emii.aatams.detection.InvalidDetection")
            {
                insertStatementList += InvalidDetection.toSqlInsert(it)
            }
            else
            {
                assert(false): "Unknown detection class: " + it.clazz
            }
        }

        batchUpdate(insertStatementList.toArray(new String[0]))
    }

    void batchUpdate(String[] statements)
    {
        log.debug("Inserting " + statements.size() + " records...")
        JdbcTemplate insert = new JdbcTemplate(dataSource)
        insert.batchUpdate(statements)
        log.debug("Batch successfully inserted")
    }

    void processSingleRecord(downloadFile, map, context) throws FileProcessingException
    {
        def detection = jdbcTemplateDetectionFactoryService.newDetection(downloadFile, map)
        context.detectionBatch.addAll(detection)
    }

    private void promoteProvisional()
    {
        def sql = Sql.newInstance(dataSource)

        // Update statistics.
        def provDetsCount = sql.firstRow(String.valueOf("select count(*) from ${QueryBuilder.getViewName()} where provisional = true;")).count
        def numValidDets = Statistics.findByKey("numValidDetections")
        numValidDets.value += provDetsCount

        // Clear 'provisional' flag on detections.
        sql.execute("update valid_detection set provisional = false where provisional = true")
    }
}
