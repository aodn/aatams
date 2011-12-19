package au.org.emii.aatams.report.filter

import org.hibernate.criterion.Restrictions

class IsNullReportFilterCriterion extends AbstractReportFilterCriterion 
{
	IsNullReportFilterCriterion(filterParams)
	{
		super(filterParams)
	}
	
	protected void addRestriction(operandCriteria, property, valueToMatch)
	{
		if (valueToMatch)
		{
			// return true if null
			operandCriteria.add(Restrictions.isNull(property))
		}
		else
		{
			operandCriteria.add(Restrictions.isNotNull(property))
		}
	}
}
