package au.org.emii.aatams

import grails.test.*

class PermissionUtilsServiceTests extends GrailsUnitTestCase 
{
    PermissionUtilsService service
    
    ProjectRoleType piRoleType
    Person person
    Project project
    ProjectRoleType nonPiType
        
    
    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(PermissionUtilsService, true)
        service = new PermissionUtilsService()
        
        piRoleType = new ProjectRoleType(displayName:ProjectRoleType.PRINCIPAL_INVESTIGATOR)
        nonPiType = new ProjectRoleType(displayName:"Non PI")
        def roleTypeList = [piRoleType, nonPiType]
        mockDomain(ProjectRoleType, roleTypeList)
        roleTypeList.each{it.save()}
        
        mockDomain(ProjectRole)
        
        person = new Person(username:"person", name:"Person")
        mockDomain(Person, [person])
        person.save()
        
        project = new Project(name:"project")
        mockDomain(Project, [project])
        project.save()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testBuildProjectReadPermission()
    {
        assertEquals("project:" + project.id + ":read",
                     service.buildProjectReadPermission(project.id))
    }

    void testBuildProjectReadPermissionBigID()
    {
        def id = Long.MAX_VALUE
        
        assertEquals("project:" + id + ":read",
                     service.buildProjectReadPermission(id))
    }

    void testBuildProjectReadAnyPermission()
    {
        assertEquals("projectReadAny", service.buildProjectReadAnyPermission())
    }

    void testBuildProjectWritePermission()
    {
        assertEquals("project:" + project.id + ":write",
                     service.buildProjectWritePermission(project.id))
    }
    
    void testBuildProjectWritePermissionNullId()
    {
        assertEquals("notPermitted", service.buildProjectWritePermission(null))
    }
    
    void testBuildProjectWriteAnyPermission()
    {
        assertEquals("projectWriteAny",
                     service.buildProjectWriteAnyPermission())
    }
    
    void testBuildPrincipalInvestigatorPermission()
    {
        assertEquals(
            "principalInvestigator:" + project.id,
            service.buildPrincipalInvestigatorPermission(project.id))
    }
    
    void testBuildPrincipalInvestigatorPermissionNullId()
    {
        assertEquals(
            "notPermitted",
            service.buildPrincipalInvestigatorPermission(null))
    }
    
    void testBuildPersonWriteAnyPermission()
    {
        assertEquals(
            "personWriteAny",
            service.buildPersonWriteAnyPermission())
    }
    
    void testBuildReceiverCreatePermission()
    {
        assertEquals(
            "receiverCreate",
            service.buildReceiverCreatePermission())
    }
    
    void testBuildReceiverUpdatePermission()
    {
        Integer receiverId = 3
        
        assertEquals("receiverUpdate:" + receiverId,
                     service.buildReceiverUpdatePermission(receiverId))
    }
    
    void testBuildReceiverUpdatePermissionNullId()
    {
        assertEquals("notPermitted",
                     service.buildReceiverUpdatePermission(null))
    }
    
    void testGetPIRoleType()
    {
        assertNotNull(service.getPIRoleType())
        assertEquals(piRoleType.displayName, service.getPIRoleType().displayName)
    }
    
    void testSetPermissionsNonPIREAD()
    {
        ProjectRole nonPiRead = 
            new ProjectRole(project:project,
                            person:person,
                            roleType:nonPiType,
                            access:ProjectAccess.READ_ONLY)
                        
        assertEquals(person, service.setPermissions(nonPiRead))
        
        assertFalse(person.permissions.contains(service.buildPersonWriteAnyPermission()))
        assertFalse(person.permissions.contains(service.buildReceiverCreatePermission()))
        assertFalse(person.permissions.contains(service.buildPrincipalInvestigatorPermission(project.id)))
        
        assertTrue(person.permissions.contains(service.buildProjectReadPermission(project.id)))
        assertTrue(person.permissions.contains(service.buildProjectReadAnyPermission()))
        
        assertFalse(person.permissions.contains(service.buildProjectWritePermission(project.id)))
        assertFalse(person.permissions.contains(service.buildProjectWriteAnyPermission()))
    }

    void testSetPermissionsNonPIWRITE()
    {
        ProjectRole nonPiWrite = 
            new ProjectRole(project:project,
                            person:person,
                            roleType:nonPiType,
                            access:ProjectAccess.READ_WRITE)
                        
        assertEquals(person, service.setPermissions(nonPiWrite))
        
        assertFalse(person.permissions.contains(service.buildPersonWriteAnyPermission()))
        assertFalse(person.permissions.contains(service.buildReceiverCreatePermission()))
        assertFalse(person.permissions.contains(service.buildPrincipalInvestigatorPermission(project.id)))
        
        assertTrue(person.permissions.contains(service.buildProjectReadPermission(project.id)))
        assertTrue(person.permissions.contains(service.buildProjectReadAnyPermission()))
        
        assertTrue(person.permissions.contains(service.buildProjectWritePermission(project.id)))
        assertTrue(person.permissions.contains(service.buildProjectWriteAnyPermission()))
    }
    
