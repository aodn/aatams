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
    List<Embargoable> applyEmbargo(List<Embargoable> embargoees) 
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
    
    Embargoable applyEmbargo(Embargoable embargoee)
    {
        String permissionString = permissionUtilsService.buildProjectReadPermission(embargoee.project?.id)
        boolean hasReadPermission = SecurityUtils.subject.isPermitted(permissionString)

        if (hasReadPermission)
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
        return (applyEmbargo(embargoee) == null)
    }
}
