package au.org.emii.aatams

import org.codehaus.groovy.grails.commons.*
import org.springframework.web.multipart.MultipartFile
import org.codehaus.groovy.grails.commons.GrailsApplication

class FileProcessorService
{
    static transactional = true

    def vueDetectionFileProcessorService
    def vueEventFileProcessorService
    
    def grailsApplication
    def mailService

//    void process(ReceiverDownloadFile receiverDownloadFile, MultipartFile file)
    void process(receiverDownloadFileId, MultipartFile file, showLink)
    {
        log.debug("Processing receiver export, download file ID: " + receiverDownloadFileId)
        
//        def receiverDownloadFileInstance = new ReceiverDownloadFile(params)
//        receiverDownloadFileInstance.receiverDownload = ReceiverDownload.get(params.downloadId)
//        receiverDownloadFileInstance.errMsg = ""
//        receiverDownloadFileInstance.importDate = new Date()
//        receiverDownloadFileInstance.status = FileProcessingStatus.PENDING
        
//        receiverDownloadFileInstance.save(flush:true, failOnError:true)

//        ReceiverDownload receiverDownload = ReceiverDownload.get(receiverDownloadId)
        
        def receiverDownloadFile = ReceiverDownloadFile.get(receiverDownloadFileId)
        assert(receiverDownloadFile != null): "receiverDownloadFile cannot be null"
        
        if (!file.isEmpty())
        {
            def receiverDownload = receiverDownloadFile?.receiverDownload
            assert (receiverDownload != null): "receiverDownload cannot be null"
            
            log.info("Adding file to receiver download, id: " + receiverDownload?.id)

            // Save the file to disk.
            def path = receiverDownloadFile.path
            File outFile = new File(path)
            
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
                        vueEventFileProcessorService.process(receiverDownloadFile)
                        break;
                    
                    default:

                        // No processing - just update the status.
                        receiverDownloadFile.status = FileProcessingStatus.PROCESSED
                        receiverDownloadFile.save()
                }

                log.info "Finished processing."
            }
//            catch (FileProcessingException e)
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
        else
        {
            log.warn("File is empty.")
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
            to receiverDownloadFile?.requestingUser?.emailAddress
            bcc grailsApplication.config.grails.mail.adminEmailAddress
            from grailsApplication.config.grails.mail.systemEmailAddress
            subject "AATAMS receiver export " + receiverDownloadFile?.name + ": " + receiverDownloadFile?.status
            body "AATAMS receiver export " + receiverDownloadFile?.name \
                 + ": " + receiverDownloadFile?.status + "\n\n" \
                 + showLink
        }
    }
}
