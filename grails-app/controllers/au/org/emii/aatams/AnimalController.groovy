package au.org.emii.aatams

import grails.converters.JSON

class AnimalController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
        [animalInstanceList: Animal.list(params), animalInstanceTotal: Animal.count()]
    }

    def create = {
        def animalInstance = new Animal()
        animalInstance.properties = params
        return [animalInstance: animalInstance]
    }

    def save = {
        def animalInstance = new Animal(params)
        if (animalInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'animal.label', default: 'Animal'), animalInstance.toString()])}"
            redirect(action: "show", id: animalInstance.id)
        }
        else {
            render(view: "create", model: [animalInstance: animalInstance])
        }
    }

    def show = {
        def animalInstance = Animal.get(params.id)
        if (!animalInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animal.label', default: 'Animal'), params.id])}"
            redirect(action: "list")
        }
        else {
            [animalInstance: animalInstance]
        }
    }

    def edit = {
        def animalInstance = Animal.get(params.id)
        if (!animalInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animal.label', default: 'Animal'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [animalInstance: animalInstance]
        }
    }

    def update = {
        def animalInstance = Animal.get(params.id)
        if (animalInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (animalInstance.version > version) {
                    
                    animalInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'animal.label', default: 'Animal')] as Object[], "Another user has updated this Animal while you were editing")
                    render(view: "edit", model: [animalInstance: animalInstance])
                    return
                }
            }
            animalInstance.properties = params
            if (!animalInstance.hasErrors() && animalInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'animal.label', default: 'Animal'), animalInstance.toString()])}"
                redirect(action: "show", id: animalInstance.id)
            }
            else {
                render(view: "edit", model: [animalInstance: animalInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animal.label', default: 'Animal'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def animalInstance = Animal.get(params.id)
        if (animalInstance) {
            try {
                animalInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'animal.label', default: 'Animal'), animalInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'animal.label', default: 'Animal'), animalInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animal.label', default: 'Animal'), params.id])}"
            redirect(action: "list")
        }
    }
   
    def lookup =
    {
		if ((params.project.id == "") || (params.species.id == ""))
		{
			return [] as JSON
		}
		
        def criteria = Animal.createCriteria()
        
        def animals = criteria
        {
            and
            {
                releases
                {
                    project
                    {
                        eq("id", Long.valueOf(params.project.id))
                    }
                }
                
                species
                {
                    eq("id", Long.valueOf(params.species.id))
                }

                if (params.sex?.id)
                {
                    sex
                    {
                        eq("id", Long.valueOf(params.sex.id))
                    }
                }
            }
        }

        log.debug("animals: " + animals)
        render (animals as JSON)
    }
}
