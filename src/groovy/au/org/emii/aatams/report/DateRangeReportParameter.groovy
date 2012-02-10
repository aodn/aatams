package au.org.emii.aatams.report

import java.util.Map;

class DateRangeReportParameter extends ReportParameter
{
	Date minRange
	Date maxRange
	
    String getTemplate()
    {
        return "/report/filter/dateRangeTemplate"
    }
	
	String getRestrictionName()
	{
		return "between"
	}
    
    /**
     * Returns the model which can be passed to GSP/render.
     */
    Map getModel()
    {
        def model = super.getModel()
        model.minRange = minRange
        model.maxRange = maxRange
        return model
    }
}
