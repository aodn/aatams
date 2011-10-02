package au.org.emii.aatams

class SpeciesService 
{
    static transactional = true

    /**
     * Lookup species with the given token.
     */
    def lookup(String token) 
    {
        // Match CAABSpecies on code, common name and scientific name.
        log.debug("Matching token: " + token)
        
        def criteria = CaabSpecies.createCriteria()
        
        def retList = criteria
        {
            or
            {
                ilike("SPCODE", token + "%")
                ilike("SCIENTIFIC_NAME", "%" + token + "%")
                ilike("COMMON_NAME", "%" + token + "%")
            }
            order("SPCODE", "asc")
            cache true
            maxResults(20)
        }
        
        return retList
    }
}
