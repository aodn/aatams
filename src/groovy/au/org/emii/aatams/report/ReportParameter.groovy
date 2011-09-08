package au.org.emii.aatams.report

/**
 * Represents a report filter parameter, including the property of the associated
 * domain class that it pertains to.  The property may be indirect, e.g. for
 * a report on receivers, the property may be 
 * "deployments.station.installation.project.name" to allow filtering by project 
 * name.
 * 
 * Subclasses may specify the valid range of values (e.g. for parameters which 
 * may be selected from a list).
 * 
 * @author jburgess
 */
abstract class ReportParameter
{
    /**
     * The label which is presented to the user (e.g. for project name, we may
     * only want to display "project", name is implicit).
     */
    String label
    
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
    /**
     * Returns the model which can be passed to GSP/render.
     */
    Map getModel()
    {
        return [label:label,
                propertyName:propertyName]
    }
    
    String toString()
    {
        return label + ":" + propertyName + ":" + type + ":" + template + ":" + model
    }
}

