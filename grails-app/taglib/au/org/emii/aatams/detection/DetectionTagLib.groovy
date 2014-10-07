package au.org.emii.aatams.detection

import org.springframework.web.servlet.support.RequestContextUtils as RCU

class DetectionTagLib
{
    def paginateDetections =
    {
        attrs ->

        def messageSource = grailsAttributes.messageSource
        def locale = RCU.getLocale(request)

        out << link(action: 'list', params: (attrs.params + attrs.pageIndex?.toRequestParams())) {
            (messageSource.getMessage('paginate.next', null, messageSource.getMessage('default.paginate.next', null, 'Next', locale), locale))
        }
    }
}
