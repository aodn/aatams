package au.org.emii.aatams.report.filter

import java.util.List
import org.apache.log4j.Logger

abstract class AbstractReportFilterCriterion 
{
	protected static final log = Logger.getLogger(AbstractReportFilterCriterion.class)
	Map filterParams
	
	AbstractReportFilterCriterion(filterParams)
	{
		this.filterParams = filterParams
	}

	void apply(criteria)
	{
		buildCriteriaTree(criteria, filterParams)
	}
	
	private void createAssociationCriteria(criteria, rootProperty, params)
	{
		// Need to create a sub-criteria...
		def associationCriteria =
			criteria.createCriteria(rootProperty)
		buildCriteriaTree(associationCriteria, params)
	}
	
	private void buildCriteriaTree(criteria, params)
	{
		params.each
		{
			property, value ->

			if (isMap(value))
			{
				log.debug("Building criteria tree for property: " + property + ", value: " + value)
				createAssociationCriteria(criteria, property, value)
			}
			else if (isLeaf(property, value))
			{
				log.debug("Building criteria for property: " + property + ", value: " + value)
				addRestriction(criteria, property, value)
			}
			else
			{
				// Ignore
				log.debug("Ignoring null or blank property: " + property + ", value: " + value)
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((filterParams == null) ? 0 : filterParams.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractReportFilterCriterion other = (AbstractReportFilterCriterion) obj;
		if (filterParams == null) {
			if (other.filterParams != null)
				return false;
		} else if (!filterParams.equals(other.filterParams))
			return false;
		return true;
	}

    /**
     * Returns true if the given parameter is a Map (of parameters).
     */
    boolean isMap(param)
    {
        if (!param)
        {
            return false
        }
        
        return (param instanceof Map)
    }
    
    /**
     * Returns true if the given parameter is a single, non-null, non-empty
     * value.
     */
    boolean isLeaf(property, value)
    {
        log.debug("Checking value: " + value)
        
        if (!value)
        {
            log.debug("Not a leaf")
            return false
        }
        
        if (property.contains("."))
        {
            log.debug("Not a leaf")
            return false
        }
        
        if (isMap(value))
        {
            log.debug("Not a leaf")
            return false
        }
        
        if (value == "")
        {
            log.debug("Not a leaf")
            return false
        }
        
        log.debug("Is a leaf")
        return true
    }
}
