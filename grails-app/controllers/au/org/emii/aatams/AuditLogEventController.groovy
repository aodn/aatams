package au.org.emii.aatams

import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent
import org.apache.shiro.SecurityUtils

class AuditLogEventController extends org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEventController {

    private static String TARGET = 'org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEventController.TARGET'

    def list = {

        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)

        if (!params.sort && ! params.order)
        {
            params.sort = 'dateCreated'
            params.order = 'desc'
        }

        if (SecurityUtils.subject.hasRole("SysAdmin"))
        {
            [auditLogEventInstanceList: AuditLogEvent.list(params), auditLogEventInstanceTotal: AuditLogEvent.count()]
        }
        else
        {
            Person user = Person.get(SecurityUtils.subject.principal)
            assert(user)

            [auditLogEventInstanceList: AuditLogEvent.findAllByActor(user.username, params),
             auditLogEventInstanceTotal: AuditLogEvent.findAllByActor(user.username).size()]
        }
    }
}
