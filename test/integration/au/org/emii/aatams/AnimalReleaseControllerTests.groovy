package au.org.emii.aatams

import grails.test.*

import org.joda.time.*
import org.joda.time.format.DateTimeFormat

import java.text.DateFormat
import java.text.SimpleDateFormat

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.ParseException
import com.vividsolutions.jts.io.WKTReader

class AnimalReleaseControllerTests extends GroovyTestCase 
{
    def animalFactoryService
    def detectionFactoryService
    def tagFactoryService
    
    WKTReader reader = new WKTReader();

    def deployment
    def project
    def netCapture
    def deployedStatus
    def retiredStatus
    
    def model
    
    def controller
    
    protected void setUp() 
    {
        super.setUp()

        controller = new AnimalReleaseController()
        
        controller.animalFactoryService = animalFactoryService
        controller.detectionFactoryService = detectionFactoryService
        controller.tagFactoryService = tagFactoryService
        
        project = Project.build().save(failOnError:true)
        
        Person personOrg =
            new Person(username:'person',
                       passwordHash:"asdf",
                       name:'Person',
                       //organisation:imosOrg,
                       phoneNumber:'1234',
                       emailAddress:'person@utas.edu.au',
                       status:EntityStatus.ACTIVE,
                       defaultTimeZone:DateTimeZone.forID("Australia/Hobart"))

        Address csiroStreetAddress =
            new Address(streetAddress:'12 Smith Street',
                        suburbTown:'Hobart',
                        state:'TAS',
                        country:'Australia',
                        postcode:'7000').save()
        Organisation org = 
            new Organisation(name:'CSIRO', 
                             department:'CMAR',
                             phoneNumber:'1234',
                             faxNumber:'1234',
                             streetAddress:csiroStreetAddress,
                             postalAddress:csiroStreetAddress,
                             status:EntityStatus.ACTIVE,
                             requestingUser:personOrg).save(failOnError: true)

        personOrg.organisation = org
        personOrg.save()
        
        def pinger = TransmitterType.build(transmitterTypeName:'PINGER').save()
        deployedStatus = DeviceStatus.build(status:'DEPLOYED').save()
        retiredStatus = DeviceStatus.build(status:'RETIRED').save()
        netCapture = CaptureMethod.build(name:'NET').save()
        model = DeviceModel.build().save()
        
        def receiver =
            Receiver.build(codeName:"VR2W-1234",
                           organisation:org,
                           status:deployedStatus,
                           model:model)
        receiver.save(failOnError:true)
        
        def installation = 
            Installation.build(project:project).save()
            
        def station =
            InstallationStation.build(installation:installation,
                                      location:(Point)reader.read("POINT(30.1234 30.1234)"))
        station.save(failOnError:true)
        
        deployment = 
            ReceiverDeployment.build(deploymentDateTime:new DateTime("2010-02-15T12:34:56+10:00"),
                                     receiver:receiver,
                                     station:station,
                                     location:(Point)reader.read("POINT(30.1234 30.1234)"))
        deployment.save(failOnError:true)
        
        def person = Person.build(username:'jbloggs', organisation:org, defaultTimeZone:DateTimeZone.forID("Australia/Perth")).save(failOnError:true)
        
        def recoverer = ProjectRole.build(project:project, person:person)
        recoverer.save(failOnError:true)
        
        def recovery = 
            ReceiverRecovery.build(recoveryDateTime:new DateTime("2012-02-15T12:34:56+10:00"),
                                   deployment:deployment,
                                   location:(Point)reader.read("POINT(30.1234 30.1234)"),
                                   recoverer:recoverer)
                               
        recovery.save(failOnError:true)
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    /**
     * Tests the scenario outlined in bug #364, where a detection is uploaded
     * before a matching surgery.
     */
    void testExistingDetections()
    {
        def dateString = "2011-08-17 12:34:56" + " " + "UTC"
        def DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z")
        def detectionDate = dateFormat.parse(dateString)
        def transmitterId = "A69-1303-1234"
        
        // Create the detection.
        Detection detection = 
            Detection.build(timestamp: detectionDate,
                            transmitterId: transmitterId,
                            receiverDeployment: deployment)
        detection.save(failOnError:true)
                        
        
        // Assert that there a no associated surgeries.
        assertEquals(0, detection.detectionSurgeries.size())
        
        // Create the release/surgery etc...
        Animal animal = Animal.build().save()
        controller.params.animal = animal
        controller.params.species = animal.species

        controller.params.project = project

        controller.params.captureLocality = "Perth"
        controller.params.captureLocation = (Point)reader.read("POINT(30.1234 30.1234)")
        controller.params.captureMethod = netCapture
        
        controller.params.releaseLocality = "Perth"
        controller.params.releaseLocation = (Point)reader.read("POINT(30.1234 30.1234)")

        controller.params.captureDateTime = new DateTime("2011-06-01T12:34:45+10:00")
        controller.params.releaseDateTime = new DateTime("2011-06-01T12:34:45+10:00")
        
        Tag tag = Tag.build(codeName:transmitterId,
                            project:project,
                            status:deployedStatus,
                            model:model,
                            serialNumber:"12345").save()
        SurgeryType surgeryType = SurgeryType.build().save()
        SurgeryTreatmentType surgeryTreatmentType = SurgeryTreatmentType.build().save()
        
        // Surgery 0.
        def surgery0 = [
            timestamp_day: '1',
            timestamp_month: '6',
            timestamp_year: '2011',
            timestamp_hour: '12',
            timestamp_minute: '34',

            timestamp_zone: "Australia/Hobart",
            type: surgeryType,
            treatmentType : surgeryTreatmentType,
            comments: "",
            tag :[codeName: tag.codeName, serialNumber: "12345", modelId: model.id]]
        
        controller.params.surgery = ['0':surgery0]
        
        controller.save()
        
        // Assert that pre-existing detection is now matched with the 
        // surgery that's just been created.
        assertEquals(1, detection?.detectionSurgeries?.size())
        
        def firstDetSurg = detection?.detectionSurgeries?.iterator().next()
        assertEquals(tag.codeName, firstDetSurg?.tag?.codeName)
    }
}
