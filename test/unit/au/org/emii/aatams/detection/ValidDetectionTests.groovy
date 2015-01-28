package au.org.emii.aatams.detection

import au.org.emii.aatams.*
import grails.test.*
import com.vividsolutions.jts.geom.*

class ValidDetectionTests extends GrailsUnitTestCase
{
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"

    protected void setUp()
    {
        super.setUp()

        mockDomain(ValidDetection)
        mockDomain(Sensor)
        mockDomain(Surgery)
        mockDomain(Tag)
    }

    void testIsDuplicateTrue()
    {
        Point location = new GeometryFactory().createPoint(new Coordinate(45.1234f, -40.1234f))
        Date timestamp = new Date().parse(DATE_FORMAT, "2009-12-08 06:44:24 UTC")
        def params =
                [receiverDownload:new ReceiverDownloadFile(),
                 timestamp:timestamp,
                 receiverName:"VR3UWM-354",
                 transmitterId:"A69-1303-62347",
                 transmitterName:"shark tag",
                 transmitterSerialNumber:"1234",
                 sensorValue:123,
                 sensorUnit:"ADC",
                 stationName:"Neptune SW 1",
                 location:location,
                 receiverDeployment:new ReceiverDeployment()]

        ValidDetection validDet = new ValidDetection(params)
        validDet.save()
        assertFalse(validDet.hasErrors())
        assertTrue(validDet.validate())

        assertTrue(ValidDetection.isDuplicate(params))
        assertTrue(validDet.isDuplicate(params))

        params.receiverName = "XYZ"
        assertFalse(ValidDetection.isDuplicate(params))
    }

    void testGetSurgeriesNoSensorOrSurgery()
    {
        def transmitterId = 'A69-1303-1234'

        def detection = new ValidDetection(transmitterId: transmitterId)
        assertEquals([], detection.surgeries)
    }

    void testGetSurgeriesWithSurgery()
    {
        def transmitterId = 'A69-1303-1234'

        def tag = new Tag(
            codeMap: new CodeMap(codeMap: 'A69-1303'),
            model: new TagDeviceModel(),
            serialNumber: '1234',
            status: new DeviceStatus()
        )
        tag.save()

        def sensor = new Sensor(
            tag: tag, transmitterId: transmitterId, pingCode: '1234', transmitterType: new TransmitterType()
        )
        sensor.save()

        tag.addToSensors(sensor)

        def surgery = new Surgery(
            tag: tag, release: new AnimalRelease(), treatmentType: new SurgeryTreatmentType(), type: new SurgeryType()
        )
        surgery.save()

        surgery.metaClass.isInWindow = {
            true
        }

        def detection = new ValidDetection(transmitterId: transmitterId)

        assertEquals([surgery], detection.surgeries)
    }

    void testGetSensorIdsMultipleSurgeries() {

        def surgeries = [
            [tag: [sensors: [
                [transmitterId: '1111'],
                [transmitterId: '2222']
            ]]],
            [tag: [sensors: [
                [transmitterId: '3333']
            ]]]
        ]

        assertEquals "1111, 2222, 3333", ValidDetection.getSensorIds(surgeries)
    }

    void testGetSensorIdsOneSurgery() {

        def surgeries = [
            [tag: [sensors: [
                [transmitterId: '1111']
            ]]]
        ]

        assertEquals "1111", ValidDetection.getSensorIds(surgeries)
    }

    void testGetSensorIdsNoSurgeries() {

        def surgeries = []

        assertEquals "", ValidDetection.getSensorIds(surgeries)
    }

    void testSpeciesNamesMultipleSurgeries() {

        def surgeries = [
            [release: [animal: [
                [species: [name: 'AAAA']],
                [species: [name: 'BBBB']]
            ]]],
            [release: [animal: [
                [species: [name: 'CCCC']]
            ]]]
        ]

        assertEquals "AAAA, BBBB, CCCC", ValidDetection.getSpeciesNames(surgeries)
    }

    void testSpeciesNamesOneSurgery() {

        def surgeries = [
            [release: [animal: [
                [species: [name: 'AAAA']]
            ]]]
        ]

        assertEquals "AAAA", ValidDetection.getSpeciesNames(surgeries)
    }

    void testSpeciesNamesNoSurgeries() {

        def surgeries = []

        assertEquals "", ValidDetection.getSpeciesNames(surgeries)
    }
}
