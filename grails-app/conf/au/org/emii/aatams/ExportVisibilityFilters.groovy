package au.org.emii.aatams

import org.apache.shiro.SecurityUtils
import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib

class ExportVisibilityFilters {

    def filters = {

        receiverDownloadFileShow(controller: 'receiverDownloadFile', action:'show')
        {
            after =
            {
                model ->
                    if (!SecurityUtils.subject.isAuthenticated())
                    {
                        redirect([controller: "auth", action: "login",
                            params: [targetUri: getTargetUri(controllerName: controllerName, actionName: actionName)]] )
                    }
                    else if(SecurityUtils.subject.hasRole("SysAdmin")
                        || Person.findByUsername(SecurityUtils.subject.principal, [cache:true]).id
                            == model.receiverDownloadFileInstance.requestingUser.id)
                    {
                        model
                    }
                    else
                    {
                        redirect([controller:"auth", action:"unauthorized"])
                    }
            }
        }

        receiverDownloadFileList(controller: 'receiverDownloadFile', action:'list')
        {
            after =
            {
                model ->
                    if (!SecurityUtils.subject.isAuthenticated())
                    {
                         redirect([controller: "auth", action: "login",
                             params: [targetUri: getTargetUri(controllerName: controllerName, actionName: actionName)]])
                    }
                    else {

                        applyVisibility(Person.findByUsername(SecurityUtils.subject?.principal, [cache:true])
                            , SecurityUtils.subject.hasRole("SysAdmin"), model)
                        model
                    }
            }
        }
    }

    def applyVisibility( currentUser, currentUserIsSysAdmin, model )
    {
        model.receiverDownloadFileInstanceList =
            model.receiverDownloadFileInstanceList.findAll({
                item -> (currentUserIsSysAdmin
                    || item.requestingUser.id == currentUser.id)
            })
    }

    def getTargetUri(params)
    {
        return new ApplicationTagLib().createLink(absolute: true, controller: params.controllerName, action: params.actionName, id: params.id)
    }
}
