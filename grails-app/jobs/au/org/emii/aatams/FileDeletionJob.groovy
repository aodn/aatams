package au.org.emii.aatams

class FileDeletionJob {

    static triggers = {}

    def execute(context) {

        def receiverDownloadFileId = context.mergedJobDataMap.get('receiverDownloadFileId')

        def receiverDownloadFileInstance = ReceiverDownloadFile.get(receiverDownloadFileId)

        try {
            receiverDownloadFileInstance.delete(flush: true)
        }
        catch (Throwable t) {
            _logException(receiverDownloadFileInstance, t, "System Error - Contact eMII")
        }
    }

    def _logException(receiverDownloadFileInstance, e, message) {

        log.error(message, e)

        receiverDownloadFileInstance?.status = FileProcessingStatus.ERROR
        receiverDownloadFileInstance?.errMsg = message
        receiverDownloadFileInstance?.save()
    }
}
