package au.org.emii.aatams

import org.codehaus.groovy.grails.commons.*
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.multipart.MultipartFile
import org.apache.shiro.SecurityUtils
import grails.converters.JSON

import groovy.xml.MarkupBuilder


class ReceiverDownloadFileController {
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
        if (!params.sort) {
            params.sort = "importDate"
            params.order = "desc"
        }
        [receiverDownloadFileInstanceList: ReceiverDownloadFile.list(params), receiverDownloadFileInstanceTotal: ReceiverDownloadFile.count()]
    }

    def create() {
        def receiverDownloadFileInstance = new ReceiverDownloadFile()
        receiverDownloadFileInstance.properties = params

        return [receiverDownloadFileInstance: receiverDownloadFileInstance]
    }

    def createDetections = {
        return create()
    }

    def createEvents = {
        return create()
    }

    def save = {
        log.debug("Processing receiver export, params: " + params)
        def receiverDownloadFileInstance = new ReceiverDownloadFile(type: params.type)

        // Save the file to disk.
        def fileMap = request.getFileMap()
        if (fileMap.size() != 1) {
            withFormat {
                html {
                    flash.error = "Number of posted files must be exactly one, you posted: " + fileMap.size()
                    render(view: "create", model: [receiverDownloadFileInstance: receiverDownloadFileInstance])
                }
                xml {
                    def xmlContent = new StringWriter()
                    def xmlBuilder = new MarkupBuilder(xmlContent)
                    xmlBuilder.mkp.xmlDeclaration(version: "1.0", encoding: "utf-8")

                    xmlBuilder.receiverDownloadFileSaveRequest(
                        errMsg: 'No file uploaded'
                    )
                    response.status = 400

                    render(contentType: 'text/xml', text: xmlContent.toString())
                }
            }
        }
        else {
            _processRxrDownload(receiverDownloadFileInstance)
            redirect(action: "show", id: receiverDownloadFileInstance.id, params: params)
        }
    }

    def _processRxrDownload(receiverDownloadFileInstance) {
        MultipartFile file = (request.fileMap.values() as List)[0]
        receiverDownloadFileInstance.initialiseForProcessing(file.getOriginalFilename())
        receiverDownloadFileInstance.save(flush: true, failOnError: true)

        flash.message = "${message(code: 'default.processing.receiverUpload.message')}"

        // Define this in web thread, as it fails if done async.
        def downloadFileId = receiverDownloadFileInstance.id
        def showLink = createLink(action:'show', id:downloadFileId, absolute:true)

        FileProcessorJob.triggerNow([downloadFileId: downloadFileId, file: file, showLink: showLink])
    }

    def show = {
        def receiverDownloadFileInstance = ReceiverDownloadFile.get(params.id)

        if (!receiverDownloadFileInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), params.id])}"
            redirect(action: "list")
        }
        else {
            withFormat {
                html {
                    [receiverDownloadFileInstance: receiverDownloadFileInstance]
                }
                xml {
                    render receiverDownloadFileInstance.toXml()
                }
            }
        }
    }

    def delete = {

        def receiverDownloadFileInstance = ReceiverDownloadFile.get(params.id)
        if (receiverDownloadFileInstance) {

            receiverDownloadFileInstance.status = FileProcessingStatus.DELETING
            receiverDownloadFileInstance.save(flush: true)

            FileDeletionJob.triggerNow([receiverDownloadFileId: receiverDownloadFileInstance.id])

            flash.message = "${message(code: 'default.deleting.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), receiverDownloadFileInstance.toString()])}"
            redirect(action: "list")
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), params.id])}"
            redirect(action: "list")
        }
    }

    /**
     * User has chosen to download the actual file.
     */
    def download = {
        def receiverDownloadFileInstance = ReceiverDownloadFile.get(params.id)
        if (!receiverDownloadFileInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile'), params.id])}"
            redirect(action: "list")
        }
        else {
            def file = new File(receiverDownloadFileInstance.path)
            response.setContentType("application/octet-stream")
            response.setHeader("Content-disposition", "attachment;filename=${file.getName()}")

            response.outputStream << file.newInputStream() // Performing a binary stream copy
            [receiverDownloadFileInstance: receiverDownloadFileInstance]
        }
    }

    def status = {
        ReceiverDownloadFile download = ReceiverDownloadFile.get(params.id)

        def receiverDownloadFileInstance = ReceiverDownloadFile.get(params.id)
        if (!receiverDownloadFileInstance) {
            response.status = 404
            render([error: [message: "Unknown receiverDownloadFile id: ${params.id}"]] as JSON)
        }
        else {
            render([status: download.status, percentComplete: download.percentComplete] as JSON)
        }
    }
}
