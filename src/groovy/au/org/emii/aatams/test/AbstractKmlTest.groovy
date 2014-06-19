package au.org.emii.aatams.test

import org.custommonkey.xmlunit.*

class AbstractKmlTest extends AbstractGrailsUnitTestCase
{
    protected void setUp()
    {
        super.setUp()
        
        XMLUnit.setIgnoreWhitespace(true)
        XMLUnit.setNormalize(true)
        
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

    protected String wrapInKmlElement(String doc) {
            return '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
    ''' + doc + '''
    </kml>
    '''
        }
}
