package au.org.emii.aatams

import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent
import org.apache.shiro.SecurityUtils

class AuditLogEventController extends org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEventController {

    private static String TARGET = 'org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEventController.TARGET'

    def list = {

        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)

        if (!params.sort && ! params.order) {
            params.sort = 'dateCreated'
            params.order = 'desc'
        }

        def result
        if (SecurityUtils.subject.hasRole("SysAdmin"))
        {
            result = [auditLogEventInstanceList: AuditLogEvent.list(params), auditLogEventInstanceTotal: AuditLogEvent.count()]
        }
        else {
            Person user = Person.get(SecurityUtils.subject.principal)
            assert(user)

            result = [auditLogEventInstanceList: AuditLogEvent.findAllByActor(user.username, params),
             auditLogEventInstanceTotal: AuditLogEvent.findAllByActor(user.username).size()]
        }

        insertActor(result)

        return result
    }

    def insertActor(result) {
        result.auditLogEventInstanceList.each { event ->
            def actor = event.actor == 'system' ? null : event.actor
            event.metaClass.person = Person.get(actor)
        }
    }
}
