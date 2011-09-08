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

                out << render(template:"/report/filter/reportTemplate",
                              model:[name:attrs.name,
                                     jrxmlFilename:reportInfo.jrxmlFilename,
                                     format:"PDF",
                                     controller:"report",
                                     action:"execute"])
            }
        }
    }
}
