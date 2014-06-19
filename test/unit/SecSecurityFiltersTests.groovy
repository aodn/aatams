import grails.test.*
import groovy.lang.MetaClass;

import au.org.emii.aatams.*
import au.org.emii.aatams.test.AbstractFiltersUnitTestCase

import org.apache.shiro.SecurityUtils
import org.apache.shiro.subject.Subject
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
        "auditLogEvent",
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
    
    def accessibleByAllControllers = 
    [
        "animal", "animalMeasurement", "organisation", "organisationProject", "project", "projectRole", "person",
        "installation", "installationStation", "receiver", "species", "tag", "sensor",
        "animalRelease", "detection", "receiverDeployment", "receiverRecovery",
        "receiverEvent", "navigationMenu",
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

    void testAuthenticatedOnly() {
        FilterConfig filter = initFilter("authenticatedOnly")

        def accessClosure  
        
        filter.metaClass.accessControl = { 
            it ->
            
            accessClosure = it
        }

        filter.before()
        
        assertTrue accessClosure() 
    }

    void testReceiverDownloadFileShowNotAuthorised() {
        FilterConfig filter = initFilter("receiverDownloadFileShow")

        Person bob = new Person(username: 'bob')
        Person sally = new Person(username: 'sally')
        
        mockDomain(Person, [bob, sally])
        
        ReceiverDownloadFile file = new ReceiverDownloadFile(requestingUser:bob)
        mockDomain(ReceiverDownloadFile, [file])

        user = sally
        hasRole = false
        mockParams.id = file.id

        def result = filter.before()
        
        assertEquals "auth", redirectArgs.controller
        assertEquals "unauthorized", redirectArgs.action
        assertFalse result
    }

    void testReceiverDownloadFileShowAuthorised() {
        FilterConfig filter = initFilter("receiverDownloadFileShow")

        Person sally = new Person(username: 'sally')
        
        mockDomain(Person, [sally])

        ReceiverDownloadFile file = new ReceiverDownloadFile(requestingUser:sally)
        mockDomain(ReceiverDownloadFile, [file])

        user = sally
        hasRole = false
        mockParams.id = file.id

        def result = filter.before()
        
        assertEquals 0, redirectArgs.size()
        assertTrue result
    }

    void testReceiverDownloadFileShowSysAdmin() {
        FilterConfig filter = initFilter("receiverDownloadFileShow")

        Person bob = new Person(username: 'bob')
        Person sally = new Person(username: 'sally')
        
        mockDomain(Person, [bob, sally])
        
        ReceiverDownloadFile file = new ReceiverDownloadFile(requestingUser:sally)
        mockDomain(ReceiverDownloadFile, [file])

        user = sally
        hasRole = true
        mockParams.id = file.id

        def result = filter.before()
        
        assertEquals 0, redirectArgs.size()
        assertTrue result
    }
}
