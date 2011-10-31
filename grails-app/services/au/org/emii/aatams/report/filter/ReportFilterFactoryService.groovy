package au.org.emii.aatams.report.filter

class ReportFilterFactoryService 
{
    static transactional = true

    ReportFilter newFilter(Class domain, Map params) 
	{
		ReportFilter filter = new ReportFilter(domain)
		
		if (!params)
		{ 
			return filter
		}
		
		if (params.eq)
		{
			filter.addCriterion(new EqualsReportFilterCriterion(params.eq))
		}
		
		if (params.in)
		{
			filter.addCriterion(new InReportFilterCriterion(params.in))
		}
		
		if (params.between)
		{
			filter.addCriterion(new BetweenReportFilterCriterion(params.between))
		}
		
		return filter
    }
}
