package au.org.emii.aatams.report

/**
 * A report parameter which can be represented by a HTML select.
 * 
 * @author jburgess
 */
class ListReportParameter extends ReportParameter
{
    /**
     * The range of values to select from.
     */
    List range
    
    Class getType()
    {
        return List.class
    }
}

