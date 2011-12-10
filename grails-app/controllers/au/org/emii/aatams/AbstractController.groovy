package au.org.emii.aatams

import java.util.List;
import java.util.Map;

abstract class AbstractController 
{
	def reportFilterFactoryService
	def reportInfoService

	protected Map generateResultList(Map params)
	{
		def resultList = []

		def filterParams = [:]
		if (params.filter?.between)
		{
			 filterParams = [between:[timestamp:[params.filter.between.min.timestamp, params.filter.between.max.timestamp]]]
		}
		
		if (params.filter?.eq)
		{
			filterParams.eq = params.filter.eq
		}
		
		if (params.filter?.in)
		{
			filterParams.in = params.filter.in
		}
		
		def filter = reportFilterFactoryService.newFilter(reportInfoService.getClassForName(params._name), filterParams)
		
		def cleanedFilterParams = [:]
		params.filter?.each
		{
			k, v ->
			
			if (v instanceof Map)
			{
				println("cleaning k: " + k + ", v: " + v)
				
			}
			else
			{
				println("adding k: " + k + ", v: " + v)
				cleanedFilterParams.put('filter.' + k, v)
			}
		}
		
		return [results:filter.list(params), count:filter.count(), filter:cleanedFilterParams]
	}
}
