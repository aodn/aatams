package au.org.emii.aatams.report

/**
 * A report parameter which can be represented by a HTML select.
 * 
 * @author jburgess
 */
class ListReportParameter extends ReportParameter {
    /**
     * The range of values to select from.
     */
    List range
    
    String getTemplate() {
        return "/report/filter/listTemplate"
    }
    
    String getRestrictionName() {
        return "eq"
    }
    
    /**
     * Returns the model which can be passed to GSP/render.
     */
    Map getModel() {
        def model = super.getModel()
        model.range = range
        return model
    }
}

