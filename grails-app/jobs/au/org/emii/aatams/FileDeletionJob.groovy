package au.org.emii.aatams

class FileDeletionJob {

    static triggers = {}

    def execute(context) {

        def receiverDownloadFileInstance = context.mergedJobDataMap.get('receiverDownloadFileInstance')

        try {
            receiverDownloadFileInstance.delete(flush: true)
        }
        catch (FileProcessingException e) {
            _logException(receiverDownloadFileInstance, e, e.getMessage())
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
