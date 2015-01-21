package au.org.emii.aatams

import au.org.emii.aatams.util.ListUtils
import de.micromata.opengis.kml.v_2_2_0.Folder

class Project
{
    static hasMany = [organisationProjects:OrganisationProject,
                      projectRoles:ProjectRole,
                      tags:Tag,
                      installations:Installation,
                      releases:AnimalRelease]

    static transients = ['organisations', 'people', 'principalInvestigators']
    static auditable = true

    String name
    String description
    boolean isProtected = false

    // These couple of attributes allow any authenticated user to request
    // project creation.
    EntityStatus status = EntityStatus.PENDING
    Person requestingUser

    static constraints =
    {
        name(blank:false, unique:true)
        description(nullable:true, blank:true)
        status()
        requestingUser(nullable:true)
        isProtected()
    }

    static mapping =
    {
        // Speed up the candidateEntitiesService (that reads user's projects).
        cache true
        sort "name"
    }

    static searchable = [only: ['name', 'description']]

    String toString()
    {
        return name
    }

    String getOrganisations()
    {
        return ListUtils.fold(organisationProjects.grep { it.organisation.status == EntityStatus.ACTIVE }, "organisation")
    }

    String getPeople()
    {
        return ListUtils.fold(projectRoles, "person")
    }

    List<Person> getPrincipalInvestigators()
    {
        ProjectRoleType piRoleType = ProjectRoleType.findByDisplayName('Principal Investigator');
        return projectRoles.findAll { it.roleType == piRoleType}*.person
    }

    /**
     * Convenience method to return a collection of organisations not related
     * to this project.
     */
    def unrelatedOrganisations()
    {
        // We also want to get a list of organisations which aren't
        // associated with this project (so that when "adding organisation",
        // those already associated with project aren't shown).
        def organisations = Organisation.listActive()

        // Relates this project to any oranisations...
        def organisationProjects = OrganisationProject.findAllByProject(this)
        organisationProjects.each
        {
            organisations.remove(it.organisation)
        }

        log.debug("unrelated organisations: " + organisations)

        return organisations
    }

    Folder toKmlFolder()
    {
        Folder projectFolder = new Folder().withName(name)

        installations.sort
        {
            a, b ->

            a.name <=> b.name
        }.each
        {
            installation ->

            projectFolder.getFeature().add(installation.toKmlFolder())
        }

        return projectFolder
    }
}
