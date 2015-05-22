package au.org.emii.aatams

import au.org.emii.aatams.detection.*

import static ProtectedSpeciesTests.AuthLevel.*
import static ProtectedSpeciesTests.ProtectionLevel.*
import static ProtectedSpeciesTests.FilterStatus.*
import static au.org.emii.aatams.test.TestUtils.VisibilityLevel.*

class ProtectedSpeciesTests extends AbstractJdbcTemplateVueDetectionFileProcessorServiceIntegrationTests {

    def permissionUtilsService
    def controller
    def kmlService

    enum AuthLevel {
        UNAUTHENTICATED,
        NON_PROJECT_MEMBER,
        PROJECT_MEMBER,
        SYS_ADMIN
    }

    enum ProtectionLevel {
        UNEMBARGOED,
        EMBARGOED,
        PROTECTED_EMBARGOED,
        PROTECTED_EMBARGO_PASSED
    }

    enum FilterStatus {
        FILTER_SET,
        FILTER_NOT_SET
    }

    @Override
    void setUp() {
        super.setUp()

        controller = new DetectionController()
    }

    void testProtectedSpeciesFilteringA() {
        assertCorrectResult(UNAUTHENTICATED, UNEMBARGOED, FILTER_NOT_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringB() {
        assertCorrectResult(UNAUTHENTICATED, UNEMBARGOED, FILTER_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringC() {
        assertCorrectResult(UNAUTHENTICATED, EMBARGOED, FILTER_NOT_SET, VISIBLE_BUT_SANITISED)
    }

    void testProtectedSpeciesFilteringD() {
        assertCorrectResult(UNAUTHENTICATED, EMBARGOED, FILTER_SET, NOT_VISIBLE)
    }

    void testProtectedSpeciesFilteringE() {
        assertCorrectResult(UNAUTHENTICATED, PROTECTED_EMBARGOED, FILTER_NOT_SET, NOT_VISIBLE)
    }

    void testProtectedSpeciesFilteringF() {
        assertCorrectResult(UNAUTHENTICATED, PROTECTED_EMBARGOED, FILTER_SET, NOT_VISIBLE)
    }

    void testProtectedSpeciesFilteringAA() {
        assertCorrectResult(UNAUTHENTICATED, PROTECTED_EMBARGO_PASSED, FILTER_NOT_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringAB() {
        assertCorrectResult(UNAUTHENTICATED, PROTECTED_EMBARGO_PASSED, FILTER_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringG() {
        assertCorrectResult(NON_PROJECT_MEMBER, UNEMBARGOED, FILTER_NOT_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringH() {
        assertCorrectResult(NON_PROJECT_MEMBER, UNEMBARGOED, FILTER_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringI() {
        assertCorrectResult(NON_PROJECT_MEMBER, EMBARGOED, FILTER_NOT_SET, VISIBLE_BUT_SANITISED)
    }

    void testProtectedSpeciesFilteringJ() {
        assertCorrectResult(NON_PROJECT_MEMBER, EMBARGOED, FILTER_SET, NOT_VISIBLE)
    }

    void testProtectedSpeciesFilteringK() {
        assertCorrectResult(NON_PROJECT_MEMBER, PROTECTED_EMBARGOED, FILTER_NOT_SET, NOT_VISIBLE)
    }

    void testProtectedSpeciesFilteringL() {
        assertCorrectResult(NON_PROJECT_MEMBER, PROTECTED_EMBARGOED, FILTER_SET, NOT_VISIBLE)
    }

    void testProtectedSpeciesFilteringAC() {
        assertCorrectResult(NON_PROJECT_MEMBER, PROTECTED_EMBARGO_PASSED, FILTER_NOT_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringAD() {
        assertCorrectResult(NON_PROJECT_MEMBER, PROTECTED_EMBARGO_PASSED, FILTER_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringM() {
        assertCorrectResult(PROJECT_MEMBER, UNEMBARGOED, FILTER_NOT_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringO() {
        assertCorrectResult(PROJECT_MEMBER, UNEMBARGOED, FILTER_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringP() {
        assertCorrectResult(PROJECT_MEMBER, EMBARGOED, FILTER_NOT_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringQ() {
        assertCorrectResult(PROJECT_MEMBER, EMBARGOED, FILTER_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringR() {
        assertCorrectResult(PROJECT_MEMBER, PROTECTED_EMBARGOED, FILTER_NOT_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringS() {
        assertCorrectResult(PROJECT_MEMBER, PROTECTED_EMBARGOED, FILTER_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringAE() {
        assertCorrectResult(PROJECT_MEMBER, PROTECTED_EMBARGO_PASSED, FILTER_NOT_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringAF() {
        assertCorrectResult(PROJECT_MEMBER, PROTECTED_EMBARGO_PASSED, FILTER_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringT() {
        assertCorrectResult(SYS_ADMIN, UNEMBARGOED, FILTER_NOT_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringU() {
        assertCorrectResult(SYS_ADMIN, UNEMBARGOED, FILTER_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringV() {
        assertCorrectResult(SYS_ADMIN, EMBARGOED, FILTER_NOT_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringW() {
        assertCorrectResult(SYS_ADMIN, EMBARGOED, FILTER_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringX() {
        assertCorrectResult(SYS_ADMIN, PROTECTED_EMBARGOED, FILTER_NOT_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringY() {
        assertCorrectResult(SYS_ADMIN, PROTECTED_EMBARGOED, FILTER_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringAG() {
        assertCorrectResult(SYS_ADMIN, PROTECTED_EMBARGO_PASSED, FILTER_NOT_SET, VISIBLE)
    }

    void testProtectedSpeciesFilteringAH() {
        assertCorrectResult(SYS_ADMIN, PROTECTED_EMBARGO_PASSED, FILTER_SET, VISIBLE)
    }

    void assertCorrectResult(authLevel, protectionLevel, speciesFilterSet, expectedResult) {

        configureAccess(authLevel)
        def project = loadProject(protectionLevel)
        def filter = loadFilter(speciesFilterSet)

        assertCorrectResultsForListAction(authLevel, protectionLevel, speciesFilterSet, expectedResult, project, filter)
        assertCorrectResultsForCsvExportAction(authLevel, protectionLevel, speciesFilterSet, expectedResult, project, filter)

        // KML feature disabled for now (see: https://github.com/aodn/aatams/issues/170)
        // assertCorrectResultsForKmlExportAction(authLevel, protectionLevel, speciesFilterSet, expectedResult, project, filter)
    }

    void assertCorrectResultsForListAction(authLevel, protectionLevel, speciesFilterSet, expectedResult, project, filter) {

        def description = "Checking list action: ${authLevel} ${protectionLevel} ${speciesFilterSet} ${expectedResult} p:${project} f:${filter}"

        controller.params.filter = filter

        def resultsForProject = { detection ->

            if (!detection) {
                return false
            }

            detection.project == project
        }

        def resultsFromListAction = controller.list().entityList.grep(resultsForProject)

        switch (expectedResult) {
            case VISIBLE:
                assertEquals(description, 1, resultsFromListAction.size())

                def detection = resultsFromListAction.first()
                assertEquals description, "37010003 - Carcharodon carcharias (White Shark)", detection.speciesName
                assertEquals description, project.tags.sort().first().toString(), detection.sensorId
                break

            case VISIBLE_BUT_SANITISED:
                assertEquals description, 1, resultsFromListAction.size()

                def detection = resultsFromListAction.first()
                assertEquals description, "", detection.speciesName
                assertEquals description, "", detection.sensorId
                break

            case NOT_VISIBLE:
                assertEquals description, [], resultsFromListAction
                break

            default:
                fail "Unhandled visibility $expectedResult"
        }
    }

    void assertCorrectResultsForCsvExportAction(authLevel, protectionLevel, speciesFilterSet, expectedResult, project, filter) {

        def description = "Checking CSV export action: ${authLevel} ${protectionLevel} ${speciesFilterSet} ${expectedResult} p:${project} f:${filter}"

        controller.params.filter = filter

        controller.export()

        def resultsFromExportAction = controller.response.contentAsString
        def resultsForProject = resultsFromExportAction.readLines().findAll{
            def tag = project.tags.sort().first()
            it.contains(tag.toString())
        }

        switch (expectedResult) {
            case VISIBLE:
                assertEquals description, 1, resultsForProject.size()

                def detectionExport = resultsForProject.first()
                assertTrue description, detectionExport.contains(",37010003 - Carcharodon carcharias (White Shark),")
                assertTrue description, detectionExport.contains(",${project.tags.sort().first()},")
                break

            case VISIBLE_BUT_SANITISED:
                assertEquals(description, 1, resultsForProject.size())

                def detectionExport = resultsForProject.first()
                assertFalse description, detectionExport.contains(",37010003 - Carcharodon carcharias (White Shark),")
                break

            case NOT_VISIBLE:
                assertEquals description, [], resultsForProject
                break

            default:
                fail "Unhandled visibility $expectedResult"
        }
    }

    void assertCorrectResultsForKmlExportAction(authLevel, protectionLevel, speciesFilterSet, expectedResult, project, filter) {

        def description = "Checking KML export action: ${authLevel} ${protectionLevel} ${speciesFilterSet} ${expectedResult} p:${project} f:${filter}"

        def params = [
            format: "KMZ (tag tracks)",
            filter: [
                surgeries: [
                    release: [
                        project: [
                            in: ['id', String.valueOf(project.id)]
                        ]
                    ]
                ]
            ],
            allowSanitisedResults: (speciesFilterSet == FILTER_NOT_SET)
        ]


        def kml = kmlService.generateKml(ValidDetection, params)

        def baos = new ByteArrayOutputStream()
        kml.marshal(baos)
        def kmlAsText = new String(baos.toByteArray())

        switch (expectedResult) {
            case VISIBLE:
                assertEquals description, 1, kmlAsText.count('<name>Releases</name>')
                assertEquals description, 1, kmlAsText.count('<name>Detections</name>')
                assertTrue description, kmlAsText.contains("37010003 - Carcharodon carcharias (White Shark)")
                assertTrue description, kmlAsText.contains("${project.tags.sort().first()}")
                break

            case VISIBLE_BUT_SANITISED:
                assertEquals description, 1, kmlAsText.count('<name>Releases</name>')
                assertEquals description, 1, kmlAsText.count('<name>Detections</name>')
                assertFalse description, kmlAsText.contains("37010003 - Carcharodon carcharias (White Shark)")
                assertFalse description, kmlAsText.contains("${project.tags.sort().first()}")
                break

            case NOT_VISIBLE:
                assertEquals description, 0, kmlAsText.count('<name>Releases</name>')
                assertEquals description, 0, kmlAsText.count('<name>Detections</name>')
                assertFalse description, kmlAsText.contains("37010003 - Carcharodon carcharias (White Shark)")
                assertFalse description, kmlAsText.contains("${project.tags.sort().first()}")
                break

            default:
                fail "Unhandled visibility $expectedResult"
        }
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
        Project.findByName(protectionLevel.toString().toLowerCase().replace("_", " "))
    }

    def loadFilter(speciesFilterSet) {

        def speciesFilter = [
            surgeries: [
                release: [
                    animal: [
                        species: [
                            in:["spcode", "37010003"]
                        ]
                    ]
                ]
            ]
        ]

        switch(speciesFilterSet) {
            case FILTER_SET:
                return speciesFilter

            case FILTER_NOT_SET:
                return null

            default:
                fail "Unhandled filter option $speciesFilterSet"
        }
    }
}
