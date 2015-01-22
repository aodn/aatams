package au.org.emii.aatams.detection

import au.org.emii.aatams.*

import grails.test.*

class DetectionExtractServiceTests extends GrailsUnitTestCase {

    def des

    protected void setUp() {
        super.setUp()

        des = new DetectionExtractService()
    }

    void testIsProtectedNullReleaseProject() {
        assertFalse(des._isProtected([:]))
    }
}
