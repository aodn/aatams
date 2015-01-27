package au.org.emii.aatams.data

import au.org.emii.aatams.*

import au.org.emii.aatams.detection.*
import au.org.emii.aatams.notification.*

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.WKTReader

import org.apache.shiro.crypto.hash.Sha256Hash
import org.joda.time.*

import shiro.*

/**
 * Set up data used in development.
 *
 * @author jburgess
 */
class DevelopmentDataInitialiser extends AbstractDataInitialiser
{
    DevelopmentDataInitialiser(permissionUtilsService)
    {
        super(permissionUtilsService)
    }

    void execute()
    {
        initData()
        initProtectedSpeciesData()
    }

    def initData()
    {
        TransmitterType pinger =
            new TransmitterType(transmitterTypeName:"PINGER").save(failOnError:true)
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
        Project sealCountProject =
            new Project(name:'Seal Count',
                        description:'Counting seals',
                        status:EntityStatus.ACTIVE).save(failOnError: true)

        Project tunaProject =
            new Project(name:'Tuna',
                        description:'Counting tuna',
                        status:EntityStatus.ACTIVE).save(failOnError: true)

        Project whaleProject =
            new Project(name:'Whale',
                        description:'Whale counting',
                        status:EntityStatus.ACTIVE).save(failOnError: true)

        OrganisationProject csiroSeals =
            new OrganisationProject(organisation:csiroOrg,
                                    project:sealCountProject)

        sealCountProject.addToOrganisationProjects(csiroSeals)
        csiroOrg.addToOrganisationProjects(csiroSeals)
        sealCountProject.save(failOnError:true)
        csiroOrg.save(failOnError:true)

        OrganisationProject csiroTuna =
            new OrganisationProject(organisation:csiroOrg,
                                    project:tunaProject)

        tunaProject.addToOrganisationProjects(csiroTuna)
        csiroOrg.addToOrganisationProjects(csiroTuna)
        tunaProject.save(failOnError:true)
        csiroOrg.save(failOnError:true)

        //
        // Security/people.
        //
        SecRole sysAdmin = new SecRole(name:"SysAdmin")
        sysAdmin.addToPermissions("*:*")
        sysAdmin.save(failOnError: true)

        //
        // People.
        //
        Person jonBurgess =
            new Person(username:'jkburges',
                       passwordHash:new Sha256Hash("password").toHex(),
                       name:'Jon Burgess',
                       organisation:imosOrg,
                       phoneNumber:'1234',
                       emailAddress:'jkburges@utas.edu.au',
                       status:EntityStatus.ACTIVE,
                       defaultTimeZone:DateTimeZone.forID("Australia/Hobart"))
        jonBurgess.addToRoles(sysAdmin)
        jonBurgess.save(failOnError: true)

        Person joeBloggs =
            new Person(username:'jbloggs',
                       passwordHash:new Sha256Hash("password").toHex(),
                       name:'Joe Bloggs',
                       organisation:csiroOrg,
                       phoneNumber:'1234',
                       emailAddress:'jbloggs@blah.au',
                       status:EntityStatus.ACTIVE,
                       defaultTimeZone:DateTimeZone.forID("Australia/Perth"))

        Person johnCitizen =
            new Person(username:'jcitizen',
                       passwordHash:new Sha256Hash("password").toHex(),
                       name:'John Citizen',
                       organisation:csiroOrg,
                       phoneNumber:'5678',
                       emailAddress:'jcitizen@blah.au',
                       status:EntityStatus.ACTIVE,
                       defaultTimeZone:DateTimeZone.forID("Australia/Adelaide"))

        Person mrPending =
            new Person(username:'pending',
                       passwordHash:new Sha256Hash("pending").toHex(),
                       name:'Pending Pending',
                       organisation:csiroOrg,
                       phoneNumber:'5678',
                       emailAddress:'pending@blah.au',
                       status:EntityStatus.PENDING,
                       defaultTimeZone:DateTimeZone.forID("Australia/Hobart"))

        csiroOrg.addToPeople(joeBloggs)
        csiroOrg.addToPeople(johnCitizen)
        csiroOrg.addToPeople(mrPending)
        csiroOrg.save(failOnError:true)

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

        ProjectRole tunaAdmin =
            new ProjectRole(project:tunaProject,
                            person: joeBloggs,
                            roleType: administrator,
                            access:ProjectAccess.READ_WRITE)
        tunaProject.addToProjectRoles(tunaAdmin).save(failOnError:true)
        joeBloggs.addToProjectRoles(tunaAdmin).save(failOnError:true, flush:true)   // flush required to keep compass happy
        permissionUtilsService.setPermissions(tunaAdmin)

        ProjectRole sealProjectInvestigator =
            new ProjectRole(project:sealCountProject,
                            person: joeBloggs,
                            roleType: principalInvestigator,
                            access:ProjectAccess.READ_WRITE)

        sealCountProject.addToProjectRoles(sealProjectInvestigator).save(failOnError:true, flush:true)
        joeBloggs.addToProjectRoles(sealProjectInvestigator).save(failOnError:true, flush:true)
        permissionUtilsService.setPermissions(sealProjectInvestigator)

        ProjectRole sealAdmin =
            new ProjectRole(project:sealCountProject,
                            person: johnCitizen,
                            roleType: administrator,
                            access:ProjectAccess.READ_ONLY).save(failOnError: true, flush:true)
        permissionUtilsService.setPermissions(sealAdmin)

        ProjectRole tunaWrite =
            new ProjectRole(project:tunaProject,
                            person: johnCitizen,
                            roleType: administrator,
                            access:ProjectAccess.READ_WRITE).save(failOnError: true, flush:true)
        permissionUtilsService.setPermissions(tunaWrite)


        //
        // Devices.
        //
        DeviceManufacturer vemco =
            new DeviceManufacturer(manufacturerName:'Vemco').save(failOnError: true)

        DeviceModel vemcoVR2 =
            new ReceiverDeviceModel(modelName:'VR2', manufacturer:vemco).save(failOnError: true)
        assert(!vemcoVR2.hasErrors())
        DeviceModel vemcoVR2W =
            new ReceiverDeviceModel(modelName:'VR2W', manufacturer:vemco).save(failOnError: true)

        DeviceModel vemcoV8 =
            new TagDeviceModel(modelName:'V8', manufacturer:vemco).save(failOnError: true)

        DeviceStatus newStatus = new DeviceStatus(status:'NEW').save(failOnError: true)
        DeviceStatus deployedStatus = new DeviceStatus(status:'DEPLOYED').save(failOnError: true)
        DeviceStatus recoveredStatus = new DeviceStatus(status:'RECOVERED').save(failOnError: true)

        Receiver rx1 =
            new Receiver(serialNumber:'101336',
                         status:deployedStatus,
                         model:vemcoVR2W,
                         organisation:csiroOrg,
                         comment:'RX 1 belonging to CSIRO').save(failOnError: true)

        Receiver rx2 =
            new Receiver(serialNumber:'101337',
                         status:deployedStatus,
                         model:vemcoVR2,
                         organisation:csiroOrg).save(failOnError: true)
        csiroOrg.addToReceivers(rx1)
        csiroOrg.addToReceivers(rx2)

        Receiver rx3 =
            new Receiver(serialNumber:'101338',
                         status:newStatus,
                         model:vemcoVR2W,
                         organisation:imosOrg).save(failOnError: true)

        Receiver rx4 =
            new Receiver(serialNumber:'101344',
                         status:newStatus,
                         model:vemcoVR2,
                         organisation:imosOrg).save(failOnError: true)

        Receiver rx5 =
            new Receiver(serialNumber:'101355',
                         status:newStatus,
                         model:vemcoVR2,
                         organisation:imosOrg).save(failOnError: true)

        Receiver rx6 =
            new Receiver(serialNumber:'103366',
                         status:newStatus,
                         model:vemcoVR2W,
                         organisation:imosOrg).save(failOnError: true)

        Receiver rxWhale =
            new Receiver(serialNumber:'103377',
                         status:deployedStatus,
                         model:vemcoVR2W,
                         organisation:imosOrg).save(failOnError: true)
        imosOrg.addToReceivers(rx3)
        imosOrg.addToReceivers(rx4)
        imosOrg.addToReceivers(rx5)
        imosOrg.addToReceivers(rx6)
        imosOrg.addToReceivers(rxWhale)

        csiroOrg.save(failOnError:true)
        imosOrg.save(failOnError:true)

        // CodeMaps.
        createCodeMaps()

        def a69_111053 = CodeMap.findByCodeMap('A69-1105')
        def a69_1303 = CodeMap.findByCodeMap('A69-1303')

        //
        // Tags.
        //
        Tag tag1 = createTag(
            [serialNumber:'62339',
             codeMap:a69_1303,
             pingCode:'62339',
             model:vemcoV8,
             project:sealCountProject,
             status:deployedStatus])

        Tag tag2 = createTag(
            [serialNumber:'46601',
             codeMap:a69_1303,
             pingCode:'46601',
             model:vemcoV8,
             project:sealCountProject,
             status:deployedStatus])

        Tag tag3 = createTag(
            [serialNumber:'1111',
             codeMap:a69_1303,
             pingCode:'11111',
             model:vemcoV8,
             project:sealCountProject,
             status:newStatus])

        // Bug #352 - this tag won't be selectable if animal release project
        // set to "tuna".
        Tag tag5 = createTag(
            [serialNumber:'3333',
             codeMap:a69_1303,
             pingCode:'3333',
             model:vemcoV8,
             project:tunaProject,
             status:newStatus,
             expectedLifeTimeDays:100])

        Tag tag6 = createTag(
            [serialNumber:'4444',
             codeMap:a69_1303,
             pingCode:'4444',
             model:vemcoV8,
             project:tunaProject,
             status:newStatus])

        Tag orphanTag = createTag(
            [serialNumber:'5555',
             codeMap:a69_1303,
             pingCode:'5555',
             model:vemcoV8,
             status:newStatus])

        Tag nonEmbargoedTag = createTag(
            [serialNumber:'6666',
                codeMap:a69_1303,
                pingCode:'6666',
                model:vemcoV8,
                status:deployedStatus])

        Tag embargoedTag = createTag(
            [serialNumber:'7777',
                codeMap:a69_1303,
                pingCode:'7777',
                model:vemcoV8,
                status:deployedStatus])

        TransmitterType depth =
            new TransmitterType(transmitterTypeName:"DEPTH").save(failOnError:true)
        TransmitterType temp =
            new TransmitterType(transmitterTypeName:"TEMP").save(failOnError:true)

        Sensor sensor1 =
            new Sensor(pingCode:'64000',
                    tag:tag1,
                    transmitterType:depth,
                    unit:'m',
                    slope:1,
                    intercept:0)


        Sensor sensor2 =
            new Sensor(pingCode:'65000',
                    tag:tag1,
                    transmitterType:temp,
                    unit:'k',
                    slope:1,
                    intercept:0)
        tag1.addToSensors(sensor1)
        tag1.addToSensors(sensor2)
        tag1.save(failOnError:true)

        a69_1303.save(failOnError:true)

        //
        // Installation data.
        //
        InstallationConfiguration array =
            new InstallationConfiguration(type:'ARRAY').save(failOnError:true)
        InstallationConfiguration curtain =
            new InstallationConfiguration(type:'CURTAIN').save(failOnError:true)

        Installation bondiLine =
            new Installation(name:'Bondi Line',
                             configuration:curtain,
                             project:sealCountProject).save(failOnError:true)

        Installation ningalooArray =
            new Installation(name:'Ningaloo Array',
                             configuration:array,
                             project:tunaProject).save(failOnError:true)

        Installation heronCurtain =
            new Installation(name:'Heron Island Curtain',
                             configuration:curtain,
                             project:sealCountProject).save(failOnError:true)

        Installation whaleInstallation =
            new Installation(name:'Whale Curtain',
                             configuration:curtain,
                             project:whaleProject).save(failOnError:true)

        WKTReader reader = new WKTReader();

        Point location = (Point)reader.read("POINT(30.1234 30.1234)")
        location.setSRID(4326)

        InstallationStation bondiSW1 =
            new InstallationStation(installation:bondiLine,
                                    name:'Bondi SW1',
                                    curtainPosition:1,
                                    location:location).save(failOnError:true)

        InstallationStation bondiSW2 =
            new InstallationStation(installation:bondiLine,
                                    name:'Bondi SW2',
                                    curtainPosition:2,
                                    location:(Point)reader.read("POINT(-10.1234 -10.1234)")).save(failOnError:true)

        InstallationStation bondiSW3 =
            new InstallationStation(installation:bondiLine,
                                    name:'Bondi SW3',
                                    curtainPosition:3,
                                    location:(Point)reader.read("POINT(10.1234 10.1234)")).save(failOnError:true)

        InstallationStation ningalooS1 =
            new InstallationStation(installation:ningalooArray,
                                    name:'Ningaloo S1',
                                    curtainPosition:1,
                                    location:(Point)reader.read("POINT(10.1234 10.1234)")).save(failOnError:true)

        InstallationStation ningalooS2 =
            new InstallationStation(installation:ningalooArray,
                                    name:'Ningaloo S2',
                                    curtainPosition:2,
                                    location:(Point)reader.read("POINT(20.1234 20.1234)")).save(failOnError:true)

        InstallationStation heronS1 =
            new InstallationStation(installation:heronCurtain,
                                    name:'Heron S1',
                                    curtainPosition:1,
                                    location:(Point)reader.read("POINT(12.34 -42.30)")).save(failOnError:true)

        InstallationStation heronS2 =
            new InstallationStation(installation:heronCurtain,
                                    name:'Heron S2',
                                    curtainPosition:2,
                                    location:(Point)reader.read("POINT(76.02 -20.1234)")).save(failOnError:true)

        InstallationStation whaleStation =
            new InstallationStation(installation:whaleInstallation,
                                    name:'Whale Station',
                                    curtainPosition:1,
                                    location:(Point)reader.read("POINT(76.02 -20.1234)")).save(failOnError:true)

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

        ReceiverDeployment rx2Bondi =
            new ReceiverDeployment(station:bondiSW2,
                                   receiver:rx2,
                                   deploymentNumber:1,
                                   initialisationDateTime:new DateTime("2010-02-15T00:34:56+10:00"),
                                   deploymentDateTime:new DateTime("2011-05-15T14:12:00+10:00"),
                                   acousticReleaseID:"asdf",
                                   mooringType:concreteMooring,
                                   bottomDepthM:16f,
                                   depthBelowSurfaceM:7.4f,
                                   receiverOrientation:ReceiverOrientation.DOWN,
                                   batteryLifeDays:90,
                                   location:(Point)reader.read("POINT(20.1234 20.1234)")).save(failOnError:true)

        ReceiverDeployment rx3Ningaloo =
            new ReceiverDeployment(station:ningalooS1,
                                   receiver:rx3,
                                   deploymentNumber:1,
                                   initialisationDateTime:new DateTime("2010-02-15T00:34:56+10:00"),
                                   deploymentDateTime:new DateTime("2011-05-15T12:34:56+10:00"),
                                   acousticReleaseID:"asdf",
                                   mooringType:concreteMooring,
                                   bottomDepthM:12f,
                                   depthBelowSurfaceM:5f,
                                   receiverOrientation:ReceiverOrientation.UP,
                                   batteryLifeDays:90,
                                   location:(Point)reader.read("POINT(10.1234 10.1234)")).save(failOnError:true)

        ReceiverDeployment rx4Heron =
            new ReceiverDeployment(station:heronS1,
                                   receiver:rx4,
                                   deploymentNumber:1,
                                   initialisationDateTime:new DateTime("2010-02-15T00:34:56+10:00"),
                                   deploymentDateTime:new DateTime("2011-05-15T12:34:56+10:00"),
                                   acousticReleaseID:"asdf",
                                   mooringType:concreteMooring,
                                   bottomDepthM:12f,
                                   depthBelowSurfaceM:5f,
                                   receiverOrientation:ReceiverOrientation.UP,
                                   batteryLifeDays:90,
                                   location:(Point)reader.read("POINT(10.1234 10.1234)"),
                                   comments:"This was fun to deploy").save(failOnError:true)

        ReceiverDeployment rx5Heron =
            new ReceiverDeployment(station:heronS2,
                                   receiver:rx5,
                                   deploymentNumber:1,
                                   initialisationDateTime:new DateTime("2010-02-15T00:34:56+10:00"),
                                   deploymentDateTime:new DateTime("2011-05-15T12:34:56+10:00"),
                                   acousticReleaseID:"asdf",
                                   mooringType:concreteMooring,
                                   bottomDepthM:12f,
                                   depthBelowSurfaceM:5f,
                                   receiverOrientation:ReceiverOrientation.UP,
                                   batteryLifeDays:90,
                                   location:(Point)reader.read("POINT(10.1234 10.1234)")).save(failOnError:true)

        ReceiverDeployment rx6Heron =
            new ReceiverDeployment(station:heronS2,
                                   receiver:rx6,
                                   deploymentNumber:1,
                                   initialisationDateTime:new DateTime("2010-02-15T00:34:56+10:00"),
                                   deploymentDateTime:new DateTime("2000-05-15T12:34:56+10:00"),
                                   acousticReleaseID:"asdf",
                                   mooringType:concreteMooring,
                                   bottomDepthM:12f,
                                   depthBelowSurfaceM:5f,
                                   receiverOrientation:ReceiverOrientation.UP,
                                   batteryLifeDays:90,
                                   location:(Point)reader.read("POINT(10.1234 10.1234)")).save(failOnError:true)

        ReceiverDeployment whaleDeployment =
            new ReceiverDeployment(station:whaleStation,
                                   receiver:rxWhale,
                                   deploymentNumber:1,
                                   initialisationDateTime:new DateTime("2010-02-15T00:34:56+10:00"),
                                   deploymentDateTime:new DateTime("2000-05-15T12:34:56+10:00"),
                                   acousticReleaseID:"asdf",
                                   mooringType:concreteMooring,
                                   bottomDepthM:12f,
                                   depthBelowSurfaceM:5f,
                                   receiverOrientation:ReceiverOrientation.UP,
                                   batteryLifeDays:90,
                                   location:(Point)reader.read("POINT(10.1234 10.1234)")).save(failOnError:true)

        //
        // Animals and Animal Releases etc.
        //
        CaabSpecies whiteShark = new CaabSpecies(scientificName:"Carcharodon carcharias", commonName:"White Shark", spcode:"37010003").save(failOnError:true)
        CaabSpecies blueFinTuna = new CaabSpecies(scientificName:"Thunnus maccoyii", commonName:"Southern Bluefin Tuna", spcode:"37441004").save(failOnError:true)
        CaabSpecies blueEyeTrevalla = new CaabSpecies(scientificName:"Hyperoglyphe antarctica", commonName:"Blue-eye Trevalla", spcode:"37445001").save(failOnError:true)
        CaabSpecies southernRightWhale = new CaabSpecies(scientificName:"Eubalaena australis", commonName:"southern right whale", spcode:"41110001").save(failOnError:true)


        Sex male = new Sex(sex:'MALE').save(failOnError:true)
        Sex female = new Sex(sex:'FEMALE').save(failOnError:true)

        Animal whiteShark1 = new Animal(species:whiteShark,
                                        sex:male).save(failOnError:true)
        Animal whiteShark2 = new Animal(species:whiteShark,
                                        sex:male).save(failOnError:true)
        Animal blueFinTuna1 = new Animal(species:blueFinTuna,
                                         sex:female).save(failOnError:true)

        Animal nonEmbargoedWhale = new Animal(species:southernRightWhale,
                                               sex:female).save(failOnError:true)
        Animal embargoedWhale = new Animal(species:southernRightWhale,
                                           sex:female).save(failOnError:true)


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

        AnimalRelease whiteShark1Release =
            new AnimalRelease(project:tunaProject,
                              surgeries:[],
                              measurements:[],
                              animal:whiteShark1,
                              captureLocality:'Neptune Islands',
                              captureLocation:(Point)reader.read("POINT(10.1234 20.1234)"),
                              captureDateTime:new DateTime("2011-05-15T14:10:00"),
                              captureMethod:net,
                              releaseLocality:'Neptune Islands',
                              releaseLocation:(Point)reader.read("POINT(30.1234 40.1234)"),
                              releaseDateTime:new DateTime("2011-05-15T14:15:00"),
                              embargoDate:Date.parse("yyyy-MM-dd hh:mm:ss", "2015-05-15 12:34:56")).save(failOnError:true)

        AnimalRelease whiteShark2Release =
            new AnimalRelease(project:tunaProject,
                              surgeries:[],
                              measurements:[],
                              animal:whiteShark2,
                              captureLocality:'Neptune Islands',
                              captureLocation:(Point)reader.read("POINT(10.1234 20.1234)"),
                              captureDateTime:new DateTime("2011-05-15T14:10:00"),
                              captureMethod:net,
                              releaseLocality:'Neptune Islands',
                              releaseLocation:(Point)reader.read("POINT(30.1234 40.1234)"),
                              releaseDateTime:new DateTime("2011-05-15T14:15:00"),
                              embargoDate:Date.parse("yyyy-MM-dd hh:mm:ss", "2015-05-15 12:34:56")).save(failOnError:true)

        AnimalRelease blueFinTuna1Release =
            new AnimalRelease(project:tunaProject,
                              surgeries:[],
                              measurements:[],
                              animal:blueFinTuna1,
                              captureLocality:'Neptune Islands',
                              captureLocation:(Point)reader.read("POINT(10.1234 20.1234)"),
                              captureDateTime:new DateTime("2011-05-15T14:10:00"),
                              captureMethod:net,
                              releaseLocality:'Neptune Islands',
                              releaseLocation:(Point)reader.read("POINT(30.1234 40.1234)"),
                              releaseDateTime:new DateTime("2011-05-15T14:15:00")).save(failOnError:true)

        AnimalRelease nonEmbargoedWhaleRelease =
            new AnimalRelease(project:whaleProject,
                        surgeries:[],
                        measurements:[],
                        animal:nonEmbargoedWhale,
                        captureLocality:'Neptune Islands',
                        captureLocation:(Point)reader.read("POINT(10.1234 20.1234)"),
                        captureDateTime:new DateTime("2011-05-15T14:10:00"),
                        captureMethod:net,
                        releaseLocality:'Neptune Islands',
                        releaseLocation:(Point)reader.read("POINT(30.1234 40.1234)"),
                        releaseDateTime:new DateTime("2011-05-15T14:15:00")).save(failOnError:true)

        AnimalRelease embargoedWhaleRelease =
            new AnimalRelease(project:whaleProject,
                        surgeries:[],
                        measurements:[],
                        animal:embargoedWhale,
                        captureLocality:'Neptune Islands',
                        captureLocation:(Point)reader.read("POINT(10.1234 20.1234)"),
                        captureDateTime:new DateTime("2011-05-15T14:10:00"),
                        captureMethod:net,
                        releaseLocality:'Neptune Islands',
                        releaseLocation:(Point)reader.read("POINT(30.1234 40.1234)"),
                        releaseDateTime:new DateTime("2011-05-15T14:15:00"),
                        embargoDate:Date.parse("yyyy-MM-dd hh:mm:ss", "2020-05-15 12:34:56")).save(failOnError:true)

        AnimalMeasurement whiteShark1Length =
            new AnimalMeasurement(release:whiteShark1Release,
                                  type:length,
                                  value:2.5f,
                                  unit:metres,
                                  estimate:false).save(failOnError:true)

        AnimalMeasurement whiteShark1Weight =
            new AnimalMeasurement(release:whiteShark1Release,
                                  type:weight,
                                  value:200f,
                                  unit:kg,
                                  estimate:true).save(failOnError:true)


        Surgery surgery1 =
            new Surgery(release:whiteShark1Release,
                        tag:tag1,
                        timestamp:new DateTime("2011-05-15T14:12:00"),
                        type:external,
                        treatmentType:antibiotic)
        tag1.addToSurgeries(surgery1).save(failOnError:true)
        whiteShark1Release.addToSurgeries(surgery1).save(failOnError:true)

        Surgery surgery2 =
            new Surgery(release:whiteShark1Release,
                        tag:tag2,
                        timestamp:new DateTime("2011-05-15T14:13:00"),
                        type:external,
                        treatmentType:antibiotic)
        tag2.addToSurgeries(surgery2).save(failOnError:true)
        whiteShark1Release.addToSurgeries(surgery2).save(failOnError:true)

        Surgery nonEmbargoedWhaleSurgery =
            new Surgery(release:nonEmbargoedWhaleRelease,
                        tag:nonEmbargoedTag,
                        timestamp:new DateTime("2011-05-15T14:15:00"),
                        type:external,
                        treatmentType:antibiotic)
        nonEmbargoedTag.addToSurgeries(nonEmbargoedWhaleSurgery).save(failOnError:true)
        nonEmbargoedWhaleRelease.addToSurgeries(nonEmbargoedWhaleSurgery).save(failOnError:true)

        Surgery embargoedWhaleSurgery =
            new Surgery(release:embargoedWhaleRelease,
                        tag:embargoedTag,
                        timestamp:new DateTime("2011-05-15T14:15:00"),
                        type:external,
                        treatmentType:antibiotic)
        embargoedTag.addToSurgeries(embargoedWhaleSurgery).save(failOnError:true)
        embargoedWhaleRelease.addToSurgeries(embargoedWhaleSurgery).save(failOnError:true)

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

        new ReceiverRecovery(recoveryDateTime: recoveryDateTimeRx1,
                             location:(Point)reader.read("POINT(10.1234 10.1234)"),
                             status:recoveredStatus,
                             recoverer:sealProjectInvestigator,
                             deployment:rx1Bondi,
                             batteryLife:12.5f,
                             batteryVoltage:3.7f).save(failOnError:true)

        new ReceiverRecovery(recoveryDateTime: recoveryDateTimeRx2,
                             location:(Point)reader.read("POINT(20.1234 20.1234)"),
                             status:recoveredStatus,
                             recoverer:sealProjectInvestigator,
                             deployment:rx2Bondi,
                             batteryLife:12.5f,
                             batteryVoltage:3.7f).save(failOnError:true)

        new ReceiverRecovery(recoveryDateTime: recoveryDateTimeRx2,
                             location:(Point)reader.read("POINT(20.1234 20.1234)"),
                             status:recoveredStatus,
                             recoverer:sealProjectInvestigator,
                             deployment:rx6Heron,
                             batteryLife:12.5f,
                             batteryVoltage:3.7f).save(failOnError:true)

        new ReceiverRecovery(recoveryDateTime: recoveryDateTimeRx2,
                             location:(Point)reader.read("POINT(20.1234 20.1234)"),
                             status:recoveredStatus,
                             recoverer:sealProjectInvestigator,
                             deployment:whaleDeployment,
                             batteryLife:12.5f,
                             batteryVoltage:3.7f).save(failOnError:true)

        createExportWithDetections("export1.csv", jonBurgess, rx1Bondi, rx1, tag1, 10)
        createExportWithDetections("export5.csv", jonBurgess, rx4Heron, rx4, tag5, 3)

        createExportWithDetections("nonEmbargoedWhale.csv", joeBloggs, whaleDeployment, rxWhale, nonEmbargoedTag, 3)
        createExportWithDetections("embargoedWhale.csv", joeBloggs, whaleDeployment, rxWhale, embargoedTag, 3)
        createExportWithDetections("unknownTagWhale.csv", joeBloggs, whaleDeployment, rxWhale, [pinger:[transmitterId:"A69-1303-8888"]], 3)

        initStatistics()

        ReceiverDownloadFile export2 =
            new ReceiverDownloadFile(type:ReceiverDownloadFileType.DETECTIONS_CSV,
                                     name:"export2.csv",
                                     importDate:new DateTime("2013-05-17T12:54:56").toDate(),
                                     status:FileProcessingStatus.PROCESSED,
                                     errMsg:"",
                                     requestingUser:jonBurgess).save(failOnError:true)

        DateTime eventDate = new DateTime("2013-05-17T12:54:56")
        10.times
        {
            ValidReceiverEvent event =
                new ValidReceiverEvent(timestamp:eventDate.plusMinutes(it).toDate(),
                                  receiverDeployment:rx2Bondi,
                                  receiverName:"VR2W-101337",
                                  description:"desc",
                                  data:"123",
                                  units:"m")

            export2.addToEvents(event)
        }

        5.times
        {
            ValidReceiverEvent event =
                new ValidReceiverEvent(timestamp:eventDate.plusMinutes(it).toDate(),
                                  receiverDeployment:rx3Ningaloo,
                                  receiverName:"VR2W-101338",
                                  description:"other desc",
                                  data:"23.4",
                                  units:"C")

            export2.addToEvents(event)
        }

        export2.save(failOnError:true)
    }

