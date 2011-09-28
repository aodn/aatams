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
            
            return notification.isActiveForPerson(permissionUtilsService.principal())
        }
        
        return retList
    }
    
    void acknowledge(String key) throws IllegalArgumentException
    {
        if (!key)
        {
            throw new IllegalArgumentException("Null notification key")
        }
        
        Notification notification = Notification.findByKey(key)
        if (!notification)
        {
            throw new IllegalArgumentException("Unknown notification key: " + key)
        }
        
        acknowledge(notification)
    }
    
    void acknowledge(Notification notification) throws IllegalArgumentException
    {
        if (!notification)
        {
            throw new IllegalArgumentException("Null notification")
        }
        
        notification.addToAcknowledgers(permissionUtilsService.principal())
        notification.save()
    }
}
