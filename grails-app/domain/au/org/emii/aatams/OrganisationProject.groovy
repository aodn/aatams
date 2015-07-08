package au.org.emii.aatams

/**
 * Models the many-to-many relationship between Organisations and Projects.
 */
class OrganisationProject  {
    static belongsTo = [organisation:Organisation, project:Project]
}
