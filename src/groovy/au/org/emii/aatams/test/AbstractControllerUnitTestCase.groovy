package au.org.emii.aatams.test

import grails.test.ControllerUnitTestCase

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

import au.org.emii.aatams.EmbargoService;
import au.org.emii.aatams.PermissionUtilsService;
import au.org.emii.aatams.Person;
import au.org.emii.aatams.filter.QueryService
import au.org.emii.aatams.report.ReportInfoService

abstract class AbstractControllerUnitTestCase extends ControllerUnitTestCase 
{
	protected hasRole = true
	protected user
	protected authenticated = true
	protected permitted = false
	
	private MetaClass originalSecurityUtilsMetaClass
	
	protected Map flashMsgParams
	
	protected void setUp()
	{
		super.setUp()

		hasRole = true
		authenticated = true
		permitted = false
	
		def subject = [ getPrincipal: { getPrincipal() },
						isAuthenticated: { isAuthenticated() },
						hasRole: { hasRole() },
						isPermitted: { isPermitted(it) },
					  ] as Subject

		// Save SecurityUtils' current meta class and clear it from
		// the registry.
		def registry = GroovySystem.metaClassRegistry
		this.originalSecurityUtilsMetaClass = registry.getMetaClass(SecurityUtils)
		registry.removeMetaClass(SecurityUtils)

		SecurityUtils.metaClass.static.getSubject = { subject }
		
		controller.metaClass.message = { Map params -> flashMsgParams = params }
		
		try
		{
			mockLogging(QueryService, true)
			mockLogging(ReportInfoService, true)
			controller.queryService = new QueryService()
			
			mockLogging(EmbargoService, true)
			controller.queryService.embargoService = new EmbargoService()
			
			mockLogging(PermissionUtilsService, true)
			controller.queryService.embargoService.permissionUtilsService = new PermissionUtilsService()
			
			controller.reportInfoService = new ReportInfoService()
			controller.reportInfoService.permissionUtilsService = new PermissionUtilsService()
		}
		catch (MissingPropertyException e)
		{
			// Some controllers don't have these properties, just ignore.
		}
	}

	protected void tearDown()
	{
		// Restore the old meta class on SecurityUtils.
		GroovySystem.metaClassRegistry.setMetaClass(SecurityUtils, this.originalSecurityUtilsMetaClass)
		
		super.tearDown()
	}
	
	protected Person getUser()
	{
		return new Person(username:"jkburges", name: "Joe Bloggs")
	}
	
	protected def getPrincipal()
	{
		return getUser()?.username
	}
	
	protected boolean isAuthenticated()
	{
		return authenticated
	}
	
	protected boolean hasRole()
	{
		return hasRole
	}
	
	protected boolean isPermitted(permission)
	{
		return permitted
	}
}
