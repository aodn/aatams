package au.org.emii.aatams.search

import org.compass.core.engine.SearchEngineQueryParseException

/**
 * Basic web interface for Grails Searchable Plugin
 *
 * @author Maurice Nicholson
 */
class SearchableController 
{
    def embargoService
    def searchableService

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
        
        try 
        {
            // Results are lazy-loaded, hence associations are null.
            def res = searchableService.search(params.q, params)
            def hibernateAttachedResults = refresh(res.results)
            res.results = embargoService.applyEmbargo(hibernateAttachedResults)
            
            return [searchResult: res]
        } 
        catch (SearchEngineQueryParseException ex) 
        {
            return [parseException: true]
        }
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
