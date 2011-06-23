package au.org.emii.aatams

import org.codehaus.groovy.grails.commons.*
import org.springframework.web.multipart.MultipartFile

class FileProcessorService extends AbstractFileProcessorService
{
    static transactional = true

    def vueFileProcessorService

    void process(receiverDownload, MultipartFile file)
    {
//        ReceiverDownload receiverDownload = ReceiverDownload.get(receiverDownloadId)
        assert(receiverDownload != null): "receiverDownload cannot be null"
        
        if (!file.isEmpty())
        {
            log.info("Processing receiver download, id: " + receiverDownload?.id)

            // Save the file to disk.
            def path = getPath(receiverDownload)
            String fullPath = path + File.separator + file.getOriginalFilename()
            log.debug("Saving file, full path: " + fullPath)
            File outFile = new File(fullPath)
            
            // Create the directory structure first...
            outFile.mkdirs()
            
            // ... then transfer the data.
            file.transferTo(outFile)

            // Create a new ReceiverDownloadFile instance and add it to the given
            // ReceiverDownload.
            ReceiverDownloadFile downloadFile = new ReceiverDownloadFile(fullPath, file.getOriginalFilename())
            downloadFile.save()
            
            try
            {
                if (vueFileProcessorService.isParseable(downloadFile))
                {
                    // Delegate to VUE Processor...
                    vueFileProcessorService.process(downloadFile)

                }
                else
                {
                    // No processing - just update the status.
                    downloadFile.status = FileProcessingStatus.PROCESSED
                    downloadFile.save()
                }

                log.info "Finished processing."
            }
            catch (FileProcessingException e)
            {
                log.error("Error processing file", e)
                downloadFile.status = FileProcessingStatus.ERROR
                downloadFile.errMsg = String.valueOf(e)
                downloadFile.save()

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
