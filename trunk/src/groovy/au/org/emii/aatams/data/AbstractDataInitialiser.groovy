package au.org.emii.aatams.data

import au.org.emii.aatams.PermissionUtilsService

/**
 *
 * @author jburgess
 */
abstract class AbstractDataInitialiser implements DataInitialiser
{
    protected final def permissionUtilsService
    
    AbstractDataInitialiser(def service)
    {
        permissionUtilsService = service
    }
    
    abstract void execute()
}

