package au.org.emii.aatams.search

/**
 *
 * @author jburgess
 */
class SearchFilters 
{
    def searchableService
    def sessionFactory
 
    def filters = 
    {
        save(controller:'*', action:'save')
        {
            before =
            {
                searchableService.stopMirroring()
            }
            
            after =
            {
                sessionFactory.getCurrentSession().flush()
                searchableService.startMirroring()
            }
        }
    }
}
