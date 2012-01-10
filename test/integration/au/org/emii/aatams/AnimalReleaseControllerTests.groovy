package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase;
import grails.test.*

class AnimalReleaseControllerTests extends AbstractControllerUnitTestCase 
{
    protected void setUp() 
	{
        super.setUp()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void pingerType() 
	{
//		2011-10-26 09:40:34,694 [http-8080-3] DEBUG grails.app.controller.au.org.emii.aatams.AnimalReleaseController - params:
		controller.params.releaseDateTime_hour = '9'
		
		def tag = [codeMap:CodeMap.findByCodeMap("A69-1303"), pingCode:"11111", serialNumber:"1111", model:TagDeviceModel.findByModelName('V8')]
		def surgery = [0:[timestamp_minute:"40", 
						  tag:tag, 
						  timestamp_year:"2011", 
						  timestamp_hour:"9", 
						  comments:null, 
						  timestamp_day:"26", 
						  timestamp_zone:"Australia/Hobart",
						  type:SurgeryType.findByType('INTERNAL'),
						  treatmentType:SurgeryTreatmentType.findByType('ANTIBIOTIC'), 
						  timestamp_month:"10"]]
		controller.params.surgery = surgery

		controller.params.releaseDateTime_zone = "Australia/Hobart"
		controller.params.releaseDateTime_minute = "40"
		controller.params.releaseLocality = "Somewhere"
//		controller.params.releaseLocation= null
		controller.params.releaseDateTime_month = "10"
		controller.params.releaseLocation_lat = null
		controller.params.releaseDateTime_year = '2011'
		controller.params.releaseLocation_srid = null
		controller.params.releaseLocation_lon = null
		controller.params.releaseDateTime_day = '26'
		
		controller.params.captureLocation_lon = null
		controller.params.captureLocality = null
		controller.params.captureLocation_srid = null
		controller.params.captureDateTime_year = '2011'
		controller.params.captureDateTime_day = '26'
		controller.params.captureDateTime_minute = '40'
		controller.params.captureLocation_lat = null
		controller.params.captureDateTime_month = '10'
		controller.params.captureMethod = CaptureMethod.findByName('NET')
		controller.params.captureDateTime_zone = "Australia/Hobart"
		controller.params.captureDateTime_hour = '9'
		
		controller.params.project = Project.findByName('Whale')
		
		CaabSpecies species = CaabSpecies.findByCommonName("White Shark")
		controller.params.speciesName = species.name
		controller.params.speciesId = species.id
		controller.params.sex = Sex.findBySex('MALE')
		controller.params.animal = [id:null]
		controller.params.embargoPeriod = null
		controller.params.comments = null

		def model = controller.save()

		println(model.animalReleaseInstance.errors)
		assertEquals("nullable", model.animalReleaseInstance.errors.getFieldError("captureLocality").getCode())
    }
}
