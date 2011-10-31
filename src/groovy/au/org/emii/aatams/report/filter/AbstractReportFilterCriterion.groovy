package au.org.emii.aatams.report.filter

import java.util.List;

abstract class AbstractReportFilterCriterion 
{
	String fullyQualifiedDomainClassProperty
	
	AbstractReportFilterCriterion(String domainClassProperty)
	{
		this.fullyQualifiedDomainClassProperty = domainClassProperty
	}

	void apply(criteria)
	{
		List<String> associations = getAssociations()

		def operandCriteria = criteria
		associations.each
		{
			operandCriteria = operandCriteria.createCriteria(it)
		}		
		
		addRestriction(operandCriteria)
	}
	
	private List<String> getAssociations()
	{
		String[] associationsArray = fullyQualifiedDomainClassProperty.split("\\.")
		def retList = new ArrayList(Arrays.asList(associationsArray))
		retList.remove(associationsArray.length - 1)
		
		return retList
	}

	private String getDomainClassProperty()
	{
		String[] associationsArray = fullyQualifiedDomainClassProperty.split("\\.")
		
		return associationsArray[associationsArray.length - 1]
	}

	protected abstract void addRestriction(operandCriteria)
}
