package au.org.emii.aatams

import org.springframework.web.multipart.MultipartFile

abstract class AbstractFileProcessorService 
{
    static transactional = true

    abstract boolean isParseable(downloadFile)
    
    abstract void process(receiverDownload, MultipartFile file) throws FileProcessingException
}
