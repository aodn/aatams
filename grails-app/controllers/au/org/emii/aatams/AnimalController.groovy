package au.org.emii.aatams

import grails.converters.JSON

class AnimalController extends AbstractController {

    def lookup = {
        if ((params.project.id == "") || (params.species.id == "")) {
            return [] as JSON
        }

        def criteria = Animal.createCriteria()

        def animals = criteria {
            and {
                releases {
                    project {
                        eq("id", Long.valueOf(params.project.id))
                    }
                }

                species {
                    eq("id", Long.valueOf(params.species.id))
                }

                if (params.sex?.id) {
                    sex {
                        eq("id", Long.valueOf(params.sex.id))
                    }
                }
            }
        }

        log.debug("animals: " + animals)
        render (animals as JSON)
    }
}
