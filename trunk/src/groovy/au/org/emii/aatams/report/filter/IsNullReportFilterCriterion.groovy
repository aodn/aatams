package au.org.emii.aatams.report.filter

import org.hibernate.criterion.CriteriaSpecification
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
			// Doesn't work, see: http://community.jboss.org/wiki/HibernateFAQ-AdvancedProblems#The_query_language_IS_NULL_syntax_wont_work_with_a_onetoone_association
//			operandCriteria.add(Restrictions.isNull(property))
			def alias = operandCriteria.createAlias("recovery", "listRecovery", CriteriaSpecification.LEFT_JOIN)
			alias.add(Restrictions.sqlRestriction("recoverer_id IS NULL"))
		}
		else
		{
//			println("Adding isNotNull restriction for property: " + property)
//			operandCriteria.add(Restrictions.isNotNull(property))
		}
	}
}
