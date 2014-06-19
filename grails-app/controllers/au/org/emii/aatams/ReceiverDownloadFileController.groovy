package au.org.emii.aatams

import org.codehaus.groovy.grails.commons.*
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.multipart.MultipartFile
import org.apache.shiro.SecurityUtils
import grails.converters.JSON

class ReceiverDownloadFileController 
{
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
        def receiverDownloadFileInstance = new ReceiverDownloadFile(type: params.type)

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
            MultipartFile file = (fileMap.values() as List)[0]
            receiverDownloadFileInstance.initialiseForProcessing(file.getOriginalFilename())
            receiverDownloadFileInstance.save(flush: true, failOnError: true)
            
            flash.message = "${message(code: 'default.processing.receiverUpload.message')}"
            
            // Define this in web thread, as it fails if done async.
            def downloadFileId = receiverDownloadFileInstance.id
            def showLink = createLink(action:'show', id:downloadFileId, absolute:true)

            FileProcessorJob.triggerNow([downloadFileId: downloadFileId, file: file, showLink: showLink])
            
            redirect(action: "show", id: downloadFileId)
        }
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
    
    def status = 
    {
        ReceiverDownloadFile download = ReceiverDownloadFile.get(params.id)
        
        def receiverDownloadFileInstance = ReceiverDownloadFile.get(params.id)
        if (!receiverDownloadFileInstance)
        {
            response.status = 404
            render([error: [message: "Unknown receiverDownloadFile id: ${params.id}"]] as JSON)
        }
        else
        {
            render([status: download.status, percentComplete: download.percentComplete] as JSON)
        }
    }
}
