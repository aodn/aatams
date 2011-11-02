package au.org.emii.aatams.report

import au.org.emii.aatams.*
import au.org.emii.aatams.detection.ValidDetection

/**
 * This service allows clients to  retrieve a list of available reports, along
 * with applicable filter parameters
 *   
 *   TODO: may move modelling of ReportInfo and associated parameters to domain classes.
 */
class ReportInfoService 
{
    static transactional = true

    def permissionUtilsService
    
    static final String MEMBER_PROJECTS = "My Projects"
    
    /**
     * Mapping between report name and relevant domain class.
     * Saves clients from having to know anything about internal package
     * hierarchy etc).
     */
    static def reportMapping =
        ["animalReleaseSummary": "au.org.emii.aatams.report.AnimalReleaseSummaryService",
         "detection": "au.org.emii.aatams.detection.ValidDetection",
         "installation": "au.org.emii.aatams.Installation",
         "installationStation": "au.org.emii.aatams.InstallationStation",
         "receiver": "au.org.emii.aatams.Receiver",
         "receiverDeployment": "au.org.emii.aatams.ReceiverDeployment",
         "receiverEvent": "au.org.emii.aatams.ReceiverEvent",
         "tag": "au.org.emii.aatams.Tag"
         ]

    static def propertyToLabel =
        ["detectionSurgeries.surgery.release.animal.species.SPCODE": "species CAAB code",
		 "detectionSurgeries.surgery.release.animal.species.COMMON_NAME": "species common name",
		 "detectionSurgeries.surgery.release.animal.species.SCIENTIFIC_NAME": "species scientific name",
		 "detectionSurgeries.tag.codeName": "tag ID",
		 "receiverDeployment.station.installation.project.name": "project",
		 "receiverDeployment.station.installation.name": "installation",
		 "receiverDeployment.station.name": "station",
		 "organisation.name": "organisation",
         "installation.project.name": "project",
         "station.installation.project.name": "project",
         "station.installation.name": "installation",
		 "timestamp": "timestamp"]
    
    static def reportNameToClass =
        ["animalReleaseSummary": AnimalReleaseSummaryService.class,
         "detection": ValidDetection.class,
         "installation": Installation.class,
         "installationStation": InstallationStation.class,
         "receiver": Receiver.class,
         "receiverDeployment": ReceiverDeployment.class,
         "receiverEvent": ReceiverEvent.class,
         "tag": Tag.class
         ]
        
