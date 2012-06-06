package au.org.emii.aatams.bulk

import org.apache.log4j.Logger
import org.apache.shiro.crypto.hash.Sha256Hash
import org.grails.plugins.csv.CSVMapReader
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

import au.org.emii.aatams.EntityStatus
import au.org.emii.aatams.Installation
import au.org.emii.aatams.InstallationConfiguration
import au.org.emii.aatams.InstallationStation
import au.org.emii.aatams.OrganisationProject
import au.org.emii.aatams.Person
import au.org.emii.aatams.Project
import au.org.emii.aatams.ProjectAccess
import au.org.emii.aatams.ProjectRole
import au.org.emii.aatams.ProjectRoleType

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory

class InstallationLoader 
{
	private static final Logger log = Logger.getLogger(InstallationLoader)
	
	static final String GRP_ID_COL = "GRP_ID"
	static final String GRP_NAME_COL = "GRP_NAME"
	static final String STA_ID_COL = "STA_ID"
	
	static final String STA_CONTACT_NAME_COL = "STA_CONTACT_NAME"
	static final String STA_SITE_NAME_COL = "STA_SITE_NAME"
	
	static final String MODIFIED_DATETIME_COL = "MODIFIED_DATETIME"
	static final String MODIFIED_BY_COL = "MODIFIED_BY"

	static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("d/m/YYYY HH:mm:ss")
	
	void load(Map context, List<InputStream> streams) throws BulkImportException
	{
		Map groupings = loadGroupings(streams[0])
		Map groupingDetail = loadGroupingDetail(streams[1])
		Map stations = loadStations(streams[2])
		
		groupingDetail.each
		{
			grpId, grpDetail ->
			
			// Take contact name from first station in the group.
			def stationsIdForGrp = groupings[grpId]
			def firstStationInGrp = stations[stationsIdForGrp[0]]
			def contactName = firstStationInGrp[STA_CONTACT_NAME_COL]
			
			Installation installation = 
				createInstallation(organisation: context.bulkImport.organisation,
								   installationName: grpDetail[GRP_NAME_COL],
								   contactName: contactName)
				
			stationsIdForGrp.each
			{
				stationId ->
			
				def stationRecord = stations[stationId]
				
				InstallationStation station = 
					new InstallationStation(
						installation: installation,
						name: stationRecord[STA_SITE_NAME_COL],
						curtainPosition: 0,
						numDeployments: 0,
						location: new GeometryFactory().createPoint(new Coordinate(0f, 0f)))	// This will be set properly later on when the deployments are loaded.
					
				station.save(failOnError: true)	
					
				BulkImportRecord importRecord =
					new BulkImportRecord(
						bulkImport: context.bulkImport,
						srcTable: "STATIONS",
						srcPk: stationId,
						srcModifiedDate: DATE_TIME_FORMATTER.parseDateTime(stationRecord[MODIFIED_DATETIME_COL]),
						dstClass: "au.org.emii.aatams.InstallationStation",
						dstPk: station.id,
						type: BulkImportRecordType.NEW)
					
				importRecord.save(failOnError: true)
			}	
		}
	}
	
	private Map<Integer, Map<String, String>> loadGroupingDetail(InputStream groupingDetailStream)
	{
		def retMap = [:]
		
		new CSVMapReader(new InputStreamReader(groupingDetailStream)).toList().each
		{
			record ->
			
			retMap[Integer.valueOf(record[GRP_ID_COL])] = record
		}
		
		return retMap
	}
	
	private Map<Integer, List<Integer>> loadGroupings(InputStream groupingsStream)
	{
		Set<Integer> alreadyGroupedStations = new TreeSet<Integer>()
		
		def retMap = [:]
		
		new CSVMapReader(new InputStreamReader(groupingsStream)).toList().each
		{
			record ->
			
			def sta_id = Integer.valueOf(record[STA_ID_COL])
			
			if (alreadyGroupedStations.contains(sta_id))
			{
				
			}
			else
			{
				alreadyGroupedStations.add(sta_id)
				def grp_id = Integer.valueOf(record[GRP_ID_COL])
				
				def stationsForGroup = retMap[grp_id]
				if (!stationsForGroup)
				{
					stationsForGroup = []
					retMap[grp_id] = stationsForGroup
				}
				
				stationsForGroup.add(sta_id)
			}
		}
		
		return retMap
	}
	
	private Map<Integer, Map<String, String>> loadStations(InputStream stationsStream)
	{
		def retMap = [:]
		
		new CSVMapReader(new InputStreamReader(stationsStream)).toList().each
		{
			record ->
			
			retMap[Integer.valueOf(record[STA_ID_COL])] = record
		}
		
		return retMap
	}
	
	private Person lookupOrCreatePerson(params)
	{
		def username = params.contactName.toLowerCase().replace(' ', '')
		
		Person person = Person.findByUsername(username)
		if (!person)
		{
			person = 
				new Person(
					organisation: params.organisation,
					name: params.contactName,
					emailAddress: username + '@unknown.com',
					phoneNumber: '',
					status: EntityStatus.ACTIVE,
					defaultTimeZone: DateTimeZone.forID("Australia/Hobart"),
					username: username,
					passwordHash: new Sha256Hash(username).toHex())
				
			person.save(failOnError: true)
		}
		
		return person
	}
	
	private Project lookupOrCreateProject(params)
	{
		// create the project
		def projectName = params.contactName + "'s project"
		Project project = Project.findByName(projectName)
		
		if (!project)
		{
			project = new Project(name: projectName, status: EntityStatus.ACTIVE)
			project.save()
			
			// create the orgProject
			OrganisationProject orgProject = new OrganisationProject(organisation: params.organisation, project: project)
			orgProject.save()
			
			// create the person
			Person person = lookupOrCreatePerson(params)
			
			// create the role
			ProjectRoleType pi = ProjectRoleType.findByDisplayName(ProjectRoleType.PRINCIPAL_INVESTIGATOR)
			assert(pi)
			ProjectRole role = new ProjectRole(person: person, project: project, roleType: pi, access: ProjectAccess.READ_WRITE)
			role.save()
		}
		
		return project
	}
	
	private Installation createInstallation(params)
	{
		Project project = lookupOrCreateProject(params)
		
		Installation installation = 
			new Installation(
				project: project,
				name: params.installationName,
				configuration: InstallationConfiguration.findByType("ARRAY"))
			
		installation.save()
		
		return installation
	}
}
