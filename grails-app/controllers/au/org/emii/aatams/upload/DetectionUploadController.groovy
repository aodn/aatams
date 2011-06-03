package au.org.emii.aatams.upload

import com.lucastex.grails.fileuploader.UFile

class DetectionUploadController 
{
    def fileProcessorService
    
    def index = 
    { 
        [files: UFile.list(), params:params]
    }
    
    def delete =
    {
        def ufile = UFile.get(params.id)
        ufile.delete()
        redirect action:index
    }
    
    def startProcessing =
    {
        log.info "Processing file with id=${params.ufileId}..."
        def fileId = "${params.ufileId}"
        
        // Kick off processing here.
        runAsync
        {
            fileProcessorService.process(fileId)
        }
        
        redirect(action: "index", params: params)
    }
    
    def error =
    {
        log.error "Error uploading file."
        redirect(action: "index", params: params)
    }
}
