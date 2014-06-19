package au.org.emii.aatams.detection

import org.springframework.context.i18n.LocaleContextHolder as LCH

import au.org.emii.aatams.Person
import au.org.emii.aatams.ReceiverDownloadFile
import au.org.emii.aatams.Sensor

class DetectionNotificationService 
{
    def grailsApplication
    def grailsTemplateEngineService
    def mailService
    def messageSource
    
    static transactional = true
    
    void sendDetectionNotificationEmails(downloadFile)
    {
        log.debug("Sending new detection notification emails for download: " + String.valueOf(downloadFile))
        
        Map<Person, SortedSet<Sensor>> sensorsGroupedByPI = Sensor.groupByOwningPI(downloadFile.getKnownSensors())
        
        sensorsGroupedByPI.each 
        {
            recipient, sensors ->
            
            sendDetectionNotificationEmailToPerson(recipient, sensors, downloadFile)
        }    
    }
    
    private void sendDetectionNotificationEmailToPerson(Person recipient, SortedSet<Sensor> sensors, ReceiverDownloadFile downloadFile)
    {
        def theSubject = getDetectionNotificationSubject(sensors)
        def htmlBody = getDetectionNotificationHtml(theSubject, recipient, sensors)
        log.debug("Sending notification email to: " + String.valueOf(recipient) + ", body:\n\n" + htmlBody)
        
        mailService.sendMail
        {
            to recipient?.emailAddress
            bcc grailsApplication.config.grails.mail.adminEmailAddress
            from grailsApplication.config.grails.mail.systemEmailAddress
            subject theSubject
            html htmlBody 
        }
    }
    
    private String getDetectionNotificationSubject(def sensors)
    {
        return messageSource.getMessage("mail.notification.detection.new.subject", [(sensors*.transmitterId).toString()] as Object[], LCH.getLocale())
    }
    
    private String getDetectionNotificationHtml(title, recipient, sensors)
    {
        return grailsTemplateEngineService.renderView(
            "/email/_newDetectionNotification", 
            [title: title, recipient: recipient, sensors: sensors])
    }
}
