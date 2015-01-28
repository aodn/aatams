package au.org.emii.aatams.detection

import grails.test.*

class DetectionVisibilityCheckerTests extends GrailsUnitTestCase {

    void testIsProtectedNullReleaseProject() {

        def row = [release_project_id: null]
        def checker = new DetectionVisibilityChecker(row, null, null, null)

        assertFalse(checker._isProtected())
    }
}
