package au.org.emii.aatams.report

/**
 * Extends the default plugin version by re-attaching model objects in order to
 * avoid LazyInitializationException.
 */
class JasperController extends org.codehaus.groovy.grails.plugins.jasper.JasperController
{
    def sessionFactory

    def indexWithSession = 
    {
        def testModel = this.getProperties().containsKey('chainModel') ? chainModel : null

        // Re-attach model objects to avoid LazyInitializationException.
        def session = sessionFactory.getCurrentSession()
        testModel?.data?.each
        {
            session.update(it)
        }

        return index()
    }
}
