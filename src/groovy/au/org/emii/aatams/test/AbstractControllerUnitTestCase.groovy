package au.org.emii.aatams.test

import au.org.emii.aatams.CaabSpecies

abstract class AbstractControllerUnitTestCase extends AbstractControllerTestCase {
    @Override
    protected void tearDown() {
        // Fix pollution of integration test classes with mocked classes (CaabSpecies)
        // https://rewoo.wordpress.com/2012/08/07/polluted-mocked-test-data-from-unit-in-integration-test-using-grails-1-3-5/
        // No idea why I need this here.  Without it I get a "not-null property references a null or transient value" error for CaabSpecies
        // in DevelopmentDataInitialiser.groovy:631 because CaabSpecies has mock test instances

        GroovySystem.metaClassRegistry.removeMetaClass CaabSpecies.class

        super.tearDown()
    }
}
