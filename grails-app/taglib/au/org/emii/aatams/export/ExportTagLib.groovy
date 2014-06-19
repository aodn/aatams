package au.org.emii.aatams.export

import au.org.emii.aatams.report.ReportInfoService

class ExportTagLib 
{
    def reportInfoService
    
    def exportPane =
    {
        attrs, body ->
        
        out << render(template:"/export/exportPaneTemplate",
                       model: attrs + [body:body])
    }
}
