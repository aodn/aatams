package au.org.emii.aatams.report.filter

import org.hibernate.criterion.Restrictions

class BetweenReportFilterCriterion extends AbstractReportFilterCriterion
{
	Object minValue
	Object maxValue
	
	BetweenReportFilterCriterion(String domainClassProperty, List<Object> valuesToMatch)
	{
		super(domainClassProperty)
		
		assert(valuesToMatch.size() == 2)
		minValue = valuesToMatch.get(0)
		maxValue = valuesToMatch.get(1)
	}
	
	protected void addRestriction(operandCriteria)
	{
		operandCriteria.add(Restrictions.between(getDomainClassProperty(), minValue, maxValue))
	}
}
