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
                SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectWritePermission(it.id))
            })
        
        return candidateProjects
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
            InstallationStations.list().grep(
            {
                projects().contains(it?.installation?.project)
            })
        
        return candidateStations
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
            for (ProjectRole role : it?.projectRoles)
            {
                if (role?.project == project)
                {
                    candidatePeople.add(role?.person)
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
                projects().contains(it?.receiver?.project)
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
}