    void testSetPermissionsPIREAD()
    {
        ProjectRole piRead = 
            new ProjectRole(project:project,
                            person:person,
                            roleType:piRoleType,
                            access:ProjectAccess.READ_ONLY)
                        
        assertEquals(person, service.setPermissions(piRead))
        
        assertTrue(person.permissions.contains(service.buildPersonWriteAnyPermission()))
        assertTrue(person.permissions.contains(service.buildReceiverCreatePermission()))
        assertTrue(person.permissions.contains(service.buildPrincipalInvestigatorPermission(project.id)))
        
        assertTrue(person.permissions.contains(service.buildProjectReadPermission(project.id)))
        assertTrue(person.permissions.contains(service.buildProjectReadAnyPermission()))
        
        assertFalse(person.permissions.contains(service.buildProjectWritePermission(project.id)))
        assertFalse(person.permissions.contains(service.buildProjectWriteAnyPermission()))
    }

    void testSetPermissionsPIWRITE()
    {
        ProjectRole piWrite = 
            new ProjectRole(project:project,
                            person:person,
                            roleType:piRoleType,
                            access:ProjectAccess.READ_WRITE)
                        
        assertEquals(person, service.setPermissions(piWrite))
        
        assertTrue(person.permissions.contains(service.buildPersonWriteAnyPermission()))
        assertTrue(person.permissions.contains(service.buildReceiverCreatePermission()))
        assertTrue(person.permissions.contains(service.buildPrincipalInvestigatorPermission(project.id)))
        
        assertTrue(person.permissions.contains(service.buildProjectReadPermission(project.id)))
        assertTrue(person.permissions.contains(service.buildProjectReadAnyPermission()))
        
        assertTrue(person.permissions.contains(service.buildProjectWritePermission(project.id)))
        assertTrue(person.permissions.contains(service.buildProjectWriteAnyPermission()))
    }



    void testRemovePermissionsNonPIREAD()
    {
        ProjectRole nonPiRead = 
            new ProjectRole(project:project,
                            person:person,
                            roleType:nonPiType,
                            access:ProjectAccess.READ_ONLY)
                        
        testSetPermissionsNonPIREAD()
        
        assertEquals(person, service.removePermissions(nonPiRead))
        assertFalse(person.permissions.contains(service.buildPersonWriteAnyPermission()))
        assertFalse(person.permissions.contains(service.buildReceiverCreatePermission()))
        assertFalse(person.permissions.contains(service.buildPrincipalInvestigatorPermission(project.id)))
        assertFalse(person.permissions.contains(service.buildProjectReadPermission(project.id)))
        assertFalse(person.permissions.contains(service.buildProjectReadAnyPermission()))
        assertFalse(person.permissions.contains(service.buildProjectWritePermission(project.id)))
        assertFalse(person.permissions.contains(service.buildProjectWriteAnyPermission()))
    }
    
    void testRemovePermissionsNonPIWRITE()
    {
        ProjectRole nonPiWrite = 
            new ProjectRole(project:project,
                            person:person,
                            roleType:nonPiType,
                            access:ProjectAccess.READ_WRITE)
        
        testSetPermissionsNonPIWRITE()
        
        assertEquals(person, service.removePermissions(nonPiWrite))
        assertFalse(person.permissions.contains(service.buildPersonWriteAnyPermission()))
        assertFalse(person.permissions.contains(service.buildReceiverCreatePermission()))
        assertFalse(person.permissions.contains(service.buildPrincipalInvestigatorPermission(project.id)))
        assertFalse(person.permissions.contains(service.buildProjectReadPermission(project.id)))
        assertFalse(person.permissions.contains(service.buildProjectReadAnyPermission()))
        assertFalse(person.permissions.contains(service.buildProjectWritePermission(project.id)))
        assertFalse(person.permissions.contains(service.buildProjectWriteAnyPermission()))
    }
    
    void testRemovePermissionsPIREAD()
    {
        ProjectRole piRead = 
            new ProjectRole(project:project,
                            person:person,
                            roleType:piRoleType,
                            access:ProjectAccess.READ_ONLY)
                  
        testSetPermissionsPIREAD()
        
        assertEquals(person, service.removePermissions(piRead))
        assertFalse(person.permissions.contains(service.buildPersonWriteAnyPermission()))
        assertFalse(person.permissions.contains(service.buildReceiverCreatePermission()))
        assertFalse(person.permissions.contains(service.buildPrincipalInvestigatorPermission(project.id)))
        assertFalse(person.permissions.contains(service.buildProjectReadPermission(project.id)))
        assertFalse(person.permissions.contains(service.buildProjectReadAnyPermission()))
        assertFalse(person.permissions.contains(service.buildProjectWritePermission(project.id)))
        assertFalse(person.permissions.contains(service.buildProjectWriteAnyPermission()))
    }

    void testRemovePermissionsPIWRITE()
    {
        ProjectRole piWrite = 
            new ProjectRole(project:project,
                            person:person,
                            roleType:piRoleType,
                            access:ProjectAccess.READ_WRITE)
                        
        testSetPermissionsPIWRITE()
        
        assertEquals(person, service.removePermissions(piWrite))
        assertFalse(person.permissions.contains(service.buildPersonWriteAnyPermission()))
        assertFalse(person.permissions.contains(service.buildReceiverCreatePermission()))
        assertFalse(person.permissions.contains(service.buildPrincipalInvestigatorPermission(project.id)))
        assertFalse(person.permissions.contains(service.buildProjectReadPermission(project.id)))
        assertFalse(person.permissions.contains(service.buildProjectReadAnyPermission()))
        assertFalse(person.permissions.contains(service.buildProjectWritePermission(project.id)))
        assertFalse(person.permissions.contains(service.buildProjectWriteAnyPermission()))
    }
}
