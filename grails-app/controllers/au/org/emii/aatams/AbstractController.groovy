package au.org.emii.aatams

import javax.servlet.http.Cookie

class AbstractController 
{
	def exportService
	def queryService
	def reportInfoService

	protected def doList(queryName)
	{
		params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
		
		def resultList = queryService.query(reportInfoService.getClassForName(queryName), params)

		flattenParams()

		flash.message = "${resultList.count} matching records (${reportInfoService.getClassForName(queryName).count()} total)."
		
		[entityList: resultList.results,
		 total: resultList.count]
	}
	
	protected def doExport(queryName)
	{
		if (!params.format)
		{
			params.format = params._action_export
		}
		
		indicateExportStart()
		
		response.setHeader("Content-disposition", "attachment; filename=" + queryName + "." + params.format.toLowerCase());
		response.contentType = getMimeType(params)
		response.characterEncoding = "UTF-8"
		
		exportService.export(reportInfoService.getClassForName(queryName), params, response.outputStream)
		response.flushBuffer()
	}
	
	protected void indicateExportStart()
	{
		response.reset()

		// Indicate to the client that we have received the export request.
		// See: http://geekswithblogs.net/GruffCode/archive/2010/10/28/detecting-the-file-download-dialog-in-the-browser.aspx
		response.addCookie(new Cookie("fileDownloadToken", params.downloadTokenValue))
	}
	
	private String getMimeType(params)
	{
		def mimeTypes = [PDF: "application/pdf", CSV: "text/csv", KML: "application/vnd.google-earth.kml+xml", KMZ: " application/vnd.google-earth.kmz"]
		
		return mimeTypes[params.format]
	}
	
	protected void flattenParams()
	{
		def flattenedParams = [:]
		
		params.each
		{
			k, v ->
			
			if (   !k.startsWith("filter") 
				|| (k.endsWith(".eq")) 
				|| (k.endsWith(".in")) 
				|| (k.endsWith(".isNull")) 
				|| (k.endsWith(".between.0"))
				|| (k.endsWith(".between.1"))
				|| (k.endsWith(".between.2")))
			{
				flattenedParams.put(k, v)
			}
		}
		
		params.clear()
		params.putAll(flattenedParams)
	}
}
