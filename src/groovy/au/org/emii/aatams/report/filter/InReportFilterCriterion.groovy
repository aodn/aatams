package au.org.emii.aatams.report.filter

import org.hibernate.criterion.Restrictions

class InReportFilterCriterion extends AbstractReportFilterCriterion
{
	InReportFilterCriterion(filterParams)
	{
		super(filterParams)
	}
	
	protected void addRestriction(operandCriteria, property, valuesToMatch)
	{
		println("Adding in restriction, property: "+ property + ", values: " + valuesToMatch)
		operandCriteria.add(Restrictions.in(property, valuesToMatch))
	}
}
