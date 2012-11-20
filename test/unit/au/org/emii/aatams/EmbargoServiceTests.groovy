package au.org.emii.aatams

import au.org.emii.aatams.detection.ValidDetection
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase
import grails.test.*
import org.joda.time.DateTime

class EmbargoServiceTests extends AbstractGrailsUnitTestCase  
{
	def embargoService
	def permissionUtilsService
	
	def acceptedPermissionString
	
	Project installationProject
	Project releaseProject
	ValidDetection det
	
    protected void setUp() 
	{
        super.setUp()
		
		embargoService = new EmbargoService()
		permissionUtilsService = new PermissionUtilsService()
		embargoService.permissionUtilsService = permissionUtilsService
		
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
		AnimalRelease release = new AnimalRelease(project: releaseProject, embargoDate: new DateTime().plusDays(1).toDate())
		Surgery surgery = new Surgery(release: release)
		det = new ValidDetection(receiverDeployment: deployment)
		DetectionSurgery detSurgery = new DetectionSurgery(surgery: surgery, detection: det)
		
		mockDomain(ValidDetection, [det])
		
		det.addToDetectionSurgeries(detSurgery)
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

	void testReadPermissionDetectionNoAssociatedRelease()
	{
		det.detectionSurgeries.clear()
		assertTrue(embargoService.hasReadPermission(det))
	}
	
    void testDetectionEmbargoMemberOfDeploymentProject() 
	{
		acceptedPermissionString = "project:${installationProject.id}:read"
		assertNull(embargoService.applyEmbargo(det))
    }
	
    void testDetectionEmbargoMemberOfReleaseProject() 
	{
		acceptedPermissionString = "project:${releaseProject.id}:read"
		assertNotNull(embargoService.applyEmbargo(det))
    }
	
	protected boolean isPermitted(permission)
	{
		return permission == acceptedPermissionString
	}
}
