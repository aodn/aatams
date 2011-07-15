import au.org.emii.aatams.*
import grails.converters.deep.JSON

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.ParseException
import com.vividsolutions.jts.io.WKTReader

import org.apache.shiro.crypto.hash.Sha256Hash

class BootStrap 
{
    def init = 
    { 
        servletContext ->

            // Add "label" property for the jquery autocomplete plugin.
            JSON.registerObjectMarshaller(Species.class)
            {
                def returnArray = [:]
                returnArray['id'] = it.id
                returnArray['name'] = it.name
                returnArray['label'] = it.name
                
                return returnArray
            }


        // TODO: this is not having any effect.
//        JSON.registerObjectMarshaller(ProjectAccess.class, 0)
//        {
//            log.info("Marshalling ProjectAccess: " + String.valueOf(it))
//            def returnArray = [:]
//            returnArray['name'] = it.name
//            returnArray['displayStatus'] = it.displayStatus
//            return returnArray
//        }

        environments
        {
            test
            {
                initData()
            }
            
            development
            {
                initData()
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
                        postcode:'7000')

        Address csiroPostalAddress =
            new Address(streetAddress:'34 Queen Street',
                        suburbTown:'Melbourne',
                        state:'VIC',
                        country:'Australia',
                        postcode:'3000')

        //
        // Organisations.
        //
        Organisation csiroOrg = 
            new Organisation(name:'CSIRO', 
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
                        postcode:'7000')

        Address imosPostalAddress =
            new Address(streetAddress:'34 Queen Street',
                        suburbTown:'Melbourne',
                        state:'VIC',
                        country:'Australia',
                        postcode:'3000')

        Organisation imosOrg = 
            new Organisation(name:'IMOS', 
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
                        postcode:'7000')

        Address imosPostalAddress2 =
            new Address(streetAddress:'34 Queen Street',
                        suburbTown:'Melbourne',
                        state:'VIC',
                        country:'Australia',
                        postcode:'3000')

        Organisation imosOrg2 = 
            new Organisation(name:'IMOS 2', 
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
                        description:'Counting seals').save(failOnError: true)

        Project tunaProject =
            new Project(name:'Tuna',
                        description:'Counting tuna').save(failOnError: true)

        Project whaleProject =
            new Project(name:'Whale',
                        description:'Whale counting').save(failOnError: true)

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
        Person jonBurgess =
            new Person(username:'jkburges',
                       passwordHash:new Sha256Hash("password").toHex(),
                       name:'Jon Burgess',
                       phoneNumber:'1234',
                       emailAddress:'jkburges@utas.edu.au')
        jonBurgess.addToRoles(sysAdmin)
        jonBurgess.save(failOnError: true)

        Person joeBloggs =
            new Person(username:'jbloggs',
                       passwordHash:new Sha256Hash("password").toHex(),
                       name:'Joe Bloggs',
                       phoneNumber:'1234',
                       emailAddress:'jbloggs@csiro.au').save(failOnError: true)

        Person johnCitizen =
            new Person(username:'jcitizen',
                       passwordHash:new Sha256Hash("password").toHex(),
                       name:'John Citizen',
                       phoneNumber:'5678',
                       emailAddress:'jcitizen@csiro.au').save(failOnError: true)

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
        ProjectRole sealProjectInvestigator =
            new ProjectRole(project:sealCountProject,
                            person: joeBloggs,
                            roleType: principalInvestigator,
                            access:ProjectAccess.READ_WRITE).save(failOnError: true)
        joeBloggs.save(failOnError:true)

        ProjectRole sealAdmin =
            new ProjectRole(project:sealCountProject,
                            person: johnCitizen,
                            roleType: administrator,
                            access:ProjectAccess.READ_ONLY).save(failOnError: true)
        ProjectRole tunaWrite =
            new ProjectRole(project:tunaProject,
                            person: johnCitizen,
                            roleType: administrator,
                            access:ProjectAccess.READ_WRITE).save(failOnError: true)

        OrganisationPerson csiroJohnCitizen =
            new OrganisationPerson(organisation:csiroOrg,
                                   person:johnCitizen).save(failOnError: true)
        OrganisationPerson csiroJoeBloggs =
            new OrganisationPerson(organisation:csiroOrg,
                                   person:joeBloggs).save(failOnError: true)

        //
        // Devices.
        //
        DeviceManufacturer vemco = 
            new DeviceManufacturer(manufacturerName:'Vemco').save(failOnError: true)

        DeviceModel vemcoXyz =
            new DeviceModel(modelName:'XYZ', manufacturer:vemco).save(failOnError: true)

        DeviceStatus newStatus = new DeviceStatus(status:'NEW').save(failOnError: true)
        DeviceStatus deployedStatus = new DeviceStatus(status:'DEPLOYED').save(failOnError: true)
        DeviceStatus recoveredStatus = new DeviceStatus(status:'RECOVERED').save(failOnError: true)

        Receiver rx1 =
            new Receiver(codeName:String.valueOf(vemcoXyz) + " - " + '12345678',
                         serialNumber:'12345678',
                         status:deployedStatus,
                         model:vemcoXyz,
                         project:sealCountProject).save(failOnError: true)

        Receiver rx2 =
            new Receiver(codeName:String.valueOf(vemcoXyz) + " - " + '87654321',
                         serialNumber:'87654321',
                         status:deployedStatus,
                         model:vemcoXyz,
                         project:tunaProject).save(failOnError: true)

        Receiver rx3 =
            new Receiver(codeName:String.valueOf(vemcoXyz) + " - " + '1111',
                         serialNumber:'1111',
                         status:newStatus,
                         model:vemcoXyz,
                         project:tunaProject).save(failOnError: true)
                     
        //
        // Tags.
        //
        TransmitterType pinger =
            new TransmitterType(transmitterTypeName:"PINGER").save(failOnError:true)
        Tag tag1 =
            new Tag(codeName:'A69-1303-62339',
                    serialNumber:'12345678',
                    codeMap:'A69-1303',
                    pingCode:'62339',
                    model:vemcoXyz,
                    project:sealCountProject,
                    status:deployedStatus,
                    transmitterType:pinger).save(failOnError: true)

        Tag tag2 =
            new Tag(codeName:'A69-1303-46601',
                    serialNumber:'12345678',
                    codeMap:'A69-1303',
                    pingCode:'46601',
                    model:vemcoXyz,
                    project:sealCountProject,
                    status:deployedStatus,
                    transmitterType:pinger).save(failOnError: true)

        Tag tag3 =
            new Tag(codeName:'A69-1303-11111',
                    serialNumber:'1111',
                    codeMap:'A69-1303',
                    pingCode:'11111',
                    model:vemcoXyz,
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
          
                
        TransmitterType depth =
            new TransmitterType(transmitterTypeName:"DEPTH").save(failOnError:true)
        TransmitterType temp =
            new TransmitterType(transmitterTypeName:"TEMP").save(failOnError:true)

        Sensor sensor1 =
            new Sensor(codeName:'A69-1400-64000',
                    serialNumber:'5678',
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
                    serialNumber:'6789',
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
                             project:sealCountProject,
                             lonOffset:15f,
                             latOffset:30f).save(failOnError:true)
                         
        Installation ningalooArray =
            new Installation(name:'Ningaloo Array',
                             configuration:array,
                             project:tunaProject,
                             lonOffset:15f,
                             latOffset:30f).save(failOnError:true)

        WKTReader reader = new WKTReader();

        InstallationStation bondiSW1 = 
            new InstallationStation(installation:bondiLine,
                                    name:'Bondi SW1',
                                    curtainPosition:1,
                                    location:(Point)reader.read("POINT(10 10)")).save(failOnError:true)

        InstallationStation bondiSW2 = 
            new InstallationStation(installation:bondiLine,
                                    name:'Bondi SW2',
                                    curtainPosition:2,
                                    location:(Point)reader.read("POINT(20 20)")).save(failOnError:true)

        InstallationStation bondiSW3 = 
            new InstallationStation(installation:bondiLine,
                                    name:'Bondi SW3',
                                    curtainPosition:3,
                                    location:(Point)reader.read("POINT(30 30)")).save(failOnError:true)
                                
        InstallationStation ningalooS1 = 
            new InstallationStation(installation:bondiLine,
                                    name:'Ningaloo S1',
                                    curtainPosition:1,
                                    location:(Point)reader.read("POINT(10 10)")).save(failOnError:true)

        InstallationStation ningalooS2 = 
            new InstallationStation(installation:bondiLine,
                                    name:'Ningaloo S2',
                                    curtainPosition:2,
                                    location:(Point)reader.read("POINT(20 20)")).save(failOnError:true)
            

        //
        //  Receiver Deployments.
        //
        MooringType concreteMooring = new MooringType(type:'CONCRETE BLOCK').save(failOnError:true)

        ReceiverDeployment rx1Bondi =
            new ReceiverDeployment(station:bondiSW1,
                                   receiver:rx1,
                                   deploymentDate:Date.parse("yyyy-MM-dd hh:mm:ss", "2011-05-15 12:34:56"),
                                   acousticReleaseID:"asdf",
                                   mooringType:concreteMooring,
                                   bottomDepthM:12f,
                                   depthBelowSurfaceM:5f,
                                   location:(Point)reader.read("POINT(10 10)")).save(failOnError:true)

        ReceiverDeployment rx2Bondi =
            new ReceiverDeployment(station:bondiSW2,
                                   receiver:rx2,
                                   deploymentDate:Date.parse("yyyy-MM-dd hh:mm:ss", "2011-05-15 14:12:00"),
                                   acousticReleaseID:"asdf",
                                   mooringType:concreteMooring,
                                   bottomDepthM:16f,
                                   depthBelowSurfaceM:7.4f,
                                   location:(Point)reader.read("POINT(20 20)")).save(failOnError:true)

        ReceiverDeployment rx3Ningaloo =
            new ReceiverDeployment(station:ningalooS1,
                                   receiver:rx3,
                                   deploymentDate:Date.parse("yyyy-MM-dd hh:mm:ss", "2011-05-15 12:34:56"),
                                   acousticReleaseID:"asdf",
                                   mooringType:concreteMooring,
                                   bottomDepthM:12f,
                                   depthBelowSurfaceM:5f,
                                   location:(Point)reader.read("POINT(10 10)")).save(failOnError:true)
                               
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


        AnimalRelease whiteShark1Release =
            new AnimalRelease(project:tunaProject,
                              surgeries:[],
                              measurements:[],
                              animal:whiteShark1,
                              captureLocality:'Neptune Islands',
                              captureLocation:(Point)reader.read("POINT(20 20)"),
                              captureDateTime:Date.parse("yyyy-MM-dd hh:mm:ss", "2011-05-15 14:10:00"),
                              releaseLocality:'Neptune Islands',
                              releaseLocation:(Point)reader.read("POINT(20 20)"),
                              releaseDateTime:Date.parse("yyyy-MM-dd hh:mm:ss", "2011-05-15 14:15:00"),
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
                        timestamp:Date.parse("yyyy-MM-dd hh:mm:ss", "2011-05-15 14:12:00"),
                        type:external,
                        sutures:false,
                        treatmentType:antibiotic,
                        surgeon:joeBloggs).save(failOnError:true)

        Surgery surgery2 = 
            new Surgery(release:whiteShark1Release,
                        tag:tag2,
                        timestamp:Date.parse("yyyy-MM-dd hh:mm:ss", "2011-05-15 14:13:00"),
                        type:external,
                        sutures:false,
                        treatmentType:antibiotic,
                        surgeon:joeBloggs).save(failOnError:true)

        // Receiver Recovery.
        ReceiverRecovery recovery1 = 
            new ReceiverRecovery(recoveryDate:Date.parse("yyyy-MM-dd hh:mm:ss", "2011-05-17 12:34:56"),
                                 location:(Point)reader.read("POINT(10 10)"),
                                 status:recoveredStatus,
                                 recoverer:sealProjectInvestigator,
                                 deployment:rx1Bondi,
                                 batteryLife:12.5f,
                                 batteryVoltage:3.7f).save(failOnError:true)
                             
        ReceiverRecovery recovery2 = 
            new ReceiverRecovery(recoveryDate:Date.parse("yyyy-MM-dd hh:mm:ss", "2011-05-17 12:54:56"),
                                 location:(Point)reader.read("POINT(20 20)"),
                                 status:recoveredStatus,
                                 recoverer:sealProjectInvestigator,
                                 deployment:rx2Bondi,
                                 batteryLife:12.5f,
                                 batteryVoltage:3.7f).save(failOnError:true)
                             
        // Detections.
        Detection detection1 =
            new Detection(timestamp:Date.parse("yyyy-MM-dd hh:mm:ss", "2011-05-20 14:10:00"),
                          receiverDeployment:rx1Bondi,
                          stationName:'Bondi SW1',
                          transmitterName:'A69-1303-62339',
                          transmitterSerialNumber:'12345678',
                          location:(Point)reader.read("POINT(10 10)"))
        detection1.addToSurgeries(surgery1)
        detection1.save(failOnError:true)
        
        Detection detection2 =
            new Detection(timestamp:Date.parse("yyyy-MM-dd hh:mm:ss", "2011-05-20 15:10:00"),
                          receiverDeployment:rx2Bondi,
                          stationName:'Bondi SW2',
                          transmitterName:'A69-1303-62339',
                          transmitterSerialNumber:'12345678',
                          location:(Point)reader.read("POINT(20 20)"))
        detection2.addToSurgeries(surgery1)
        detection2.save(failOnError:true)
        
    }
}
