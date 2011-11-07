package au.org.emii.aatams.data

import au.org.emii.aatams.*

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.ParseException
import com.vividsolutions.jts.io.WKTReader

import org.apache.shiro.crypto.hash.Sha256Hash
import org.joda.time.*
import org.joda.time.format.DateTimeFormat

import com.vividsolutions.jts.geom.Point

import shiro.*

/**
 *
 * @author jburgess
 */
class PerformanceDataInitialiser extends AbstractDataInitialiser
{
    PerformanceDataInitialiser(def service)
    {
        super(service)
    }
    
    void execute()
    {
        initPerformanceData()
    }
    
    // Small counts for test-data testing.
 /**   
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
*/
/** */    
    // Large numbers for performance/load testing.
    
    def smallTest = false
    def numOrgs = 1 //2 // 10
    def numProjectsPerOrg = 1 //2 //5           // 50
    def numPeoplePerProject = 4         // 200

    def numInstallationsPerProject = 5  // 250
    def numStationsPerInstallation = 5  // 1250
    def numDeploymentsPerReceiver = 1   // 1250
    def numRecoveriesPerReceiver = 1    // 1250

    def numTagsPerProject = 100         // 5000
    def numDetectionsPerSurgery = 5     // 25000
    
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
                                         status: deployedStatus)
                                             
                receiverDeployment.recovery = receiverRecovery
                receiver.save()
                
                totalReceiverCount++
            }
            
            installation.save()
        }
        
//        org.save(flush:true)    // Flush so that Receiver.findByCodeName (in tags) works.

    }
}
    

