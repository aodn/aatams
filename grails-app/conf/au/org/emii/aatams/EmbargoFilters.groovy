package au.org.emii.aatams

import org.apache.shiro.SecurityUtils
import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib

/**
 * Filters tag/receiver/species if it is embargoed and principal doesn't have
 * read access on associated project.
 * 
 * @author jburgess
 */
class EmbargoFilters
{
	def grailsApplication
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
					redirect(getRedirectParams(id: model?.animalReleaseInstance.id, controllerName: controllerName, actionName: actionName))
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
					redirect(getRedirectParams(id: model?.tagInstance.id, controllerName: controllerName, actionName: actionName))
                }
            }
        }

        sensorList(controller:'sensor', action:'list')
        {
            after =
            {
                model ->

                model.entityList = 
                    embargoService.applyEmbargo(model.entityList)
            }
        }

        sensorNotList(controller:'sensor', action:notListActions)
        {
            after =
            {
                model ->

                if (embargoService.isEmbargoed(model?.sensorInstance))
                {
					redirect(getRedirectParams(id: model?.sensorInstance.id, controllerName: controllerName, actionName: actionName))
                }
            }
        }

        detectionNotList(controller:'detection', action:notListActions)
        {
            after =
            {
                model ->
                
                def detectionInstance = model?.detectionInstance
                model?.detectionInstance = embargoService.applyEmbargo(detectionInstance)
            }
        }
    }
	
	def getRedirectParams(params)
	{
		if (SecurityUtils.subject.isAuthenticated())
		{
			return [controller:"auth", action:"unauthorized"]
		}
		else
		{
			return [controller: "auth", action: "login", params: [targetUri: getTargetUri(params)]]
		}
	}

	def getTargetUri(params) 
	{
		return new ApplicationTagLib().createLink(absolute: true, controller: params.controllerName, action: params.actionName, id: params.id)
	}
}

