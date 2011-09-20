package au.org.emii.aatams

import grails.converters.JSON

class SpeciesController {

    def speciesService
    
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [speciesInstanceList: Species.list(params), speciesInstanceTotal: Species.count()]
    }

    def create = {
        def speciesInstance = new Species()
        speciesInstance.properties = params
        return [speciesInstance: speciesInstance]
    }

    def save = {
        def speciesInstance = new Species(params)
        if (speciesInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'species.label', default: 'Species'), speciesInstance.toString()])}"
            redirect(action: "show", id: speciesInstance.id)
        }
        else {
            render(view: "create", model: [speciesInstance: speciesInstance])
        }
    }

    def show = {
        def speciesInstance = Species.get(params.id)
        if (!speciesInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'species.label', default: 'Species'), params.id])}"
            redirect(action: "list")
        }
        else {
            [speciesInstance: speciesInstance]
        }
    }

    def edit = {
        def speciesInstance = Species.get(params.id)
        if (!speciesInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'species.label', default: 'Species'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [speciesInstance: speciesInstance]
        }
    }

    def update = {
        def speciesInstance = Species.get(params.id)
        if (speciesInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (speciesInstance.version > version) {
                    
                    speciesInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'species.label', default: 'Species')] as Object[], "Another user has updated this Species while you were editing")
                    render(view: "edit", model: [speciesInstance: speciesInstance])
                    return
                }
            }
            speciesInstance.properties = params
            if (!speciesInstance.hasErrors() && speciesInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'species.label', default: 'Species'), speciesInstance.toString()])}"
                redirect(action: "show", id: speciesInstance.id)
            }
            else {
                render(view: "edit", model: [speciesInstance: speciesInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'species.label', default: 'Species'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def speciesInstance = Species.get(params.id)
        if (speciesInstance) {
            try {
                speciesInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'species.label', default: 'Species'), speciesInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'species.label', default: 'Species'), speciesInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'species.label', default: 'Species'), params.id])}"
            redirect(action: "list")
        }
    }
    
    /**
     * Allows auto-complete functionality on front-end.
     */
    def lookupByName =
    {
        log.debug("Looking up species, name: " + params.term)
        
        // Delegate to the species service (limit to the first 25 items
        // since any more than that will barely fit on the screen).
        def species = speciesService.lookup(params.term) + Species.findAllByNameIlike("%" + params.term + "%")
        if (species.size() > 20)
        {
            species = species[0..19]
        }
        
        log.debug("Returning: " + (species as JSON))
        render species as JSON
    }
}
