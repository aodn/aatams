package au.org.emii.aatams

import org.apache.shiro.SecurityUtils

class VisibilityControlService
{
    static transactional = false

    def permissionUtilsService

    private Map<Integer, Boolean> projectPermissionCache = [:]

    public void clearCache()
    {
        projectPermissionCache.clear()
    }

    List applyVisibilityControls(Class domain, List itemsToControl)
    {
        if (!Arrays.asList(domain.getInterfaces()).contains(Embargoable.class))
        {
            return itemsToControl
        }

        return applyVisibilityControls(itemsToControl)
    }

    /**
     * Filter embargoed entities from the given list, where the user doesn't
     * have sufficient permissions.
     */
    List applyVisibilityControls(List embargoees)
    {
        clearCache()

        def retList = embargoees.collect
        {
            applyVisibilityControls(it)
        }

        retList.removeAll
        {
            it == null
        }

       return retList
    }

    private boolean hasReadPermission(itemToControl)
    {
        def projectId = itemToControl.project?.id

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

    Object applyVisibilityControls(Object itemToControl)
    {
        if (!(itemToControl instanceof Embargoable))
        {
            return itemToControl
        }

        if (hasReadPermission(itemToControl))
        {
            return itemToControl
        }

        return itemToControl.applyEmbargo()
    }

    boolean isAccessControlled(Embargoable embargoee)
    {
        if (embargoee == null)
        {
            return false
        }

        return (applyVisibilityControls(embargoee) == null)
    }
}
