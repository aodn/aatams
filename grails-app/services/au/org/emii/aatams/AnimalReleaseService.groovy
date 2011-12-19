package au.org.emii.aatams

class AnimalReleaseService 
{
    static transactional = true

	def animalFactoryService
	def jdbcTemplateDetectionFactoryService
    def tagFactoryService

	void save(AnimalRelease animalReleaseInstance, Map params) throws IllegalArgumentException 
	{
		updateAnimalRelease(animalReleaseInstance, params)
		validateParams(animalReleaseInstance, params)

		updateAnimal(params, animalReleaseInstance)

		validateRelease(animalReleaseInstance, params)
		
		addSurgeries(animalReleaseInstance, params)
		addMeasurements(animalReleaseInstance, params)
		setEmbargoDate(animalReleaseInstance, params)
		
		saveAnimalRelease(animalReleaseInstance)
		
		rescanExistingDetections(animalReleaseInstance)
    }

	private void rescanExistingDetections(animalReleaseInstance)
	{
		runAsync
		{
			animalReleaseInstance.surgeries.each
			{
				try
				{
					jdbcTemplateDetectionFactoryService.rescanForSurgery(it)
				}
				catch (Throwable t)
				{
					log.error("Error saving detection surgery", t)
					throw t
				}
			}
		}	
	}
	
	private saveAnimalRelease(AnimalRelease animalReleaseInstance) 
	{
		animalReleaseInstance.save(flush:true)	// Flush so that everything is in DB for the JDBC template detectionSurgery stuff.
	}

	private updateAnimalRelease(AnimalRelease animalReleaseInstance, Map params) {
		animalReleaseInstance.properties = params
	}

	private updateAnimal(Map params, AnimalRelease animalReleaseInstance) 
	{
		Animal animalInstance = animalFactoryService.lookupOrCreate(params)
		assert(animalInstance)
		
		animalReleaseInstance.animal = animalInstance
		animalInstance.addToReleases(animalReleaseInstance)
		finishExistingReleases(animalInstance, animalReleaseInstance)
		animalInstance.save()
	}

	private void addSurgeries(AnimalRelease animalReleaseInstance, Map params) throws IllegalArgumentException
	{
		params.surgery.each 
		{
			surgeryIndex, surgeryParams ->
			
			// See http://stackoverflow.com/questions/1811395/grails-indexed-parameters
			if (!surgeryIndex.contains("."))
			{
				log.debug("params: " + params)
				log.debug("surgeryParams: " + surgeryParams)
				
				def tag = tagFactoryService.lookupOrCreate(surgeryParams.tag)
				assert(tag)
				surgeryParams.tag = tag
				
				surgeryParams.type = SurgeryType.get(surgeryParams.type.id)
				surgeryParams.treatmentType = SurgeryTreatmentType.get(surgeryParams.treatmentType.id)
				
				Surgery surgery = new Surgery(surgeryParams)
				animalReleaseInstance.addToSurgeries(surgery)
				tag.addToSurgeries(surgery)
				
				tag.status = DeviceStatus.findByStatus('DEPLOYED')
				
				if (!tag.project)
				{
					tag.project = animalReleaseInstance.project
				}
			}
		}
	}
	
	private void addMeasurements(AnimalRelease animalReleaseInstance, Map params) throws IllegalArgumentException
	{
		params.measurement.each 
		{
			measurementIndex, measurementParams ->
			
			// See http://stackoverflow.com/questions/1811395/grails-indexed-parameters
			if (!measurementIndex.contains("."))
			{
				AnimalMeasurement measurement = new AnimalMeasurement(measurementParams)
				animalReleaseInstance.addToMeasurements(measurement)
			}
		}
	}
	
	private void validateParams(AnimalRelease animalReleaseInstance, Map params) throws IllegalArgumentException
	{
		if (!params)
		{
			throw new IllegalArgumentException("Parameters cannot be empty.")
		}

		if (noSurgeries(params))
		{
			animalReleaseInstance.errors.reject("animalRelease.noTaggings")
			throw new IllegalArgumentException("Animal release must have at least one tagging.")
		}

		if (noAnimalOrSpecies(params))
		{
			animalReleaseInstance.errors.reject("animalRelease.noSpeciesOrAnimal")
			throw new IllegalArgumentException("Species or existing animal must be specified.")
		}
	}

	private void validateRelease(AnimalRelease animalReleaseInstance, Map params) throws IllegalArgumentException 
	{
		if (!animalReleaseInstance.validate())
		{
			throw new IllegalArgumentException()
		}
	}
	
	private boolean noSurgeries(Map params)
	{
		return (!params.surgery || params.surgery.isEmpty())
	}
	
	private boolean noAnimalOrSpecies(params)
	{
		return ((!params.animal || params.animal?.id == null) && (params.speciesId == null))
	}
	
	private void finishExistingReleases(animal, animalReleaseInstance)
	{
		(animal.releases - animalReleaseInstance).each
		{
			it.status = AnimalReleaseStatus.FINISHED 
		}
	}
	
    private void setEmbargoDate(animalReleaseInstance, params)
    {
        if (params.embargoPeriod)
        {
            Calendar releaseCalendar = animalReleaseInstance.releaseDateTime?.toGregorianCalendar()
            def embargoDate = releaseCalendar.add(Calendar.MONTH, Integer.valueOf(params.embargoPeriod))
            animalReleaseInstance.embargoDate = releaseCalendar.getTime()
        }
        else
        {
            animalReleaseInstance.embargoDate = null
        }
    }
}
