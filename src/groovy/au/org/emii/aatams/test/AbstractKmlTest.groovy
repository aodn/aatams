package au.org.emii.aatams.test

import org.custommonkey.xmlunit.*

class AbstractKmlTest extends AbstractGrailsUnitTestCase
{
	protected void setUp()
	{
		super.setUp()
		
		XMLUnit.setIgnoreWhitespace(true)
		
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
		
		def xmlDiff = new Diff(expectedKmlAsString, actualKmlAsString)
		
		if (!xmlDiff.similar())
		{
			println "expected:\n" + expectedKmlAsString + "end"
			println "\n\nactual:\n" + actualKmlAsString + "end"
		}
		
		assert xmlDiff.similar()
	}
}
