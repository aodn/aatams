package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase

import grails.test.*
import grails.converters.JSON

class ProjectRoleControllerTests extends AbstractControllerUnitTestCase 
{
	def permissionUtilsService
	def searchableService
	def sessionFactory
	
    protected void setUp() 
	{
        super.setUp()
		
		controller.metaClass.message = {}
		controller.permissionUtilsService = permissionUtilsService
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testSave() 
	{
		Person joeBloggs = Person.findByUsername("jbloggs")
		assertNotNull(joeBloggs)
		
		Project sealCount = Project.findByName("Seal Count")
		assertNotNull(sealCount)
		
		ProjectRoleType administrator = ProjectRoleType.findByDisplayName("Administrator")
		assertNotNull(administrator)
		
		ProjectAccess readWriteAccess = ProjectAccess.READ_WRITE
		
		controller.params.person = joeBloggs
		controller.params.project = sealCount
		controller.params.roleType = administrator
		controller.params.access = readWriteAccess
		
		beforeFilter()
		controller.save()
		afterFilter()
		
		def jsonResponse = JSON.parse(controller.response.contentAsString)
		
		assertNotNull(jsonResponse.instance)
		def projectRoleId = jsonResponse.instance.id
		
		controller.params.id = projectRoleId
		
		beforeFilter()
		controller.delete()
		afterFilter()
    }
	
	private void beforeFilter()
	{
		searchableService.stopMirroring()
	}
	
	private void afterFilter()
	{
		sessionFactory.getCurrentSession().flush()
		searchableService.startMirroring()
	}
}