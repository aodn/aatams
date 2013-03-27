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
    def embargoControllers = 'animalRelease|detection|detectionSurgery|sensor|surgery|tag'
    
    def filters = 
    {
        genericList(controller: embargoControllers, action:'list')
        {
            after = 
            {
                model ->

                // Filter out entities which are embargoed.
                model.entityList = 
                    embargoService.applyEmbargo(model.entityList)
            }
        }
        
        genericNotList(controller: embargoControllers, action:notListActions)
        {
            after =
            {
                model ->

                    if (controllerName == "detection") {
                        def detectionInstance = model?.detectionInstance
                        model?.detectionInstance = embargoService.applyEmbargo(detectionInstance)
                    }
                    else {
                        
                        def instanceName = "${controllerName}Instance"
                        if (embargoService.isEmbargoed(model[instanceName]))
                        {
                            redirect(getRedirectParams(id: model[instanceName].id, controllerName: controllerName, actionName: actionName))
                        }
                    }
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

