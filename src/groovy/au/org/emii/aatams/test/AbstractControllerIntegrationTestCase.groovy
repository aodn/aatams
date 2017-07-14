package au.org.emii.aatams.test

import au.org.emii.aatams.Person

class AbstractControllerIntegrationTestCase extends AbstractControllerTestCase {

    protected void setUp() {
        super.setUp()

        user = Person.findByUsername('jkburges')
    }
}
