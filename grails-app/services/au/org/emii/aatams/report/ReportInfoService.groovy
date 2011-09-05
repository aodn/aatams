package au.org.emii.aatams.report

import au.org.emii.aatams.*

/**
 * This service allows clients to  retrieve a list of available reports, along
 * with applicable filter parameters
 *   
 *   TODO: may move modelling of ReportInfo and associated parameters to domain classes.
 */
class ReportInfoService 
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
     * Return info for all available reports (keyed by the domain class).
     */
    Map<Class, ReportInfo> getReportInfo() 
    {
        def projectRange = Project.list()*.name
        def organisationRange = Organisation.list()*.name
        def installationRange = Installation.list()*.name
        
        def receiverFilterParams = 
            [new ListReportParameter(label: "organisation", propertyName:"organisation.name", range:organisationRange)]
            
//            [new ListReportParameter(label: "project", propertyName:"deployments.station.installation.project.name", range:projectRange),
//             new ListReportParameter(label: "installation", propertyName:"deployments.station.installation.name", range:installationRange)]
        
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
