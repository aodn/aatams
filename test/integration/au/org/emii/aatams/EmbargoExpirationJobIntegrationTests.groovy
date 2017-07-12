package au.org.emii.aatams

import grails.test.*

class EmbargoExpirationJobIntegrationTests extends GroovyTestCase {
    def grailsApplication

    void testFindEmbargoedReleasesToday() {
        EmbargoExpirationJob.triggerNow()
    }

    void testCreateLink() {
        def embargoExpirationJob = new EmbargoExpirationJob()
        embargoExpirationJob.grailsApplication = grailsApplication

        assertEquals(
            "http://localhost:8090/aatams/animalRelease/show/123",
            embargoExpirationJob.createLink(
                [
                    controller: 'animalRelease',
                    action: 'show',
                    id: 123
                ]
            )
        )
    }
}
