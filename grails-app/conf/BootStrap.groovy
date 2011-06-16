import au.org.emii.aatams.*
import grails.converters.deep.JSON

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.ParseException
import com.vividsolutions.jts.io.WKTReader

class BootStrap 
{

    def init = 
    { 
        servletContext ->

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
            development
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
                // People.
                //
                Person joeBloggs =
                    new Person(name:'Joe Bloggs',
                               phoneNumber:'1234',
                               emailAddress:'jbloggs@csiro.au').save(failOnError: true)
                           
                Person johnCitizen =
                    new Person(name:'John Citizen',
                               phoneNumber:'5678',
                               emailAddress:'jcitizen@csiro.au').save(failOnError: true)
                   
                //
                // Roles.
                //
                ProjectRoleType principalInvestigator =
                    new ProjectRoleType(displayName:'Principal Investigator').save(failOnError: true)
                ProjectRoleType administrator =
                    new ProjectRoleType(displayName:'Administrator').save(failOnError: true)
                    
                ProjectRole tunaAdmin =
                    new ProjectRole(project:tunaProject,
                                    person: joeBloggs,
                                    roleType: administrator,
                                    access: ProjectAccess.READ_ONLY).save(failOnError: true)
                ProjectRole sealProjectInvestigator =
                    new ProjectRole(project:sealCountProject,
                                    person: joeBloggs,
                                    roleType: principalInvestigator,
                                    access: ProjectAccess.READ_WRITE).save(failOnError: true)
                sealCountProject.setPrincipalInvestigator(sealProjectInvestigator)
                sealCountProject.save(failOnError: true)
                                
                ProjectRole sealAdmin =
                    new ProjectRole(project:sealCountProject,
                                    person: johnCitizen,
                                    roleType: administrator,
                                    access: ProjectAccess.READ_WRITE).save(failOnError: true)

                OrganisationPerson csiroJohnCitizen =
                    new OrganisationPerson(organisation:csiroOrg,
                                           person:johnCitizen).save(failOnError: true)
                OrganisationPerson csiroJoeBloggs =
                    new OrganisationPerson(organisation:csiroOrg,
                                           person:joeBloggs).save(failOnError: true)
                
                //
                // Devices.
                //
                DeviceManufacturer seimens = 
                    new DeviceManufacturer(manufacturerName:'Seimens').save(failOnError: true)
                    
                DeviceModel seimensXyz =
                    new DeviceModel(modelName:'XYZ', manufacturer:seimens).save(failOnError: true)
                    
                DeviceStatus newStatus = new DeviceStatus(status:'NEW').save(failOnError: true)
                DeviceStatus deployedStatus = new DeviceStatus(status:'DEPLOYED').save(failOnError: true)
                
                Receiver rx1 =
                    new Receiver(codeName:'VR2W-101336',
                                 serialNumber:'12345678',
                                 embargoDate:null,
                                 status:deployedStatus,
                                 model:seimensXyz,
                                 project:sealCountProject).save(failOnError: true)
                             
                Tag tag1 =
                    new Tag(codeName:'A69-1303-62339',
                            serialNumber:'12345678',
                            embargoDate:null,
                            codeMap:'A69-1303',
                            pingCode:'62339',
                            model:seimensXyz,
                            project:sealCountProject,
                            status:deployedStatus).save(failOnError: true)
                            
                Tag tag2 =
                    new Tag(codeName:'A69-1303-46601',
                            serialNumber:'12345678',
                            embargoDate:null,
                            codeMap:'A69-1303',
                            pingCode:'46601',
                            model:seimensXyz,
                            project:sealCountProject,
                            status:deployedStatus).save(failOnError: true)
                        
                
                //
                // Installation data.
                //
                InstallationConfiguration array =
                    new InstallationConfiguration(type:'ARRAY').save(failOnError:true)
                InstallationConfiguration curtain =
                    new InstallationConfiguration(type:'CURTAIN').save(failOnError:true)
                    
                Installation bondiLine =
                    new Installation(name:'Bondi Line',
                                     configuration:array,
                                     project:sealCountProject,
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
            }
        }
    }
    
    def destroy = 
    {
    }
}
