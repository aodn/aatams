package au.org.emii.aatams.data

import au.org.emii.aatams.*

import au.org.emii.aatams.detection.*

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.WKTReader

import org.apache.shiro.crypto.hash.Sha256Hash
import org.joda.time.*

import shiro.*

class ProtectedSpeciesTestDataInitialiser extends AbstractDataInitialiser
{
    ProtectedSpeciesTestDataInitialiser(permissionUtilsService)
    {
        super(permissionUtilsService)
    }

    void execute()
    {
        initData()
    }

    def initData()
    {
        //
        // Projects.
        //
        Project unembargoedProject =
                new Project(name:'unembargoed',
                        description:'',
                        status:EntityStatus.ACTIVE).save(failOnError: true)

        Project embargoedProject =
                new Project(name:'embargoed',
                        description:'',
                        status:EntityStatus.ACTIVE).save(failOnError: true)

        Project protectedProject =
                new Project(name:'protected',
                        description:'',
                        status:EntityStatus.ACTIVE,
                        isProtected: true).save(failOnError: true)

            def imosOrg = Organisation.findByName('IMOS')
            def csiroOrg = Organisation.findByName('CSIRO')

            def sysAdmin = SecRole.findByName('SysAdmin')
            ProjectRoleType administrator = ProjectRoleType.findByDisplayName('Administrator')
            def newStatus = DeviceStatus.findByStatus('NEW')
            def rx1 = Receiver.findBySerialNumber('101336')

        Person sysAdminUser = new Person(username:'admin',
                passwordHash:new Sha256Hash("password").toHex(),
                name:'Admin',
                organisation:imosOrg,
                phoneNumber:'1234',
                emailAddress:'jkburges@utas.edu.au',
                status:EntityStatus.ACTIVE,
                defaultTimeZone:DateTimeZone.forID("Australia/Hobart"))
        sysAdminUser.addToRoles(sysAdmin)
        sysAdminUser.save(failOnError: true)

        Person projectUser =
                new Person(username:'project',
                        passwordHash:new Sha256Hash("password").toHex(),
                        name:'Project User',
                        organisation:imosOrg,
                        phoneNumber:'1234',
                        emailAddress:'jkburges@utas.edu.au',
                        status:EntityStatus.ACTIVE,
                        defaultTimeZone:DateTimeZone.forID("Australia/Hobart"))
        projectUser.save(failOnError: true)

        Person nonProjectUser =
                new Person(username:'nonProject',
                        passwordHash:new Sha256Hash("password").toHex(),
                        name:'Joe "Non Project" Bloggs',
                        organisation:csiroOrg,
                        phoneNumber:'1234',
                        emailAddress:'jbloggs@blah.au',
                        status:EntityStatus.ACTIVE,
                        defaultTimeZone:DateTimeZone.forID("Australia/Perth"))
        nonProjectUser.save(failOnError: true);

        ProjectRole protectedRole =
                new ProjectRole(project:protectedProject,
                        person: projectUser,
                        roleType: administrator,
                        access:ProjectAccess.READ_WRITE)
        protectedProject.addToProjectRoles(protectedRole).save(failOnError:true)
        projectUser.addToProjectRoles(protectedRole).save(failOnError:true, flush:true)   // flush required to keep compass happy
        permissionUtilsService.setPermissions(protectedRole)

        ProjectRole embargoedRole =
                new ProjectRole(project:embargoedProject,
                        person: projectUser,
                        roleType: administrator,
                        access:ProjectAccess.READ_WRITE)
        embargoedProject.addToProjectRoles(embargoedRole).save(failOnError:true)
        projectUser.addToProjectRoles(embargoedRole).save(failOnError:true, flush:true)   // flush required to keep compass happy
        permissionUtilsService.setPermissions(embargoedRole)

        def a69_1303 = CodeMap.findByCodeMap('A69-1303')

        def tagDeviceModel = TagDeviceModel.findByModelName('V8')

        def deployedStatus = DeviceStatus.findByStatus('DEPLOYED')

        //
        // Tags.
        //
        println "Creating unembargoedTag"
        Tag unembargoedTag = createTag(
                [serialNumber:'21111',
                 codeMap:a69_1303,
                 pingCode:'21111',
                 model:tagDeviceModel,
                 project:unembargoedProject,
                 status:deployedStatus])
        unembargoedProject.addToTags(unembargoedTag).save(failOnError:true)

        println "Creating embargoedTag"
        Tag embargoedTag = createTag(
                [serialNumber:'22222',
                 codeMap:a69_1303,
                 pingCode:'22222',
                 model:tagDeviceModel,
                 project:embargoedProject,
                 status:deployedStatus])
        embargoedProject.addToTags(embargoedTag).save(failOnError:true)

        println "Creating protectedTag"
        Tag protectedTag = createTag(
                [serialNumber:'23333',
                 codeMap:a69_1303,
                 pingCode:'23333',
                 model:tagDeviceModel,
                 project:protectedProject,
                 status:newStatus])
        protectedProject.addToTags(protectedTag).save(failOnError:true)

        a69_1303.save(failOnError:true)

        WKTReader reader = new WKTReader()
        def bondiSW1 = InstallationStation.findByName('Bondi SW1')

        //
        //  Receiver Deployments.
        //
        MooringType concreteMooring = MooringType.findByType('CONCRETE BLOCK')

        ReceiverDeployment rx1Bondi =
                new ReceiverDeployment(station:bondiSW1,
                        receiver:rx1,
                        deploymentNumber:1,
                        initialisationDateTime:new DateTime("2010-02-15T00:34:56+10:00"),
                        deploymentDateTime:new DateTime("2010-02-15T12:34:56+10:00"),
                        acousticReleaseID:"asdf",
                        mooringType:concreteMooring,
                        bottomDepthM:12f,
                        depthBelowSurfaceM:5f,
                        receiverOrientation:ReceiverOrientation.UP,
                        batteryLifeDays:90,
                        location:(Point)reader.read("POINT(10.1234 10.1234)")).save(failOnError:true)
        rx1.addToDeployments(rx1Bondi)
        rx1.save()

        //
        // Animals and Animal Releases etc.
        //
        CaabSpecies whiteShark = CaabSpecies.findBySpcode("37010003")
        Sex male = Sex.findBySex('MALE')

        Animal whiteShark1 = new Animal(species:whiteShark,
                sex:male).save(failOnError:true)

        SurgeryTreatmentType antibiotic = SurgeryTreatmentType.findByType('ANTIBIOTIC')
        SurgeryType external = SurgeryType.findByType('EXTERNAL')
        CaptureMethod net = CaptureMethod.findByName('NET')

        AnimalRelease unembargoedRelease =
                new AnimalRelease(project:unembargoedProject,
                        surgeries:[],
                        measurements:[],
                        animal:whiteShark1,
                        captureLocality:'Neptune Islands',
                        captureLocation:(Point)reader.read("POINT(10.1234 20.1234)"),
                        captureDateTime:new DateTime("2010-02-16T14:10:00"),
                        captureMethod:net,
                        releaseLocality:'Neptune Islands',
                        releaseLocation:(Point)reader.read("POINT(30.1234 40.1234)"),
                        releaseDateTime:new DateTime("2010-02-16T14:15:00")).save(failOnError:true)

        AnimalRelease embargoedRelease =
                new AnimalRelease(project:embargoedProject,
                        surgeries:[],
                        measurements:[],
                        animal:whiteShark1,
                        captureLocality:'Neptune Islands',
                        captureLocation:(Point)reader.read("POINT(10.1234 20.1234)"),
                        captureDateTime:new DateTime("2010-02-16T14:10:00"),
                        captureMethod:net,
                        releaseLocality:'Neptune Islands',
                        releaseLocation:(Point)reader.read("POINT(30.1234 40.1234)"),
                        releaseDateTime:new DateTime("2010-02-16T14:15:00"),
                        embargoDate:Date.parse("yyyy-MM-dd hh:mm:ss", "2020-05-15 12:34:56")).save(failOnError:true)

        AnimalRelease protectedRelease =
                new AnimalRelease(project:protectedProject,
                        surgeries:[],
                        measurements:[],
                        animal:whiteShark1,
                        captureLocality:'Neptune Islands',
                        captureLocation:(Point)reader.read("POINT(10.1234 20.1234)"),
                        captureDateTime:new DateTime("2010-02-16T14:10:00"),
                        captureMethod:net,
                        releaseLocality:'Neptune Islands',
                        releaseLocation:(Point)reader.read("POINT(30.1234 40.1234)"),
                        releaseDateTime:new DateTime("2010-02-16T14:15:00"),
                        embargoDate:Date.parse("yyyy-MM-dd hh:mm:ss", "2020-05-15 12:34:56")).save(failOnError:true)

        Surgery unembargoedSurgery =
                new Surgery(release:unembargoedRelease,
                        tag:unembargoedTag,
                        timestamp:new DateTime("2011-05-15T14:12:00"),
                        type:external,
                        treatmentType:antibiotic)
        unembargoedTag.addToSurgeries(unembargoedSurgery).save(failOnError:true)
        unembargoedRelease.addToSurgeries(unembargoedSurgery).save(failOnError:true)

        Surgery embargoedSurgery =
                new Surgery(release:embargoedRelease,
                        tag:embargoedTag,
                        timestamp:new DateTime("2011-05-15T14:13:00"),
                        type:external,
                        treatmentType:antibiotic)
        embargoedTag.addToSurgeries(embargoedSurgery).save(failOnError:true)
        embargoedRelease.addToSurgeries(embargoedSurgery).save(failOnError:true)

        Surgery protectedSurgery =
                new Surgery(release:protectedRelease,
                        tag:protectedTag,   // Can't really have a tag on two different animals.
                        timestamp:new DateTime("2011-05-15T14:12:00"),
                        type:external,
                        treatmentType:antibiotic)
        protectedTag.addToSurgeries(protectedSurgery).save(failOnError:true)
        protectedRelease.addToSurgeries(protectedSurgery).save(failOnError:true)

        ReceiverDownloadFile export =
                new ReceiverDownloadFile(type:ReceiverDownloadFileType.DETECTIONS_CSV,
                        name:"asdfmate",
                        importDate:new DateTime("2013-05-17T12:54:56").toDate(),
                        status:FileProcessingStatus.PROCESSED,
                        errMsg:"",
                        requestingUser:projectUser).save(failOnError:true)

        export.save(failOnError:true)

        createDetections(rx1Bondi, rx1, unembargoedTag, export)
        createDetections(rx1Bondi, rx1, embargoedTag, export)
        createDetections(rx1Bondi, rx1, protectedTag, export)
    }

    private void createDetections(ReceiverDeployment rx1Bondi, Receiver rx1, tag, ReceiverDownloadFile export1)
    {

        ValidDetection detection =
                new ValidDetection(receiverDeployment:rx1Bondi,
                        timestamp: new DateTime("2011-05-17T02:54:00+00:00").toDate(),
                        receiverName: rx1.name,
                        transmitterId: tag.pinger.transmitterId,
                        receiverDownload: export1,
                        provisional: false)

        export1.addToDetections(detection)
    }

    private Tag createTag(params)
    {
        TransmitterType pinger =
                TransmitterType.findByTransmitterTypeName("PINGER")
        assert(pinger)

        Tag tag =
                new Tag(serialNumber:params.serialNumber,
                        codeMap:params.codeMap,
                        model:params.model,
                        project:params.project,
                        status:params.status,
                        expectedLifeTimeDays:params.expectedLifeTimeDays)

        Sensor sensor = new Sensor(tag:tag, pingCode: params.pingCode, transmitterType: pinger)
        tag.addToSensors(sensor)

        tag.save(failOnError:true, flush:true)
        params.codeMap.addToTags(tag)

        return tag
    }
}
