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
        log.debug("indexWithSession, params: " + params)
        
        def testModel = this.getProperties().containsKey('chainModel') ? chainModel : null
        
        // Re-attach model objects to avoid LazyInitializationException.
        def session = sessionFactory.getCurrentSession()
        testModel?.data?.each
        {
            session.update(it)
        }
        
        // Get the filter parameters out of flash and put in to "params" (which
        // is where they need to be, but we put in flash in the report controller
        // to avoid them being converted to toString() representation).
        params.FILTER_PARAMS = flash.FILTER_PARAMS.collect
        {
            key, value ->
            
            new KeyValueBean(key:key, value:String.valueOf(value))
        }
        
//        params.FILTER_PARAMS = [user: "Joe Bloggs", project: "Seal Count"]
        
        return index()
    }
}
