package au.org.emii.aatams.report

import java.util.Map;

class AjaxMultiSelectReportParameter extends ReportParameter  {
    String lookupPath
    
    @Override
    public String getTemplate()  {
        return "/report/filter/ajaxMultiSelectTemplate"
    }
    
    String getRestrictionName() {
        return "in"
    }

    @Override
    public Map getModel()  {
        def model = super.getModel()
        model.lookupPath = lookupPath
        return model
    }
}
