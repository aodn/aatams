import au.org.emii.aatams.*
import grails.converters.JSON

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.ParseException
import com.vividsolutions.jts.io.WKTReader

import org.apache.shiro.crypto.hash.Sha256Hash
import org.joda.time.*
import org.joda.time.format.DateTimeFormat

import com.vividsolutions.jts.geom.Point

class BootStrap 
{
    def permissionUtilsService // = new PermissionUtilsService()
    
    def init = 
    { 
        servletContext ->

        JSON.registerObjectMarshaller(Animal.class)
        {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['name'] = it.toString()

            return returnArray
        }
        
        // Add "label" property for the jquery autocomplete plugin.
        JSON.registerObjectMarshaller(Species.class)
        {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['name'] = it.toString()
            returnArray['label'] = it.toString()

            return returnArray
        }

        JSON.registerObjectMarshaller(CaabSpecies.class)
        {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['name'] = it.toString()
            returnArray['label'] = it.toString()

            return returnArray
        }

        JSON.registerObjectMarshaller(ProjectAccess.class)
        {
            def returnArray = [:]
            returnArray['displayStatus'] = it.displayStatus
            return returnArray
        }

        // TODO: this is being ignored for some reason (so we register a
        // custom marshaller for Surgery, which includes a DateTime, 
        // instead).
        JSON.registerObjectMarshaller(DateTime.class, 0)
        {
            println("Formatting date")
            return DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss zz").print(it)
        }

        JSON.registerObjectMarshaller(Surgery.class)
        {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['timestamp'] = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss zz").print(it.timestamp)
            returnArray['tag'] = it.tag
            returnArray['type'] = it.type
            returnArray['treatmentType'] = it.treatmentType
            returnArray['comments'] = it.comments

            return returnArray
        }
        
        JSON.registerObjectMarshaller(Tag.class)
        {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['label'] = it.getCodeMapPingCode()
            returnArray['serialNumber'] = it.serialNumber
            returnArray['model'] = it.model
            returnArray['transmitterType'] = it.transmitterType

            return returnArray
        }
        

        JSON.registerObjectMarshaller(Point.class)
        {
            return "(" + it.coordinate.x + ", " + it.coordinate.y + ")"
        }
           
        JSON.registerObjectMarshaller(AnimalMeasurement.class)
        {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['type'] = it.type
            returnArray['value'] = it.value
            returnArray['unit'] = it.unit
            returnArray['estimate'] = it.estimate
            returnArray['comments'] = it.comments
            
            return returnArray
        }
        
        JSON.registerObjectMarshaller(ProjectRole.class)
        {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['person'] = it.person
            returnArray['roleType'] = it.roleType
            returnArray['access'] = it.access
            
            return returnArray
        }
        
        JSON.registerObjectMarshaller(OrganisationProject.class)
        {
            def returnArray = [:]
            
            returnArray['id'] = it.id
            returnArray['organisation'] = it.organisation
            
            return returnArray
        }
        
        JSON.registerObjectMarshaller(Sensor.class)
        {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['transmitterType'] = it.transmitterType
            returnArray['codeMap'] = it.codeMap
            returnArray['pingCode'] = it.pingCode
            returnArray['slope'] = it.slope
            returnArray['intercept'] = it.intercept
            returnArray['unit'] = it.unit
            returnArray['status'] = it.status

            returnArray['label'] = it.getCodeMapPingCode()
            returnArray['serialNumber'] = it.serialNumber
            returnArray['model'] = it.model
            
            return returnArray
        }
        
        environments
        {
            test
            {
//                initData()
                initPerformanceData()
            }
            
            development
            {
                initData()
//                initPerformanceData()
            }
            
            // TODO: remove this.
            production
            {
//                initData()
                initPerformanceData()
            }
            
            performance
            {
                 
            }
            
            
        }
    }
    
    def destroy = 
    {
    }
    
