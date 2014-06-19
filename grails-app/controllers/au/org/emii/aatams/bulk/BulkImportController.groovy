package au.org.emii.aatams.bulk

import au.org.emii.aatams.Organisation
import org.springframework.web.multipart.MultipartFile
import org.joda.time.DateTime

class BulkImportController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def bulkImportService
    
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [bulkImportInstanceList: BulkImport.list(params), bulkImportInstanceTotal: BulkImport.count()]
    }

    def create = {
        def bulkImportInstance = new BulkImport()
        bulkImportInstance.properties = params
        return [bulkImportInstance: bulkImportInstance]
    }

    def save = 
    {
        def bulkImportInstance = new BulkImport(params)
        
        def fileMap = request.getFileMap()
        
        if (fileMap.size() != 1)
        {
            // Error.
            flash.error = "Number of posted files must be exactly one, you posted: " + fileMap.size()
            render(view: "create", model: [bulkImportInstance: bulkImportInstance])
        }
        else
        {
            def multipartFile = (fileMap.values() as List)[0]
            bulkImportInstance.organisation = Organisation.findByNameAndDepartment('CSIRO', 'CMAR Hobart')
            bulkImportInstance.importStartDate = new DateTime()
            bulkImportInstance.status = BulkImportStatus.IN_PROGRESS
            bulkImportInstance.filename = multipartFile.getOriginalFilename()
            bulkImportInstance.save(flush: true, failOnError: true)
            
            BulkImportJob.triggerNow([bulkImportId: bulkImportInstance.id, multipartFile: multipartFile])
            
            redirect(action: "show", id: bulkImportInstance.id)
        }
    }
    
    def show = {
        def bulkImportInstance = BulkImport.get(params.id)
        if (!bulkImportInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bulkImport.label', default: 'BulkImport'), params.id])}"
            redirect(action: "list")
        }
        else {
            [bulkImportInstance: bulkImportInstance]
        }
    }

    def edit = {
        def bulkImportInstance = BulkImport.get(params.id)
        if (!bulkImportInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bulkImport.label', default: 'BulkImport'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [bulkImportInstance: bulkImportInstance]
        }
    }

    def update = {
        def bulkImportInstance = BulkImport.get(params.id)
        if (bulkImportInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (bulkImportInstance.version > version) {
                    
                    bulkImportInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'bulkImport.label', default: 'BulkImport')] as Object[], "Another user has updated this BulkImport while you were editing")
                    render(view: "edit", model: [bulkImportInstance: bulkImportInstance])
                    return
                }
            }
            bulkImportInstance.properties = params
            if (!bulkImportInstance.hasErrors() && bulkImportInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'bulkImport.label', default: 'BulkImport'), bulkImportInstance.id])}"
                redirect(action: "show", id: bulkImportInstance.id)
            }
            else {
                render(view: "edit", model: [bulkImportInstance: bulkImportInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bulkImport.label', default: 'BulkImport'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def bulkImportInstance = BulkImport.get(params.id)
        if (bulkImportInstance) {
            try {
                bulkImportInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'bulkImport.label', default: 'BulkImport'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'bulkImport.label', default: 'BulkImport'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bulkImport.label', default: 'BulkImport'), params.id])}"
            redirect(action: "list")
        }
    }
}
