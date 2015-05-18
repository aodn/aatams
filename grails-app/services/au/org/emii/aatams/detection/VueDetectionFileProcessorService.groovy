package au.org.emii.aatams.detection

import au.org.emii.aatams.AbstractBatchProcessor
import au.org.emii.aatams.FileProcessingException
import au.org.emii.aatams.ReceiverDownloadFile
import au.org.emii.aatams.ReceiverDownloadFileType

import org.springframework.jdbc.core.JdbcTemplate

/**
 * Process a file downloaded from receiver's VUE application.
 */
class VueDetectionFileProcessorService extends AbstractBatchProcessor
{
    def dataSource
    def detectionFactoryService
    def detectionNotificationService

    static transactional = true

    void processSingleRecord(downloadFile, map, context) throws FileProcessingException {
        context.detectionBatch.add(detectionFactoryService.newDetection(downloadFile, map))
    }

    void process(ReceiverDownloadFile downloadFile) throws FileProcessingException {
        super.process(downloadFile)

        detectionNotificationService.sendDetectionNotificationEmails(downloadFile)
    }

    protected int getBatchSize() {
        return 500
    }

    protected void startBatch(context) {
        log.debug("Start batch")

        // Create lists
        context.detectionBatch = new ArrayList<Map>()
    }

    protected void endBatch(context) {
        if (context.detectionBatch.isEmpty()) {
            log.warn("Detection batch empty.")
        }
        else {
            log.debug("End batch, inserting detections...")
            insertDetections(context)
        }

        super.endBatch(context)
    }

    private void insertDetections(context) {
        def insertStatementList = context.detectionBatch.collect {
            Detection.toSqlInsert(it)
        }

        batchUpdate(insertStatementList.toArray(new String[0]))
    }

    void batchUpdate(String[] statements) {
        log.debug("Inserting " + statements.size() + " records...")
        JdbcTemplate insert = new JdbcTemplate(dataSource)
        insert.batchUpdate(statements)
        log.debug("Batch successfully inserted")
    }
}
