package au.org.emii.aatams

import org.codehaus.groovy.grails.commons.*
import org.springframework.web.multipart.MultipartFile

class ReceiverDownloadFileController 
{
    def fileProcessorService
    
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [receiverDownloadFileInstanceList: ReceiverDownloadFile.list(params), receiverDownloadFileInstanceTotal: ReceiverDownloadFile.count()]
    }

    def create = 
    {
        def receiverDownloadFileInstance = new ReceiverDownloadFile()
        receiverDownloadFileInstance.properties = params
        receiverDownloadFileInstance.receiverDownload = ReceiverDownload.get(params.downloadId)
        
        return [receiverDownloadFileInstance: receiverDownloadFileInstance, projectId:params.projectId]
    }

    def save = 
    {
        log.debug("Processing receiver export, params: " + params)

        // Save the file to disk.
        def fileMap = request.getFileMap()
        if (fileMap.size() != 1)
        {
            // Error.
            flash.error = "Number of posted files must be exactly one, you posted: " + fileMap.size()
        }
        else
        {
            def receiverDownload = ReceiverDownload.get(params.downloadId)

            def receiverDownloadFileInstance = new ReceiverDownloadFile(params)
            receiverDownloadFileInstance.receiverDownload = receiverDownload
            receiverDownloadFileInstance.errMsg = ""
            receiverDownloadFileInstance.importDate = new Date()
            receiverDownloadFileInstance.status = FileProcessingStatus.PROCESSING

            MultipartFile file = (fileMap.values() as List)[0]
            def path = getPath(receiverDownload)
            String fullPath = path + File.separator + file.getOriginalFilename()

            receiverDownloadFileInstance.path = fullPath
            receiverDownloadFileInstance.name = file.getOriginalFilename()

            receiverDownload.addToDownloadFiles(receiverDownloadFileInstance)
            receiverDownload.save(flush:true, failOnError:true)

            flash.message = "${message(code: 'default.processing.receiverUpload.message')}"

            runAsync
            {
                try
                {
                    fileProcessorService.process(receiverDownloadFileInstance.id, file)
                }
                catch (Throwable t)
                {
                    log.error(t)
                    
                    receiverDownloadFileInstance.status = FileProcessingStatus.ERROR
                    receiverDownloadFileInstance.errMsg = t.getMessage()
                    receiverDownloadFileInstance.save()
                }
            }
        }
        
        // Go back to receiver recovery edit screen.
        redirect(controller: "receiverRecovery", 
                 action: "edit", 
                 id: ReceiverDownload.get(params.downloadId).receiverRecovery?.id,
                 params:[projectId:params.projectId])
    }

    def show = {
        def receiverDownloadFileInstance = ReceiverDownloadFile.get(params.id)
        if (!receiverDownloadFileInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), params.id])}"
            redirect(action: "list")
        }
        else {
            [receiverDownloadFileInstance: receiverDownloadFileInstance]
        }
    }

    def edit = {
        def receiverDownloadFileInstance = ReceiverDownloadFile.get(params.id)
        if (!receiverDownloadFileInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [receiverDownloadFileInstance: receiverDownloadFileInstance]
        }
    }

    def update = {
        def receiverDownloadFileInstance = ReceiverDownloadFile.get(params.id)
        if (receiverDownloadFileInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (receiverDownloadFileInstance.version > version) {
                    
                    receiverDownloadFileInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile')] as Object[], "Another user has updated this ReceiverDownloadFile while you were editing")
                    render(view: "edit", model: [receiverDownloadFileInstance: receiverDownloadFileInstance])
                    return
                }
            }
            receiverDownloadFileInstance.properties = params
            if (!receiverDownloadFileInstance.hasErrors() && receiverDownloadFileInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), receiverDownloadFileInstance.id])}"
                redirect(action: "show", id: receiverDownloadFileInstance.id)
            }
            else {
                render(view: "edit", model: [receiverDownloadFileInstance: receiverDownloadFileInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def receiverDownloadFileInstance = ReceiverDownloadFile.get(params.id)
        if (receiverDownloadFileInstance) {
            try {
                receiverDownloadFileInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), params.id])}"
            redirect(action: "list")
        }
    }
    
    /**
     * User has chosen to download the actual file.
     */
    def download =
    {
        def receiverDownloadFileInstance = ReceiverDownloadFile.get(params.id)
        if (!receiverDownloadFileInstance) 
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), params.id])}"
            redirect(action: "list")
        }
        else 
        {
            def file = new File(receiverDownloadFileInstance.path)    
            response.setContentType("application/octet-stream")
            response.setHeader("Content-disposition", "attachment;filename=${file.getName()}")

            response.outputStream << file.newInputStream() // Performing a binary stream copy
            [receiverDownloadFileInstance: receiverDownloadFileInstance]
        }
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
