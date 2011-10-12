package au.org.emii.aatams

import org.apache.shiro.SecurityUtils
import java.util.Arrays

class EmbargoService 
{
    static transactional = true

    def permissionUtilsService
    
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
    
    Object applyEmbargo(Object embargoee)
    {
        if (!(embargoee instanceof Embargoable))
        {
            return embargoee
        }
        
        String permissionString = permissionUtilsService.buildProjectReadPermission(embargoee.project?.id)
        boolean hasReadPermission = SecurityUtils.subject.isPermitted(permissionString)

        if (hasReadPermission)
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
        return (applyEmbargo(embargoee) == null)
    }
}
