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
		saveAnimalRelease(animalReleaseInstance)
		
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
				
				def tagParams = surgeryParams.tag
				tagParams += [project: animalReleaseInstance.project]
				
				def tag = tagFactoryService.lookupOrCreate(tagParams)
				assert(tag)
				
				// Workaround for #1731.
				// See https://forum.hibernate.org/viewtopic.php?p=2221154&sid=ca3afa263ffe05d3bcba7826b4be3ea0#p2221154
				tag.setSensors(new HashSet(tag.getSensors()))
				
				// Need to make a "clean" map, due to bug #1257.
				// Note: unable to reproduce the bug in an integration test.
				def cleanSurgeryParams = 
					[timestamp_zone:surgeryParams.timestamp_zone, 
					 timestamp_year:surgeryParams.timestamp_year, 
					 timestamp_month:surgeryParams.timestamp_month,
					 timestamp_day:surgeryParams.timestamp_day, 
					 timestamp_hour:surgeryParams.timestamp_hour, 
					 timestamp_minute:surgeryParams.timestamp_minute, 
					 timestamp_second:surgeryParams.timestamp_second, 
					 comments: surgeryParams.comments,
					 type: SurgeryType.get(surgeryParams.type.id),
					 treatmentType: SurgeryTreatmentType.get(surgeryParams.treatmentType.id),
					 tag: tag,
					 release: animalReleaseInstance]
				 
				Surgery surgery = new Surgery(cleanSurgeryParams)
				surgery.save(failOnError:true)
				
				tag.status = DeviceStatus.findByStatus('DEPLOYED')
				
				if (!tag.project)
				{
					tag.project = animalReleaseInstance.project
				}
				
				tag.save()
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
	
	void delete(AnimalRelease animalReleaseInstance)
	{
		def surgeries = Surgery.findAllByRelease(animalReleaseInstance)
		surgeries.each
		{
			it.tag.status = DeviceStatus.findByStatus('NEW', [cache:true])
			it.tag.save()
		}
		
		animalReleaseInstance.delete(flush: true)
	}
}
