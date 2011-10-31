package au.org.emii.aatams.report.filter

import org.hibernate.criterion.Restrictions

class EqualsReportFilterCriterion extends AbstractReportFilterCriterion
{
	Object valueToMatch
	
	EqualsReportFilterCriterion(String domainClassProperty, Object valueToMatch)
	{
		super(domainClassProperty)
		this.valueToMatch = valueToMatch
	}
	
	protected void addRestriction(operandCriteria) 
	{
		operandCriteria.add(Restrictions.eq(getDomainClassProperty(), valueToMatch))
	}
}
