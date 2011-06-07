package au.org.emii.aatams.upload

import com.lucastex.grails.fileuploader.UFile

abstract class AbstractFileProcessorService 
{
    static transactional = true

    abstract boolean isParseable(fileId)
    
    abstract void process(fileId) throws FileProcessingException
    
    UFile getUFile(Long fileId)
    {
        UFile file = UFile.get(fileId)
        assert(file != null): "No file with ID: " + fileId
        
        return file
    }
    
    ProcessedUploadFile getFile(Long fileId)
    {
        UFile file = getUFile(fileId)
        
        ProcessedUploadFile puFile = ProcessedUploadFile.findByUFile(file)
        assert(puFile != null): "No processed upload file with ufile ID: " + fileId
        
        return puFile
    }
}
