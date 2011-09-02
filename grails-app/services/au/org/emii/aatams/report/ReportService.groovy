package au.org.emii.aatams.report

import au.org.emii.aatams.*

/**
 * This service allows clients to:
 *
 * - retrieve a list of available reports, along with applicable filter parameters
 * - execute a query for a given report, applying the given set of filter parameters,
 *   and returning a list of domain objects which can then be passed to the 
 *   jasper reporting "engine".
 *   
 *   TODO: may move modelling of ReportInfo and associated parameters to domain classes.
 */
class ReportService 
{
    static transactional = true

    /**
     * Mapping between report name and relevant domain class.
     * Saves clients from having to know anything about internal package
     * hierarchy etc).
     */
    static def reportMapping =
        ["installation": "au.org.emii.aatams.Installation",
         "receiver": "au.org.emii.aatams.Receiver"]

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
                                                filterParams:receiverFilterParams),
                (Installation.class):new ReportInfo(displayName:"Installations",
                                                    jrxmlFilename:"installationList")]
    }
    
    /**
     * Return reoprt info for a particular domain class.
     */
    ReportInfo getReportInfo(Class domain)
    {
        log.debug("getReportInfo(Class)")
        return getReportInfo().get(domain)
    }
    
    /**
     * Return report info for a particular name.
     */
    ReportInfo getReportInfo(String name)
    {
        log.debug("getReportInfo(String)")
        def domainClassName = reportMapping[name]
        if (!domainClassName)
        {
            log.error("No domain class mapped to report name: " + name)
            return null
        }
        
        log.debug("Report name: " + name + ", domain class name: " + domainClassName)
        
        Class reportDomainClass = Thread.currentThread().contextClassLoader.loadClass(domainClassName)
        return getReportInfo(reportDomainClass)
    }
}
