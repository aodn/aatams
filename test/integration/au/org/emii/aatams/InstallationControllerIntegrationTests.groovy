package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase;
import grails.test.*

class InstallationControllerIntegrationTests extends AbstractControllerUnitTestCase {
    def grailsApplication

    List<Installation> installations = []
    int numInstallations = 10

    protected void setUp() {
        super.setUp()

        installations.addAll(Installation.list())

        createInstallationsForProject(Project.findByName("Tuna"))
        createInstallationsForProject(Project.findByName("Seal Count"))
    }

    private void createInstallationsForProject(Project project) {
        InstallationConfiguration array = InstallationConfiguration.findByType('ARRAY')

        assert(project)
        numInstallations.times {
            Installation installation = new Installation(project: project, name: project.name + " installation " + it, configuration:array)
            installation.save()
            installations.add(installation)
        }
    }

    protected void tearDown() {
        installations.each {
            it.delete()
        }

        super.tearDown()
    }

    void testListPagination() {
        int max = 5
        int offset = 0

        3.times {
            assertListPagination(max, offset, installations)
            offset += max
        }
    }

    void testListWithFilter() {
        controller.params.filter = [project: [eq: ["name", "Tuna"]]]

        def matchingInstallations = installations.grep { it.project.name == "Tuna" }
        assertListPagination(null, null, matchingInstallations)
    }

    void testListSortByName() {
        assertListSort([sort:"name", order: "asc", max: 50], installations)
    }

    void testListSortWithFilter() {
        controller.params.filter = [project: [eq: ["name", "Tuna"]]]
        def matchingInstallations = installations.grep { it.project.name == "Tuna" }
        assertListSort([sort:"name", order: "asc", max: 50], matchingInstallations)
    }

    void testListSortPaginateWithFilter() {
        int max = 5
        int offset = 0

        def matchingInstallations = installations.grep { it.project.name == "Tuna" }

        2.times {
            controller.params.filter = [project: [eq: ["name", "Tuna"]]]
            assertListSort([sort:"name", order: "asc", max: 5], matchingInstallations)
            offset += max
        }
    }

    private void assertListSort(params, candidateInstallations) {
        controller.params.putAll(params)
        def sortedInstallations = candidateInstallations.sort {
            a, b ->

            a[params.sort] <=> b[params.sort]
        }

        assertListPagination(params.max, params.offset, sortedInstallations)
    }

    private void assertListPagination(def max, def offset, List matchingInstallations) {
        int maxResults = max?: matchingInstallations.size()
        maxResults = Math.min(maxResults, matchingInstallations.size())

        if (max) {
            controller.params.max = max
        }

        if (offset) {
            controller.params.offset = offset
        }
        int offsetResults = offset?: 0

        def model = controller.list()

        assertEquals(maxResults, model.entityList.size())
        assertEquals(matchingInstallations.size(), model.total)
        assertEquals(
            matchingInstallations[offsetResults..(offsetResults + maxResults - 1)]*.name.sort(),
            model.entityList*.name.sort()
        )
    }
}
