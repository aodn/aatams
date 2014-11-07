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
                    else {

                    println "Not filtering"
                }
            }
        }

        genericNotList(controller: visibilityControlControllers, action:notListActions)
        {
            after =
            {
                model ->

                    if (controllerName == "detection") {
                        def detectionInstance = model?.detectionInstance
                        model?.detectionInstance = visibilityControlService.applyVisibilityControls(detectionInstance)
                    }
                    else {

                        def instanceName = "${controllerName}Instance"
                        if (visibilityControlService.isAccessControlled(model[instanceName]))
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

