package au.org.emii.aatams

import org.apache.shiro.SecurityUtils
import java.util.Arrays

class EmbargoService 
{
    static transactional = false

    def permissionUtilsService
   
	private Map<Integer, Boolean> projectPermissionCache = [:]
	 
    List applyEmbargo(Class domain, List embargoees)
    {
        if (!Arrays.asList(domain.getInterfaces()).contains(Embargoable.class))
        {
            return embargoees
        }
        
        return applyEmbargo(embargoees)
    }
    
    /**
     * Filter embargoed entities from the given list, where the user doesn't
     * have sufficient permissions.
     */
    List applyEmbargo(List embargoees) 
    {
		projectPermissionCache.clear()
		
        def retList = embargoees.collect
        {
            applyEmbargo(it)
        }

        retList.removeAll
        {
            it == null
        }
        
        return retList
    }

	private boolean hasReadPermission(embargoee)
	{
		def projectId = embargoee.project?.id

		if (!projectPermissionCache.containsKey(projectId))
		{
	        String permissionString = permissionUtilsService.buildProjectReadPermission(projectId)
	        projectPermissionCache.put(projectId, SecurityUtils.subject.isPermitted(permissionString))
		}
		
		assert(projectPermissionCache.containsKey(projectId))
		return projectPermissionCache[projectId]
	}
	    
    Object applyEmbargo(Object embargoee)
    {
        if (!(embargoee instanceof Embargoable))
        {
            return embargoee
        }
        
        if (hasReadPermission(embargoee))
        {
            return embargoee
        }
        else
        {
            def embargoedVal = embargoee.applyEmbargo()
            return embargoee.applyEmbargo()
        }
    }
    
    boolean isEmbargoed(Embargoable embargoee)
    {
		if (embargoee == null)
		{
			return false
		}
		
        return (applyEmbargo(embargoee) == null)
    }
}
