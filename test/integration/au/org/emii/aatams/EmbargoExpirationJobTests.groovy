package au.org.emii.aatams

import grails.test.*

class EmbargoExpirationJobTests extends GroovyTestCase 
{
    protected void setUp() 
    {
        super.setUp()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testFindEmbargoedReleasesToday()
    {
        EmbargoExpirationJob.triggerNow()
    }
}
