package au.org.emii.aatams

import org.joda.time.DateTimeZone;

import grails.test.*
import grails.converters.JSON

class ProjectRoleControllerTests extends ControllerUnitTestCase
{
    def permissionUtilsService
    def searchableService
    def sessionFactory

    Person fredSmith
    Project someProject
    ProjectRoleType administrator

    protected void setUp()
    {
        super.setUp()

        controller.metaClass.message = {}
        controller.permissionUtilsService = permissionUtilsService

        fredSmith = new Person(username: "fsmith",
                               passwordHash: "asdf",
                               name: "Fred Smith",
                               emailAddress: "fsmith@asdf.com",
                               phoneNumber:"1234",
                               status: EntityStatus.ACTIVE,
                               defaultTimeZone: DateTimeZone.forID("Australia/Hobart"),
                               organisation: Organisation.build())
        fredSmith.save(failOnError:true)

        assertNotNull(fredSmith)

        someProject = Project.build()
        administrator = ProjectRoleType.build()

        ProjectRoleType.build(displayName: ProjectRoleType.PRINCIPAL_INVESTIGATOR)
    }

    void testSave()
    {
        assertNoPermissions()
        def projectRole = saveRole(administrator)
        assertAdministratorPermissions()

        deleteRole(projectRole)
    }

    void testUpdate()
    {
        assertNoPermissions()
        def projectRole = saveRole(administrator)
        assertAdministratorPermissions()

        controller.params.id = projectRole.id
        controller.params.person = projectRole.person
        controller.params.project = projectRole.project
        controller.params.roleType = ProjectRoleType.findByDisplayName(ProjectRoleType.PRINCIPAL_INVESTIGATOR)
        controller.params.access = ProjectAccess.READ_WRITE
        controller.update()

        assertProjectInvestigatorPermissions()

        deleteRole(projectRole)
    }

    private def saveRole(roleType)
    {
        controller.params.person = fredSmith
        controller.params.project = someProject
        controller.params.roleType = roleType
        controller.params.access = ProjectAccess.READ_WRITE

        controller.save()

        def jsonResponse = JSON.parse(controller.response.contentAsString)

        assertNotNull(jsonResponse.instance)
        def projectRole = ProjectRole.get(jsonResponse.instance.id)
        assertNotNull(projectRole)

        return projectRole
    }

    private void deleteRole(projectRole)
    {
        controller.params.id = projectRole.id
        controller.delete()
        assertNoPermissions()
    }

    private void assertProjectInvestigatorPermissions()
    {
        assertPermissions(
                [permissionUtilsService.buildProjectWritePermission(someProject.id),
                    permissionUtilsService.buildProjectReadPermission(someProject.id),
                    permissionUtilsService.PROJECT_WRITE_ANY,
                    permissionUtilsService.PROJECT_READ_ANY,
                    permissionUtilsService.PERSON_WRITE_ANY,
                    permissionUtilsService.RECEIVER_CREATE],
                [])
    }

    private void assertAdministratorPermissions()
    {
        assertPermissions([permissionUtilsService.buildProjectWritePermission(someProject.id),
                           permissionUtilsService.buildProjectReadPermission(someProject.id),
                           permissionUtilsService.PROJECT_WRITE_ANY,
                           permissionUtilsService.PROJECT_READ_ANY],
                          [permissionUtilsService.PERSON_WRITE_ANY,
                           permissionUtilsService.RECEIVER_CREATE])
    }

    private void assertNoPermissions()
    {
        assertPermissions([],
        [permissionUtilsService.buildProjectWritePermission(someProject.id),
            permissionUtilsService.buildProjectReadPermission(someProject.id),
            permissionUtilsService.PROJECT_WRITE_ANY,
            permissionUtilsService.PROJECT_READ_ANY,
            permissionUtilsService.PERSON_WRITE_ANY,
            permissionUtilsService.RECEIVER_CREATE])
    }

    private void assertPermissions(truePermissions, falsePermissions)
    {
        truePermissions.each
        {
            assertTrue(it, fredSmith.permissions.contains(it))
        }

        falsePermissions.each
        {
            assertFalse(it, fredSmith.permissions.contains(it))
        }
    }
}
