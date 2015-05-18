package au.org.emii.aatams

import org.codehaus.groovy.grails.commons.*
import org.springframework.web.multipart.MultipartFile
import org.codehaus.groovy.grails.commons.GrailsApplication

class FileProcessorService
{
    static transactional = true

    def vueDetectionFileProcessorService
    def jdbcTemplateVueEventFileProcessorService

    def grailsApplication
    def mailService

    /**
     * When uploading CSV, firefox and chrome report "text/csv", IE reports "text/plain".
     */
    private static final CSV_CONTENT_TYPES = ["text/csv", "text/plain"]

    void process(receiverDownloadFileId, MultipartFile file, showLink) throws FileProcessingException
    {
        log.debug("Processing receiver export, download file ID: " + receiverDownloadFileId + ", content type: " + file.getContentType())

        def receiverDownloadFile = ReceiverDownloadFile.get(receiverDownloadFileId)
        assert(receiverDownloadFile != null): "receiverDownloadFile cannot be null"

        validateContent(receiverDownloadFile, file)

        // Save the file to disk.
        saveToDiskAndProcess(receiverDownloadFile, file,showLink)
    }

    private void saveToDiskAndProcess(ReceiverDownloadFile receiverDownloadFile, MultipartFile file, showLink) throws FileProcessingException
    {
        log.debug("Creating file at path: " + receiverDownloadFile.path)
        File outFile = new File(receiverDownloadFile.path)

        // Create the directory structure first...
        outFile.mkdirs()

        // ... then transfer the data.
        file.transferTo(outFile)

        try
        {
            switch (receiverDownloadFile.type)
            {
                case ReceiverDownloadFileType.DETECTIONS_CSV:

                    // Delegate to VUE Detection Processor...
                    log.debug("Delegating to VUE detection file processor...")
                    vueDetectionFileProcessorService.process(receiverDownloadFile)

                    break;

                case ReceiverDownloadFileType.EVENTS_CSV:

                    // Delegate to VUE Event Processor...
                    log.debug("Processing events...")
                    jdbcTemplateVueEventFileProcessorService.process(receiverDownloadFile)
                    break;

                default:

                    // No processing - just update the status.
                    receiverDownloadFile.status = FileProcessingStatus.PROCESSED
                    receiverDownloadFile.save()
            }

            log.info "Finished processing."
        }
        catch (Throwable e)
        {
            log.error("Error processing file", e)
            receiverDownloadFile.status = FileProcessingStatus.ERROR
            receiverDownloadFile.errMsg = String.valueOf(e)
            receiverDownloadFile.save()

            throw e
        }
        finally
        {
            sendNotification(receiverDownloadFile, showLink)
        }
    }

    boolean isParseable(downloadFile)
    {
        return true
    }

    def sendNotification(receiverDownloadFile, showLink)
    {
        mailService.sendMail
        {
            // Required to avoid hibernate exception, since session is flushed and cleared above.
            receiverDownloadFile = ReceiverDownloadFile.get(receiverDownloadFile.id)
            to receiverDownloadFile?.requestingUser?.emailAddress
            bcc grailsApplication.config.grails.mail.adminEmailAddress
            from grailsApplication.config.grails.mail.systemEmailAddress
            subject "AATAMS receiver export " + receiverDownloadFile?.name + ": " + receiverDownloadFile?.status
            body "AATAMS receiver export " + receiverDownloadFile?.name \
                 + ": " + receiverDownloadFile?.status + "\n\n" \
                 + showLink
        }
    }

    private void validateContent(ReceiverDownloadFile receiverDownloadFile, MultipartFile file) throws FileProcessingException
    {
        if (file.isEmpty())
        {
            def errMsg = "File is empty"
            log.error(errMsg)
            throw new FileProcessingException(errMsg)
        }

        log.debug("Validating file with content type: " + file.getContentType())

        // See: http://blog.futtta.be/2009/08/24/http-upload-mime-type-hell/ as to why we're no longer validating mime-type

        if (!file.getOriginalFilename().endsWith(ReceiverDownloadFileType.getExtension(receiverDownloadFile.type)))
        {
            def errMsg = "Invalid " + ReceiverDownloadFileType.getCategory(receiverDownloadFile.type) + \
                         " filename (" + file.getOriginalFilename() + ") - must have extension \"." + ReceiverDownloadFileType.getExtension(receiverDownloadFile.type) + "\"."
            throw new FileProcessingException(errMsg)
        }
    }
}
