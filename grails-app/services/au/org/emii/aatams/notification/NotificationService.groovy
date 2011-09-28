package au.org.emii.aatams.notification

import au.org.emii.aatams.*

class NotificationService 
{
    static transactional = true

    def permissionUtilsService
    
    List<Notification> listActive()
    {
        List<Notification> retList = Notification.list().grep
        {
            notification ->
            
            log.debug("Checking notification: " + notification)
            
            return notification.isActiveForPerson(permissionUtilsService.principal())
        }
        
        return retList
    }
    
    void acknowledge(Notification notification)
    {
        notification.addToAcknowledgers(permissionUtilsService.principal())
        notification.save()
    }
}
