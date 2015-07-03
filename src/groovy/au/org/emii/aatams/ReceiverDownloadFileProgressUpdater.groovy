package au.org.emii.aatams

import au.org.emii.aatams.ReceiverDownloadFileProgress
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ReceiverDownloadFileProgressUpdater {
    static final Logger log = LoggerFactory.getLogger(ReceiverDownloadFileProgressUpdater.class)

    def receiverDownloadFile
    def recordTotal
    def startTime
    def percentComplete

    ReceiverDownloadFileProgressUpdater(receiverDownloadFile, recordTotal) {
        this.receiverDownloadFile = receiverDownloadFile
        this.recordTotal = recordTotal
    }

    def start() {
        startTime = System.currentTimeMillis()
        percentComplete = -1
    }

    def updateProgress(recordsCount) {
        def progress = (int) (recordsCount / recordTotal * 100)
        if (progress > percentComplete) {
            percentComplete = progress
            logProgress(recordsCount)
            saveReceiverDownloadFileProgress()
        }
    }

    def logStatistics(batchSize) {
        long elapsedTime = System.currentTimeMillis() - startTime
        log.info("Batch details, size: $batchSize, time per record (ms) : ${averageTimePerRecord(recordTotal)}")
        log.info("Records processed: ($recordTotal) in (ms): $elapsedTime")
    }

    private def progressMessage(recordsCount) {
        return "Download file processing, id: ${receiverDownloadFile.id}, progress: ${percentComplete}%, average time per record: ${averageTimePerRecord(recordsCount)}ms"
    }

    private def averageTimePerRecord(recordsCount) {
        return (System.currentTimeMillis() - startTime) / recordsCount
    }

    private def logProgress(recordsCount) {
        if ((percentComplete % 10) == 0) {
            log.info(progressMessage(recordsCount))
        }
        else {
            log.debug(progressMessage(recordsCount))
        }
    }

    private void saveReceiverDownloadFileProgress() {
        ReceiverDownloadFileProgress.withNewTransaction {
            ReceiverDownloadFileProgress progress = ReceiverDownloadFileProgress.findByReceiverDownloadFile(receiverDownloadFile)

            log.debug("Updating progress, percentComplete: ${percentComplete}")
            progress?.percentComplete = percentComplete
            progress?.save(failOnError: true)
        }
    }
}
