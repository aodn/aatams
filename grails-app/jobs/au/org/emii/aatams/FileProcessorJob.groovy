package au.org.emii.aatams

class FileProcessorJob 
{
	def fileProcessorService
	
	// This job is triggered dynamically from the receiver download file controller.
	static triggers = {}
	
	def execute(context)
	{
        try
        {
            fileProcessorService.process(context.mergedJobDataMap.get('downloadFileId'), 
                                         context.mergedJobDataMap.get('file'),
                                         context.mergedJobDataMap.get('showLink'))
        }
        catch (FileProcessingException e)
        {
            _logException(context, e, e.getMessage())
        }
        catch (Throwable t)
        {
            _logException(context, t, "System Error - Contact eMII")
        }
    }
    
    def _logException(context, e, message)
    {
        log.error("", e)
        
        def receiverDownloadFileInstance = ReceiverDownloadFile.get(context.mergedJobDataMap.get('downloadFileId'))
        
        receiverDownloadFileInstance?.status = FileProcessingStatus.ERROR
        receiverDownloadFileInstance?.errMsg = message
        receiverDownloadFileInstance?.save()
    }
}
