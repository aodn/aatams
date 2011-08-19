package au.org.emii.aatams

import grails.test.*

class CachedDetectionFactoryServiceTests extends DetectionFactoryServiceTests 
{
    protected void setUp() 
    {
        super.setUp()

        mockLogging(CachedDetectionFactoryService)
        service = new CachedDetectionFactoryService()
    }
    
//    void testOneMatchingSurgeries() 
//    {
//        super.testOneMatchingSurgeries()
//    }
}
