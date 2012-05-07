package au.org.emii.aatams.report

import au.org.emii.aatams.*
import de.micromata.opengis.kml.v_2_2_0.Data
import de.micromata.opengis.kml.v_2_2_0.Document
import de.micromata.opengis.kml.v_2_2_0.ExtendedData
import de.micromata.opengis.kml.v_2_2_0.Folder
import de.micromata.opengis.kml.v_2_2_0.Kml
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import groovy.sql.Sql

import org.apache.shiro.SecurityUtils

import org.codehaus.groovy.grails.plugins.jasper.*

import org.joda.time.*

class ReportController extends AbstractController
{
    def animalReleaseSummaryService
	def detectionExtractService
    def jasperService
	def kmlService
    def permissionUtilsService
    
    def index = { }
    
    def create =
    {
        renderDefaultModel(params)
    }

	private renderDefaultModel(Map params) 
	{
		def model = 
			[name:params.name,
			 displayName:reportInfoService.getReportInfo(params.name).displayName,
			 formats:params.formats]
	}
    
    def execute =
    {
		log.debug("Executing report, params: " + params)
		
		indicateExportStart()
		
		// The only time this will be set is in integration tests (to "CSV").
		if (!params._format)
		{
			params._format = params._action_execute
		}
        
		if (params._name == "detection"  && params._format == "CSV")
		{
			detectionExtractService.generateReport(params, request, response)
		}
		else
		{
	        def resultList = generateResultList(params)
			
	        if (!checkResultList(resultList, flash, params))
			{
				return
			}
	        
			if (params._format == "KML")
			{
				long startTime = System.currentTimeMillis()
				InstallationStation.refreshDetectionCounts()
				assert(!resultList.isEmpty())
				generateKml(params, resultList)
	
				log.debug("KML generated, time: " + (System.currentTimeMillis() - startTime) + "ms.")
			}
			else
			{
				long startTime = System.currentTimeMillis()
				
				params.SUBREPORT_DIR = servletContext.getRealPath('/reports') + "/"
				params._file = reportInfoService.getReportInfo(params._name).jrxmlFilename[params._action_execute]
		
				generateReport(params, log, request, resultList)
	
				log.debug("Report generated, time: " + (System.currentTimeMillis() - startTime) + "ms.")
			}
		}
		
		response.flushBuffer()
	}

	private generateKml(params, resultList)
	{
		response.setHeader("Content-disposition", "attachment; filename=" + params._name + ".kml");
		response.contentType = "application/vnd.google-earth.kml+xml"
		response.characterEncoding = "UTF-8"
		
		final Kml kml = kmlService.toKml(resultList)
		kml.marshal(response.outputStream)
		response.outputStream.flush()
	}
	
	private generateReport(Map params, org.apache.commons.logging.Log log, javax.servlet.http.HttpServletRequest request, List resultList)
	{
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

	private boolean checkResultList(resultList, Map flash, Map params)
	{
		if (!resultList || resultList.isEmpty())
		{
			flash.message = "No matching records."

			def redirectParams = [name:params._name, formats:[params._format]]
			def action
			if (params._type == "report")
			{
				action = "create"
			}
			else
			{
				action = "extract"
			}

			redirect(action:action, params:redirectParams)
			return false
		}
		
		return true
	}

	private List generateResultList(Map params) 
	{
		def results = []

		long startTime = System.currentTimeMillis()
		
		// Special handling for animal release summary.
		// TODO: refactor to remove dependency of this controller on to
		// AnimalReleaseSummaryService.
		if (params._name == "animalReleaseSummary")
		{
			results = animalReleaseSummaryService.countBySpecies()
			params.putAll(animalReleaseSummaryService.summary())
		}
		else
		{
			
			results = queryService.query(reportInfoService.getClassForName(params._name),
										 params).results
		}
		
		log.debug("Report query executed, time: " + (System.currentTimeMillis() - startTime) + "ms.")
		return results
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
}
