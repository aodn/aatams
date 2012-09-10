package au.org.emii.aatams

import grails.test.*

class AnimalControllerTests extends ControllerUnitTestCase 
{
    protected void setUp() 
	{
        super.setUp()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testLookupAnimalWithEmptySpecies() 
	{
		controller.params.project = [id: ""]
		controller.params.species = [id: ""]
		
		controller.lookup()
    }
}
