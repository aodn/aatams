package au.org.emii.aatams

import au.org.emii.aatams.util.ListUtils

class Organisation {
    static hasMany = [organisationProjects:OrganisationProject,
                      receivers:Receiver,
                      people:Person]

    static transients = ['projects', 'totalReceivers']
    static auditable = true

    String name
    String department
    String phoneNumber
    String faxNumber
    Address streetAddress
    Address postalAddress
    EntityStatus status = EntityStatus.PENDING

    // The person requesting creation of Organisation.
    static hasOne = [request:Request]

    static constraints = {
        name(blank:false)
        department(blank:false)
        phoneNumber(blank:false)
        faxNumber(nullable:true)
        streetAddress()
        postalAddress()
        status()    // Default to PENDING
        organisationProjects()
        request(nullable:true)
    }

    static mapping = {
        sort "name"
    }

    static searchable = [only: ['name', 'department']]

    String toString() {
        return name + " (" + department  + ")"
    }

    String getProjects() {
        return ListUtils.fold(organisationProjects, "project")
    }

    static List<Organisation> listActive() {
        return findAllByStatus(EntityStatus.ACTIVE)
    }

    Integer getTotalReceivers() {
        if (!receivers) {
            return 0
        }

        return receivers.size()
    }
}
