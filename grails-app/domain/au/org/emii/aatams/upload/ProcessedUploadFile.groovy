package au.org.emii.aatams.upload

import com.lucastex.grails.fileuploader.UFile

/**
 * Adds a processing status to uploaded files (as processing may take some 
 * time).
 */
class ProcessedUploadFile
{
    static belongsTo = [uFile: UFile]
    
    FileProcessingStatus status
    
    String errMsg
    
    String toString()
    {
        return super.toString + ", status: " + String.valueOf(status)
    }
}
