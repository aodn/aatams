package au.org.emii.aatams

class AnimalReleaseController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [animalReleaseInstanceList: AnimalRelease.list(params), animalReleaseInstanceTotal: AnimalRelease.count()]
    }

    def create = {
        def animalReleaseInstance = new AnimalRelease()
        animalReleaseInstance.properties = params
        return [animalReleaseInstance: animalReleaseInstance]
    }

    def save = {
        def animalReleaseInstance = new AnimalRelease(params)
        
        log.info("params: " + params)
        
        // The request does not include an Animal instance as such (just species
        // and sex) - so we need to create one here.
        Sex sex = Sex.get(params.sex.id)
        
        Species species = lookupOrCreateSpecies(params.speciesName)
        Animal animalInstance = new Animal(sex:sex, species:species)
        
        animalReleaseInstance.animal = animalInstance
        
        if (animalInstance.save(flush:true))
        {
            if (animalReleaseInstance.save(flush: true)) 
            {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'animalRelease.label', default: 'AnimalRelease'), animalReleaseInstance.id])}"
                redirect(action: "show", id: animalReleaseInstance.id)
            }
            else 
            {
                render(view: "create", model: [animalReleaseInstance: animalReleaseInstance, animal:animalInstance])
            }
        }
        else 
        {
            log.error("animalInstance.save() error: " + animalInstance.errors)
            render(view: "create", model: [animalReleaseInstance: animalReleaseInstance, animal:animalInstance])
        }
    }

    def show = {
        def animalReleaseInstance = AnimalRelease.get(params.id)
        if (!animalReleaseInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animalRelease.label', default: 'AnimalRelease'), params.id])}"
            redirect(action: "list")
        }
        else {
            [animalReleaseInstance: animalReleaseInstance]
        }
    }

    def edit = {
        def animalReleaseInstance = AnimalRelease.get(params.id)
        if (!animalReleaseInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animalRelease.label', default: 'AnimalRelease'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [animalReleaseInstance: animalReleaseInstance]
        }
    }

    def update = {
        def animalReleaseInstance = AnimalRelease.get(params.id)
        if (animalReleaseInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (animalReleaseInstance.version > version) {
                    
                    animalReleaseInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'animalRelease.label', default: 'AnimalRelease')] as Object[], "Another user has updated this AnimalRelease while you were editing")
                    render(view: "edit", model: [animalReleaseInstance: animalReleaseInstance])
                    return
                }
            }
            animalReleaseInstance.properties = params
            
            Species species = lookupOrCreateSpecies(params.speciesName)
            animalReleaseInstance.animal.species = species
            
            if (!animalReleaseInstance.hasErrors() && animalReleaseInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'animalRelease.label', default: 'AnimalRelease'), animalReleaseInstance.id])}"
                redirect(action: "show", id: animalReleaseInstance.id)
            }
            else {
                render(view: "edit", model: [animalReleaseInstance: animalReleaseInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animalRelease.label', default: 'AnimalRelease'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def animalReleaseInstance = AnimalRelease.get(params.id)
        if (animalReleaseInstance) {
            try {
                animalReleaseInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'animalRelease.label', default: 'AnimalRelease'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'animalRelease.label', default: 'AnimalRelease'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animalRelease.label', default: 'AnimalRelease'), params.id])}"
            redirect(action: "list")
        }
    }
    
    def lookupOrCreateSpecies(speciesName)
    {
        Species species = Species.findByName(speciesName)
        if (!species)
        {
            log.info("Creating new species, name: " + speciesName)
            species = new Species(name:speciesName).save(flush:true)
        }
         
        return species
    }
}
