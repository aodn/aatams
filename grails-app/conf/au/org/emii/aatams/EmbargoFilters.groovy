package au.org.emii.aatams

import org.apache.shiro.SecurityUtils

/**
 * Filters tag/receiver/species if it is embargoed and principal doesn't have
 * read access on associated project.
 * 
 * @author jburgess
 */
class EmbargoFilters
{
    def permissionUtilsService

    def notListActions = 'create|save|show|edit|update|delete'
    def filters = 
    {
        animalReleaseList(controller:'animalRelease', action:'list')
        {
            after =
            {
                model ->

                // Filter out releases which are embargoed.
                model.animalReleaseInstanceList =
                    model?.animalReleaseInstanceList.grep
                    {
                        String permissionString = permissionUtilsService.buildProjectReadPermission(it?.project?.id)
                        boolean hasReadPermission = SecurityUtils.subject.isPermitted(permissionString)
                        boolean embargoed = isEmbargoed(it)
                        
                        (!embargoed || hasReadPermission)
                    }
            }
        }

        animalReleaseNotList(controller:'animalRelease', action:notListActions)
        {
            after =
            {
                model ->

                def animalReleaseInstance = model?.animalReleaseInstance
                String permissionString = permissionUtilsService.buildProjectReadPermission(animalReleaseInstance?.project?.id)
                boolean hasReadPermission = SecurityUtils.subject.isPermitted(permissionString)
                boolean embargoed = isEmbargoed(animalReleaseInstance)
                        
                if (embargoed && !hasReadPermission)
                {
                    // Redirect.
                    redirect(controller:"auth", action:"unauthorized")
                }
            }
        }

        tagList(controller:'tag', action:'list')
        {
            after =
            {
                model ->

                // Filter out tag which have associated embargoed releases.
                model.tagInstanceList =
                    model?.tagInstanceList.grep
                    {
                        String permissionString = permissionUtilsService.buildProjectReadPermission(it?.project?.id)
                        boolean hasReadPermission = SecurityUtils.subject.isPermitted(permissionString)
                        boolean embargoed = isEmbargoed(it?.surgeries*.release)
                            
                        if (it instanceof Sensor)
                        {
                            embargoed |= isEmbargoed(it?.tag?.surgeries*.release)
                        }
                        
                        (!embargoed || hasReadPermission)
                    }
            }
        }

        tagNotList(controller:'tag', action:notListActions)
        {
            after =
            {
                model ->

                def tagInstance = model?.tagInstance
                String permissionString = permissionUtilsService.buildProjectReadPermission(tagInstance?.project?.id)
                boolean hasReadPermission = SecurityUtils.subject.isPermitted(permissionString)
                boolean embargoed = isEmbargoed(tagInstance?.surgeries*.release)

                if (tagInstance instanceof Sensor)
                {
                    embargoed |= isEmbargoed(tagInstance?.tag?.surgeries*.release)
                }
                        
                if (embargoed && !hasReadPermission)
                {
                    // Redirect.
                    redirect(controller:"auth", action:"unauthorized")
                }
            }
        }

        sensorList(controller:'sensor', action:'list')
        {
            after =
            {
                model ->

                // Filter out sensor which have associated embargoed releases.
                model.sensorInstanceList =
                    model?.sensorInstanceList.grep
                    {
                        String permissionString = permissionUtilsService.buildProjectReadPermission(it?.project?.id)
                        boolean hasReadPermission = SecurityUtils.subject.isPermitted(permissionString)
                        boolean embargoed = isEmbargoed(it?.tag?.surgeries*.release)
                        
                        (!embargoed || hasReadPermission)
                    }
            }
        }

        sensorNotList(controller:'sensor', action:notListActions)
        {
            after =
            {
                model ->

                def sensorInstance = model?.sensorInstance
                String permissionString = permissionUtilsService.buildProjectReadPermission(sensorInstance?.project?.id)
                boolean hasReadPermission = SecurityUtils.subject.isPermitted(permissionString)
                boolean embargoed = isEmbargoed(sensorInstance?.tag?.surgeries*.release)
                        
                if (embargoed && !hasReadPermission)
                {
                    // Redirect.
                    redirect(controller:"auth", action:"unauthorized")
                }
            }
        }

        // TODO: check for transmitter name which matches a tag name - and remove
        // if it matches an embargoed tag.
//        detectionList(controller:'detection', action:'list')
//        {
//            after =
//            {
//                model ->
//
//                // Remove any embargoed surgeries.
//                model.detectionInstanceList =
//                    model?.detectionInstanceList.each
//                    {
//                        String permissionString = permissionUtilsService.buildProjectReadPermission(it?.project?.id)
//                        boolean hasReadPermission = SecurityUtils.subject.isPermitted(permissionString)
//                        boolean embargoed = isEmbargoed(it?.surgeries*.release)
//                            
//                        if (   !embargoed
//                            || (   embargoed 
//                                && hasReadPermission))
//                        {
//                            asdf
//                        }
//                    }
//            }
//        }

        detectionNotList(controller:'detection', action:notListActions)
        {
            after =
            {
                model ->

                def detectionInstance = model?.detectionInstance
                
                String permissionString = permissionUtilsService.buildProjectReadPermission(detectionInstance?.project?.id)
                boolean hasReadPermission = SecurityUtils.subject.isPermitted(permissionString)
                
                // No need to do any filtering.
                println("detection.id: " + detectionInstance.id)
                if (hasReadPermission)
                {
                    println("hasReadPermission")
                }
                else
                {
                    println("not hasReadPermission")
                    
                    // Return a temporary detection, with embargoed surgeries removed.
                    Detection retDetection = new Detection(detectionInstance)
                    retDetection.detectionSurgeries = []

                    // Filter
                    detectionInstance?.detectionSurgeries.each
                    {
                        if (!isEmbargoed(it.surgery.release))
                        {
                            retDetection.addToDetectionSurgeries(it)
                        }
                    }

                    model.detectionInstance = retDetection
                }
            }
        }
    }

    def isEmbargoed(Collection<AnimalRelease> animalReleases)
    {
        for (AnimalRelease animalRelease : animalReleases)
        {
            if (animalRelease?.embargoDate?.after(now()))
            {
                return true
            }
        }
        
        return false
    }
    
    def isEmbargoed(AnimalRelease animalRelease)
    {
        if (animalRelease?.embargoDate?.after(now()))
        {
            return true
        }
        
        return false
    }
    
    private Date now()
    {
        return new Date()
    }
}

