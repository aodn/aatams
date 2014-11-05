package au.org.emii.aatams.data

import au.org.emii.aatams.*

import au.org.emii.aatams.detection.*
import au.org.emii.aatams.notification.*

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.WKTReader

import org.apache.shiro.crypto.hash.Sha256Hash
import org.joda.time.*

import shiro.*

class ProtectedSpeciesTestDataInitialiser extends AbstractDataInitialiser
{
    ProtectedSpeciesTestDataInitialiser(def service)
    {
        super(service)
    }

    void execute()
    {
        initData()
    }

    def initData()
    {
        TransmitterType pinger = new TransmitterType(transmitterTypeName:"PINGER").save(failOnError:true)
        assert(!pinger.hasErrors())

        Notification receiverRecoveryCreate =
                new Notification(key:"RECEIVER_RECOVERY_CREATE",
                        htmlFragment:"Click here to create a receiver recovery",
                        anchorSelector:"td.rowButton > [href*='/receiverRecovery/create']:first").save(failOnError:true)

        Notification register =
                new Notification(key:"REGISTER",
                        htmlFragment:"Click here to register to use AATAMS",
                        anchorSelector:"#userlogin > [href\$='/person/create']",
                        unauthenticated:true).save(failOnError:true)
        //
        // Addresses.
        //
        Address csiroStreetAddress =
                new Address(streetAddress:'12 Smith Street',
                        suburbTown:'Hobart',
                        state:'TAS',
                        country:'Australia',
                        postcode:'7000').save()

        Address csiroPostalAddress =
                new Address(streetAddress:'34 Queen Street',
                        suburbTown:'Melbourne',
                        state:'VIC',
                        country:'Australia',
                        postcode:'3000').save()

        //
        // Organisations.
        //
        Organisation csiroOrg =
                new Organisation(name:'CSIRO',
                        department:'CMAR',
                        phoneNumber:'1234',
                        faxNumber:'1234',
                        streetAddress:csiroStreetAddress,
                        postalAddress:csiroPostalAddress,
                        status:EntityStatus.ACTIVE).save(failOnError: true)

        Address imosStreetAddress =
                new Address(streetAddress:'12 Smith Street',
                        suburbTown:'Hobart',
                        state:'TAS',
                        country:'Australia',
                        postcode:'7000').save()

        Address imosPostalAddress =
                new Address(streetAddress:'34 Queen Street',
                        suburbTown:'Melbourne',
                        state:'VIC',
                        country:'Australia',
                        postcode:'3000').save()

        Organisation imosOrg =
                new Organisation(name:'IMOS',
                        department:'eMII',
                        phoneNumber:'5678',
                        faxNumber:'5678',
                        streetAddress:imosStreetAddress,
                        postalAddress:imosPostalAddress,
                        status:EntityStatus.PENDING).save(failOnError: true)

        Address imosStreetAddress2 =
                new Address(streetAddress:'12 Smith Street',
                        suburbTown:'Hobart',
                        state:'TAS',
                        country:'Australia',
                        postcode:'7000').save()

        Address imosPostalAddress2 =
                new Address(streetAddress:'34 Queen Street',
                        suburbTown:'Melbourne',
                        state:'VIC',
                        country:'Australia',
                        postcode:'3000').save()

        Organisation imosOrg2 =
                new Organisation(name:'IMOS 2',
                        department:'AATAMS',
                        phoneNumber:'5678',
                        faxNumber:'5678',
                        streetAddress:imosStreetAddress2,
                        postalAddress:imosPostalAddress2,
                        status:EntityStatus.PENDING).save(failOnError: true)


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

        /*OrganisationProject csiroSeals =
                new OrganisationProject(organisation:csiroOrg,
                        project:sealCountProject)*/

//        sealCountProject.addToOrganisationProjects(csiroSeals)
//        csiroOrg.addToOrganisationProjects(csiroSeals)
//        sealCountProject.save(failOnError:true)
//        csiroOrg.save(failOnError:true)

/*        OrganisationProject csiroTuna =
                new OrganisationProject(organisation:csiroOrg,
                        project:tunaProject)

        tunaProject.addToOrganisationProjects(csiroTuna)
        csiroOrg.addToOrganisationProjects(csiroTuna)
        tunaProject.save(failOnError:true)
        csiroOrg.save(failOnError:true)
*/
        //
        // Security/people.
        //
        SecRole sysAdmin = new SecRole(name:"SysAdmin")
        sysAdmin.addToPermissions("*:*")
        sysAdmin.save(failOnError: true)

        //
        // People.
        //

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

//        csiroOrg.addToPeople(joeBloggs)
//        csiroOrg.addToPeople(johnCitizen)
//        csiroOrg.addToPeople(mrPending)
//        csiroOrg.save(failOnError:true)

        //
        // Project Roles.
        //
        ProjectRoleType principalInvestigator = ProjectRoleType.findByDisplayName(ProjectRoleType.PRINCIPAL_INVESTIGATOR)
        if (!principalInvestigator)
        {
            principalInvestigator =
                    new ProjectRoleType(displayName:ProjectRoleType.PRINCIPAL_INVESTIGATOR).save(failOnError: true)
        }

        ProjectRoleType administrator = ProjectRoleType.findByDisplayName('Administrator')
        if (!administrator)
        {
            administrator =
                    new ProjectRoleType(displayName:'Administrator').save(failOnError: true)
        }

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

        //
        // Devices.
        //
        DeviceManufacturer vemco =
                new DeviceManufacturer(manufacturerName:'Vemco').save(failOnError: true)

        DeviceModel vemcoVR2 =
                new ReceiverDeviceModel(modelName:'VR2', manufacturer:vemco).save(failOnError: true)

        DeviceModel tagDeviceModel = new TagDeviceModel(modelName: 'Tag Device', manufacturer: vemco).save(failOnError: true)

        DeviceStatus newStatus = new DeviceStatus(status:'NEW').save(failOnError: true)
        DeviceStatus deployedStatus = new DeviceStatus(status:'DEPLOYED').save(failOnError: true)
        DeviceStatus recoveredStatus = new DeviceStatus(status:'RECOVERED').save(failOnError: true)

        Receiver rx1 =
                new Receiver(serialNumber:'101336',
                        status:deployedStatus,
                        model:vemcoVR2,
                        organisation:csiroOrg,
                        comment:'RX 1 belonging to CSIRO').save(failOnError: true)

        csiroOrg.addToReceivers(rx1)

        csiroOrg.save(failOnError:true)
        imosOrg.save(failOnError:true)

        // CodeMaps.
        createCodeMaps()

        def a69_1303 = CodeMap.findByCodeMap('A69-1303')

        //
        // Tags.
        //
        println "Creatign unembargoedTag"
        Tag unembargoedTag = createTag(
                [serialNumber:'1111',
                 codeMap:a69_1303,
                 pingCode:'1111',
                 model:tagDeviceModel,
                 project:unembargoedProject,
                 status:deployedStatus])
        unembargoedProject.addToTags(unembargoedTag).save(failOnError:true)

        println "Creatign embargoedTag"
        Tag embargoedTag = createTag(
                [serialNumber:'2222',
                 codeMap:a69_1303,
                 pingCode:'2222',
                 model:tagDeviceModel,
                 project:embargoedProject,
                 status:deployedStatus])
        embargoedProject.addToTags(embargoedTag).save(failOnError:true)

        println "Creatign protectedTag"
        Tag protectedTag = createTag(
                [serialNumber:'3333',
                 codeMap:a69_1303,
                 pingCode:'3333',
                 model:tagDeviceModel,
                 project:protectedProject,
                 status:newStatus])
        protectedProject.addToTags(protectedTag).save(failOnError:true)

        a69_1303.save(failOnError:true)

        //
        // Installation data.
        //
        InstallationConfiguration array =
                new InstallationConfiguration(type:'ARRAY').save(failOnError:true)

        Installation bondiLine =
                new Installation(name:'Bondi Line',
                        configuration:array,
                        project:unembargoedProject).save(failOnError:true)


        WKTReader reader = new WKTReader();

        Point location = (Point)reader.read("POINT(30.1234 30.1234)")
        location.setSRID(4326)

        InstallationStation bondiSW1 =
                new InstallationStation(installation:bondiLine,
                        name:'Bondi SW1',
                        curtainPosition:1,
                        location:location).save(failOnError:true)

        //
        //  Receiver Deployments.
        //
        MooringType concreteMooring = new MooringType(type:'CONCRETE BLOCK').save(failOnError:true)

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
        CaabSpecies whiteShark = new CaabSpecies(scientificName:"Carcharodon carcharias", commonName:"White Shark", spcode:"37010003").save(failOnError:true)

        Sex male = new Sex(sex:'MALE').save(failOnError:true)

        Animal whiteShark1 = new Animal(species:whiteShark,
                sex:male).save(failOnError:true)

        AnimalMeasurementType length = new AnimalMeasurementType(type:'LENGTH').save(failOnError:true)
        AnimalMeasurementType weight = new AnimalMeasurementType(type:'WEIGHT').save(failOnError:true)
        MeasurementUnit metres = new MeasurementUnit(unit:'m').save(failOnError:true)
        MeasurementUnit kg = new MeasurementUnit(unit:'kg').save(failOnError:true)

        SurgeryTreatmentType antibiotic = new SurgeryTreatmentType(type:'ANTIBIOTIC').save(failOnError:true)
        SurgeryTreatmentType anesthetic = new SurgeryTreatmentType(type:'ANESTHETIC').save(failOnError:true)
        SurgeryType internal = new SurgeryType(type:'INTERNAL').save(failOnError:true)
        SurgeryType external = new SurgeryType(type:'EXTERNAL').save(failOnError:true)

        CaptureMethod net = new CaptureMethod(name:'NET').save(failOnError:true)
        CaptureMethod line = new CaptureMethod(name:'LINE').save(failOnError:true)
        CaptureMethod longLine = new CaptureMethod(name:'LONG LINE').save(failOnError:true)

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

        // Receiver Recovery.
        Calendar cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 12)
        cal.set(Calendar.MINUTE, 34)
        cal.set(Calendar.SECOND, 56)
        cal.set(Calendar.MILLISECOND, 0)
        cal.add(Calendar.DATE, 2)

