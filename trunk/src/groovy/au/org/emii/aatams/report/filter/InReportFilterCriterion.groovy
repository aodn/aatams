package au.org.emii.aatams.report.filter

import org.hibernate.criterion.Restrictions

class InReportFilterCriterion extends AbstractReportFilterCriterion
{
	InReportFilterCriterion(filterParams)
	{
		super(filterParams)
	}
	
	protected void addRestriction(operandCriteria, property, commaSeparatedListOfValues)
	{
		List<String> valuesToMatch = Arrays.asList(commaSeparatedListOfValues.split(","))
		valuesToMatch = valuesToMatch.collect { it.trim() }
		operandCriteria.add(Restrictions.in(property, valuesToMatch))
	}
}