    def initData()
    {
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

        Person jonBurgess =
            new Person(username:'jkburges',
                       passwordHash:new Sha256Hash("password").toHex(),
                       name:'Jon Burgess',
                       //organisation:imosOrg,
                       phoneNumber:'1234',
                       emailAddress:'jkburges@utas.edu.au',
                       status:EntityStatus.ACTIVE)
                   
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
                             status:EntityStatus.ACTIVE,
                             requestingUser:jonBurgess).save(failOnError: true)

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
                             status:EntityStatus.PENDING,
                             requestingUser:jonBurgess).save(failOnError: true)

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
                             status:EntityStatus.PENDING,
                             requestingUser:jonBurgess).save(failOnError: true)


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
                                    project:sealCountProject).save(failOnError: true)

        OrganisationProject csiroTuna =
            new OrganisationProject(organisation:csiroOrg,
                                    project:tunaProject).save(failOnError: true)

        //
        // Security/people.
        //
        SecRole sysAdmin = new SecRole(name:"SysAdmin")
        sysAdmin.addToPermissions("*:*")
        sysAdmin.save(failOnError: true)
            
        //
        // People.
        //
//        Person jonBurgess =
//            new Person(username:'jkburges',
//                       passwordHash:new Sha256Hash("password").toHex(),
//                       name:'Jon Burgess',
//                       organisation:imosOrg,
//                       phoneNumber:'1234',
//                       emailAddress:'jkburges@utas.edu.au',
//                       status:EntityStatus.ACTIVE)
        jonBurgess.addToRoles(sysAdmin)
        jonBurgess.save(failOnError: true)

        Person joeBloggs =
            new Person(username:'jbloggs',
                       passwordHash:new Sha256Hash("password").toHex(),
                       name:'Joe Bloggs',
                       organisation:csiroOrg,
                       phoneNumber:'1234',
                       emailAddress:'jbloggs@blah.au',
                       status:EntityStatus.ACTIVE).save(failOnError: true)

        Person johnCitizen =
            new Person(username:'jcitizen',
                       passwordHash:new Sha256Hash("password").toHex(),
                       name:'John Citizen',
                       organisation:csiroOrg,
                       phoneNumber:'5678',
                       emailAddress:'jcitizen@blah.au',
                       status:EntityStatus.ACTIVE).save(failOnError: true)

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
                            access:ProjectAccess.READ_WRITE).save(failOnError: true)
        permissionUtilsService.setPermissions(tunaAdmin)
        
        ProjectRole sealProjectInvestigator =
            new ProjectRole(project:sealCountProject,
                            person: joeBloggs,
                            roleType: principalInvestigator,
                            access:ProjectAccess.READ_WRITE).save(failOnError: true)
        joeBloggs.save(failOnError:true)
        permissionUtilsService.setPermissions(sealProjectInvestigator)

        ProjectRole sealAdmin =
            new ProjectRole(project:sealCountProject,
                            person: johnCitizen,
                            roleType: administrator,
                            access:ProjectAccess.READ_ONLY).save(failOnError: true)
        permissionUtilsService.setPermissions(sealAdmin)

        ProjectRole tunaWrite =
            new ProjectRole(project:tunaProject,
                            person: johnCitizen,
                            roleType: administrator,
                            access:ProjectAccess.READ_WRITE).save(failOnError: true)
        permissionUtilsService.setPermissions(tunaWrite)


        //
        // Devices.
        //
        DeviceManufacturer vemco = 
            new DeviceManufacturer(manufacturerName:'Vemco').save(failOnError: true)

        DeviceModel vemcoXyz =
            new DeviceModel(modelName:'XYZ', manufacturer:vemco).save(failOnError: true)
        DeviceModel vemcoAbc =
            new DeviceModel(modelName:'ABC', manufacturer:vemco).save(failOnError: true)

        DeviceStatus newStatus = new DeviceStatus(status:'NEW').save(failOnError: true)
        DeviceStatus deployedStatus = new DeviceStatus(status:'DEPLOYED').save(failOnError: true)
        DeviceStatus recoveredStatus = new DeviceStatus(status:'RECOVERED').save(failOnError: true)

        Receiver rx1 =
            new Receiver(codeName:'VR2W-101336',
                         serialNumber:'12345678',
                         status:deployedStatus,
                         model:vemcoXyz,
                         organisation:csiroOrg,
                         comment:'RX 1 belonging to CSIRO').save(failOnError: true)

        Receiver rx2 =
            new Receiver(codeName:'VR2W-101337',
                         serialNumber:'87654321',
                         status:deployedStatus,
                         model:vemcoXyz,
                         organisation:csiroOrg).save(failOnError: true)

        Receiver rx3 =
            new Receiver(codeName:'VR2W-101338',
                         serialNumber:'1111',
                         status:newStatus,
                         model:vemcoXyz,
                         organisation:imosOrg).save(failOnError: true)
                     
        //
        // Tags.
        //
        TransmitterType pinger =
            new TransmitterType(transmitterTypeName:"PINGER").save(failOnError:true)
        Tag tag1 =
            new Tag(codeName:'A69-1303-62339',
                    serialNumber:'62339',
                    codeMap:'A69-1303',
                    pingCode:'62339',
                    model:vemcoXyz,
                    project:sealCountProject,
                    status:deployedStatus,
                    transmitterType:pinger).save(failOnError: true)

        Tag tag2 =
            new Tag(codeName:'A69-1303-46601',
                    serialNumber:'46601',
                    codeMap:'A69-1303',
                    pingCode:'46601',
                    model:vemcoAbc,
                    project:sealCountProject,
                    status:deployedStatus,
                    transmitterType:pinger).save(failOnError: true)

        Tag tag3 =
            new Tag(codeName:'A69-1303-11111',
                    serialNumber:'1111',
                    codeMap:'A69-1303',
                    pingCode:'11111',
                    model:vemcoAbc,
                    project:sealCountProject,
                    status:newStatus,
                    transmitterType:pinger).save(failOnError: true)
              
        // Bug #352 - this tag won't be selectable if animal release project
        // set to "tuna".
        Tag tag5 =
            new Tag(codeName:'A70-1303-33333',
                    serialNumber:'3333',
                    codeMap:'A70-1303',
                    pingCode:'3333',
                    model:vemcoXyz,
                    project:tunaProject,
                    status:newStatus,
                    transmitterType:pinger).save(failOnError: true)
          
        Tag tag6 =
            new Tag(codeName:'A70-1303-44444',
                    serialNumber:'4444',
                    codeMap:'A70-1303',
                    pingCode:'4444',
                    model:vemcoXyz,
                    project:tunaProject,
                    status:newStatus,
                    transmitterType:pinger).save(failOnError: true)
                
        TransmitterType depth =
            new TransmitterType(transmitterTypeName:"DEPTH").save(failOnError:true)
        TransmitterType temp =
            new TransmitterType(transmitterTypeName:"TEMP").save(failOnError:true)

        Sensor sensor1 =
            new Sensor(codeName:'A69-1400-64000',
                    serialNumber:'64000',
                    codeMap:'A69-1400',
                    pingCode:'64000',
                    model:vemcoXyz,
                    project:sealCountProject,
                    status:newStatus,
                    tag:tag1,
                    transmitterType:depth,
                    unit:'m',
                    slope:1,
                    intercept:0).save(failOnError: true)

        Sensor sensor2 =
            new Sensor(codeName:'A69-1500-65000',
                    serialNumber:'65000',
                    codeMap:'A69-1500',
                    pingCode:'65000',
                    model:vemcoXyz,
                    project:sealCountProject,
                    status:newStatus,
                    tag:tag1,
                    transmitterType:temp,
                    unit:'k',
                    slope:1,
                    intercept:0).save(failOnError: true)

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

        WKTReader reader = new WKTReader();

        InstallationStation bondiSW1 = 
            new InstallationStation(installation:bondiLine,
                                    name:'Bondi SW1',
                                    curtainPosition:1,
                                    location:(Point)reader.read("POINT(30.1234 30.1234)")).save(failOnError:true)

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
            

        //
        //  Receiver Deployments.
        //
        MooringType concreteMooring = new MooringType(type:'CONCRETE BLOCK').save(failOnError:true)

        ReceiverDeployment rx1Bondi =
            new ReceiverDeployment(station:bondiSW1,
                                   receiver:rx1,
                                   deploymentNumber:1,
                                   deploymentDateTime:new DateTime("2010-02-15T12:34:56+10:00"),
                                   acousticReleaseID:"asdf",
                                   mooringType:concreteMooring,
                                   bottomDepthM:12f,
                                   depthBelowSurfaceM:5f,
                                   receiverOrientation:ReceiverOrientation.UP,
                                   batteryLifeDays:90,
                                   location:(Point)reader.read("POINT(10.1234 10.1234)")).save(failOnError:true)

        ReceiverDeployment rx2Bondi =
            new ReceiverDeployment(station:bondiSW2,
                                   receiver:rx2,
                                   deploymentNumber:1,
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
                                   deploymentDateTime:new DateTime("2011-05-15T12:34:56+10:00"),
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
        Species whiteShark = new Species(name:'White Shark').save(failOnError:true)
        Species blueFinTuna = new Species(name:'Blue Fin Tuna').save(failOnError:true)
        Species blueEyeTrevalla = new Species(name:'Blue Eye Trevalla').save(failOnError:true)

        Sex male = new Sex(sex:'MALE').save(failOnError:true)
        Sex female = new Sex(sex:'FEMALE').save(failOnError:true)

        Animal whiteShark1 = new Animal(species:whiteShark,
                                        sex:male).save(failOnError:true)
        Animal whiteShark2 = new Animal(species:whiteShark,
                                        sex:male).save(failOnError:true)
        Animal blueFinTuna1 = new Animal(species:blueFinTuna,
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
                              releaseDateTime:new DateTime("2011-05-15T14:15:00"),
                              embargoDate:Date.parse("yyyy-MM-dd hh:mm:ss", "2015-05-15 12:34:56")).save(failOnError:true)
                          
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

        Surgery surgery2 = 
            new Surgery(release:whiteShark1Release,
                        tag:tag2,
                        timestamp:new DateTime("2011-05-15T14:13:00"),
                        type:external,
                        treatmentType:antibiotic)
        tag2.addToSurgeries(surgery1).save(failOnError:true)
        
        Surgery surgery3 = 
            new Surgery(release:whiteShark2Release,
                        tag:tag1,   // Can't really have a tag on two different animals.
                        timestamp:new DateTime("2011-05-15T14:12:00"),
                        type:external,
                        treatmentType:antibiotic)
        tag1.addToSurgeries(surgery3).save(failOnError:true)
                    
        Surgery surgery4 = 
            new Surgery(release:blueFinTuna1Release,
                        tag:tag1,   // Can't really have a tag on two different animals.
                        timestamp:new DateTime("2011-05-15T14:12:00"),
                        type:external,
                        treatmentType:antibiotic)
        tag1.addToSurgeries(surgery4).save(failOnError:true)
                    
        // Receiver Recovery.
        ReceiverRecovery recovery1 = 
            new ReceiverRecovery(recoveryDateTime: new DateTime("2011-07-25T12:34:56"),
                                 location:(Point)reader.read("POINT(10.1234 10.1234)"),
                                 status:recoveredStatus,
                                 recoverer:sealProjectInvestigator,
                                 deployment:rx1Bondi,
                                 batteryLife:12.5f,
                                 batteryVoltage:3.7f)
        ReceiverDownload download1 =
            new ReceiverDownload(receiverRecovery:recovery1,
                                 downloadDateTime:new DateTime("2011-05-17T12:34:56"))
        recovery1.save(failOnError:true)
                             
        ReceiverRecovery recovery2 = 
            new ReceiverRecovery(recoveryDateTime: new DateTime("2011-05-17T12:54:56"),
                                 location:(Point)reader.read("POINT(20.1234 20.1234)"),
                                 status:recoveredStatus,
                                 recoverer:sealProjectInvestigator,
                                 deployment:rx2Bondi,
                                 batteryLife:12.5f,
                                 batteryVoltage:3.7f)
        ReceiverDownload download2 =
            new ReceiverDownload(receiverRecovery:recovery2,
                                 downloadDateTime:new DateTime("2011-05-17T12:54:56"))
        recovery2.save(failOnError:true)
                             
        // Detections.
        /**
        Detection detection1 =
            new Detection(timestamp:Date.parse("yyyy-MM-dd hh:mm:ss", "2011-05-20 14:10:00"),
                          receiverDeployment:rx1Bondi,
                          stationName:'Bondi SW1',
                          transmitterName:'A69-1303-62339',
                          transmitterSerialNumber:'12345678',
                          location:(Point)reader.read("POINT(10.1234 10.1234)")).save(failOnError:true)
        DetectionSurgery detSurg1 = 
            new DetectionSurgery(detection:detection1,
                                 surgery:surgery1).save(failOnError:true)
        
        Detection detection2 =
            new Detection(timestamp:Date.parse("yyyy-MM-dd hh:mm:ss", "2011-05-20 15:10:00"),
                          receiverDeployment:rx2Bondi,
                          stationName:'Bondi SW2',
                          transmitterName:'A69-1303-62339',
                          transmitterSerialNumber:'12345678',
                          location:(Point)reader.read("POINT(20.1234 20.1234)")).save(failOnError:true)
        DetectionSurgery detSurg2 = 
            new DetectionSurgery(detection:detection2,
                                 surgery:surgery2).save(failOnError:true)
                                 */
    }
    