        DateTime recoveryDateTimeRx1 = new DateTime(cal.getTime().getTime())

        cal.set(Calendar.MINUTE, 54)
        DateTime recoveryDateTimeRx2 = new DateTime(cal.getTime().getTime())

        ReceiverRecovery recovery1 =
                new ReceiverRecovery(recoveryDateTime: new DateTime("2014-10-15T00:00:00Z"),
                        location:(Point)reader.read("POINT(10.1234 10.1234)"),
                        status:recoveredStatus,
                        recoverer:protectedRole,
                        deployment:rx1Bondi,
                        batteryLife:12.5f,
                        batteryVoltage:3.7f).save(failOnError:true)

/*
        createExportWithDetections("export1.csv", jonBurgess, rx1Bondi, rx1, unembargoedTag, unembargoedSurgery, 10)
        createExportWithDetections("export6.csv", jonBurgess, rx1Bondi, rx1, embargoedTag, surgery4, 3)
        createExportWithDetections("export3.csv", jonBurgess, rx2Bondi, rx2, embargoedTag, surgery4, 3)
        createExportWithDetections("export4.csv", jonBurgess, rx3Ningaloo, rx3, protectedTag, surgery4, 3)
        createExportWithDetections("export5.csv", jonBurgess, rx4Heron, rx4, tag5, null, 3)

        createExportWithDetections("nonEmbargoedWhale.csv", joeBloggs, whaleDeployment, rxWhale, nonEmbargoedTag, nonEmbargoedWhaleSurgery, 3)
        createExportWithDetections("embargoedWhale.csv", joeBloggs, whaleDeployment, rxWhale, embargoedTag, embargoedWhaleSurgery, 3)
        createExportWithDetections("unknownTagWhale.csv", joeBloggs, whaleDeployment, rxWhale, [pinger:[transmitterId:"A69-1303-8888"]], null, 3)
*/




