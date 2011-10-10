package au.org.emii.aatams.search

//import org.compass.core.engine.SearchEngineQueryParseException

/**
 * Basic web interface for Grails Searchable Plugin
 *
 * @author Maurice Nicholson
 */
class SearchableController 
{
    def elasticSearchService
//    def searchableService

    /**
     * Index page with search form and results
     */
    def index = 
    {
        params.max = 20
        
        if (!params.q?.trim()) 
        {
            return [:]
        }
        
//        try 
//        {
            // Results are lazy-loaded, hence associations are null.
            def res = elasticSearchService.search(params.q, params)
            res.searchResults = refresh(res.searchResults)
            return [searchResult: res]
//        } 
//        catch (SearchEngineQueryParseException ex) 
//        {
//            return [parseException: true]
//        }
    }
    
    def refresh(results)
    {
        results.collect
        {
            result ->
            
            result.get(result.id)
        }
    }
}
