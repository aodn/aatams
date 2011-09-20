package au.org.emii.aatams

import grails.test.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

class NavigationMenuControllerTests extends ControllerUnitTestCase 
{
    def permissionUtilsService
    
    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(PermissionUtilsService)
        permissionUtilsService = new PermissionUtilsService()
        assert(permissionUtilsService)
        
        controller.permissionUtilsService = permissionUtilsService
        
        def person = new Person(username:"person")
                               
        mockDomain(Person, [person])
        person.save()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testFieldDataControllersAsNonSysAdmin() 
    {
        def subject = [ getPrincipal: { person.username },
                        isAuthenticated: { true },
                        hasRole: { false },
                        isPermitted:
                        {
                            if (it == "projectWriteAny")
                            {
                                return true
                            }
                            
                            return false
                        }
                      ] as Subject

        ThreadContext.put( ThreadContext.SECURITY_MANAGER_KEY, 
                            [ getSubject: { subject } ] as SecurityManager )

        SecurityUtils.metaClass.static.getSubject = { subject }
        
        def fieldDataControllers =
            controller.index().fieldDataControllers
        
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'tag', label:'Tags', canCreateNew:true)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'animalRelease', label:'Tag Releases', canCreateNew:true)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'detection', label:'Tag Detections', canCreateNew:false)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'receiverDeployment', label:'Receiver Deployment', canCreateNew:true)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'receiverRecovery', label:'Receiver Recovery', canCreateNew:true)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'receiverEvent', label:'Receiver Events', canCreateNew:false)))
    }

    void testFieldDataControllersAsSysAdmin() 
    {
        def subject = [ getPrincipal: { person.username },
                        isAuthenticated: { true },
                        hasRole: { true },
                        isPermitted:
                        {
                            if (it == "projectWriteAny")
                            {
                                return true
                            }
                            
                            return false
                        }
                      ] as Subject

        ThreadContext.put( ThreadContext.SECURITY_MANAGER_KEY, 
                            [ getSubject: { subject } ] as SecurityManager )

        SecurityUtils.metaClass.static.getSubject = { subject }

        def fieldDataControllers =
            controller.index().fieldDataControllers
        
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'tag', label:'Tags', canCreateNew:true)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'animalRelease', label:'Tag Releases', canCreateNew:true)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'detection', label:'Tag Detections', canCreateNew:true)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'receiverDeployment', label:'Receiver Deployment', canCreateNew:true)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'receiverRecovery', label:'Receiver Recovery', canCreateNew:true)))
        assertTrue(fieldDataControllers.contains(new NavigationMenuItem(controllerName:'receiverEvent', label:'Receiver Events', canCreateNew:true)))
    }
}
