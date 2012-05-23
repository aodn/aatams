package au.org.emii.aatams.test

import grails.test.GrailsUnitTestCase

class AbstractKmlTest extends GrailsUnitTestCase
{
	{
		super.setUp()
		
		mockConfig('''grails
					{
						serverURL = "http://localhost:8090/aatams"
					}''')
	}

	protected void assertKmlEquals(String expectedKmlAsString, actualKml)
	{
		StringWriter writer = new StringWriter()
		actualKml.marshal(writer)
		String actualKmlAsString = writer.toString()
		
		if (expectedKmlAsString != actualKmlAsString)
		{
			println "expected:\n" + expectedKmlAsString + "end"
			println "\n\nactual:\n" + actualKmlAsString + "end"
		}
		
		assertEquals(expectedKmlAsString, actualKmlAsString)
	}
}
