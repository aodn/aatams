package au.org.emii.aatams.report

/**
 *
 * @author jburgess
 */
class ReportInfo 
{
    /**
     * The name to be displayed to users.
     */
    String displayName
    
    /**
     * The name of the associated jrxml file.
     * Maps report type (i.e. "report" or "extract" to name.
     */
    Map<String, String> jrxmlFilename
    
    /**
     * List of filter parameters, which encapsulates the type (e.g. list, number)
     * and the valid range of values as well as a display name.
     */
    List<ReportParameter> filterParams
    
    String toString()
    {
        return displayName
    }
}

