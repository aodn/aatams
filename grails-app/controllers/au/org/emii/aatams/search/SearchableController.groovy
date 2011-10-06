package au.org.emii.aatams.search

import org.compass.core.engine.SearchEngineQueryParseException

/**
 * Basic web interface for Grails Searchable Plugin
 *
 * @author Maurice Nicholson
 */
class SearchableController 
{
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
            return [searchResult: searchableService.search(params.q, params)]
        } 
        catch (SearchEngineQueryParseException ex) 
        {
            return [parseException: true]
        }
    }
}
