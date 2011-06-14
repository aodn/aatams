import au.org.emii.aatams.*

class BootStrap 
{

    def init = 
    { 
        servletContext ->
        
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
                                    roleType: administrator).save(failOnError: true)
                ProjectRole sealProjectInvestigator =
                    new ProjectRole(project:sealCountProject,
                                    person: joeBloggs,
                                    roleType: principalInvestigator).save(failOnError: true)
                sealCountProject.setPrincipalInvestigator(sealProjectInvestigator)
                sealCountProject.save(failOnError: true)
                                
                ProjectRole sealAdmin =
                    new ProjectRole(project:sealCountProject,
                                    person: johnCitizen,
                                    roleType: administrator).save(failOnError: true)

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
                            
            }
        }
    }
    
    def destroy = 
    {
    }
}
