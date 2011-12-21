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
    def embargoService

    def notListActions = 'show|edit|update|delete'
    def filters = 
    {
        animalReleaseList(controller:'animalRelease', action:'list')
        {
            after =
            {
                model ->

                // Filter out entities which are embargoed.
                model.entityList = 
                    embargoService.applyEmbargo(model.entityList)
            }
        }

        animalReleaseNotList(controller:'animalRelease', action:notListActions)
        {
            after =
            {
                model ->

                if (embargoService.isEmbargoed(model?.animalReleaseInstance))
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
                model.entityList = 
                    embargoService.applyEmbargo(model.entityList)
            }
        }

        tagNotList(controller:'tag', action:notListActions)
        {
            after =
            {
                model ->
                
				if (embargoService.isEmbargoed(model?.tagInstance))
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

                model.sensorInstanceList = 
                    embargoService.applyEmbargo(model.sensorInstanceList)
				model.entityList = model.sensorInstanceList
            }
        }

        sensorNotList(controller:'sensor', action:notListActions)
        {
            after =
            {
                model ->

                if (embargoService.isEmbargoed(model?.sensorInstance))
                {
                    // Redirect.
                    redirect(controller:"auth", action:"unauthorized")
                }
            }
        }

        detectionNotList(controller:'detection', action:notListActions)
        {
            after =
            {
                model ->
                
                def detectionInstance = model?.detectionInstance
                
                model.detectionInstance = embargoService.applyEmbargo(detectionInstance)
            }
        }
    }
}

