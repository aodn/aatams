package au.org.emii.aatams

import grails.test.*

class CodeMapTests extends GrailsUnitTestCase 
{
    protected void setUp() 
	{
        super.setUp()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testSaveValid()
	{
		CodeMap codeMap = new CodeMap(codeMap:"A69-1303")
		mockDomain(CodeMap, [codeMap])
		
		codeMap.save(failOnError:true)
		assertNotNull(codeMap.id)
    }
}
