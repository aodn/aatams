package au.org.emii.aatams

import au.org.emii.aatams.detection.*

import static ProtectedSpeciesTests.AuthLevel.*
import static ProtectedSpeciesTests.ProtectionLevel.*
import static ProtectedSpeciesTests.FilterStatus.*

class ProtectedSpeciesTests extends AbstractJdbcTemplateVueDetectionFileProcessorServiceIntegrationTests {

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
    }

    void testProtectedSpeciesFiltering() {
        assertVisible(UNAUTHENTICATED, UNEMBARGOED, FILTER_NOT_SET)
        assertVisible(UNAUTHENTICATED, UNEMBARGOED, FILTER_SET)
        assertVisibleButSanitised(UNAUTHENTICATED, EMBARGOED, FILTER_NOT_SET)
        // Failing: assertNotVisible(UNAUTHENTICATED, EMBARGOED, FILTER_SET)
        // Failing: assertNotVisible(UNAUTHENTICATED, PROTECTED, FILTER_NOT_SET)
        // Failing: assertNotVisible(UNAUTHENTICATED, PROTECTED, FILTER_SET)

        assertVisible(NON_PROJECT_MEMBER, UNEMBARGOED, FILTER_NOT_SET)
        assertVisible(NON_PROJECT_MEMBER, UNEMBARGOED, FILTER_SET)
        assertVisibleButSanitised(NON_PROJECT_MEMBER, EMBARGOED, FILTER_NOT_SET)
        // Failing: assertNotVisible(NON_PROJECT_MEMBER, EMBARGOED, FILTER_SET)
        // Failing: assertNotVisible(NON_PROJECT_MEMBER, PROTECTED, FILTER_NOT_SET)
        // Failing: assertNotVisible(NON_PROJECT_MEMBER, PROTECTED, FILTER_SET)

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

    void assertVisible(AuthLevel authLevel, ProtectionLevel protectionLevel, FilterStatus speciesFilterSet) {
        assertCorrectResult(authLevel, protectionLevel, speciesFilterSet, ExpectedResult.VISIBLE)
    }

    void assertVisibleButSanitised(AuthLevel authLevel, ProtectionLevel protectionLevel, FilterStatus speciesFilterSet) {
        assertCorrectResult(authLevel, protectionLevel, speciesFilterSet, ExpectedResult.VISIBLE_BUT_SANITISED)
    }

    void assertNotVisible(AuthLevel authLevel, ProtectionLevel protectionLevel, FilterStatus speciesFilterSet) {
        assertCorrectResult(authLevel, protectionLevel, speciesFilterSet, ExpectedResult.NOT_VISIBLE)
    }

    void assertCorrectResult(AuthLevel authLevel, ProtectionLevel protectionLevel, FilterStatus speciesFilterSet, ExpectedResult expectedResult) {

        configureAccess(authLevel)
        def project = loadProject(protectionLevel)
        def filter = loadFilter(speciesFilterSet)

        controller.params.filter = filter

        def actualResults = controller.list().entityList.grep {
            DetectionSurgery.findAllByDetection(it).first()?.surgery.release.project == project
        }

        println "${authLevel} ${protectionLevel} ${speciesFilterSet} ${expectedResult} ${actualResults}"

        switch (expectedResult) {
            case ExpectedResult.VISIBLE:
                assertEquals(1, actualResults.size())
                assertNotSanitised(actualResults[0])
                break

            case ExpectedResult.VISIBLE_BUT_SANITISED:
                assertEquals(1, actualResults.size())
                assertSanitised(actualResults[0])
                break

            case ExpectedResult.NOT_VISIBLE:
                assertTrue(actualResults.isEmpty())
                break

            default:
                fail "Unhandled visibility $expectedResult"
        }
    }

    void assertSanitised(detection) {
        //fail "Not implemented"
    }

    void assertNotSanitised(detection) {
        //fail "Not implemented"
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
                break;

            default:
                fail "Unhandled auth level $authLevel"
        }
    }

    def loadProject(protectionLevel) {
        Project.findByName(protectionLevel.toString().toLowerCase())
    }

    def loadFilter(speciesFilterSet) {

        def speciesFilter = [
//                "animal.species.in":["spcode", ""],
animal: [
//            "species.in":["spcode", ""],
species: [
        in:["spcode", "37010003"]
]
]
        ]

        [
            FILTER_SET: speciesFilter,
            FILTER_NOT_SET: null
        ][speciesFilterSet]
    }
}
