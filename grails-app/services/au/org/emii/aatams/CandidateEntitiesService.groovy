package au.org.emii.aatams

import org.apache.shiro.SecurityUtils

/**
 * A service which provides lists of candidate entities based on the principal's
 * roles and permissions.
 */
class CandidateEntitiesService 
{
    static transactional = true

    def permissionUtilsService
    
    def projects()
    {
		def candidateProjects = 
            Project.findAllByStatus(EntityStatus.ACTIVE).grep
            {
				SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectWritePermission(it.id))
            }
			
        return candidateProjects 
    }
    
    def organisations()
    {
        def subjectsOrganisations

        // SysAdmin "belongs" to all organisations.
        if (SecurityUtils.subject.hasRole("SysAdmin"))
        { 
            subjectsOrganisations = Organisation.list()
        }
        else
        {
            Person person = permissionUtilsService.principal()
            subjectsOrganisations = [person?.organisation]
        }
        
        return subjectsOrganisations
    }
    
    def installations()
    {
        return Installation.findAllByProjectInList(projects())
    }
    
    def stations()
    {
		def candidateStations = InstallationStation.findAllByInstallationInList(installations(), [sort:"name"])

        return candidateStations
    }
    
    def receivers()
    {
        def candOrganisations = organisations()
        
        // receivers.organisation in candOrganisations
        // and receiver.status != DEPLOYED
		def receivers = 
        	Receiver.findAllByOrganisationInListAndStatusNotEqual(
                                candOrganisations, 
                                DeviceStatus.findByStatus("DEPLOYED"))
			
		receivers = receivers.sort
		{
			a, b ->
			
			a.toString() <=> b.toString()
		}
	
		return receivers
    }
    
    def tags(Project project)
    {
        def candidateTags =
            Tag.list().grep(
            {
                it?.project == project
            })
        
        return candidateTags
    }
    
    def people(Project project)
    {
        List<Person> candidatePeople = new ArrayList<Person>()
        
        Person.list().each(
        {
            if (it?.status == EntityStatus.PENDING)
            {
                // Ignore PENDING people.
            }
            else
            {
                for (ProjectRole role : it?.projectRoles)
                {
                    if (role?.project == project)
                    {
                        candidatePeople.add(role?.person)
                    }
                }
            }
        })
        
        return candidatePeople
    }
    
    def deployments()
    {
		def projects = projects()
		
        def candidateDeployments =
            ReceiverDeployment.list().grep(
            {
                projects.contains(it?.station?.installation?.project)
            })
        
        return candidateDeployments
    }
    
    def surgeries()
    {
		def projects = projects()
		
        def candidateSurgeries =
            Surgery.list().grep(
            {
                projects.contains(it?.tag?.project)
            })
        
        return candidateSurgeries
    }
    
    /**
     * The list of projects that the current user has read access on.
     */
    def readableProjects()
    {
        def subject = SecurityUtils.subject
        if (!subject)
        {
            return []
        }
        
        Project.findAllByStatus(EntityStatus.ACTIVE).grep
        {
            SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectReadPermission(it.id))
        }
    }
}
