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
    
    def projects =
    {
        def candidateProjects = 
            Project.list().grep(
            {
                (   (SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectWritePermission(it.id)))
                 && (it.status == EntityStatus.ACTIVE))
            })
        
        return candidateProjects
    }
    
    def organisations =
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
            subjectsOrganisations = person?.organisation
        }
        
        return subjectsOrganisations
    }
    
    def installations =
    {
        def candidateInstallations = 
            Installation.list().grep(
            {
                projects().contains(it.project)
            })
        
        return candidateInstallations
    }
    
    def stations =
    {
        def candidateStations = 
            InstallationStation.list().grep(
            {
                projects().contains(it?.installation?.project)
            })
        
        return candidateStations
    }
    
    def receivers =
    {
        def candOrganisations = organisations()
        
        def candidateReceivers = candOrganisations*.receivers?.flatten()
        return candidateReceivers
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
    
    def deployments =
    {
        def candidateDeployments =
            ReceiverDeployment.list().grep(
            {
                projects().contains(it?.station?.installation?.project)
            })
        
        return candidateDeployments
    }
    
    def surgeries =
    {
        def candidateSurgeries =
            Surgery.list().grep(
            {
                projects().contains(it?.tag?.project)
            })
        
        return candidateSurgeries
    }
    
    /**
     * The list of projects that the current user has read access on.
     */
    def readableProjects =
    {
        def subject = SecurityUtils.subject
        if (!subject)
        {
            return []
        }
        
        Project.list().grep(
        {
            (   (subject.isPermitted(permissionUtilsService.buildProjectReadPermission(it.id)))
             && (it.status == EntityStatus.ACTIVE))
        })
    }
}
