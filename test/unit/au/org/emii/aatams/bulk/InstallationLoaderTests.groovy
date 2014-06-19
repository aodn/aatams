package au.org.emii.aatams.bulk

import grails.test.*

import org.apache.shiro.crypto.hash.Sha256Hash
import org.joda.time.DateTimeZone

import au.org.emii.aatams.*

class InstallationLoaderTests extends AbstractLoaderTests
{
    ProjectRoleType principalInvestigator
    
    protected void setUp() 
    {
        super.setUp()
        
        loader = new InstallationLoader()
        
        mockDomain(Installation)
        mockDomain(InstallationStation)
        
        InstallationConfiguration array = new InstallationConfiguration(type: "ARRAY")
        mockDomain(InstallationConfiguration, [array])
        array.save()
        
        mockDomain(Person)
        mockDomain(Project)

        mockDomain(OrganisationProject)
        mockDomain(ProjectRole)
        
        principalInvestigator = new ProjectRoleType(displayName: ProjectRoleType.PRINCIPAL_INVESTIGATOR)
        mockDomain(ProjectRoleType, [principalInvestigator])
        principalInvestigator.save()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }
    
    void testLoadGroupingDetail()
    {
        def groupingDetailText = '''"GRP_ID","GRP_NAME","GRP_DESCRIPTION"
1,"Neptunes","neptune description"
2,"GAB","Great Australian Bight"
'''
        InputStream groupingDetailStream = new ByteArrayInputStream(groupingDetailText.bytes)
        
        assertEquals([1: [GRP_ID: "1", GRP_NAME: "Neptunes", GRP_DESCRIPTION: "neptune description"],
                      2: [GRP_ID: "2", GRP_NAME: "GAB", GRP_DESCRIPTION: "Great Australian Bight"]],
                     loader.loadGroupingDetail(groupingDetailStream))    
    }
    
    void testLoadGroupings()
    {
        def groupingsText = '''"GRP_ID","STA_ID"
1,69
1,70
2,69
3,71
'''
        
        InputStream groupingsStream = new ByteArrayInputStream(groupingsText.bytes)

        assertEquals([1: [69, 70], 3: [71]], 
                     loader.loadGroupings(groupingsStream))        
    }
    
    void testLoadStations()
    {
        def stationsText = '''"STA_ID","STA_SITE_NAME","STA_CONTACT_NAME","STA_DESCRIPTION","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
1,"MB11C","Richard Pillans","Old Name: MB11B",1/10/2010 15:05:31,"TAG",1/10/2010 15:05:31,"TAG"
2,"MB12A","Richard Pillans",,1/10/2010 15:11:07,"TAG",1/10/2010 15:11:26,"TAG"
'''
        
        InputStream stationsStream = new ByteArrayInputStream(stationsText.bytes)
        
        assertEquals(
            [1: [STA_ID: "1", STA_SITE_NAME: "MB11C", STA_CONTACT_NAME: "Richard Pillans", STA_DESCRIPTION: "Old Name: MB11B", ENTRY_DATETIME: "1/10/2010 15:05:31", ENTRY_BY: "TAG", MODIFIED_DATETIME: "1/10/2010 15:05:31", MODIFIED_BY: "TAG"],
             2: [STA_ID: "2", STA_SITE_NAME: "MB12A", STA_CONTACT_NAME: "Richard Pillans", STA_DESCRIPTION: "", ENTRY_DATETIME: "1/10/2010 15:11:07", ENTRY_BY: "TAG", MODIFIED_DATETIME: "1/10/2010 15:11:26", MODIFIED_BY: "TAG"]],
         loader.loadStations(stationsStream))
    }
    
    void testLookupOrCreatePersonNotExisting()
    {
        assertEquals(0, Person.count())
        Person joeBloggs = loader.lookupOrCreatePerson(organisation: csiro, contactName: "Joe Bloggs")
        assertNotNull(joeBloggs)
        assertEquals(1, Person.count())
    }
    
    void testLookupOrCreatePersonExisting()
    {
        createPerson()
        assertEquals(1, Person.count())
        
        Person joeBloggs = loader.lookupOrCreatePerson(organisation: csiro, contactName: "Joe Bloggs")
        assertNotNull(joeBloggs)
        assertEquals(1, Person.count())
    }
    
    void testLookupOrCreateProjectNotExisting()
    {
        createPerson()
        
        assertEquals(0, Project.count())
        assertEquals(0, ProjectRole.count())
        assertEquals(0, OrganisationProject.count())
        
        assertLookupOrCreateProject(organisation: csiro, contactName: "Joe Bloggs")
    }
    
