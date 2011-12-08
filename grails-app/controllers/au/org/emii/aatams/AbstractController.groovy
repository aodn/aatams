package au.org.emii.aatams

import java.util.List;
import java.util.Map;

abstract class AbstractController 
{
	def reportFilterFactoryService
	def reportInfoService
	def reportQueryExecutorService
	

	protected List generateResultList(Map params)
	{
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
			
			filterParams.max = Integer.valueOf(params.max)
			filterParams.offset = Integer.valueOf(params.offset ?: 0)
			log.debug("filterParams: " + filterParams)
			
			long startTime = System.currentTimeMillis()
			
			resultList = reportQueryExecutorService.executeQuery(
					reportFilterFactoryService.newFilter(reportInfoService.getClassForName(params._name),
					filterParams))
			
			log.debug("Report query executed, time: " + (System.currentTimeMillis() - startTime) + "ms.")
		}
		
		return resultList
	}
}
