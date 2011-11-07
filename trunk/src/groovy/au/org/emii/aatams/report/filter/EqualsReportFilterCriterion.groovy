package au.org.emii.aatams.report.filter

import au.org.emii.aatams.PermissionUtilsService
import au.org.emii.aatams.ProjectRole
import au.org.emii.aatams.report.ReportInfoService

import org.hibernate.criterion.Restrictions

class EqualsReportFilterCriterion extends AbstractReportFilterCriterion
{
	EqualsReportFilterCriterion(filterParams)
	{
		super(filterParams)
	}
	
	protected void addRestriction(operandCriteria, property, valueToMatch) 
	{
		// Add each filter value to the criteria.
		if (valueToMatch == ReportInfoService.MEMBER_PROJECTS)
		{
			// Special case for this value.
			def roles = ProjectRole.findAllByPerson(PermissionUtilsService.principal())
			
			operandCriteria.add(Restrictions.in(property, roles*.project.name))
		}
		else
		{
			operandCriteria.add(Restrictions.eq(property, valueToMatch))
		}
	}
}
