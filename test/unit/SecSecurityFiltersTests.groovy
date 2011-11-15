import grails.test.*

import au.org.emii.aatams.*
import au.org.emii.aatams.test.AbstractFiltersUnitTestCase

import org.codehaus.groovy.grails.plugins.web.filters.FilterConfig

class SecSecurityFiltersTests extends AbstractFiltersUnitTestCase 
{
    def permissionUtilsService

    protected void setUp() 
    {
        super.setUp()

        mockLogging(PermissionUtilsService, true)
        permissionUtilsService = new PermissionUtilsService()

        filters.permissionUtilsService = permissionUtilsService
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

        authenticated = true
        [true, false].each
        {
            hasSysAdminRole ->

            hasRole = hasSysAdminRole
            
            [true, false].each
            {
                hasProjectWritePermission ->
                
                permitted = hasProjectWritePermission
                
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
                        
                        println("hasRole: " + hasRole + ", isPermitted: " + permitted + ", controller: " + controllerName + ", action: " + actionName)
                        println("redirectArgs: " + String.valueOf(redirectArgs))
//                        println("renderArgs: " + renderArgs)
                    }
                }
            }
        }
    }
}
