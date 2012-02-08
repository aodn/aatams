package au.org.emii.aatams

import java.util.List;
import java.util.Map;

abstract class AbstractController 
{
	def queryService
	def reportInfoService

	protected def doList(queryName)
	{
		params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
		
		def resultList = queryService.query(reportInfoService.getClassForName(queryName), params)
		
		[entityList: resultList.results,
		 total: resultList.count]
	}
}
