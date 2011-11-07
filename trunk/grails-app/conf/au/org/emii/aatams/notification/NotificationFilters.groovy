package au.org.emii.aatams.notification

class NotificationFilters 
{
    def notificationService
    
    def filters = 
    {
        insertActive(controller:'*', action:'*')
        {
            after = 
            {
                model ->
                
                model?.notifications = notificationService.listActive()
            }
        }
    }
}
