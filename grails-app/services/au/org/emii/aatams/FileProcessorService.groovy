package au.org.emii.aatams

import org.codehaus.groovy.grails.commons.*
import org.springframework.web.multipart.MultipartFile

class FileProcessorService extends AbstractFileProcessorService
{
    static transactional = true

    def vueDetectionFileProcessorService
    def vueEventFileProcessorService

    void process(ReceiverDownloadFile receiverDownloadFile, MultipartFile file)
    {
        log.debug "process()"
    
//        ReceiverDownload receiverDownload = ReceiverDownload.get(receiverDownloadId)
        assert(receiverDownloadFile != null): "receiverDownloadFile cannot be null"
        
        if (!file.isEmpty())
        {
            def receiverDownload = receiverDownloadFile?.receiverDownload
            assert (receiverDownload != null): "receiverDownload cannot be null"
            
            log.info("Adding file to receiver download, id: " + receiverDownload?.id)

            // Save the file to disk.
            def path = getPath(receiverDownload)
            String fullPath = path + File.separator + file.getOriginalFilename()
            log.info("Saving file, full path: " + fullPath)
            File outFile = new File(fullPath)
            
            // Create the directory structure first...
            outFile.mkdirs()
            
            // ... then transfer the data.
            file.transferTo(outFile)

            receiverDownloadFile.path = fullPath
            receiverDownloadFile.name = file.getOriginalFilename()
            
            receiverDownload.addToDownloadFiles(receiverDownloadFile)
            receiverDownload.save()
            
            try
            {
                switch (receiverDownloadFile.type)
                {
                    case ReceiverDownloadFileType.DETECTIONS_CSV:
                    
                        // Delegate to VUE Detection Processor...
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
            catch (FileProcessingException e)
            {
                log.error("Error processing file", e)
                receiverDownloadFile.status = FileProcessingStatus.ERROR
                receiverDownloadFile.errMsg = String.valueOf(e)
                receiverDownloadFile.save()

                throw e
            }
        }
    }
    
    boolean isParseable(downloadFile)
    {
        return true
    }
    
        
    /**
     *  Files are stored at: 
     *  
     *      <basepath>/<project>/<installation>/<station>/<receiver>/<filename>.
     */
    String getPath(ReceiverDownload download)
    {
        def config = ConfigurationHolder.config['fileimport']
        
        // Save the file to disk.
        def path = config.path
        if (!path.endsWith(File.separator))
        {
            path = path + File.separator
        }
        
        ReceiverRecovery recovery = download.receiverRecovery
        ReceiverDeployment deployment = recovery.deployment
        Receiver receiver = deployment.receiver
        InstallationStation station = download.receiverRecovery.deployment.station
        Installation installation = station.installation
        Project project = installation.project
        
        path += project.name + File.separator
        path += installation.name + File.separator
        path += station.name + File.separator
        path += receiver.codeName
    }
}
