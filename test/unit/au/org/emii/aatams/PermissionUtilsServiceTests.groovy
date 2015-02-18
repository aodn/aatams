package au.org.emii.aatams

import grails.test.*

class PermissionUtilsServiceTests extends GrailsUnitTestCase 
{
    PermissionUtilsService service
    
    ProjectRoleType piRoleType
    Person person
    Project projectA, projectB
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
        
        projectA = new Project(name:"projectA")
        projectB = new Project(name:"projectB")
        def projectList = [projectA, projectB]
        mockDomain(Project, projectList)
        projectList.each 
        {
            it.save()
        }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testBuildProjectReadPermission()
    {
        assertEquals("project:" + projectA.id + ":read",
                     service.buildProjectReadPermission(projectA.id))
    }

    void testBuildProjectReadPermissionStringId()
    {
        assertEquals("project:" + projectA.id + ":read",
                     service.buildProjectReadPermission(String.valueOf(projectA.id)))
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
        assertEquals("project:" + projectA.id + ":edit_children",
                     service.buildProjectEditChildrenPermission(projectA.id))
    }
    
    void testBuildProjectWritePermissionStringId()
    {
        assertEquals("project:" + projectA.id + ":edit_children",
                     service.buildProjectEditChildrenPermission(String.valueOf(projectA.id)))
    }
    
    void testBuildProjectWritePermissionNullId()
    {
        assertEquals("notPermitted", service.buildProjectEditChildrenPermission(null))
    }
    
    void testBuildProjectWriteAnyPermission()
    {
        assertEquals("projectWriteAny",
                     service.buildProjectWriteAnyPermission())
    }

    void testBuildProjectEditPermission()
    {
        assertEquals("project:" + projectA.id + ":edit",
                service.buildProjectEditPermission(projectA.id))
    }

    void testBuildProjectEditPermissionStringId()
    {
        assertEquals("project:" + projectA.id + ":edit",
                service.buildProjectEditPermission(String.valueOf(projectA.id)))
    }

    void testBuildProjectEditPermissionNullId()
    {
        assertEquals("notPermitted", service.buildProjectEditPermission(null))
    }
    
    void testBuildPrincipalInvestigatorPermission()
    {
        assertEquals(
            "principalInvestigator:" + projectA.id,
            service.buildPrincipalInvestigatorPermission(projectA.id))
    }
    
