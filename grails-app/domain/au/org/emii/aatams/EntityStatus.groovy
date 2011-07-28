package au.org.emii.aatams

/**
 * Certain entities (e.g. Organisation, Project) have a life-cycle, whereby
 * a non-authorised user can request for the creation of an entity
 * (e.g. Organisation), at which point the entity will be created but have a 
 * status of PENDING.
 *
 * It is then up to an authorised user (e.g. eMII staff or sys admin) to set the
 * Organisation's status to "ACTIVE", so that it becomes visible to other users.
 */
enum EntityStatus 
{
    PENDING('Pending'),
    ACTIVE('Active'),
    DEACTIVATED('Deactivated')
    
    String displayStatus
    
    EntityStatus(String displayStatus)
    {
        this.displayStatus = displayStatus;
    }
    
    static list()
    {
        [PENDING, ACTIVE, DEACTIVATED]
    }
}