/**    
    // Small counts for test-data testing.
    def smallTest = true
    def numOrgs = 3
    def numProjectsPerOrg = 2           // 6
    def numPeoplePerProject = 2         // 12

    def numInstallationsPerProject = 2  // 12
    def numStationsPerInstallation = 2  // 24
    def numDeploymentsPerReceiver = 1   // 24
    def numRecoveriesPerReceiver = 1    // 24

    def numTagsPerProject = 2           // 24
    def numDetectionsPerSurgery = 2     // 48
    
//    def numEventsPerRecovery = 2
    
    // Large numbers for performance/load testing.
    
    def smallTest = false
    def numOrgs = 10
    def numProjectsPerOrg = 5           // 50
    def numPeoplePerProject = 4         // 200

    def numInstallationsPerProject = 5  // 250
    def numStationsPerInstallation = 5  // 1250
    def numDeploymentsPerReceiver = 1   // 1250
    def numRecoveriesPerReceiver = 1    // 1250

    def numTagsPerProject = 100         // 5000
    def numDetectionsPerSurgery = 5     // 25000
    
//    def numEventsPerRecovery = 2
    
    ProjectRoleType principalInvestigator = new ProjectRoleType(displayName:ProjectRoleType.PRINCIPAL_INVESTIGATOR).save()
    ProjectRoleType student = new ProjectRoleType(displayName:'student').save()
    DeviceModel deviceModel = DeviceModel.build().save() 
    DeviceStatus newStatus = DeviceStatus.build(status:'NEW').save()
    DeviceStatus deployedStatus = DeviceStatus.build(status:'DEPLOYED').save()
    WKTReader reader = new WKTReader();
    
    InstallationConfiguration installationConfig = InstallationConfiguration.build(type:'ARRAY').save()
    Organisation imosOrg
    
    Species testSpecies = new Species(name:'Test Species').save()
    Animal testAnimal = new Animal(species:testSpecies).save()
    
    MooringType mooringType = MooringType.build().save()
    TransmitterType pinger = TransmitterType.build(transmitterTypeName:'PINGER').save()
    SurgeryType surgeryType = SurgeryType.build().save()
    SurgeryTreatmentType surgeryTreatmentType = SurgeryTreatmentType.build().save()
    CaptureMethod defaultCaptureMethod = CaptureMethod.build().save()
    
    Address defaultAddress = Address.build().save()
    
    def totalProjectCount = 0

    def deploymentDateTime = new DateTime("2000-01-01T12:54:56")
    def recoveryDateTime = new DateTime("2020-01-01T12:54:56")
    def releaseDateTime = new DateTime("2000-01-01T12:54:56")
    
    Person jonBurgess
    def sessionFactory
    def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP
    
    def cleanUpGorm() 
    {
        def session = sessionFactory.currentSession
        session.flush()
        session.clear()
        propertyInstanceMap.get().clear()
    }
    
    def initPerformanceData()
    {
        def startTimestamp = System.currentTimeMillis()
        def results = new File("/tmp/profiling.csv")
        results.write("project count,elapsed time\n")
        
        // 
        // Security/people.
        //
        SecRole sysAdmin = new SecRole(name:"SysAdmin")
        sysAdmin.addToPermissions("*:*")
        sysAdmin.save(failOnError: true)
            
        //
        // People.
        //
        jonBurgess =
            new Person(username:'jkburges',
                       passwordHash:new Sha256Hash("password").toHex(),
                       name:'Jon Burgess',
//                       organisation:Organisation.build(),
                       phoneNumber:'1234',
                       emailAddress:'jkburges@utas.edu.au',
                       status:EntityStatus.ACTIVE)
                   
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

        imosOrg = 
            new Organisation(name:'IMOS', 
                             department:'eMII',
                             phoneNumber:'5678',
                             faxNumber:'5678',
                             streetAddress:imosStreetAddress,
                             postalAddress:imosPostalAddress,
                             status:EntityStatus.PENDING,
                             requestingUser:jonBurgess).save(failOnError: true)
        
        jonBurgess.addToRoles(sysAdmin)
        jonBurgess.save(failOnError: true)
        
        
        numOrgs.times
        {
            orgCount ->
            
//            println("org count: " + orgCount)
            
            String orgName = "Org " + orgCount
            def org = new Organisation(name: orgName,
                                         department: "asdf",
                                         phoneNumber: "1234",
                                         faxNumber: "1234",
                                         streetAddress: defaultAddress,
                                         postalAddress: defaultAddress,
                                         status: EntityStatus.ACTIVE,
                                         requestingUser: jonBurgess)
            org.save(flush:true)
            
            // 5 projects for each organisation, each with 4 people (including one PI).
            numProjectsPerOrg.times
            {
                projectCount ->
                
                // Do this to ensure that correct organisation object is used in session (after clearing/flushing).
                org = Organisation.findByName(orgName)
                assert(org != null): "Organisation null, name: " + orgName
                
                def project = new Project(name: "Project " + orgCount + "." + projectCount,
                                          description: "asdf",
                                          status: EntityStatus.ACTIVE,
                                          requestingUser: jonBurgess).save()
                def orgProject = new OrganisationProject(organisation:org,
                                                         project:project).save()
                
                createPeople(org, orgCount, project, projectCount)
                createInstallations(org, orgCount, project, projectCount)
                createTags(org, orgCount, project, projectCount)
               
                totalProjectCount++
                
                def elapsedTime = System.currentTimeMillis() - startTimestamp
                println(String.valueOf(new Date()) + ": total projects: " + totalProjectCount)
                results.append(totalProjectCount)
                results.append(',')
                results.append(elapsedTime)
                results.append('\n')

                cleanUpGorm()
            }
        }
    }
    
    def totalDetectionCount = 0
    def totalTagCount = 0
    
    def createTags(org, orgCount, project, projectCount)
    {
        numTagsPerProject.times
        {
            tagCount ->
            
            // 1 release/surgery per tag.
            def tag = new Tag(codeName:"A69-1303-" + totalTagCount,
                              codeMap:"A69-1303",
                              pingCode:totalTagCount,
                              project:project,
                              model:deviceModel,
                              status:deployedStatus,
                              transmitterType:pinger,
                              serialNumber:"1234").save(failOnError:true)
                                    
            totalTagCount++            
            
            def release = new AnimalRelease(project:project, animal:testAnimal,
                                              captureLocation:(Point)reader.read("POINT(10.1234 10.1234)"),
                                              releaseLocation:(Point)reader.read("POINT(10.1234 10.1234)"),
                                              captureLocality:"asdf",
                                              captureDateTime:releaseDateTime,
                                              captureMethod:defaultCaptureMethod,
                                              releaseLocality:"awef",
                                              releaseDateTime:releaseDateTime).save(failOnError:true)
                                              
            
            def surgery = new Surgery(release:release, // tag:tag,
                                      timestamp:releaseDateTime,
                                      type:surgeryType,
                                      treatmentType:surgeryTreatmentType)
                                    
            tag.addToSurgeries(surgery)
            tag.save(flush:true, failOnError:true)
           
            assert(surgery != null): "surgery cannot be null"
            
//            numDetectionsPerSurgery.times
//            {
//                def codeName = String.valueOf(totalDetectionCount % totalReceiverCount)
//                Receiver receiver = Receiver.findByCodeName(codeName)
//                assert receiver != null: "Receiver not found, codeName: " + codeName
////                def deployment = receiver.deployments[0]
//                def deployment = ReceiverDeployment.findByReceiver(receiver)
//                assert deployment != null: "No deployment for receiver: " + String.valueOf(receiver)
//                
//                def detection = new Detection(receiverDeployment:deployment,
//                                              location:(Point)reader.read("POINT(10.1234 10.1234)"),
//                                              timestamp:new Date(),
//                                              receiverName:codeName,
//                                              transmitterId:tag.codeName)
//               
//                def detectionSurgery = new DetectionSurgery(surgery:surgery,
//                                                            tag:tag,
//                                                            detection:detection)
//                                                            
//                detection.addToDetectionSurgeries(detectionSurgery)
//                detection.save(failOnError:true)
//                
//                totalDetectionCount++
//            }
            
            
            // This should cascade save the above.
            project.save()
        }
    }
    
    def createPeople(org, orgCount, project, projectCount)
    {
        numPeoplePerProject.times
        {
            personCount ->

            def person = new Person(name: "Person " + orgCount + "." + projectCount + "." + personCount,
                                    organisation:org,
                                    emailAddress:"asdf@asdf.com",
                                    phoneNumber:"1234",
                                    status:EntityStatus.ACTIVE).save()

            project.save()
            
            ProjectRole role = new ProjectRole(project:project,
                                               person:person,
                                               roleType:student,
                                               access:ProjectAccess.READ_WRITE)

            // Make the first person on each project a PI.
            if (personCount == 0)
            {
                role.roleType = principalInvestigator
            }
            role.save(flush:true)
            
            // Don't set permissions for now as it is too slow.
//            permissionUtilsService.setPermissions(role)
        }
    }
    
    // This is used when creating detections.
    def totalReceiverCount = 0
    
    def createInstallations(org, orgCount, project, projectCount)
    {
        ProjectRole recoverer = new ProjectRole(project:project,
                                   person:jonBurgess,
                                   roleType:principalInvestigator,
                                   access:ProjectAccess.READ_WRITE).save()

        numInstallationsPerProject.times
        {
            installationCount ->
            
            def installation = new Installation(name: "Installation " + orgCount + "." + projectCount + "." + installationCount,
                                                project: project,
                                                configuration: installationConfig).save()
            
            numStationsPerInstallation.times
            {
                stationCount ->
                
                def station = new InstallationStation(name: "InstallationStation " + orgCount + "." + projectCount + "." + installationCount + "." + stationCount,
                                                        location:(Point)reader.read("POINT(10.1234 10.1234)"),
                                                        installation:installation).save()
                        
                installation.addToStations(station)
                
                // Just create one receiver/deployment per station for now.
                def codeName = String.valueOf(totalReceiverCount)
                def receiver = new Receiver(codeName: codeName,
                                            model: deviceModel,
                                            serialNumber: "1234",
                                            status: deployedStatus,
                                            organisation: org).save(flush:true)
                org.addToReceivers(receiver).save(flush:true)
                
                def receiverDeployment = 
                    new ReceiverDeployment(station: station,
                                           receiver: receiver,
                                           deploymentNumber:0,
                                           deploymentDateTime:deploymentDateTime,
                                           mooringType: mooringType)
                receiver.addToDeployments(receiverDeployment)                       
                                       
                def receiverRecovery =
                    new ReceiverRecovery(deployment:receiverDeployment,
                                         recoverer:recoverer,
                                         recoveryDateTime:recoveryDateTime,
                                         location:(Point)reader.read("POINT(10.1234 10.1234)"),
                                         status: deployedStatus,
                                         download:new ReceiverDownload(downloadDateTime:recoveryDateTime))
                                             
                receiverDeployment.recovery = receiverRecovery
                receiver.save()
                
                totalReceiverCount++
            }
            
            installation.save()
        }
        
//        org.save(flush:true)    // Flush so that Receiver.findByCodeName (in tags) works.
    }
    */    
}
