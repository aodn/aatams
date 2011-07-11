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
}
