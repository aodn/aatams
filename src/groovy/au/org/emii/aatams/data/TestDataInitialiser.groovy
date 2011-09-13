package au.org.emii.aatams.data

import au.org.emii.aatams.PermissionUtilsService

/**
 * Initialise data for integration tests.
 * 
 * @author jburgess
 */
class TestDataInitialiser extends AbstractDataInitialiser
{
    TestDataInitialiser(def service)
    {
        super(service)
    }
    
    void execute()
    {
        // Does nothing currently.
    }
}

