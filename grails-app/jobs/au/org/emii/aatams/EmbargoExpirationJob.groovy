package au.org.emii.aatams

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class EmbargoExpirationJob implements ApplicationContextAware
{
    def grailsApplication
    def mailService
    
    def applicationContext

    def piRoleType
    
    static triggers =
    {
        // Execute daily at midnight.
        cron name: 'embargoExpirationDailyTrigger', cronExpression: "30 0 0 * * ?"
    }
    
    def execute() 
    {
        log.info("Executing embargo expiration job...")
        
        // Emails are sent to Principal Investigators for each project.
        piRoleType = ProjectRoleType.findByDisplayName(ProjectRoleType.PRINCIPAL_INVESTIGATOR)
        
        // First, find releases which we need to send a warning for...
        def embargoWarningPeriodMonths = grailsApplication.config.animalRelease.embargoExpiration.warningPeriodMonths 
        def warningReleases = findEmbargoedReleases(embargoWarningPeriodMonths)
        log.info("Releases requiring embargo warning: " + warningReleases)
        warningReleases.each
        {
            sendEmail(it, createWarningSubject(it), createWarningBody(it))
        }
        
        // Second, find releases whose embargoes have expired today.
        // Find release embargoes due for expiration with (n-1) to (n+2) months.
        def expiresTodayReleases = findEmbargoedReleases(0)
        log.info("Releases requiring embargo expiration: " + expiresTodayReleases)
        expiresTodayReleases.each
        {
            sendEmail(it, createExpiringSubject(it), createExpiringBody(it))
        }
    }
    
    def findEmbargoedReleases(embargoWarningPeriodMonths)
    {
        Calendar lowerMonths = Calendar.getInstance()
        lowerMonths.add(Calendar.MONTH, (embargoWarningPeriodMonths - 1))
        Date lowerDate = lowerMonths.getTime()
        
        Calendar upperMonths = Calendar.getInstance()
        upperMonths.add(Calendar.MONTH, (embargoWarningPeriodMonths + 1))
        Date upperDate = upperMonths.getTime()
        
        log.debug("Searching for embargoes, lower date: " + String.valueOf(lowerDate) + ", upper date: " + String.valueOf(upperDate))

        def c = AnimalRelease.createCriteria()
        def embargoedReleases = c.list
        {
            and
            {
                isNotNull("embargoDate")
                between("embargoDate", lowerDate, upperDate)
            }
        }
        
        // Now filter out those releases whose embargoes do NOT expire on
        // the target day.
        Calendar targetCalendar = Calendar.getInstance()
        targetCalendar.add(Calendar.MONTH, embargoWarningPeriodMonths)
        Date targetDate = targetCalendar.getTime()
        
        Calendar testCalendar = Calendar.getInstance()
        
        embargoedReleases = embargoedReleases.grep
        {
            testCalendar.setTime(it.embargoDate)
            
            if (testCalendar.get(Calendar.YEAR) != targetCalendar.get(Calendar.YEAR))
            {
                log.debug("Filtering out release based on year, id: " + it.id + ", embargo date: " + String.valueOf(it.embargoDate))
                return false
            }

            if (testCalendar.get(Calendar.DAY_OF_YEAR) != targetCalendar.get(Calendar.DAY_OF_YEAR))
            {
                log.debug("Filtering out release based on day of year, id: " + it.id + ", embargo date: " + String.valueOf(it.embargoDate))
                return false
            }
            
            log.debug("Animal release due for expiration: " + String.valueOf(it))
            
            return true
        }
        
        // We've now got our list of embargoed releases.
        return embargoedReleases
    }
    
    def sendEmail(release, theSubject, theBody)
    {
        // Get the project's PIs.
        def pis = 
            ProjectRole.findAllByProjectAndRoleType(release.project,
                                                    piRoleType)
                                                       
        log.debug("Sending embargo notification email to PIs: " + String.valueOf(pis))
        
        // Problems having the framework inject this.
//        def mailService  //= new MailService()

        mailService.sendMail 
        {     
            to pis*.person*.emailAddress
            bcc grailsApplication.config.grails.mail.adminEmailAddress
            from grailsApplication.config.grails.mail.systemEmailAddress
            subject theSubject
            body theBody
        }
    }
    
    def createWarningSubject(release)
    {
        return "AATAMS tag embargo expiration warning: " + release*.surgeries?.tag
    }

    def createWarningBody(release)
    {
        def buf = warningHeader()
        
        return commonBody(buf, release)
    }
    
    def createExpiringSubject(release)
    {
        return "AATAMS tag embargo expiring today: " + release*.surgeries?.tag
    }

    def createExpiringBody(release)
    {
        def buf = expiringHeader()
        
        return commonBody(buf, release)
    }
    
    def warningHeader()
    {
        StringBuilder buf = new StringBuilder("The embargoes on the following tags will expire in ")
        buf.append(grailsApplication.config.animalRelease.embargoExpiration.warningPeriodMonths)
        buf.append(" month(s):")
        buf.append("\n")
        buf.append("\n")

        return buf
    }
    
    def expiringHeader()
    {
        StringBuilder buf = new StringBuilder("The embargoes on the following tags will expire today.")
        buf.append("\n")
        buf.append("\n")

        return buf
    }
    
    def commonBody(buf, release)
    {
        release.surgeries.each
        {
            buf.append(it.tag)
            buf.append("\n")
        }
        buf.append("\n")
        
        // Add a link to the release.
        def releaseLink = createLink(controller: 'animalRelease', action: 'show', id:release.id, absolute:true)
        buf.append(releaseLink)
        
        return buf.toString()
    }
    
    void setApplicationContext(ApplicationContext theApplicationContext) 
    {
        applicationContext = theApplicationContext
    }
    
    def createLink(Map urlAttrs)
    {
        def controller = urlAttrs.remove("controller")
        def action = urlAttrs.remove("action")
        def id = urlAttrs.remove("id")
        def frag = urlAttrs.remove('fragment')
        def params = urlAttrs.params && urlAttrs.params instanceof Map ? urlAttrs.remove('params') : [:]

        def url
        if (id != null) params.id = id
        def urlMappings = applicationContext.getBean("grailsUrlMappingsHolder")
        def mapping = urlMappings.getReverseMapping(controller, action, params)
        url = mapping.createURL(controller, action, params, "UTF-8", frag)

        log.debug("url: " + url)
        
//        if(!url.startsWith(ServletContextHolder.servletContext.contextPath)) {
//            url = "$ServletContextHolder.servletContext.contextPath$url".toString()
//        }

        return "${ConfigurationHolder.config.grails.serverHost}$url".toString()
    }
}
