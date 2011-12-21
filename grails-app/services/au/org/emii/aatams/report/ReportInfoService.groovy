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
    
	def detectionTimestampMin
	def eventTimestampMin
	
    static final String MEMBER_PROJECTS = "My Projects"
    
    /**
     * Mapping between report name and relevant domain class.
     * Saves clients from having to know anything about internal package
     * hierarchy etc).
     */
    static def reportMapping =
        ["animalReleaseSummary": "au.org.emii.aatams.report.AnimalReleaseSummaryService",
		 "animalRelease": "au.org.emii.aatams.AnimalRelease",
         "detection": "au.org.emii.aatams.detection.ValidDetection",
         "installation": "au.org.emii.aatams.Installation",
         "installationStation": "au.org.emii.aatams.InstallationStation",
		 "person": "au.org.emii.aatams.Person",
		 "project": "au.org.emii.aatams.Project",
		 "organisation": "au.org.emii.aatams.Organisation",
         "receiver": "au.org.emii.aatams.Receiver",
         "receiverDeployment": "au.org.emii.aatams.ReceiverDeployment",
         "receiverRecovery": "au.org.emii.aatams.ReceiverRecovery",
         "receiverEvent": "au.org.emii.aatams.ValidReceiverEvent",
         "tag": "au.org.emii.aatams.Tag"
         ]

	static def speciesCaabLabel = "species (CAAB, common or scientific name)"
	static def speciesCommonName = "species common name"
	static def speciesScientificName = "species scientific name"
	static def tagId = "tag ID"
	
    static def propertyToLabel =
		["animal.species.spcode": speciesCaabLabel,
		 "animal.species.commonName": speciesCommonName,
		 "animal.species.scientificName": speciesScientificName,
		 "codeName": tagId,
		 "detectionSurgeries.surgery.release.animal.species.spcode": speciesCaabLabel,
		 "detectionSurgeries.surgery.release.animal.species.commonName": speciesCommonName,
		 "detectionSurgeries.surgery.release.animal.species.scientificName": speciesScientificName,
		 "detectionSurgeries.tag.codeName": tagId,
		 "releaseDateTime": "release date/time",
		 "receiverDeployment.station.installation.project.name": "project",
		 "receiverDeployment.station.installation.name": "installation",
		 "receiverDeployment.station.name": "station",
		 "recovery": "unrecovered deployments only",
         "project.name": "project",
		 "projectRoles.project.name": "project",
		 "organisation.name": "organisation",
		 "organisationProject.project.name": "project",
		 "organisationProject.organisation.name": "organisation",
         "installation.project.name": "project",
         "station.installation.project.name": "project",
         "station.installation.name": "installation",
		 "surgeries.tag.codeName": tagId,
		 "timestamp": "timestamp"]
    
    static def reportNameToClass =
        ["animalReleaseSummary": AnimalReleaseSummaryService.class,
		 "animalRelease": AnimalRelease.class,
         "detection": ValidDetection.class,
         "installation": Installation.class,
         "installationStation": InstallationStation.class,
		 "organisation": Organisation.class,
		 "person": Person.class,
		 "project": Project.class,
         "receiver": Receiver.class,
         "receiverDeployment": ReceiverDeployment.class,
         "receiverRecovery": ReceiverDeployment.class,	// This is deliberate - the "recovery" list view is actually a list of deployments and associated recovery (if there is one)
         "receiverEvent": ValidReceiverEvent.class,
         "tag": Tag.class
         ]
     
	private def getDetectionTimestampMin()
	{
		if (!detectionTimestampMin)
		{
			detectionTimestampMin = ValidDetection.createCriteria().get
			{
				projections
				{
					min('timestamp')
				}
			}
		}
		
		return detectionTimestampMin
	}	   

	private def getEventTimestampMin()
	{
		if (!eventTimestampMin)
		{
			eventTimestampMin = ValidReceiverEvent.createCriteria().get
			{
				projections
				{
					min('timestamp')
				}
			}
		}
		
		return detectionTimestampMin
	}

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
		
		def timestampMin = getDetectionTimestampMin()
		def timestampMax = new Date()
		
		def animalReleaseFilterParams =
		[
			new ListReportParameter(label: propertyToLabel["project.name"], propertyName:"project.name", range:projectRange),
			new AjaxMultiSelectReportParameter(label: propertyToLabel["surgeries.tag.codeName"],
											   propertyName:"surgeries.tag.codeName",
											   lookupPath:"/tag/lookupByCodeName"),
			new AjaxMultiSelectReportParameter(label: propertyToLabel["animal.species.spcode"],
											   propertyName:"animal.species.spcode",
											   lookupPath:"/species/lookupByNameAndReturnSpcode")
		]
		
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
			 new AjaxMultiSelectReportParameter(label: propertyToLabel["detectionSurgeries.tag.codeName"], 
									 			propertyName:"detectionSurgeries.tag.codeName", 
												lookupPath:"/tag/lookupByCodeName"),
			 new AjaxMultiSelectReportParameter(label: propertyToLabel["detectionSurgeries.surgery.release.animal.species.spcode"], 
									 			propertyName:"detectionSurgeries.surgery.release.animal.species.spcode", 
												lookupPath:"/species/lookupByNameAndReturnSpcode"),
			 new DateRangeReportParameter(label: propertyToLabel["timestamp"],
				 						  propertyName:"timestamp",
										   minRange:timestampMin,
										   maxRange:timestampMax)]
 
		def eventFilterParams =
			[new AjaxMultiSelectReportParameter(label: propertyToLabel["receiverDeployment.station.installation.project.name"],
												 propertyName:"receiverDeployment.station.installation.project.name",
												lookupPath:"/project/lookupByName"),
			 new AjaxMultiSelectReportParameter(label: propertyToLabel["receiverDeployment.station.installation.name"],
												 propertyName:"receiverDeployment.station.installation.name",
												lookupPath:"/installation/lookupByName"),
			 new AjaxMultiSelectReportParameter(label: propertyToLabel["receiverDeployment.station.name"],
												 propertyName:"receiverDeployment.station.name",
												lookupPath:"/installationStation/lookupByName"),
			 new DateRangeReportParameter(label: propertyToLabel["timestamp"],
										   propertyName:"timestamp",
										  minRange:getEventTimestampMin(),
										  maxRange:new Date())]

		def installationFilterParams =
			[new ListReportParameter(label: propertyToLabel["project.name"], propertyName:"project.name", range:projectRange)]

        def installationStationFilterParams = 
            [new ListReportParameter(label: propertyToLabel["installation.project.name"], propertyName:"installation.project.name", range:projectRange)]

		def personFilterParams =
			[new ListReportParameter(label: propertyToLabel["organisation.name"], propertyName:"organisation.name", range:organisationRange),
			 new ListReportParameter(label: propertyToLabel["projectRoles.project.name"], propertyName:"projectRoles.project.name", range:projectRange)]
		
        def receiverFilterParams = 
            [new ListReportParameter(label: propertyToLabel["organisation.name"], propertyName:"organisation.name", range:organisationRange)]
            
        def receiverDeploymentFilterParams = 
            [new ListReportParameter(label: propertyToLabel["station.installation.project.name"], propertyName:"station.installation.project.name", range:projectRange),
             new ListReportParameter(label: propertyToLabel["station.installation.name"], propertyName:"station.installation.name", range:installationRange)]
			
        def receiverRecoveryFilterParams = 
            [new ListReportParameter(label: propertyToLabel["station.installation.project.name"], propertyName:"station.installation.project.name", range:projectRange),
             new IsNullReportParameter(label: propertyToLabel["recovery"], propertyName:"recovery")]
			
		def tagFilterParams =
			[new ListReportParameter(label: propertyToLabel["project.name"], propertyName:"project.name", range:projectRange),
			 new AjaxMultiSelectReportParameter(label: propertyToLabel["codeName"], 
									 			propertyName:"codeName", 
												lookupPath:"/tag/lookupByCodeName")]

            
        return [(AnimalReleaseSummaryService.class):new ReportInfo(displayName:"Tag Summary", 
                                                          jrxmlFilename:["report":"animalReleaseSummary"], 
                                                          filterParams:[]),
				(AnimalRelease.class): new ReportInfo(displayName:"Tag Releases",
													  jrxmlFilename:[:],
													  filterParams:animalReleaseFilterParams),
                (ValidDetection.class):new ReportInfo(displayName:"Detections",
                                                    jrxmlFilename:["extract":"detectionExtract"],
                                                    filterParams:detectionFilterParams),
                (Installation.class):new ReportInfo(displayName:"Installations",
                                                    jrxmlFilename:["extract":"installationExtract"],
                                                    filterParams:installationFilterParams),
                (InstallationStation.class):new ReportInfo(displayName:"Installation Stations",
                                                           jrxmlFilename:["report":"installationStationList",
                                                                          "extract":"installationStationExtract"],
                                                           filterParams:installationStationFilterParams),
                (Person.class):new ReportInfo(displayName:"People",
                                                           jrxmlFilename:[:],
                                                           filterParams:personFilterParams),
                (Receiver.class):new ReportInfo(displayName:"Receivers", 
                                                jrxmlFilename:["report":"receiverList",
                                                               "extract":"receiverExtract"], 
                                                filterParams:receiverFilterParams),
                (ReceiverDeployment.class):new ReportInfo(displayName:"Receiver Deployments", 
                                                          jrxmlFilename:["report":"receiverDeploymentList"], 
                                                          filterParams:receiverDeploymentFilterParams),
                (ReceiverRecovery.class):new ReportInfo(displayName:"Receiver Recoveries", 
                                                          jrxmlFilename:[:], 
                                                          filterParams:receiverRecoveryFilterParams),
                (ValidReceiverEvent.class):new ReportInfo(displayName:"Receiver Events", 
                                                     jrxmlFilename:["extract":"receiverEventExtract"], 
                                                     filterParams:eventFilterParams),
                (Tag.class):new ReportInfo(displayName:"Tags", 
                                           jrxmlFilename:["extract":"tagExtract"], 
                                           filterParams:tagFilterParams)
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
