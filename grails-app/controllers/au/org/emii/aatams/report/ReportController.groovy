package au.org.emii.aatams.report

import au.org.emii.aatams.*
import org.apache.shiro.SecurityUtils

import org.codehaus.groovy.grails.plugins.jasper.*

class ReportController 
{
    def animalReleaseSummaryService
    def jasperService
    def permissionUtilsService
    def reportInfoService
    def reportQueryExecutorService
    
    def index = { }
    
    def create =
    {
        return [name:params.name,
                displayName:reportInfoService.getReportInfo(params.name).displayName,
                formats:params.formats]
    }
    
    def extract =
    {
        return [name:params.name,
                displayName:reportInfoService.getReportInfo(params.name).displayName,
                formats:params.formats]
    }
    
    def execute =
    {
        log.debug("Executing report, params: " + params)

        // Test which button was clicked (which tells us the format.
        def possibleFormats = ["PDF", "CSV"]
        possibleFormats.each
        {
            format ->
            if (params[format])
            {
                params._format = format
            }
        }
        
        def resultList = []
        
        // Special handling for animal release summary.
        // TODO: refactor to remove dependency of this controller on to 
        // AnimalReleaseSummaryService.
        if (params._name == "animalReleaseSummary")
        {
            resultList = animalReleaseSummaryService.countBySpecies()
            params.putAll(animalReleaseSummaryService.summary())
        }
        else
        {
            resultList = reportQueryExecutorService.executeQuery(reportInfoService.getClassForName(params._name), 
                                                        params.filter)
        }

        if (!resultList || resultList.isEmpty())
        {
            redirect(action:"create", params:[name:params._name, formats:[params._format]])
            flash.message = "No matching records."
            return
        }
        
        params.SUBREPORT_DIR = servletContext.getRealPath('/reports') + "/" 
        
        // Put the filter params in flash scope, since the controller chaining
        // below converts everything in "params" to its toString() representation
        // (whereas we want an actual Map delivered to Jasper).
        def filterParams = [:]
        
        Person person = permissionUtilsService.principal()
        if (person)
        {
            filterParams.user = person.name
        }

        filterParams = filterParams + reportInfoService.filterParamsToReportFormat(params.filter)
        
        log.debug("Filter params: " + filterParams)
        
        params.FILTER_PARAMS = filterParams.entrySet()
        
        // Delegate to report controller, including our wrapped data.
        JasperReportDef report = jasperService.buildReportDefinition(params, request.getLocale(), [data:resultList])
        generateResponse(report)
    }

    /**
    * Generate a html response.
    */
    def generateResponse = 
    {
        reportDef ->
    
        if (!reportDef.fileFormat.inline && !reportDef.parameters._inline) 
        {
            response.setHeader("Content-disposition", "attachment; filename="+(reportDef.parameters._name ?: reportDef.name) + "." + reportDef.fileFormat.extension);
            response.contentType = reportDef.fileFormat.mimeTyp
            response.characterEncoding = "UTF-8"
            response.outputStream << reportDef.contentStream.toByteArray()
        } 
        else
        {
            render(text: reportDef.contentStream, contentType: reportDef.fileFormat.mimeTyp, encoding: reportDef.parameters.encoding ? reportDef.parameters.encoding : 'UTF-8');
        }
    }
    
    def animalReleaseSummaryCreate =
    {
        redirect(action:"create", params:[name:"animalReleaseSummary", formats:["PDF"]])
    }
    
    def installationStationCreate =
    {
        redirect(action:"create", params:[name:"installationStation", formats:["PDF"]])
    }
    
    def receiverCreate =
    {
        redirect(action:"create", params:[name:"receiver", formats:["PDF"]])
    }
    
    def receiverDeploymentCreate =
    {
        redirect(action:"create", params:[name:"receiverDeployment", formats:["PDF"]])
    }

    def detectionExtract =
    {
        redirect(action:"extract", params:[name:"detection", formats:["CSV"]])
    }
    
    def installationExtract =
    {
        redirect(action:"extract", params:[name:"installation", formats:["CSV"]])
    }
    
    def installationStationExtract =
    {
        redirect(action:"extract", params:[name:"installationStation", formats:["CSV"]])
    }
    
    def receiverExtract =
    {
        redirect(action:"extract", params:[name:"receiver", formats:["CSV"]])
    }

    def receiverEventExtract =
    {
        redirect(action:"extract", params:[name:"receiverEvent", formats:["CSV"]])
    }

    def tagExtract =
    {
        redirect(action:"extract", params:[name:"tag", formats:["CSV"]])
    }
}
