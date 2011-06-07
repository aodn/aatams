package au.org.emii.aatams.upload

import com.lucastex.grails.fileuploader.UFile

class FileProcessorService extends AbstractFileProcessorService
{
    static transactional = true

    def vueFileProcessorService

    void process(fileId)
    {
        log.info "Start of processing, file ID: " + fileId
        
        UFile file = UFile.get(Long.parseLong(fileId))

        // Create a ProcessedUploadFile record.
        ProcessedUploadFile processedFile = new ProcessedUploadFile()
        processedFile.uFile = file
        processedFile.status = FileProcessingStatus.PENDING
        
        if (!processedFile.save())
        {
            processedFile.errors.each
            {
                log.error(it)
            }
            
            throw new FileProcessingException("Error saving ProcessedUploadFile instance.")
        }
        
        try
        {
            if (vueFileProcessorService.isParseable(Long.parseLong(fileId)))
            {
                // Delegate to VUE Processor...
                vueFileProcessorService.process(Long.parseLong(fileId))

            }
            else
            {
                // No processing - just update the status.
                processedFile.status = FileProcessingStatus.PROCESSED
                processedFile.save()
            }

            log.info "Finished processing."
        }
        catch (FileProcessingException e)
        {
            log.error("Error processing file", e)
            processedFile.status = FileProcessingStatus.ERROR
            throw e
        }
    }
    
    boolean isParseable(fileId)
    {
        return true
    }
}