    /**
     * Return info for all available reports (keyed by the domain class).
     */
    Map<Class, ReportInfo> getReportInfo() 
    {
        def projectRange = Project.list()*.name
        
        if (permissionUtilsService.isAuthenticated())
        {
            projectRange.add(0, MEMBER_PROJECTS)
        }
        
        def organisationRange = Organisation.list()*.name
        def installationRange = Installation.list()*.name
		def speciesCaabCodeRange = CaabSpecies.list(max:20)*.SPCODE
		def speciesCommonNameRange = CaabSpecies.list(max:20)*.COMMON_NAME
		def speciesScientificNameRange = CaabSpecies.list(max:20)*.SCIENTIFIC_NAME
		def stationRange = InstallationStation.list()*.name
		def tagRange = Tag.list()*.codeName
		def timestampMin = ValidDetection.list()*.timestamp.min()
		def timestampMax = ValidDetection.list()*.timestamp.max()
		
        def detectionFilterParams = 
            [new AjaxMultiSelectReportParameter(label: propertyToLabel["receiverDeployment.station.installation.project.name"], 
									 			propertyName:"receiverDeployment.station.installation.project.name", 
												lookupPath:"/project/lookupByName"),
			 new AjaxMultiSelectReportParameter(label: propertyToLabel["receiverDeployment.station.installation.name"], 
									 			propertyName:"receiverDeployment.station.installation.name", 
												lookupPath:"/installation/lookupByName"),
			 new AjaxMultiSelectReportParameter(label: propertyToLabel["receiverDeployment.station.name"], 
									 			propertyName:"receiverDeployment.station.name", 
												lookupPath:"/installationStation/lookupByName"),
 			 new ListReportParameter(label: propertyToLabel["detectionSurgeries.tag.codeName"],
			 	 propertyName:"detectionSurgeries.tag.codeName",
				 range:tagRange),
			 new ListReportParameter(label: propertyToLabel["detectionSurgeries.surgery.release.animal.species.SPCODE"],
				 propertyName:"detectionSurgeries.surgery.release.animal.species.SPCODE",
				 range:speciesCaabCodeRange),
			 new ListReportParameter(label: propertyToLabel["detectionSurgeries.surgery.release.animal.species.COMMON_NAME"],
				 propertyName:"detectionSurgeries.surgery.release.animal.species.COMMON_NAME",
				 range:speciesCommonNameRange),
			 new ListReportParameter(label: propertyToLabel["detectionSurgeries.surgery.release.animal.species.SCIENTIFIC_NAME"],
				 propertyName:"detectionSurgeries.surgery.release.animal.species.SCIENTIFIC_NAME",
				 range:speciesScientificNameRange),
			 new DateRangeReportParameter(label: propertyToLabel["timestamp"],
				 						  propertyName:"timestamp",
										  minRange:timestampMin,
										  maxRange:timestampMax)]
			
        def installationStationFilterParams = 
            [new ListReportParameter(label: propertyToLabel["installation.project.name"], propertyName:"installation.project.name", range:projectRange)]

        def receiverFilterParams = 
            [new ListReportParameter(label: propertyToLabel["organisation.name"], propertyName:"organisation.name", range:organisationRange)]
            
        def receiverDeploymentFilterParams = 
            [new ListReportParameter(label: propertyToLabel["station.installation.project.name"], propertyName:"station.installation.project.name", range:projectRange),
             new ListReportParameter(label: propertyToLabel["station.installation.name"], propertyName:"station.installation.name", range:installationRange)]
        
            
        return [(AnimalReleaseSummaryService.class):new ReportInfo(displayName:"Tag Summary", 
                                                          jrxmlFilename:["report":"animalReleaseSummary"], 
                                                          filterParams:[]),
                (ValidDetection.class):new ReportInfo(displayName:"Detections",
                                                    jrxmlFilename:["extract":"detectionExtract"],
                                                    filterParams:detectionFilterParams),
                (Installation.class):new ReportInfo(displayName:"Installations",
                                                    jrxmlFilename:["extract":"installationExtract"],
                                                    filterParams:[]),
                (InstallationStation.class):new ReportInfo(displayName:"Installation Stations",
                                                           jrxmlFilename:["report":"installationStationList",
                                                                          "extract":"installationStationExtract"],
                                                           filterParams:installationStationFilterParams),
                (Receiver.class):new ReportInfo(displayName:"Receivers", 
                                                jrxmlFilename:["report":"receiverList",
                                                               "extract":"receiverExtract"], 
                                                filterParams:receiverFilterParams),
                (ReceiverDeployment.class):new ReportInfo(displayName:"Receiver Deployments", 
                                                          jrxmlFilename:["report":"receiverDeploymentList"], 
                                                          filterParams:receiverDeploymentFilterParams),
                (ReceiverEvent.class):new ReportInfo(displayName:"Receiver Events", 
                                                     jrxmlFilename:["extract":"receiverEventExtract"], 
                                                     filterParams:[]),
                (Tag.class):new ReportInfo(displayName:"Tags", 
                                           jrxmlFilename:["extract":"tagExtract"], 
                                           filterParams:[])
                ]
    }
    
    /**
     * Return report info for a particular domain class.
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
            
			key = removeParamTypePrefix(key)
			
            retVals[propertyToLabel.get(key, key)] = val
        }
        
        return retVals
    }

	private String removeParamTypePrefix(String key) {
		if (key.startsWith("eq."))
		{
			key = key.substring("eq.".length())
		}
		if (key.startsWith("in."))
		{
			key = key.substring("in".length())
		}
		if (key.startsWith("between."))
		{
			key = key.substring("between.".length())
		}
		return key
	}
}
