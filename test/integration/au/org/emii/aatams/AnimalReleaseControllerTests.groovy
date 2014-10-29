package au.org.emii.aatams

import grails.test.*

class AnimalReleaseControllerTests extends ControllerUnitTestCase
{
    void testPingerType()
    {
        CodeMap a69_1303 = CodeMap.build(codeMap: 'A69-1303')
        TagDeviceModel v8 = TagDeviceModel.build(modelName: 'V8')
        SurgeryType internal = SurgeryType.build(type: 'INTERNAL')
        SurgeryTreatmentType antibiotic = SurgeryTreatmentType.build(type: 'antibiotic')
        CaptureMethod net = CaptureMethod.build(name: 'NET')
        Project whale = Project.build(name: 'Whale')
        CaabSpecies whiteShark = CaabSpecies.build(commonName: 'White Shark')
        Sex male = Sex.build(sex: 'MALE')

        controller.params.releaseDateTime_hour = '9'

        def tag = [
            codeMap: a69_1303,
            pingCode: "11111",
            serialNumber:"1111",
            model: v8
        ]

        def surgery = [0:[timestamp_minute:"40",
                          tag:tag,
                          timestamp_year:"2011",
                          timestamp_hour:"9",
                          comments:null,
                          timestamp_day:"26",
                          timestamp_zone:"Australia/Hobart",
                          type: internal,
                          treatmentType: antibiotic,
                          timestamp_month:"10"]]
        controller.params.surgery = surgery

        controller.params.releaseDateTime_zone = "Australia/Hobart"
        controller.params.releaseDateTime_minute = "40"
        controller.params.releaseLocality = "Somewhere"
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
        controller.params.captureMethod = net
        controller.params.captureDateTime_zone = "Australia/Hobart"
        controller.params.captureDateTime_hour = '9'

        controller.params.project = whale

        CaabSpecies species = whiteShark
        controller.params.speciesName = species.name
        controller.params.speciesId = species.id
        controller.params.sex = male
        controller.params.animal = [id:null]
        controller.params.embargoPeriod = null
        controller.params.comments = null

        def model = controller.save()

        assertEquals("nullable", model.animalReleaseInstance.errors.getFieldError("captureLocality").getCode())
    }
}
