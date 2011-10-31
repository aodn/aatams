package au.org.emii.aatams.report.filter

import org.hibernate.criterion.Restrictions

class ReportFilter 
{
	Class domainClass
	
	List<AbstractReportFilterCriterion> criteria = new ArrayList<AbstractReportFilterCriterion>()
	
	ReportFilter(Class domainClass)
	{
		this.domainClass = domainClass
	}
	
	void addCriterion(AbstractReportFilterCriterion criterion)
	{
		criteria.add(criterion)
	}
	
	List apply()
	{
		if (criteria.isEmpty())
		{
			return domainClass.list()
		}
		
		def hibernateCriteria = domainClass.createCriteria()
		
		def results =  hibernateCriteria.list
		{
			applyCriteria(hibernateCriteria)
		}
		
		return results
	}

	private applyCriteria(hibernateCriteria) 
	{
		criteria.each
		{
			it.apply(hibernateCriteria)
		}
	}
}
