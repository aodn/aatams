package au.org.emii.aatams

import au.org.emii.aatams.detection.DetectionFactoryService

class AnimalReleaseController extends AbstractController
{
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    
    def animalFactoryService
    def animalReleaseService
    def candidateEntitiesService
    def detectionFactoryService
    
    def tagFactoryService
    
    def index = {
        redirect(action: "list", params: params)
    }

    def list = 
    {
        doList("animalRelease")
    }

    def create = {
        def animalReleaseInstance = new AnimalRelease()
        animalReleaseInstance.properties = params
        
        renderDefaultModel(animalReleaseInstance, params)
    }
    
    def addDependantEntity(params)
    {
        //
        // If the animalReleaseId has been specified (i.e. the request has 
        // originated as part of editing an existing animal release), then
        // look up the animal release and put it in the model.
        //
        def animalReleaseInstance
        
        if (params.id)
        {
            animalReleaseInstance = AnimalRelease.get(params.id)
            if (!animalReleaseInstance) 
            {
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animalRelease.label', default: 'AnimalRelease'), params.id])}"
                redirect(action: "list")
                return
            }
        }
        else
        {
            animalReleaseInstance = new AnimalRelease()
            animalReleaseInstance.project = Project.get(params.projectId)
        }

        [animalReleaseInstance: animalReleaseInstance]
    }
    
    /**
     * This method just populates the model with appropriate objects when
     * adding surgery to an animal release.
     */
    def addSurgery =
    {
        log.debug("In addSurgery, params: " + params + ", flash: " + flash)
        addDependantEntity(params)
    }

    def addMeasurement =
    {
        log.debug("In addMeasurement, params: " + params + ", flash: " + flash)
        addDependantEntity(params)
    }

    def save =
    {
        def animalReleaseInstance = new AnimalRelease()
        
        try 
        {
            animalReleaseService.save(animalReleaseInstance, params)
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'animalRelease.label', default: 'AnimalRelease'), animalReleaseInstance.toString()])}"
            redirect(action: "show", id: animalReleaseInstance.id)
        }
        catch (IllegalArgumentException e)
        {
            log.error("Error saving animal release", e)
            renderDefaultModel(animalReleaseInstance, params)
        }
    }

    private renderDefaultModel(AnimalRelease animalReleaseInstance, params) 
    {
        def model =
                [animalReleaseInstance: animalReleaseInstance] \
              + [candidateProjects:candidateEntitiesService.projects()] \
              + embargoPeriods()
              
        if (params.speciesId && params.speciesName)      
        {
            def species = [id:params.speciesId, name:params.speciesName]
            model.species = species
        }

        render(view: "create", model: model)
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
            def model = [animalReleaseInstance: animalReleaseInstance]
            addEmbargoPeriodsToModel(model)
            model.candidateProjects = candidateEntitiesService.projects()

            return model
        }
    }

    def update = 
    {
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
            
            def animal = animalFactoryService.lookupOrCreate(params)
            
            params.remove('animal')
            params.remove('animal.id')
            log.debug("params: " + params)
            
            animalReleaseInstance.properties = params
            animalReleaseInstance.animal = animal
            
            // Set the embargo date if embargo period (in months) has been specified.
            setEmbargoDate(params, animalReleaseInstance)
            
            if (   !animalReleaseInstance.hasErrors() 
                && !animal.hasErrors()
                && animal.save(flush: true)) 
            {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'animalRelease.label', default: 'AnimalRelease'), animalReleaseInstance.toString()])}"
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
                animalReleaseService.delete(animalReleaseInstance)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'animalRelease.label', default: 'AnimalRelease'), animalReleaseInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'animalRelease.label', default: 'AnimalRelease'), animalReleaseInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'animalRelease.label', default: 'AnimalRelease'), params.id])}"
            redirect(action: "list")
        }
    }
    
    def setEmbargoDate(params, animalReleaseInstance)
    {
        if (params.embargoPeriod)
        {
            Calendar releaseCalendar = animalReleaseInstance.releaseDateTime?.toGregorianCalendar()
            def embargoDate = releaseCalendar.add(Calendar.MONTH, Integer.valueOf(params.embargoPeriod))
            animalReleaseInstance.embargoDate = releaseCalendar.getTime()
        }
    }
    
    def addEmbargoPeriodsToModel(def model)
    {
        def embargoPeriods = [6: '6 months', 12: '12 months', 36: '3 years']
        model?.embargoPeriods = embargoPeriods.entrySet()
    }

    def embargoPeriods()
    {
        return [embargoPeriods:[6: '6 months', 12: '12 months', 36: '3 years']]
    }
}


