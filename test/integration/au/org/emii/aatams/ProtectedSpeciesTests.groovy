package au.org.emii.aatams

import au.org.emii.aatams.detection.DetectionController
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase

import static ProtectedSpeciesTests.AuthLevel.*
import static ProtectedSpeciesTests.ProtectionLevel.*
import static ProtectedSpeciesTests.FilterStatus.*

class ProtectedSpeciesTests extends AbstractGrailsUnitTestCase {

    def permissionUtilsService
    def controller

    enum AuthLevel {
        UNAUTHENTICATED,
        NON_PROJECT_MEMBER,
        PROJECT_MEMBER,
        SYS_ADMIN
    }

    enum ProtectionLevel {
        UNEMBARGOED,
        EMBARGOED,
        PROTECTED
    }

    enum FilterStatus {
        FILTER_SET,
        FILTER_NOT_SET
    }

    enum ExpectedResult {
        VISIBLE,
        VISIBLE_BUT_SANITISED,
        NOT_VISIBLE
    }

    @Override
    void setUp() {
        super.setUp()

        controller = new DetectionController()

        println "--------------------------------------------------"
        println controller.detectionExtractService
        println "--------------------------------------------------"

//        def initialiser = new ProtectedSpeciesTestDataInitialiser(permissionUtilsService)
//        initialiser.execute()
    }

    void testProtectedSpeciesFiltering() {
        println "testProtectedSpeciesFiltering()"

        assertVisible(UNAUTHENTICATED, UNEMBARGOED, FILTER_NOT_SET)
        assertVisible(UNAUTHENTICATED, UNEMBARGOED, FILTER_SET)
        assertVisibleButSanitised(UNAUTHENTICATED, EMBARGOED, FILTER_NOT_SET)
        assertNotVisible(UNAUTHENTICATED, EMBARGOED, FILTER_SET)
        assertNotVisible(UNAUTHENTICATED, PROTECTED, FILTER_NOT_SET)
        assertNotVisible(UNAUTHENTICATED, PROTECTED, FILTER_SET)

        assertVisible(NON_PROJECT_MEMBER, UNEMBARGOED, FILTER_NOT_SET)
        assertVisible(NON_PROJECT_MEMBER, UNEMBARGOED, FILTER_SET)
        assertVisibleButSanitised(NON_PROJECT_MEMBER, EMBARGOED, FILTER_NOT_SET)
        assertNotVisible(NON_PROJECT_MEMBER, EMBARGOED, FILTER_SET)
        assertNotVisible(NON_PROJECT_MEMBER, PROTECTED, FILTER_NOT_SET)
        assertNotVisible(NON_PROJECT_MEMBER, PROTECTED, FILTER_SET)

        assertVisible(PROJECT_MEMBER, UNEMBARGOED, FILTER_NOT_SET)
        assertVisible(PROJECT_MEMBER, UNEMBARGOED, FILTER_SET)
        assertVisible(PROJECT_MEMBER, EMBARGOED, FILTER_NOT_SET)
        assertVisible(PROJECT_MEMBER, EMBARGOED, FILTER_SET)
        assertVisible(PROJECT_MEMBER, PROTECTED, FILTER_NOT_SET)
        assertVisible(PROJECT_MEMBER, PROTECTED, FILTER_SET)

        assertVisible(SYS_ADMIN, UNEMBARGOED, FILTER_NOT_SET)
        assertVisible(SYS_ADMIN, UNEMBARGOED, FILTER_SET)
        assertVisible(SYS_ADMIN, EMBARGOED, FILTER_NOT_SET)
        assertVisible(SYS_ADMIN, EMBARGOED, FILTER_SET)
        assertVisible(SYS_ADMIN, PROTECTED, FILTER_NOT_SET)
        assertVisible(SYS_ADMIN, PROTECTED, FILTER_SET)
    }

    void assertVisible(AuthLevel authLevel, ProtectionLevel protectionlevel, FilterStatus speciesFilterSet) {
        assertCorrectResult(authLevel, protectionlevel, speciesFilterSet, ExpectedResult.VISIBLE)
    }

    void assertVisibleButSanitised(AuthLevel authLevel, ProtectionLevel protectionlevel, FilterStatus speciesFilterSet) {
        assertCorrectResult(authLevel, protectionlevel, speciesFilterSet, ExpectedResult.VISIBLE_BUT_SANITISED)
    }

    void assertNotVisible(AuthLevel authLevel, ProtectionLevel protectionlevel, FilterStatus speciesFilterSet) {
        assertCorrectResult(authLevel, protectionlevel, speciesFilterSet, ExpectedResult.NOT_VISIBLE)
    }

    void assertCorrectResult(AuthLevel authLevel, ProtectionLevel protectionlevel, FilterStatus speciesFilterSet, ExpectedResult expectedResult) {

        configureAccess(authLevel)
        def project = loadProject(protectionlevel)
        def filter = loadFilter(speciesFilterSet)




        /*
        Authenticate as user

        call controller with filter

        from results find those belonging to the specified project

        ensure there is:
        1, with full fields - Is visible
        1, but with empty fields - if sanitised
        0 - if not visible
         */


        fail "Write me"
    }

    void configureAccess(authLevel) {

        switch(authLevel) {
            case UNAUTHENTICATED:
                authenticated = false
                permitted = false
                break
            case NON_PROJECT_MEMBER:
                authenticated = true
                permitted = false
                break
            case PROJECT_MEMBER:
            case SYS_ADMIN:
                authenticated = true
                permitted = true
        }
    }

    def loadProject(protectionLevel) {

        [
            UNEMBARGOED: Project.findByName('unembargoed'),
            EMBARGOED: Project.findByName('embargoed'),
            PROTECTED: Project.findByName('protected')
        ][protectionLevel]
    }

    def loadFilter(speciesFilterSet) {

        def specoesFilter = [
//                "animal.species.in":["spcode", ""],
animal: [
//            "species.in":["spcode", ""],
species: [
        in:["spcode", "37010003"]
]
]
        ]

        [
            FILTER_SET: specoesFilter,
            FILTER_NOT_SET: null
        ][speciesFilterSet]
    }
}
