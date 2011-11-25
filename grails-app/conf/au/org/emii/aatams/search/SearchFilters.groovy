package au.org.emii.aatams.search

/**
 * Need to flush and temporarily disable mirroring to avoid hibernate errors.
 * 
 * @author jburgess
 */
class SearchFilters 
{
    def searchableService
    def sessionFactory
 
    def filters = 
    {
        save(controller:'*', action:'save|delete|update')
        {
            before =
            {
                searchableService.stopMirroring()
                sessionFactory.getCurrentSession().flush()
            }
            
            after =
            {
                sessionFactory.getCurrentSession().flush()
                searchableService.startMirroring()
            }
        }
    }
}
