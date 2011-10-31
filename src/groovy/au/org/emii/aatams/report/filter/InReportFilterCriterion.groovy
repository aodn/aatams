package au.org.emii.aatams.report.filter

import org.hibernate.criterion.Restrictions

class InReportFilterCriterion extends AbstractReportFilterCriterion
{
	Collection<Object> valuesToMatch
	
	InReportFilterCriterion(String domainClassProperty, Collection<Object> valuesToMatch)
	{
		super(domainClassProperty)
		this.valuesToMatch = valuesToMatch
	}
	
	protected void addRestriction(operandCriteria)
	{
		operandCriteria.add(Restrictions.in(getDomainClassProperty(), valuesToMatch))
	}
}