    void testLookupOrCreateProjectExisting()
    {
        Person person = createPerson()
        
        def projectName = "Joe Bloggs's project"
        Project project = new Project(name: projectName, status: EntityStatus.ACTIVE)
        project.save()
        
        def organisationProject = new OrganisationProject(organisation: csiro, project: project)
        organisationProject.save()
        
        def role = new ProjectRole(project: project, person: person, roleType: principalInvestigator, access: ProjectAccess.READ_WRITE)
        role.save()
        
        assertEquals(1, Project.count())
        assertEquals(1, ProjectRole.count())
        assertEquals(1, OrganisationProject.count())
        
        assertLookupOrCreateProject(organisation: csiro, contactName: "Joe Bloggs")
    }
    
    void testCreateInstallation()
    {
        assertEquals(0, Installation.count())
        Installation installation = loader.createInstallation(organisation: csiro, contactName: "Joe Bloggs", installationName: "Ningaloo")
        
        assertEquals(1, Installation.count())
        assertEquals("Ningaloo", installation.name)
        
        Project project = Project.findByName("Joe Bloggs's project")
        assertNotNull(project)
        
        assertEquals(project.name, installation.project.name)
    }

    void testNewStation()
    {
        def groupingsText = '''"GRP_ID","STA_ID"
1,1
'''
        
        def groupingDetailText = '''"GRP_ID","GRP_NAME","GRP_DESCRIPTION"
1,"Neptunes","neptune description"
'''
        
        def stationsText = '''"STA_ID","STA_SITE_NAME","STA_CONTACT_NAME","STA_DESCRIPTION","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
1,"MB11C","Richard Pillans","Old Name: MB11B",1/10/2010 15:05:31,"TAG",1/10/2010 15:05:31,"TAG"
'''
        
        assertEquals(0, InstallationStation.count())
        
        assertSuccess(
            [groupingsText, groupingDetailText, stationsText],
            [["type": BulkImportRecordType.NEW, 
             "srcPk": 1, 
             "srcTable": "STATIONS", 
             "srcModifiedDate": InstallationLoader.DATE_TIME_FORMATTER.parseDateTime("1/10/2010 15:05:31"),
             "dstClass": "au.org.emii.aatams.InstallationStation",
             "name": "MB11C"]])
    }
    
    void testGroupDetailWithNoStations()
    {
        def groupingsText = '''"GRP_ID","STA_ID"
'''
        
        def groupingDetailText = '''"GRP_ID","GRP_NAME","GRP_DESCRIPTION"
1,"Neptunes","neptune description"
'''
        
        def stationsText = '''"STA_ID","STA_SITE_NAME","STA_CONTACT_NAME","STA_DESCRIPTION","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
'''
        
        try
        {
            assertSuccess(
                [groupingsText, groupingDetailText, stationsText],
                [])
        }
        catch (Throwable t)
        {
            fail(t.message)
        }
    }
    
    void testUpdated()
    {
        
    }
    
    void testIgnored()
    {
        
    }
    
    protected void assertSuccessDetail(it, importRecord)
    {
        if (importRecord.dstPk)
        {
            def station = InstallationStation.get(importRecord.dstPk)
            assertNotNull(station)
            assertEquals(it.name, station.name)
        }
    }

    private void assertLookupOrCreateProject(params)
    {
        Project joeBloggsProject = loader.lookupOrCreateProject(params)
        
        assertEquals(1, Project.count())
        assertNotNull(joeBloggsProject)
        def projectName = "Joe Bloggs's project"
        assertEquals(projectName, joeBloggsProject.name)
        assertEquals(EntityStatus.ACTIVE, joeBloggsProject.status)
        
        // orgProject
        assertEquals(1, OrganisationProject.count())
        OrganisationProject orgProject = OrganisationProject.findByProject(joeBloggsProject)
        assertEquals(csiro.name, orgProject.organisation.name)
        assertEquals(projectName, orgProject.project.name)
        
        assertEquals(1, ProjectRole.count())
        ProjectRole role = ProjectRole.findByProject(joeBloggsProject)
        assertEquals(projectName, role.project.name)
        assertEquals("Joe Bloggs", role.person.name)
        assertEquals(ProjectRoleType.PRINCIPAL_INVESTIGATOR, role.roleType.displayName)
        assertEquals(ProjectAccess.READ_WRITE, role.access)
    }
    
    private Person createPerson()
    {
        def username = "joebloggs"
        
        Person person =
            new Person(
                organisation: csiro,
                name: "Joe Bloggs",
                emailAddress: 'joebloggs@unknown.com',
                phoneNumber: '',
                status: EntityStatus.ACTIVE,
                defaultTimeZone: DateTimeZone.forID("Australia/Hobart"),
                username: username,
                passwordHash: new Sha256Hash(username).toHex())
        
        person.save(failOnError: true)
    
        return person
    }
}

