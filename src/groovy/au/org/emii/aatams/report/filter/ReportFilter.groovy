package au.org.emii.aatams.report.filter

import org.hibernate.criterion.Restrictions

class ReportFilter 
{
	Class domainClass
	
	List<AbstractReportFilterCriterion> criteria = new ArrayList<AbstractReportFilterCriterion>()
	
	Integer max
	Integer offset
	
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
			return domainClass.list(max:max, offset:offset)
		}
		
		def hibernateCriteria = domainClass.createCriteria()
		hibernateCriteria.getInstance()?.setCacheable(true)
		
		paginate(hibernateCriteria)
		
		def results =  hibernateCriteria.list
		{
			applyCriteria(hibernateCriteria)
		}
		
		return results
	}

	private def paginate( hibernateCriteria) 
	{
		if (max)
		{
			hibernateCriteria.getInstance()?.setMaxResult(max)
		}

		if (offset)
		{
			hibernateCriteria.getInstance()?.setFirstResult(offset)
		}
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
