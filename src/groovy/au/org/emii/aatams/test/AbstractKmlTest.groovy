package au.org.emii.aatams.test

class AbstractKmlTest extends AbstractGrailsUnitTestCase
{
	protected void setUp()
	{
		super.setUp()
		
		mockConfig('''grails
					{
						serverURL = "http://localhost:8090/aatams"
					}''')
	}

	protected void tearDown()
	{
		super.tearDown()
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
