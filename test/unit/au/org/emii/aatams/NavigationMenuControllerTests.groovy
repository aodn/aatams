package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*

class NavigationMenuControllerTests extends AbstractControllerUnitTestCase 
{
    def permissionUtilsService
    def person
    
    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(PermissionUtilsService)
        permissionUtilsService = new PermissionUtilsService()
        assert(permissionUtilsService)
        
        controller.permissionUtilsService = permissionUtilsService
        
        person = new Person(username:"person")
                               
        mockDomain(Person, [person])
        person.save()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    protected def getPrincipal()
    {
        return person.id
    }

    protected boolean isPermitted(String permission)
    {
        if (permission == "projectWriteAny")
        {
            return true
        }
        
        return false
    }    
    
    void testFieldDataControllersAsNonSysAdmin() 
    {
        hasRole = false
        
        def fieldDataControllers =
            controller.index().fieldDataControllers
        
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'sensor', label:'Tags', canCreateNew:true)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'animalRelease', label:'Tag Releases', canCreateNew:true)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'detection', label:'Tag Detections', canCreateNew:true)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'receiverDeployment', label:'Receiver Deployment', canCreateNew:true)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'receiverRecovery', label:'Receiver Recovery', canCreateNew:false)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'receiverEvent', label:'Receiver Events', canCreateNew:true)))
    }

    void testFieldDataControllersAsSysAdmin() 
    {
        hasRole = true

        def fieldDataControllers =
            controller.index().fieldDataControllers
        
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'sensor', label:'Tags', canCreateNew:true)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'animalRelease', label:'Tag Releases', canCreateNew:true)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'detection', label:'Tag Detections', canCreateNew:true)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'receiverDeployment', label:'Receiver Deployment', canCreateNew:true)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'receiverRecovery', label:'Receiver Recovery', canCreateNew:false)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'receiverEvent', label:'Receiver Events', canCreateNew:true)))
    }
}
