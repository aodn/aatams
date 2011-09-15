package au.org.emii.aatams.report

import org.hibernate.criterion.Restrictions

/**
 * This service allows clients to execute a query for a given report, applying
 * the given set of filter parameters, and returning a list of domain objects
 * which can then be passed to the   jasper reporting "engine".
 */
class ReportQueryExecutorService 
{
    static transactional = true

    def embargoService
    
    /**
     * Executes a query for the named domain class with given filter parameters
     * applied.
     *
     * @param domain    The applicable domain class.
     * @param params    Map (of name to value) parameters.
     * @return          List of matching domain objects.
     */
    List executeQuery(Class domain, Map params)
    {
        log.debug("Executing report query, domain: " + domain + ", params: " + params)
        
        def results = []
        
        if (!params || params.isEmpty())
        {
            // No filter parameters specified, just return all.
            results = domain.list()
        }
        else
        {
            // Filter parameters specified, need to build a criteria.
            def criteria = domain.createCriteria()

            results = criteria.list
            {
                buildCriteriaTree(criteria, null, params)
            }
        }
        
        // Apply embargo filter if necessary
        results = embargoService.applyEmbargo(domain, results)

        return results
    }
    
    void buildCriteriaTree(criteria, rootProperty, params)
    {
        if (rootProperty)
        {
            // Need to create a sub-criteria...
            def associationCriteria = 
                criteria.createCriteria(rootProperty)
            buildCriteriaTree(associationCriteria, params)
        }
        else
        {
            buildCriteriaTree(criteria, params)
        }
    }
    
    void buildCriteriaTree(criteria, params)
    {
        params.each
        {
            property, value ->

            if (isMap(value))
            {
                log.debug("Building criteria tree for property: " + property + ", value: " + value)
                buildCriteriaTree(criteria, property, value)
            }
            else if (isLeaf(property, value))
            {
                log.debug("Building criteria for property: " + property + ", value: " + value)
                buildCriteria(criteria, property, value)
            }
            else
            {
                // Ignore
                log.debug("Ignoring null or blank property: " + property + ", value: " + value)
            }
        }
    }
    
    void buildCriteria(criteria, property, value)
    {
        // Add each filter value to the criteria.
        criteria.add(Restrictions.eq(property, value))
    }

    List executeQuery(String name, Map params)
    {
        log.debug("getReportInfo(String)")
        def domainClassName = reportMapping[name]
        if (!domainClassName)
        {
            log.error("No domain class mapped to report name: " + name)
            return null
        }

        Class reportDomainClass = Thread.currentThread().contextClassLoader.loadClass(domainClassName)
        return executeQuery(reportDomainClass, params)
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