    def initProtectedSpeciesData()
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
        Tag unembargoedTag = createTag(
                [serialNumber:'21111',
                 codeMap:a69_1303,
                 pingCode:'21111',
                 model:tagDeviceModel,
                 project:unembargoedProject,
                 status:deployedStatus])
        unembargoedProject.addToTags(unembargoedTag).save(failOnError:true)

        Tag embargoedTag = createTag(
                [serialNumber:'22222',
                 codeMap:a69_1303,
                 pingCode:'22222',
                 model:tagDeviceModel,
                 project:embargoedProject,
                 status:deployedStatus])
        embargoedProject.addToTags(embargoedTag).save(failOnError:true)

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

    private void initStatistics() {
        new Statistics(
            key: "numValidDetections",
            value: 25
        ).save(failOnError: true)
    }

    private void createExportWithDetections(String exportName, Person uploader, ReceiverDeployment deployment, Receiver receiver, tag, int numDetections)
    {
        ReceiverDownloadFile export =
            new ReceiverDownloadFile(type:ReceiverDownloadFileType.DETECTIONS_CSV,
                                     name:exportName,
                                     importDate:new DateTime("2013-05-17T12:54:56").toDate(),
                                     status:FileProcessingStatus.PROCESSED,
                                     errMsg:"",
                                     requestingUser:uploader).save(failOnError:true)

        createDetections(deployment, receiver, tag, export, numDetections)
        export.save(failOnError:true)
    }

    private void createDetections(ReceiverDeployment rx1Bondi, Receiver rx1, tag, ReceiverDownloadFile export1, int numDetections = 1)
    {
        numDetections.times
        {
            ValidDetection detection =
                    new ValidDetection(receiverDeployment:rx1Bondi,
                    timestamp: new DateTime("2011-05-17T02:54:00+00:00").plusSeconds(it).toDate(),
                    receiverName: rx1.name,
                    transmitterId: tag.pinger.transmitterId,
                    receiverDownload: export1,
                    provisional: false)

            export1.addToDetections(detection)
        }
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
