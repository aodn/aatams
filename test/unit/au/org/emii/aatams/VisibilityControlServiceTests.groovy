package au.org.emii.aatams

import au.org.emii.aatams.detection.ValidDetection
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase
import org.joda.time.DateTime

class VisibilityControlServiceTests extends AbstractGrailsUnitTestCase
{
    def visibilityControlService
    def permissionUtilsService

    def acceptedPermissionString

    Project installationProject
    Project releaseProject

    ValidDetection det
    AnimalRelease release
    Surgery surgery

    protected void setUp()
    {
        super.setUp()

        visibilityControlService = new VisibilityControlService()
        permissionUtilsService = new PermissionUtilsService()
        visibilityControlService.permissionUtilsService = permissionUtilsService

        hasRole = false

        installationProject = new Project(name: "installationProject")

        Installation installation = new Installation(project: installationProject)
        InstallationStation station = new InstallationStation(installation: installation)
        ReceiverDeployment deployment = new ReceiverDeployment(station: station)

        releaseProject = new Project(name: "releaseProject")

        mockDomain(Project, [installationProject, releaseProject])
        [installationProject, releaseProject].each {
            it.save()
        }

        mockLogging(AnimalRelease)
        release = new AnimalRelease(project: releaseProject, embargoDate: new DateTime().plusDays(1).toDate())
        surgery = new Surgery(release: release)
        det = new ValidDetection(receiverDeployment: deployment, transmitterId: 'A69-1303-1234')

        mockDomain(ValidDetection, [det])
        mockDomain(Sensor)
        mockDomain(Surgery, [surgery])

        det.metaClass.getProject = {
            return releaseProject
        }
        det.metaClass.getSurgeries = {
            return [surgery]
        }
    }

    void testReadPermissionDetectionNoAssociatedRelease()
    {
        det.metaClass.getProject = {
            return null
        }

        assertTrue(visibilityControlService.hasReadPermission(det))
    }

    void testDetectionEmbargoMemberOfDeploymentProject()
    {
        acceptedPermissionString = "project:${installationProject.id}:read"

        [surgery, release].each {
            assertNull(visibilityControlService.applyVisibilityControls(it))
        }
    }

    void testDetectionEmbargoMemberOfReleaseProject()
    {
        acceptedPermissionString = "project:${releaseProject.id}:read"
        [det, surgery, release].each {
            assertNotNull(visibilityControlService.applyVisibilityControls(it))
        }
    }

    protected boolean isPermitted(permission)
    {
        return permission == acceptedPermissionString
    }
}
