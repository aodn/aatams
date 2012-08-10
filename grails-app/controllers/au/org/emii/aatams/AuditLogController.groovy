package au.org.emii.aatams

class AuditLogController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [auditLogInstanceList: AuditLog.list(params), auditLogInstanceTotal: AuditLog.count()]
    }
/**
    def show = {
        def auditLogInstance = AuditLog.get(params.id)
        if (!auditLogInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'auditLog.label', default: 'AuditLog'), params.id])}"
            redirect(action: "list")
        }
        else {
            [auditLogInstance: auditLogInstance]
        }
    }
*/
}
