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
		exportService.export(reportInfoService.getClassForName(queryName), params, response.getOutputStream())
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
