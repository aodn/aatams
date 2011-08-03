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
        
//        def retList = CaabSpecies.findAllBySPCODEIlike(token + "%")
//        retList.addAll(CaabSpecies.findAllBySCIENTIFIC_NAME("%" + token + "%"))
//        retList.addAll(CaabSpecies.findAllByCOMMON_NAME("%" + token + "%"))

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
        }
        
        return retList
    }
}
