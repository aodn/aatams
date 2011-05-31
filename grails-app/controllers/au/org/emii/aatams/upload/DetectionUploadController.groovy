package au.org.emii.aatams.upload

import com.lucastex.grails.fileuploader.UFile

class DetectionUploadController 
{

    def index = 
    { 
        log.debug "Uploaded file with id=${params.ufileId}"
        [files: UFile.list(), params:params]
    }
    
    def delete =
    {
        def ufile = UFile.get(params.id)
        ufile.delete()
        redirect action:index
    }
}
