import grails.test.*

import au.org.emii.aatams.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

import org.codehaus.groovy.grails.plugins.web.filters.FilterConfig

class SecSecurityFiltersTests extends FiltersUnitTestCase 
{
    def permissionUtilsService
    
    boolean hasRole
    boolean isAuthenticated
    boolean isPermitted

    protected void setUp() 
    {
        super.setUp()

        mockLogging(PermissionUtilsService, true)
        permissionUtilsService = new PermissionUtilsService()

        filters.permissionUtilsService = permissionUtilsService

        hasRole = false
        isAuthenticated = false
        isPermitted = false
        
        def subject = [ getPrincipal: { "username" },
                        isAuthenticated: { isAuthenticated },
                        hasRole: { hasRole },
                        isPermitted: { isPermitted }
                      ] as Subject

        SecurityUtils.metaClass.static.getSubject = { subject }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    def allControllers = 
    [
        "about",
        "address",
        "animal",
        "animalMeasurement",
        "animalMeasurementType",
        "animalRelease",
        "detection",
        "detectionSurgery",
        "device",
        "deviceManufacturerd",
        "deviceStatus",
        "gettingStarted",
        "installationConfiguration",
        "installation",
        "installationStation",
        "measurementUnit",
        "mooringType",
        "navigationMenu",
        "organisation",
        "organisationProject",
        "person",
        "project",
        "projectRole",
        "projectRoleType",
        "receiver",
        "receiverDeployment",
        "receiverDownload",
        "receiverDownloadFile",
        "receiverEvent",
        "receiverRecovery",
        "sensor",
        "sensorDetection",
        "sex",
        "species",
        "surgery",
        "surgeryTreatmentType",
        "surgeryType",
        "tag",
        "transmitterType",
        "auth",
        "secRole",
        "secUser"
    ]
    
    def accessibleControllers = 
    [
        "animal", "animalMeasurement", "organisation", "organisationProject", "project", "projectRole", "person",
        "installation", "installationStation", "receiver", "species", "tag", "sensor",
        "animalRelease", "detection", "receiverDeployment", "receiverRecovery",
        "receiverEvent", "navigationMenu", "receiverDownloadFile",
        "surgery", "detectionSurgery",
        "gettingStarted", "about"
    ]
    
    def deleteControllers = 
        ["animalMeasurement", "organisationProject", "projectRole", "sensor", "surgery"]
        
    
    def actions = ["create", "save", "update", "delete", "list", "index"]
        
    void testDelete() 
    {
        FilterConfig deleteFilter = getFilter("delete")

        isAuthenticated = true
        [true, false].each
        {
            hasSysAdminRole ->

            hasRole = hasSysAdminRole
            
            [true, false].each
            {
                hasProjectWritePermission ->
                
                isPermitted = hasProjectWritePermission
                
                (allControllers - deleteControllers).each
                {
                    controllerName ->
                    
                    controllerName = controllerName
                    
                    actions.each
                    {
                        actionName ->
                        
                        actionName = actionName
                        
                        boolean retVal = deleteFilter.before()

//                        assertFalse(retVal)
                        
                        println("hasRole: " + hasRole + ", isPermitted: " + isPermitted + ", controller: " + controllerName + ", action: " + actionName)
                        println("redirectArgs: " + String.valueOf(redirectArgs))
//                        println("renderArgs: " + renderArgs)
                    }
                }
            }
        }
    }
}