        ReceiverDownloadFile export =
                new ReceiverDownloadFile(type:ReceiverDownloadFileType.DETECTIONS_CSV,
                        name:"asdfmate",
                        importDate:new DateTime("2013-05-17T12:54:56").toDate(),
                        status:FileProcessingStatus.PROCESSED,
                        errMsg:"",
                        requestingUser:projectUser).save(failOnError:true)

        export.save(failOnError:true)

        //        createDetections(rx1Bondi, rx1, null /* todo - Tag with no release*/, )
        createDetections(rx1Bondi, rx1, unembargoedTag, export, unembargoedSurgery)
        createDetections(rx1Bondi, rx1, embargoedTag, export, embargoedSurgery)
        createDetections(rx1Bondi, rx1, protectedTag, export, protectedSurgery)

        new Statistics(key: "numValidDetections", value: 3).save(failOnError: true)
    }

    private void createDetections(ReceiverDeployment rx1Bondi, Receiver rx1, tag, ReceiverDownloadFile export1, Surgery surgery1)
    {

        ValidDetection detection =
                new ValidDetection(receiverDeployment:rx1Bondi,
                        timestamp: new DateTime("2011-05-17T02:54:00+00:00").toDate(),
                        receiverName: rx1.name,
                        transmitterId: tag.pinger.transmitterId,
                        receiverDownload: export1,
                        provisional: false)

        if (surgery1)
        {
            DetectionSurgery detSurgery =
                    new DetectionSurgery(surgery:surgery1,
                            detection:detection,
                            sensor:tag.pinger)
            detection.addToDetectionSurgeries(detSurgery)
        }
        export1.addToDetections(detection)
    }

    private void createCodeMaps()
    {
        ["A69", "A180"].each
                {
                    freq ->

                        ["1303", "1601", "9001", "9003", "1206", "1105", "9002", "9004"].each
                                {
                                    codeSpace ->

                                        CodeMap codeMap = new CodeMap(codeMap:freq + "-" + codeSpace).save(failOnError:true, flush:true)
                                }
                }
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
