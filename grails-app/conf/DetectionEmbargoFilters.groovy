import au.org.emii.aatams.*

import org.apache.shiro.SecurityUtils

/**
 * Filters detections based on whether associated tag/receiver/species is
 * embargoed and principal's read access on associated project.
 * 
 * @author jburgess
 */
class DetectionEmbargoFilters
{
    def permissionUtilsService

    def filters = 
    {
        detectionList(controller:'detection', action:'list')
        {
            after = 
            {   
                model ->
              
                model.detectionInstanceList = 
                    model?.detectionInstanceList.grep
                    {
                        String permissionString = permissionUtilsService.buildProjectReadPermission(it?.receiverDeployment?.receiver?.project?.id)
                        boolean hasReadPermission = SecurityUtils.subject.isPermitted(permissionString)
                        boolean embargoed = isEmbargoed(it)

                        (   !embargoed
                         || (   embargoed 
                             && hasReadPermission))
                    }
            }
        }

        detectionNotList(controller:'detection', action:'list', invert:true)
        {
            after = 
            {  
                model ->
              
                if (isEmbargoed(model?.detectionInstance))
                {
                    if (!SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectReadPermission(model?.detectionInstance?.receiverDeployment?.receiver?.project?.id)))
                    {
                        // Redirect.
                        redirect(controller:"auth", action:"unauthorized")
                    }
                }
            }
        }
    }
    
    def isEmbargoed(Detection detection)
    {
        Date now = new Date()
        
        if (detection?.receiverDeployment?.embargoDate?.after(now))
        {
            return true
        }

        for (Surgery it : detection?.surgeries)
        {
            if (it?.release?.embargoDate?.after(now))
            {
                return true
            }
            
            if (it?.release?.animal?.species?.embargoDate?.after(now))
            {
                return true
            }
        }

        return false
    }
}

