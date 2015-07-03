package au.org.emii.aatams

import grails.test.*

class ProjectTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }
    
    void testActiveOrgsOnlyInGetOrganisations() {
        Project project = new Project(name: "seals")
        mockDomain(Project, [project])
        project.save(validate: false)
        
        Organisation active = new Organisation(name: "active", department: "department", status: EntityStatus.ACTIVE)
        Organisation deactivated = new Organisation(name: "deactivated", department: "department", status: EntityStatus.DEACTIVATED)
        mockDomain(Organisation, [active, deactivated])
        [active, deactivated].each { it.save(validate: false) }
        
        OrganisationProject activeOrgProject = new OrganisationProject(organisation: active, project: project)
        OrganisationProject deactivatedOrgProject = new OrganisationProject(organisation: deactivated, project: project)
        mockDomain(OrganisationProject, [activeOrgProject, deactivatedOrgProject])
        [activeOrgProject, deactivatedOrgProject].each { it.save() }
        
        project.addToOrganisationProjects(activeOrgProject)
        project.addToOrganisationProjects(deactivatedOrgProject)
        project.save(validate: false)
        
        assertEquals("active (department)", project.getOrganisations())
    }
}
