package au.org.emii.aatams

import grails.converters.JSON

class SpeciesController {

    def speciesService

    /**
     * Allows auto-complete functionality on front-end.
     */
    def lookupByName = {
        log.debug("Looking up species, name: " + params.term)

        // Delegate to the species service (limit to the first 25 items
        // since any more than that will barely fit on the screen).
        def species = speciesService.lookup(params.term) + CaabSpecies.findAllByNameIlike("%" + params.term + "%")

        // Fix for #1729 - remove duplicates (matched in both speciesService.lookup() and findAllByNameIlike() above).
        species = species.unique()

        if (species.size() > 20) {
            species = species[0..19]
        }

        log.debug("Returning: " + (species as JSON))
        render species as JSON
    }

    def lookupByNameAndReturnSpcode = {
        def species = speciesService.lookup(params.term)
        def jsonResults = species.collect  {
            [label:it.toString(), value:it.spcode]
        }
        render(jsonResults as JSON)
    }
}
