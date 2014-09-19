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
    static transactional = false

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
        ["animal": "au.org.emii.aatams.Animal",
         "animalMeasurement": "au.org.emii.aatams.AnimalMeasurement",
         "animalReleaseSummary": "au.org.emii.aatams.report.AnimalReleaseSummaryService",
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
         "sensor": "au.org.emii.aatams.Sensor",
         "surgery": "au.org.emii.aatams.Surgery",
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
         "releaseDateTime": "release date/time",
         "receiverDeployment.station.installation.project.name": "project",
         "receiverDeployment.station.installation.name": "installation",
         "receiverDeployment.station.name": "station",
         "recovery": "unrecovered deployments only",
         "pingCode": "ping code",
         "project.name": "project",
         "projectRoles.project.name": "project",
         "organisation.name": "organisation",
         "organisationProject.project.name": "project",
         "organisationProject.organisation.name": "organisation",
         "installation.project.name": "project",
         "station.installation.project.name": "project",
         "station.installation.name": "installation",
         "surgeries.release.animal.species.spcode": speciesCaabLabel,
         "surgeries.tag.sensors.transmitterId": tagId,
         "tag.project.name": "project",
         "timestamp": "timestamp",
         "transmitterId": tagId]

    static def reportNameToClass =
        ["animal": Animal.class,
         "animalMeasurement": AnimalMeasurement.class,
         "animalReleaseSummary": AnimalReleaseSummaryService.class,
         "animalRelease": AnimalRelease.class,
         "detection": ValidDetection.class,
         "detectionSurgery": DetectionSurgery.class,
         "installation": Installation.class,
         "installationStation": InstallationStation.class,
         "organisation": Organisation.class,
         "person": Person.class,
         "project": Project.class,
         "receiver": Receiver.class,
         "receiverDeployment": ReceiverDeployment.class,
         "receiverRecovery": ReceiverDeployment.class,    // This is deliberate - the "recovery" list view is actually a list of deployments and associated recovery (if there is one)
         "receiverEvent": ValidReceiverEvent.class,
         "sensor": Sensor.class,
         "surgery": Surgery.class,
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

        def organisationRange = Organisation.findAllByStatus(EntityStatus.ACTIVE)*.name
        def installationRange = Installation.list()*.name

        def timestampMin = getDetectionTimestampMin()
        def timestampMax = new Date()

        def animalReleaseFilterParams =
        [
            new ListReportParameter(label: propertyToLabel["project.name"], associationName: "project", propertyName:"name", range:projectRange),
            new AjaxMultiSelectReportParameter(label: propertyToLabel["surgeries.tag.sensors.transmitterId"],
                                               associationName: "surgeries.tag.sensors",
                                               propertyName:"transmitterId",
                                               lookupPath:"/sensor/lookupByTransmitterId"),
            new AjaxMultiSelectReportParameter(label: propertyToLabel["animal.species.spcode"],
                                               associationName: "animal.species",
                                               propertyName:"spcode",
                                               lookupPath:"/species/lookupByNameAndReturnSpcode")
        ]

        def detectionFilterParams =
            [new AjaxMultiSelectReportParameter(label: propertyToLabel["receiverDeployment.station.installation.project.name"],
                                                associationName: "receiverDeployment.station.installation.project",
                                                 propertyName:"name",
                                                lookupPath:"/project/lookupByName"),
             new AjaxMultiSelectReportParameter(label: propertyToLabel["receiverDeployment.station.installation.name"],
                                                associationName: "receiverDeployment.station.installation",
                                                 propertyName:"name",
                                                lookupPath:"/installation/lookupByName"),
             new AjaxMultiSelectReportParameter(label: propertyToLabel["receiverDeployment.station.name"],
                                                associationName: "receiverDeployment.station",
                                                 propertyName:"name",
                                                lookupPath:"/installationStation/lookupByName"),
             new AjaxMultiSelectReportParameter(label: propertyToLabel["transmitterId"],
                                                associationName: "",
                                                 propertyName:"transmitterId",
                                                lookupPath:"/sensor/lookupByTransmitterId"),
             new DateRangeReportParameter(label: propertyToLabel["timestamp"],
                                           propertyName:"timestamp",
                                          minRange:timestampMin,
                                          maxRange:timestampMax)]

        def eventFilterParams =
            [new AjaxMultiSelectReportParameter(label: propertyToLabel["receiverDeployment.station.installation.project.name"],
                                                associationName: "receiverDeployment.station.installation.project",
                                                 propertyName:"name",
                                                lookupPath:"/project/lookupByName"),
             new AjaxMultiSelectReportParameter(label: propertyToLabel["receiverDeployment.station.installation.name"],
                                                associationName: "receiverDeployment.station.installation",
                                                propertyName:"name",
                                                lookupPath:"/installation/lookupByName"),
             new AjaxMultiSelectReportParameter(label: propertyToLabel["receiverDeployment.station.name"],
                                                associationName: "receiverDeployment.station",
                                                propertyName:"name",
                                                lookupPath:"/installationStation/lookupByName"),
             new DateRangeReportParameter(label: propertyToLabel["timestamp"],
                                          propertyName:"timestamp",
                                          minRange:getEventTimestampMin(),
                                          maxRange:new Date())]

        def installationFilterParams =
            [new ListReportParameter(
                label: propertyToLabel["project.name"],
                associationName: "project",
                propertyName:"name",
                range:projectRange)]

        def installationStationFilterParams =
            [new ListReportParameter(
                label: propertyToLabel["installation.project.name"],
                associationName: "installation.project",
                propertyName:"name",
                range:projectRange)]

        def personFilterParams =
            [new ListReportParameter(
                label: propertyToLabel["organisation.name"],
                associationName: "organisation",
                propertyName:"name",
                range:organisationRange),
             new ListReportParameter(
                 label: propertyToLabel["projectRoles.project.name"],
                 associationName: "projectRoles.project",
                 propertyName:"name",
                 range:projectRange)]

        def receiverFilterParams =
            [new ListReportParameter(
                label: propertyToLabel["organisation.name"],
                associationName: "organisation",
                propertyName:"name",
                range:organisationRange)]

        def receiverDeploymentFilterParams =
            [new ListReportParameter(
                label: propertyToLabel["station.installation.project.name"],
                associationName: "station.installation.project",
                propertyName:"name",
                range:projectRange),
             new ListReportParameter(
                 label: propertyToLabel["station.installation.name"],
                 associationName: "station.installation",
                 propertyName:"name",
                 range:installationRange)]

        def receiverRecoveryFilterParams =
            [new ListReportParameter(
                label: propertyToLabel["station.installation.project.name"],
                associationName: "station.installation.project",
                propertyName: "name",
                range:projectRange),
             new AjaxMultiSelectReportParameter(label: propertyToLabel["station.installation.name"],
                                                associationName: "station.installation",
                                                propertyName:"name",
                                                lookupPath:"/installation/lookupByName"),
             new IsNullReportParameter(
                 label: propertyToLabel["recovery"],
                 associationName: "recovery",
                 propertyName:"recovery")]

        def sensorFilterParams =
            [new ListReportParameter(
                label: propertyToLabel["tag.project.name"],
                associationName: "tag.project",
                propertyName:"name",
                range:projectRange),
             new AjaxMultiSelectReportParameter(label: propertyToLabel["transmitterId"],
                                                 propertyName:"transmitterId",
                                                lookupPath:"/sensor/lookupByTransmitterId")]

        def tagFilterParams =
            [new ListReportParameter(
                label: propertyToLabel["project.name"],
                associationName: "project",
                propertyName:"name",
                range:projectRange),
             new AjaxMultiSelectReportParameter(label: propertyToLabel["codeName"],
                                                 propertyName:"codeName",
                                                lookupPath:"/tag/lookupByCodeName")]


        return [(AnimalReleaseSummaryService.class):new ReportInfo(displayName:"Tag Summary",
                                                          jrxmlFilename:[PDF:"animalReleaseSummary"],
                                                          filterParams:[]),
                (AnimalRelease.class): new ReportInfo(displayName:"Tag Releases",
                                                      jrxmlFilename:[:],
                                                      filterParams:animalReleaseFilterParams),
                (ValidDetection.class):new ReportInfo(displayName:"Detections",
                                                    jrxmlFilename:[CSV:"detectionExtract"],
                                                    filterParams:detectionFilterParams),
                (Installation.class):new ReportInfo(displayName:"Installations",
                                                    jrxmlFilename:[CSV:"installationExtract"],
                                                    filterParams:installationFilterParams),
                (InstallationStation.class):new ReportInfo(displayName:"Installation Stations",
                                                           jrxmlFilename:[PDF:"installationStationList",
                                                                          CSV:"installationStationExtract"],
                                                           filterParams:installationStationFilterParams),
                (Person.class):new ReportInfo(displayName:"People",
                                                           jrxmlFilename:[:],
                                                           filterParams:personFilterParams),
                (Receiver.class):new ReportInfo(displayName:"Receivers",
                                                jrxmlFilename:[PDF:"receiverList",
                                                               CSV:"receiverExtract"],
                                                filterParams:receiverFilterParams),
                (ReceiverDeployment.class):new ReportInfo(displayName:"Receiver Deployments",
                                                          jrxmlFilename:[PDF:"receiverDeploymentList"],
                                                          filterParams:receiverDeploymentFilterParams),
                (ReceiverRecovery.class):new ReportInfo(displayName:"Receiver Recoveries",
                                                          jrxmlFilename:[:],
                                                          filterParams:receiverRecoveryFilterParams),
                (ValidReceiverEvent.class):new ReportInfo(displayName:"Receiver Events",
                                                     jrxmlFilename:[CSV:"receiverEventExtract"],
                                                     filterParams:eventFilterParams),
                (Sensor.class):new ReportInfo(displayName:"Tags",
                                              jrxmlFilename:[CSV:"sensorExtract"],
                                              filterParams:sensorFilterParams),
                (Tag.class):new ReportInfo(displayName:"Tags",
                                           jrxmlFilename:[CSV:"tagExtract"],
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
        log.debug("Converting params to report format, params: " + params)

        if (!params)
        {
            return [:]
        }

        def filterParamsInReportFormat = [:]

        def flattenedParams = params.flatten()

        flattenedParams.each
        {
            k, v ->

            if (k.endsWith(".eq"))
            {
                assert (v.size() == 2): "Invalid filter parameter: k: " + k + ", v: " + v

                def reportKey = k[0..k.size() - 4]
                reportKey += "." + v[0]

                def reportValue = v[1]
                if (reportValue != null && reportValue != "")
                {
                    filterParamsInReportFormat[propertyToLabel[reportKey]] = reportValue
                }
            }
            else
            {
                filterParamsInReportFormat[k] = v
            }
        }

        return filterParamsInReportFormat
    }
}
