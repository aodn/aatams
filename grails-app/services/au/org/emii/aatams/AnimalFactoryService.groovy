package au.org.emii.aatams

class AnimalFactoryService 
{
    static transactional = true

    def lookupOrCreate(params)
    {
        // If animal.id is specified, just return that animal.
        if (params.animal?.id)
        {
            log.debug("Returning existing animal...")
            return Animal.get(params.animal?.id)
        }
        
        // Otherwise, we need to create a new Animal.
        log.debug("Creating new animal...")
        Sex sex = Sex.get(params.sex?.id)
        Species species = lookupOrCreateSpecies(params)
        
        Animal animalInstance = new Animal(sex:sex, species:species).save()
        assert(animalInstance)
        
        if (animalInstance?.hasErrors())
        {
            log.error(animalInstance?.errors)
        }
        
        return animalInstance
    }
    
    def lookupOrCreateSpecies(params)
    {
        // Use species ID if specified, otherwise create a new species (from
        // the species name).
        
        if (params.speciesId)
        {
            return Species.get(params.speciesId)
        }
        
        // No species ID specified - create a new species.
        log.info("Creating new species, name: " + params.speciesName)
        return new Species(name:params.speciesName).save()
    }
}
