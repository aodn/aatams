package au.org.emii.aatams.bulk

class BulkImportRecordController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
        [bulkImportRecordInstanceList: BulkImportRecord.list(params), bulkImportRecordInstanceTotal: BulkImportRecord.count()]
    }

    def create = {
        def bulkImportRecordInstance = new BulkImportRecord()
        bulkImportRecordInstance.properties = params
        return [bulkImportRecordInstance: bulkImportRecordInstance]
    }

    def save = {
        def bulkImportRecordInstance = new BulkImportRecord(params)
        if (bulkImportRecordInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'bulkImportRecord.label', default: 'BulkImportRecord'), bulkImportRecordInstance.id])}"
            redirect(action: "show", id: bulkImportRecordInstance.id)
        }
        else {
            render(view: "create", model: [bulkImportRecordInstance: bulkImportRecordInstance])
        }
    }

    def show = {
        def bulkImportRecordInstance = BulkImportRecord.get(params.id)
        if (!bulkImportRecordInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bulkImportRecord.label', default: 'BulkImportRecord'), params.id])}"
            redirect(action: "list")
        }
        else {
            [bulkImportRecordInstance: bulkImportRecordInstance]
        }
    }

    def edit = {
        def bulkImportRecordInstance = BulkImportRecord.get(params.id)
        if (!bulkImportRecordInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bulkImportRecord.label', default: 'BulkImportRecord'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [bulkImportRecordInstance: bulkImportRecordInstance]
        }
    }

    def update = {
        def bulkImportRecordInstance = BulkImportRecord.get(params.id)
        if (bulkImportRecordInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (bulkImportRecordInstance.version > version) {
                    
                    bulkImportRecordInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'bulkImportRecord.label', default: 'BulkImportRecord')] as Object[], "Another user has updated this BulkImportRecord while you were editing")
                    render(view: "edit", model: [bulkImportRecordInstance: bulkImportRecordInstance])
                    return
                }
            }
            bulkImportRecordInstance.properties = params
            if (!bulkImportRecordInstance.hasErrors() && bulkImportRecordInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'bulkImportRecord.label', default: 'BulkImportRecord'), bulkImportRecordInstance.id])}"
                redirect(action: "show", id: bulkImportRecordInstance.id)
            }
            else {
                render(view: "edit", model: [bulkImportRecordInstance: bulkImportRecordInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bulkImportRecord.label', default: 'BulkImportRecord'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def bulkImportRecordInstance = BulkImportRecord.get(params.id)
        if (bulkImportRecordInstance) {
            try {
                bulkImportRecordInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'bulkImportRecord.label', default: 'BulkImportRecord'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'bulkImportRecord.label', default: 'BulkImportRecord'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bulkImportRecord.label', default: 'BulkImportRecord'), params.id])}"
            redirect(action: "list")
        }
    }
}
