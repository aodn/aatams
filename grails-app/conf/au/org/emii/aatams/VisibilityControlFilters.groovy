package au.org.emii.aatams

import org.apache.shiro.SecurityUtils
import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib

/**
 * Filters tag/receiver/species if it is embargoed and principal doesn't have
 * read access on associated project.
 *
 * @author jburgess
 */
class VisibilityControlFilters
{
    def grailsApplication
    def visibilityControlService

    def notListActions = 'show|edit|update|delete'
    def visibilityControlControllers = 'animal|animalRelease|detection|detectionSurgery|sensor|surgery|tag'

    def filters =
    {
        genericList(controller: visibilityControlControllers, action:'list')
        {
            after = {
                model ->

                // Filter out entities which are embargoed.
                if (controllerName != 'detection') {
                    model.entityList =
                            visibilityControlService.applyVisibilityControls(model.entityList)
                }
            }
        }

        genericNotList(controller: visibilityControlControllers, action: notListActions)
        {
            after = { model ->

                if (model) {
                    def instanceName = "${controllerName}Instance"
                    def instance = model[instanceName]

                    if (visibilityControlService.isAccessControlled(instance)) {
                        redirect(getRedirectParams(id: instance.id, controllerName: controllerName, actionName: actionName))
                    }
                    else {
                        model[instanceName] = visibilityControlService.applyVisibilityControls(instance)
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

