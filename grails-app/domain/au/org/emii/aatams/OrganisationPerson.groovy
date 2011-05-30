package au.org.emii.aatams

/**
 * Models the many-to-many relationship between Organisations and People.
 */
class OrganisationPerson 
{
    static belongsTo = [organisation:Organisation, person:Person]
}
