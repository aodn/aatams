package au.org.emii.aatams

import org.codehaus.groovy.grails.commons.*
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.multipart.MultipartFile
import org.apache.shiro.SecurityUtils

class ReceiverDownloadFileController 
{
	def dataSource
    def fileProcessorService
    
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
        [receiverDownloadFileInstanceList: ReceiverDownloadFile.list(params), receiverDownloadFileInstanceTotal: ReceiverDownloadFile.count()]
    }

    def create()  
    {
        def receiverDownloadFileInstance = new ReceiverDownloadFile()
        receiverDownloadFileInstance.properties = params
        
        return [receiverDownloadFileInstance: receiverDownloadFileInstance]
    }

    def createDetections =  
    {
        return create()
    }

    def createEvents =  
    {
        return create()
    }

    def save = 
    {
        log.debug("Processing receiver export, params: " + params)
        def receiverDownloadFileInstance = new ReceiverDownloadFile(params)

        // Save the file to disk.
        def fileMap = request.getFileMap()
        if (fileMap.size() != 1)
        {
            // Error.
            flash.error = "Number of posted files must be exactly one, you posted: " + fileMap.size()
            render(view: "create", model: [receiverDownloadFileInstance: receiverDownloadFileInstance])
        }
        else
        {
            receiverDownloadFileInstance.errMsg = ""
            receiverDownloadFileInstance.importDate = new Date()
            receiverDownloadFileInstance.status = FileProcessingStatus.PROCESSING
            receiverDownloadFileInstance.requestingUser = Person.findByUsername(SecurityUtils.getSubject().getPrincipal())
            
            MultipartFile file = (fileMap.values() as List)[0]
            receiverDownloadFileInstance.name = file.getOriginalFilename()
            receiverDownloadFileInstance.path = String.valueOf(System.currentTimeMillis())
            receiverDownloadFileInstance.save()

            def path = getPath(receiverDownloadFileInstance)
            String fullPath = path + File.separator + file.getOriginalFilename()
            receiverDownloadFileInstance.path = fullPath
            receiverDownloadFileInstance.save(flush:true, failOnError:true)

            flash.message = "${message(code: 'default.processing.receiverUpload.message')}"
            
            // Define this in web thread, as it fails if done async.
            def downloadFileId = receiverDownloadFileInstance.id
            def showLink = createLink(action:'show', id:downloadFileId, absolute:true)
            
            runAsync
            {
                try
                {
                    receiverDownloadFileInstance = ReceiverDownloadFile.get(downloadFileId)
                    fileProcessorService.process(downloadFileId, 
                                                 file,
                                                 showLink)
					
					runAsync
					{
						refreshDetectionCountPerStationView()
					}
                }
                catch (FileProcessingException e)
                {
                    log.error(e)
                    
                    receiverDownloadFileInstance.status = FileProcessingStatus.ERROR
                    receiverDownloadFileInstance.errMsg = e.getMessage()
                    receiverDownloadFileInstance.save()
                }
                catch (Throwable t)
                {
                    log.error(t)
                    
                    receiverDownloadFileInstance.status = FileProcessingStatus.ERROR
                    receiverDownloadFileInstance.errMsg = "System Error - Contact eMII" //t.getMessage()
                    receiverDownloadFileInstance.save()
                }
            }
        
            redirect(action: "show", id: downloadFileId)
        }
    }

	private void refreshDetectionCountPerStationView()
	{
		long startTime = System.currentTimeMillis()
		log.info("Refreshing 'detection counts' materialized view...")
		doRefreshDetectionCountView()
		long endTime = System.currentTimeMillis()
		log.info("'detection counts' materialized view refreshed, time taken (ms): " + (endTime - startTime))
	}
	
	private void doRefreshDetectionCountView()
	{
		JdbcTemplate refreshStatement = new JdbcTemplate(dataSource)
		refreshStatement.execute('''SELECT refresh_matview('detection_count_per_station_mv');''')
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
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), receiverDownloadFileInstance.toString()])}"
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
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), receiverDownloadFileInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), receiverDownloadFileInstance.toString()])}"
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
     *      <basepath>/<downloadFile.id>.
     */
    String getPath(ReceiverDownloadFile downloadFile)
    {
        // Save the file to disk.
        def path = grailsApplication.config.fileimport.path
        log.debug("File import config: " + grailsApplication.config.fileimport)
        if (!path.endsWith(File.separator))
        {
            path = path + File.separator
        }
        
        path += downloadFile.id
    }
}
