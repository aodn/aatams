package au.org.emii.aatams

import org.apache.shiro.SecurityUtils
import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib

class ReceiverDownloadFileFilters {

    def filters = 
    {
        receiverDownloadFileList(controller: 'receiverDownloadFile', action:'list')
        {
            after =
            {
                model ->
                    applyVisibility(Person.get(SecurityUtils.subject?.principal)
                        , SecurityUtils.subject.hasRole("SysAdmin"), model)
                    model
            }
        }
    }

    def applyVisibility(currentUser, currentUserIsSysAdmin, model)
    {
        model.receiverDownloadFileInstanceList =
            model.receiverDownloadFileInstanceList.findAll({
                item -> (currentUserIsSysAdmin
                    || item.requestingUser.id == currentUser.id)
            })
    }
}
