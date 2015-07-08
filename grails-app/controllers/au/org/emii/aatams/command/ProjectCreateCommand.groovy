package au.org.emii.aatams.command

import au.org.emii.aatams.*

/**
 * Provides two additional fields (i.e. organisation and PI) used for project
 * creation.
 */
class ProjectCreateCommand {
    Organisation organisation
    Person person   // TODO: rename to principalInvestigator
    String name
    String description
    boolean isProtected

    Project createProject() {
        def project = new Project(name:name, description:description)
        project.addToOrganisationProjects(new OrganisationProject(organisation:organisation, project:project))

        return project
    }
}

