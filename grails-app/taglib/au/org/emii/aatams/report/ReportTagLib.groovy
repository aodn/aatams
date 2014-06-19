package au.org.emii.aatams.report

import au.org.emii.aatams.*

/**
 * Renders a filter for a given report.
 */
class ReportTagLib 
{
    def reportInfoService

    def reportFilterParameter =
    {
        attrs, body ->
        
        log.debug("Rendering filter parameter, attrs: " + attrs)
        out << render(template:attrs.template,
                      model:attrs.model)
    }

    def reportFilter = 
    {
        attrs, body ->
          
        ReportInfo reportInfo = reportInfoService.getReportInfo(attrs.name)
        if (!reportInfo)
        {
            // TODO: error
        }
        else if (reportInfo.filterParams)
        {
            log.debug("Rendering filter, params: " + reportInfo.filterParams)
            out << render(template:"/report/filter/reportFilter",
                          model:[filterParams:reportInfo.filterParams])
        }
    }
    
    def report =
    {
        attrs, body ->

        delegateTemplate(attrs, body, true, "report")
    }
    
    def extract =
    {
        attrs, body ->

        delegateTemplate(attrs, body, true, "extract")
    }
    
    def delegateTemplate(attrs, body, showFilter, type)
    {
        if (!attrs.name)
        {
            // TODO: error
        }
        else
        {
            ReportInfo reportInfo = reportInfoService.getReportInfo(attrs.name)
            if (!reportInfo)
            {
                // TODO: error
            }
            else
            {
                log.debug("Report info: " + reportInfo)
                log.debug("Formats: " + attrs.formats)
                
                out << render(template:"/report/reportTemplate",
                              model:[displayName:attrs.displayName,
                                     name:attrs.name,
                                     jrxmlFilename:reportInfo.jrxmlFilename[type],
                                     formats:attrs.formats,
                                     controller:"report",
                                     action:"execute",
                                     type:type,
                                     showFilter:showFilter])
            }
        }
    }
}
