package au.org.emii.aatams.test

import grails.test.GrailsUnitTestCase
import groovy.lang.MetaClass;

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

import au.org.emii.aatams.Person;

abstract class AbstractGrailsUnitTestCase extends GrailsUnitTestCase 
{
	protected hasRole = true
	protected user
	protected authenticated = true
	protected permitted = false
	
	private MetaClass originalSecurityUtilsMetaClass
	
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
	}

	protected void tearDown()
	{
		// Restore the old meta class on SecurityUtils.
		GroovySystem.metaClassRegistry.setMetaClass(SecurityUtils, this.originalSecurityUtilsMetaClass)
		
		super.tearDown()
	}
	
	protected Person getUser()
	{
		return new Person(username:"jkburges")
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
