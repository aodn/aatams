package au.org.emii.aatams

import org.apache.shiro.SecurityUtils
import java.util.Arrays

class EmbargoService
{
    static transactional = false

    def permissionUtilsService

    private Map<Integer, Boolean> projectPermissionCache = [:]

    public void clearCache()
    {
        projectPermissionCache.clear()
    }

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
        clearCache()

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

        // Some detections have no related project, as far as embargoes go.
        if (projectId == null)
        {
            return true
        }

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
