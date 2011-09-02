package au.org.emii.aatams.report

/**
 *
 * @author jburgess
 */
abstract class ReportParameter
{
    /**
     * The name of the domain (e.g. "project", "receiver" etc).
     */
    String domainName
    
    /**
     * The domain object's property that this parameter pertains to.
     */
    String propertyName
    
    abstract Class getType()
    
    /**
     * Returns the associated GSP template (used to render this parameter as GSP).
     */
    abstract String getTemplate()
    
    /**
     * Returns the model which can be passed to GSP/render.
     */
    abstract Map getModel()
}

