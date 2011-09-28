package au.org.emii.aatams.notification

import grails.converters.JSON

class NotificationController 
{
    def notificationService
    
    static allowedMethods = [acknowledge: "POST"]
    
    def acknowledge = 
    {
        try
        {
            notificationService.acknowledge(params.key)
            render ([result:true] as JSON)
        }
        catch (IllegalArgumentException e)
        {
            log.error("Error acknowledging notification", e)
            render ([result:false] as JSON)
        }
    }
}
