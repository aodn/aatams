package au.org.emii.aatams

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

		[entityList: resultList.results,
		 total: resultList.count]
	}
	
	protected def doExport(queryName)
	{
		if (!params.format)
		{
			params.format = params._action_export
		}
		
		response.setHeader("Content-disposition", "attachment; filename=" + queryName + "." + params.format.toLowerCase());
		response.contentType = getMimeType(params)
		response.characterEncoding = "UTF-8"

		exportService.export(reportInfoService.getClassForName(queryName), params, response.outputStream)
	}
	
	private String getMimeType(params)
	{
		def mimeTypes = [PDF: "application/pdf", CSV: "text/csv", KML: "application/vnd.google-earth.kml+xml"]
		
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
