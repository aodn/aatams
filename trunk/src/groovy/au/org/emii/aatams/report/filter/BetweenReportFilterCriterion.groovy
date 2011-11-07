package au.org.emii.aatams.report.filter

import org.hibernate.criterion.Restrictions

class BetweenReportFilterCriterion extends AbstractReportFilterCriterion
{
	BetweenReportFilterCriterion(filterParams)
	{
		super(filterParams)
	}
	
	protected void addRestriction(operandCriteria, property, valuesToMatch)
	{
		println("property: " + property + ", valuesToMatch: " + valuesToMatch)
		operandCriteria.add(Restrictions.between(property, valuesToMatch.get(0), valuesToMatch.get(1)))
	}
}
