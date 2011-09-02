package au.org.emii.aatams.report

import au.org.emii.aatams.*

/**
 * This service allows clients to:
 *
 * - retrieve a list of available reports, along with applicable filter parameters
 * - execute a query for a given report, applying the given set of filter parameters,
 *   and returning a list of domain objects which can then be passed to the 
 *   jasper reporting "engine".
 */
class ReportService 
{
    static transactional = true

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
        if (!params || params.isEmpty())
        {
            // No filter parametes specified, just return all.
            return domain.list()
        }
        
        // Filter parameters specified, need to build a criteria.
        def criteria = domain.createCriteria()

        def results = criteria.list
        {
            // Add each filter value to the criteria.
            params.each
            {
                property, value ->

                criteria.eq(property, value)
            }
        }
        
        return results
    }

    /**
     * Return info for all available reports (keyed by the domain class).
     */
    Map<Class, ReportInfo> getReportInfo() 
    {
        def projectRange = Project.list()*.name
        
        def installationRange = Installation.list()*.name
        def receiverFilterParams = 
            [new ListReportParameter(domainName: "project", propertyName:"name", range:projectRange),
             new ListReportParameter(domainName: "installation", propertyName:"name", range:installationRange)]
        
        return [(Receiver.class):new ReportInfo(displayName:"Receivers", 
                                        jrxmlFilename:"receiverList", 
                                        filterParams:receiverFilterParams)]
    }
    
    /**
     * Return reoprt info for a particular domain class.
     */
    ReportInfo getReportInfo(Class domain)
    {
        return getReportInfo().get(domain)
    }
}
