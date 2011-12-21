package au.org.emii.aatams.report

import au.org.emii.aatams.*
import au.org.emii.aatams.report.filter.*

import org.hibernate.criterion.Restrictions

/**
 * This service allows clients to execute a query for a given report, applying
 * the given set of filter parameters, and returning a list of domain objects
 * which can then be passed to the   jasper reporting "engine".
 */
class ReportQueryExecutorService 
{
    static transactional = true

    def embargoService
    def permissionUtilsService
    
	List executeQuery(ReportFilter filter)
	{
		List results = filter.list([:])
		results = embargoService.applyEmbargo(results)
		return results
	}
}
