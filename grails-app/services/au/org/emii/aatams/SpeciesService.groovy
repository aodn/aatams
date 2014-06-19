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
        
        def spcodeMatches =  CaabSpecies.findAllBySpcodeIlike("%" + token + "%", [cache:true, max:20, sort:"spcode", order:"asc"])      
        def commonNameMatches =  CaabSpecies.findAllByCommonNameIlike("%" + token + "%", [cache:true, max:20, sort:"commonName", order:"asc"])      
        def scientificNameMatches =  CaabSpecies.findAllByScientificNameIlike("%" + token + "%", [cache:true, max:20, sort:"scientificName", order:"asc"])
        
        return (spcodeMatches + commonNameMatches + scientificNameMatches).unique()      
    }
}
