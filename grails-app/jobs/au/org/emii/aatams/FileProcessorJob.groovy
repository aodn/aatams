package au.org.emii.aatams

class FileProcessorJob 
{
	// Avoid concurrent modification exceptions (#1466).
	def concurrent = false
	
	def fileProcessorService
	
	// This job is triggered dynamically from the receiver download file controller.
	static triggers = {}
	
	def execute(context)
	{
		def receiverDownloadFileInstance
		
        try
        {
            receiverDownloadFileInstance = ReceiverDownloadFile.get(context.mergedJobDataMap.get('downloadFileId'))
            fileProcessorService.process(context.mergedJobDataMap.get('downloadFileId'), 
                                         context.mergedJobDataMap.get('file'),
                                         context.mergedJobDataMap.get('showLink'))
        }
        catch (FileProcessingException e)
        {
            log.error(e)
            
            receiverDownloadFileInstance?.status = FileProcessingStatus.ERROR
            receiverDownloadFileInstance?.errMsg = e.getMessage()
            receiverDownloadFileInstance?.save()
        }
        catch (Throwable t)
        {
            log.error(t)
            
            receiverDownloadFileInstance?.status = FileProcessingStatus.ERROR
            receiverDownloadFileInstance?.errMsg = "System Error - Contact eMII" //t.getMessage()
            receiverDownloadFileInstance?.save()
        }
	}
}