    void testBuildPrincipalInvestigatorPermissionStringId()
    {
        assertEquals(
            "principalInvestigator:" + projectA.id,
            service.buildPrincipalInvestigatorPermission(String.valueOf(projectA.id)))
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
    
    void testBuildReceiverUpdatePermissionStringId()
    {
        Integer receiverId = 3
        
        assertEquals("receiverUpdate:" + receiverId,
                     service.buildReceiverUpdatePermission(String.valueOf(receiverId)))
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
    
    ProjectRole testSetPermissionsNonPIREAD()
    {
        return testSetPermissionsNonPIREAD(projectA)
    }
    
    ProjectRole testSetPermissionsNonPIREAD(project)
    {
        ProjectRole nonPiRead = 
            new ProjectRole(project:project,
                            person:person,
                            roleType:nonPiType,
                            access:ProjectAccess.READ_ONLY)
        nonPiRead.save()    
                        
        assertEquals(person, service.setPermissions(nonPiRead))
        
        assertIsNotPermitted([
            service.buildPersonWriteAnyPermission(),
            service.buildReceiverCreatePermission(),
            service.buildPrincipalInvestigatorPermission(project.id),
            service.buildProjectEditChildrenPermission(project.id),
            service.buildProjectWriteAnyPermission()])
        
        assertIsPermitted([
            service.buildProjectReadPermission(project.id),
            service.buildProjectReadAnyPermission()])

        return nonPiRead
    }

    ProjectRole testSetPermissionsNonPIWRITE()
    {
        return testSetPermissionsNonPIWRITE(projectA)
    }
    
    ProjectRole testSetPermissionsNonPIWRITE(project)
    {
        ProjectRole nonPiWrite = 
            new ProjectRole(project:project,
                            person:person,
                            roleType:nonPiType,
                            access:ProjectAccess.READ_WRITE)
        nonPiWrite.save()
                        
        assertEquals(person, service.setPermissions(nonPiWrite))
        
        assertIsNotPermitted([
            service.buildPersonWriteAnyPermission(),
            service.buildReceiverCreatePermission(),
            service.buildPrincipalInvestigatorPermission(project.id)])
        
        assertIsPermitted([
            service.buildProjectReadPermission(project.id),
            service.buildProjectReadAnyPermission(),
            service.buildProjectEditChildrenPermission(project.id),
            service.buildProjectWriteAnyPermission()])
        
        return nonPiWrite
    }
    
    ProjectRole testSetPermissionsPIREAD()
    {
        return testSetPermissionsPIREAD(projectA)
    }
    
    ProjectRole testSetPermissionsPIREAD(project)
    {
        ProjectRole piRead = 
            new ProjectRole(project:project,
                            person:person,
                            roleType:piRoleType,
                            access:ProjectAccess.READ_ONLY)
        piRead.save()
                        
        assertEquals(person, service.setPermissions(piRead))
        
        assertIsPermitted([
            service.buildPersonWriteAnyPermission(),
            service.buildReceiverCreatePermission(),
            service.buildPrincipalInvestigatorPermission(project.id),
            service.buildProjectReadPermission(project.id),
            service.buildProjectReadAnyPermission()])
            
        assertIsNotPermitted([
            service.buildProjectEditPermission(project.id),
            service.buildProjectEditChildrenPermission(project.id),
            service.buildProjectWriteAnyPermission()])
        
        return piRead
    }

    ProjectRole testSetPermissionsPIWRITE()
    {
        return testSetPermissionsPIWRITE(projectA)
    }

    ProjectRole testSetPermissionsPIWRITE(project)
    {
        ProjectRole piWrite =
            new ProjectRole(project:project,
                            person:person,
                            roleType:piRoleType,
                            access:ProjectAccess.READ_WRITE)
        piWrite.save()
                        
        assertEquals(person, service.setPermissions(piWrite))
        
        assertIsPermitted([
            service.buildPersonWriteAnyPermission(),
            service.buildReceiverCreatePermission(),
            service.buildPrincipalInvestigatorPermission(project.id),
            service.buildProjectReadPermission(project.id),
            service.buildProjectReadAnyPermission(),
            service.buildProjectEditChildrenPermission(project.id),
            service.buildProjectWriteAnyPermission()])
        
        return piWrite
    }

    void testRemovePermissionsNonPIREAD()
    {
        ProjectRole nonPiRead = testSetPermissionsNonPIREAD()
        assertEquals(person, service.removePermissions(nonPiRead))
        
        assertIsOnlyPermitted([])
    }
    
    void testRemovePermissionsNonPIWRITE()
    {
        ProjectRole nonPiWrite = testSetPermissionsNonPIWRITE()
        assertEquals(person, service.removePermissions(nonPiWrite))
        
        assertIsOnlyPermitted([])
    }
    
    void testRemovePermissionsPIREAD()
    {
        ProjectRole piRead = testSetPermissionsPIREAD()
        
        assertEquals(person, service.removePermissions(piRead))
        
        assertIsOnlyPermitted([])
    }

    void testRemovePermissionsPIWRITE()
    {
        ProjectRole piWrite = testSetPermissionsPIWRITE()
        
        assertEquals(person, service.removePermissions(piWrite))
        
        assertIsOnlyPermitted([])
    }
    
    void testRemovePermissionsMultiRolePIRead()
    {
        ProjectRole piReadA = testSetPermissionsPIREAD(projectA)
        ProjectRole piReadB = testSetPermissionsPIREAD(projectB)
        
        assertEquals(person, service.removePermissions(piReadA))
        piReadA.delete()
        
        assertIsOnlyPermitted([
            service.buildPersonWriteAnyPermission(),
            service.buildReceiverCreatePermission(),
            service.buildProjectReadAnyPermission(),
            service.buildPrincipalInvestigatorPermission(projectB.id),
            service.buildProjectReadPermission(projectB.id)])
        

        assertEquals(person, service.removePermissions(piReadB))
        piReadB.delete()
        
        assertIsOnlyPermitted([])
    }
    
    void testRemovePermissionsMultiRolePIWrite()
    {
        ProjectRole piWriteA = testSetPermissionsPIWRITE(projectA)
        ProjectRole piWriteB = testSetPermissionsPIWRITE(projectB)
        
        assertEquals(person, service.removePermissions(piWriteA))
        piWriteA.delete()
        
        assertIsOnlyPermitted([
            service.buildPersonWriteAnyPermission(),
            service.buildReceiverCreatePermission(),
            service.buildProjectReadAnyPermission(),
            service.buildProjectWriteAnyPermission(),
            service.buildPrincipalInvestigatorPermission(projectB.id),
            service.buildProjectReadPermission(projectB.id),
            service.buildProjectEditPermission(projectB.id),
            service.buildProjectEditChildrenPermission(projectB.id)])
        

        assertEquals(person, service.removePermissions(piWriteB))
        piWriteB.delete()
        
        assertIsOnlyPermitted([])
    }
    
    void testRemovePermissionsMultiRoleNonPIRead()
    {
        ProjectRole nonpiReadA = testSetPermissionsNonPIREAD(projectA)
        ProjectRole nonpiReadB = testSetPermissionsNonPIREAD(projectB)
        
        assertEquals(person, service.removePermissions(nonpiReadA))
        nonpiReadA.delete()
        
        assertIsOnlyPermitted([
            service.buildProjectReadAnyPermission(),
            service.buildProjectReadPermission(projectB.id)])

        assertEquals(person, service.removePermissions(nonpiReadB))
        nonpiReadB.delete()
        
        assertIsOnlyPermitted([])
    }
    
    void testRemovePermissionsMultiRoleNonPIWrite()
    {
        ProjectRole nonpiWriteA = testSetPermissionsNonPIWRITE(projectA)
        ProjectRole nonpiWriteB = testSetPermissionsNonPIWRITE(projectB)
        
        assertEquals(person, service.removePermissions(nonpiWriteA))
        nonpiWriteA.delete()
        
        assertIsOnlyPermitted([
            service.buildProjectReadAnyPermission(),
            service.buildProjectWriteAnyPermission(),
            service.buildProjectReadPermission(projectB.id),
            service.buildProjectEditChildrenPermission(projectB.id)])

        assertEquals(person, service.removePermissions(nonpiWriteB))
        nonpiWriteB.delete()
        
        assertIsOnlyPermitted([])
    }
    
    private void assertIsPermitted(permissionStrings)
    {
        permissionStrings.each
        {
            permission ->
            
            assertTrue(person.permissions.contains(permission))
        }
    }
    
    private void assertIsOnlyPermitted(permissionStrings)
    {
        if (permissionStrings.isEmpty() && person.permissions.isEmpty())
        {
            return
        }
        
        if (permissionStrings.size() != person.permissions.size())
        {
            println "Expected permissions: " + permissionStrings
            println "Actual permissions: " + person.permissions
        }
        
        assertEquals(permissionStrings.size(), person.permissions.size())
        
        permissionStrings.each
        {
            permission ->
            
            assertTrue(person.permissions.contains(permission))
        }
    }
    

    private void assertIsNotPermitted(permissionStrings)
    {
        permissionStrings.each
        {
            permission ->
            
            if (person.permissions.contains(permission))
            {
                println "Permission exists: " + permission
            }
            
            assertFalse(person.permissions.contains(permission))
        }
    }
}
