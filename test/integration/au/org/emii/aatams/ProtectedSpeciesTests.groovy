package au.org.emii.aatams

import au.org.emii.aatams.detection.*
import au.org.emii.aatams.test.AbstractControllerUnitTestCase

import static ProtectedSpeciesTests.AuthLevel.*
import static ProtectedSpeciesTests.ProtectionLevel.*
import static ProtectedSpeciesTests.FilterStatus.*
import static ProtectedSpeciesTests.ExpectedResult.*

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

        AbstractControllerUnitTestCase.createDetectionViews(dataSource)// Todo - DN: Is this a smell?
    }

    void testProtectedSpeciesFilteringA() {
        assertVisible(UNAUTHENTICATED, UNEMBARGOED, FILTER_NOT_SET)
    }

    void testProtectedSpeciesFilteringB() {
        assertVisible(UNAUTHENTICATED, UNEMBARGOED, FILTER_SET)
    }

    void testProtectedSpeciesFilteringC() {
        assertVisibleButSanitised(UNAUTHENTICATED, EMBARGOED, FILTER_NOT_SET)
    }

    void testProtectedSpeciesFilteringD() {
        assertNotVisible(UNAUTHENTICATED, EMBARGOED, FILTER_SET)
    }

    void testProtectedSpeciesFilteringE() {
        assertNotVisible(UNAUTHENTICATED, PROTECTED, FILTER_NOT_SET)
    }

    void testProtectedSpeciesFilteringF() {
        assertNotVisible(UNAUTHENTICATED, PROTECTED, FILTER_SET)
    }

    void testProtectedSpeciesFilteringG() {
        assertVisible(NON_PROJECT_MEMBER, UNEMBARGOED, FILTER_NOT_SET)
    }

    void testProtectedSpeciesFilteringH() {
        assertVisible(NON_PROJECT_MEMBER, UNEMBARGOED, FILTER_SET)
    }

    void testProtectedSpeciesFilteringI() {
        assertVisibleButSanitised(NON_PROJECT_MEMBER, EMBARGOED, FILTER_NOT_SET)
    }

    void testProtectedSpeciesFilteringJ() {
        assertNotVisible(NON_PROJECT_MEMBER, EMBARGOED, FILTER_SET)
    }

    void testProtectedSpeciesFilteringK() {
        assertNotVisible(NON_PROJECT_MEMBER, PROTECTED, FILTER_NOT_SET)
    }

    void testProtectedSpeciesFilteringL() {
        assertNotVisible(NON_PROJECT_MEMBER, PROTECTED, FILTER_SET)
    }

    void testProtectedSpeciesFilteringM() {
        assertVisible(PROJECT_MEMBER, UNEMBARGOED, FILTER_NOT_SET)
    }

    void testProtectedSpeciesFilteringO() {
        assertVisible(PROJECT_MEMBER, UNEMBARGOED, FILTER_SET)
    }

    void testProtectedSpeciesFilteringP() {
        assertVisible(PROJECT_MEMBER, EMBARGOED, FILTER_NOT_SET)
    }

    void testProtectedSpeciesFilteringQ() {
        assertVisible(PROJECT_MEMBER, EMBARGOED, FILTER_SET)
    }

    void testProtectedSpeciesFilteringR() {
        assertVisible(PROJECT_MEMBER, PROTECTED, FILTER_NOT_SET)
    }

    void testProtectedSpeciesFilteringS() {
        assertVisible(PROJECT_MEMBER, PROTECTED, FILTER_SET)
    }

    void testProtectedSpeciesFilteringT() {
        assertVisible(SYS_ADMIN, UNEMBARGOED, FILTER_NOT_SET)
    }

    void testProtectedSpeciesFilteringU() {
        assertVisible(SYS_ADMIN, UNEMBARGOED, FILTER_SET)
    }

    void testProtectedSpeciesFilteringV() {
        assertVisible(SYS_ADMIN, EMBARGOED, FILTER_NOT_SET)
    }

    void testProtectedSpeciesFilteringW() {
        assertVisible(SYS_ADMIN, EMBARGOED, FILTER_SET)
    }

    void testProtectedSpeciesFilteringX() {
        assertVisible(SYS_ADMIN, PROTECTED, FILTER_NOT_SET)
    }

    void testProtectedSpeciesFilteringY() {
        assertVisible(SYS_ADMIN, PROTECTED, FILTER_SET)
    }

    void assertVisible(AuthLevel authLevel, ProtectionLevel protectionLevel, FilterStatus speciesFilterSet) {
        assertCorrectResult(authLevel, protectionLevel, speciesFilterSet, VISIBLE)
    }

    void assertVisibleButSanitised(AuthLevel authLevel, ProtectionLevel protectionLevel, FilterStatus speciesFilterSet) {
        assertCorrectResult(authLevel, protectionLevel, speciesFilterSet, VISIBLE_BUT_SANITISED)
    }

    void assertNotVisible(AuthLevel authLevel, ProtectionLevel protectionLevel, FilterStatus speciesFilterSet) {
        assertCorrectResult(authLevel, protectionLevel, speciesFilterSet, NOT_VISIBLE)
    }

    void assertCorrectResult(AuthLevel authLevel, ProtectionLevel protectionLevel, FilterStatus speciesFilterSet, ExpectedResult expectedResult) {

        configureAccess(authLevel)
        def project = loadProject(protectionLevel)
        def filter = loadFilter(speciesFilterSet)

        assertCorrectResultsForListAction(authLevel, protectionLevel, speciesFilterSet, expectedResult, project, filter)
        assertCorrectResultsForCsvExportAction(authLevel, protectionLevel, speciesFilterSet, expectedResult, project, filter)
        assertCorrectResultsForKmlExportAction(authLevel, protectionLevel, speciesFilterSet, expectedResult, project, filter)
    }

    void assertCorrectResultsForListAction(AuthLevel authLevel, ProtectionLevel protectionLevel, FilterStatus speciesFilterSet, ExpectedResult expectedResult, project, filter) {

        def description = "Checking list action:   ${authLevel} ${protectionLevel} ${speciesFilterSet} ${expectedResult} p:${project} f:${filter}"

        println description

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
                assertEquals description, "37010003 - Carcharodon carcharias (White Shark)", detection.speciesNames

                println "___________________________________________"
                println project.tags.sort().first().toString()
                println detection.getSensorIds(detection.mostRecentSurgery)
                println "___________________________________________"

                // assertEquals description, project.tags.sort().first().toString(), detection.getSensorIds(detection.mostRecentSurgery) // Todo - DN: REINTRODUCE THIS TEST (Check w/ Jon re: changes to getSurgeries())
                break

            case VISIBLE_BUT_SANITISED:
                assertEquals description, 1, resultsFromListAction.size()

                def detection = resultsFromListAction.first()
                assertEquals description, "", detection.speciesNames
                assertEquals description, "", detection.sensorIds
                break

            case NOT_VISIBLE:
                assertEquals description, [], resultsFromListAction
                break

            default:
                fail "Unhandled visibility $expectedResult"
        }
    }

    void assertCorrectResultsForCsvExportAction(AuthLevel authLevel, ProtectionLevel protectionLevel, FilterStatus speciesFilterSet, ExpectedResult expectedResult, project, filter) {

        def description = "Checking CSV export action: ${authLevel} ${protectionLevel} ${speciesFilterSet} ${expectedResult} p:${project} f:${filter}"

        println description

        controller.params.filter = filter

        controller.export()

        def resultsFromExportAction = controller.response.contentAsString
        def resultsForProject = resultsFromExportAction.readLines().findAll{
            def tag = project.tags.sort().first()
            println "Finding '$tag' in '$it'"
            it.contains(tag.toString())
        }

        println "${resultsFromExportAction.readLines().size()} -> ${resultsForProject.size()}"

        switch (expectedResult) {
            case VISIBLE:
                assertEquals description, 1, resultsForProject.size()

                println "project.tags.sort().first() - ${project.tags.sort().first()}"

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

    void assertCorrectResultsForKmlExportAction(AuthLevel authLevel, ProtectionLevel protectionLevel, FilterStatus speciesFilterSet, ExpectedResult expectedResult, project, filter) {

        def description = "Checking KML export action: ${authLevel} ${protectionLevel} ${speciesFilterSet} ${expectedResult} p:${project} f:${filter}"

        println description

        def params = [
            format: "KMZ (tag tracks)",
            filter: [
                detectionSurgeries: [
                    surgery: [
                        release: [
                            project: [
                                in: ['id', String.valueOf(project.id)]
                            ]
                        ]
                    ]
                ]
            ],
            allowSanitisedResults: (speciesFilterSet == FILTER_NOT_SET)
        ]


        def kml = kmlService.generateKml(ValidDetection, params)

        println "KML"
        println "${kml.class}"
        println "${kml}"

        def baos = new ByteArrayOutputStream()
        kml.marshal(baos)
        def kmlAsText = new String(baos.toByteArray())

        println "_____________________________________________-"
        println "kmlAsText: ${kmlAsText.size()}"
        println kmlAsText
        println "_____________________________________________-"

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
        Project.findByName(protectionLevel.toString().toLowerCase())
    }

    def loadFilter(speciesFilterSet) {

/*        def speciesFilter = [ // Todo - DN: "Definitely wrong" - jkburges
                              // "animal.species.in":["spcode", ""],
            animal: [
                // "species.in":["spcode", ""],
                species: [
                    in:["spcode", "37010003"]
                ]
            ]
        ]*/

        // params.filter?.detectionSurgeries?.surgery?.release?.animal?.species?.in.grep{
        def speciesFilter = [
            detectionSurgeries: [
                surgery: [
                    release: [
                        animal: [
                            species: [
                                in:["spcode", "37010003"]
                            ]
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
