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
        ["animalReleaseSummary": "au.org.emii.aatams.report.AnimalReleaseSummaryService",
         "installationStation": "au.org.emii.aatams.InstallationStation",
         "receiver": "au.org.emii.aatams.Receiver",
         "receiverDeployment": "au.org.emii.aatams.ReceiverDeployment",
         "tag": "au.org.emii.aatams.Tag"
         ]

    static def propertyToLabel =
        ["organisation.name": "organisation",
         "installation.project.name": "project",
         "station.installation.project.name": "project",
         "station.installation.name": "installation"]
    
    static def reportNameToClass =
        ["animalReleaseSummary": AnimalReleaseSummaryService.class,
         "installationStation": InstallationStation.class,
         "receiver": Receiver.class,
         "receiverDeployment": ReceiverDeployment.class,
         "tag": Tag.class
         ]
        
    /**
     * Return info for all available reports (keyed by the domain class).
     */
    Map<Class, ReportInfo> getReportInfo() 
    {
        def projectRange = Project.list()*.name
        def organisationRange = Organisation.list()*.name
        def installationRange = Installation.list()*.name
        
        def installationStationFilterParams = 
            [new ListReportParameter(label: propertyToLabel["installation.project.name"], propertyName:"installation.project.name", range:projectRange)]

        def receiverFilterParams = 
            [new ListReportParameter(label: propertyToLabel["organisation.name"], propertyName:"organisation.name", range:organisationRange)]
            
        def receiverDeploymentFilterParams = 
            [new ListReportParameter(label: propertyToLabel["station.installation.project.name"], propertyName:"station.installation.project.name", range:projectRange),
             new ListReportParameter(label: propertyToLabel["station.installation.name"], propertyName:"station.installation.name", range:installationRange)]
        
            
        return [(AnimalReleaseSummaryService.class):new ReportInfo(displayName:"Tag Summary", 
                                                          jrxmlFilename:"animalReleaseSummary", 
                                                          filterParams:[]),
                (InstallationStation.class):new ReportInfo(displayName:"Installations",
                                                           jrxmlFilename:"installationStationList",
                                                           filterParams:installationStationFilterParams),
                (Receiver.class):new ReportInfo(displayName:"Receivers", 
                                                jrxmlFilename:"receiverList", 
                                                filterParams:receiverFilterParams),
                (ReceiverDeployment.class):new ReportInfo(displayName:"Receiver Deployments", 
                                                          jrxmlFilename:"receiverDeploymentList", 
                                                          filterParams:receiverDeploymentFilterParams),
                (Tag.class):new ReportInfo(displayName:"Tags", 
                                           jrxmlFilename:"tagList", 
                                           filterParams:[])
                ]
    }
    
    /**
     * Return reoprt info for a particular domain class.
     */
    ReportInfo getReportInfo(Class domain)
    {
        log.debug("getReportInfo(Class)")
        return getReportInfo().get(domain)
    }
    
    Class getClassForName(String name)
    {
        return reportNameToClass[name]
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
    
    /**
     * Useful for converting a map of nested map of filter paramaters to entries
     * of the form:
     * 
     *  "level1.level2":"value" 
     */
    Map<String, Object> filterParamsToReportFormat(params)
    {
        if (!params)
        {
            return [:]
        }
        
        // Only include entries with non-map, non-blank, non-null values.
        Map filteredVals = params.findAll
        {
            key, val ->
            
            log.debug("Filtering key: " + key + ", value: " + val)    
            
            if (!val)
            {
                log.debug("Filtered out key: " + key + ", value: " + val)    
                return false
            }

            if (val == "")
            {
                log.debug("Filtered out key: " + key + ", value: " + val)    
                return false
            }
            
            if (val instanceof Map)
            {
                log.debug("Filtered out key: " + key + ", value: " + val)    
                return false
            }
            
            log.debug("Keeping key: " + key + ", value: " + val)
            return true
        }
        
        // Replace key with equivalent label.
        def retVals = [:]
        filteredVals.each
        {
            key, val ->
            
            retVals[propertyToLabel.get(key, key)] = val
        }
        
        return retVals
    }
}
