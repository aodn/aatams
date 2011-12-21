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
	
	private def createCriteria()
	{
		def hibernateCriteria = domainClass.createCriteria()
		hibernateCriteria.getInstance()?.setCacheable(true)
		return hibernateCriteria
	}
	
	List list(params)
	{
		if (criteria.isEmpty())
		{
			return domainClass.list(params)
		}
		
		def criteria = createCriteria()
		def results = criteria.list(params)
		{
			applyCriteria(criteria)
		}
		
		return results
	}

	Long count()
	{
		if (criteria.isEmpty())
		{
			return domainClass.count()
		}
		
		def criteria = createCriteria()
		return criteria.count { applyCriteria(criteria) }
	}
	
	private applyCriteria(hibernateCriteria) 
	{
		criteria.each
		{
			it.apply(hibernateCriteria)
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((criteria == null) ? 0 : criteria.hashCode());
		result = prime * result
				+ ((domainClass == null) ? 0 : domainClass.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportFilter other = (ReportFilter) obj;
		if (criteria == null) {
			if (other.criteria != null)
				return false;
		} else if (!criteria.equals(other.criteria))
			return false;
		if (domainClass == null) {
			if (other.domainClass != null)
				return false;
		} else if (!domainClass.equals(other.domainClass))
			return false;
		return true;
	}
}
