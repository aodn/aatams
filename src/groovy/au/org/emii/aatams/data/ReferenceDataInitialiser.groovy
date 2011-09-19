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
class ReferenceDataInitialiser extends AbstractDataInitialiser
{
    ReferenceDataInitialiser(def service)
    {
        super(service)
    }
    
    void execute()
    {
        initReferenceData()
    }
    
    def initReferenceData()
    {
        Person admin =
            new Person(username:'admin',
                       passwordHash:new Sha256Hash("password").toHex(),
                       name:'System Administrator',
                       //organisation:imosOrg,
                       phoneNumber:'1234',
                       emailAddress:'jkburges@utas.edu.au',
                       status:EntityStatus.ACTIVE)
        
        Address imosAddress =
            new Address(streetAddress:'University of Tasmania, Private Bag 110',
                        suburbTown:'Hobart',
                        state:'TAS',
                        country:'Australia',
                        postcode:'7001').save()

        Organisation imosOrg = 
            new Organisation(name:'IMOS', 
                             department:'eMII',
                             phoneNumber:'+61 (03) 6226 7549',
                             faxNumber:'+61 (03) 6226 2107',
                             streetAddress:imosAddress,
                             postalAddress:imosAddress,
                             status:EntityStatus.ACTIVE,
                             requestingUser:admin).save(failOnError: true)

        SecRole sysAdmin = new SecRole(name:"SysAdmin")
        sysAdmin.addToPermissions("*:*")
        sysAdmin.save(failOnError: true)
        
        admin.addToRoles(sysAdmin)
        admin.save(failOnError:true)
        
        ProjectRoleType principalInvestigator = new ProjectRoleType(displayName:ProjectRoleType.PRINCIPAL_INVESTIGATOR).save(failOnError: true)
        ProjectRoleType coInvestigator = new ProjectRoleType(displayName:"Co-Investigator").save(failOnError: true)
        ProjectRoleType researchAssistant = new ProjectRoleType(displayName:"Research Assistant").save(failOnError: true)
        ProjectRoleType technicalAssistant = new ProjectRoleType(displayName:"Technical Assistant").save(failOnError: true)
        ProjectRoleType administrator = new ProjectRoleType(displayName:"Administrator").save(failOnError: true)
        ProjectRoleType student = new ProjectRoleType(displayName:"Student").save(failOnError: true)
        
        DeviceManufacturer vemco = 
            new DeviceManufacturer(manufacturerName:'Vemco').save(failOnError: true)

        // Receiver models.
        DeviceModel vemcoVR2 =
            new ReceiverDeviceModel(modelName:'VR2', manufacturer:vemco).save(failOnError: true)
        DeviceModel vemcoVR2W =
            new ReceiverDeviceModel(modelName:'VR2W', manufacturer:vemco).save(failOnError: true)
        DeviceModel vemcoVR3UWM =
            new ReceiverDeviceModel(modelName:'VR3-UWM', manufacturer:vemco).save(failOnError: true)
            
        // Tag models.
        DeviceModel vemcoV6180 =
            new TagDeviceModel(modelName:'V6-180kHz', manufacturer:vemco).save(failOnError: true)
        DeviceModel vemcoV7 =
            new TagDeviceModel(modelName:'V7', manufacturer:vemco).save(failOnError: true)
        DeviceModel vemcoV8 =
            new TagDeviceModel(modelName:'V8', manufacturer:vemco).save(failOnError: true)
        DeviceModel vemcoV9 =
            new TagDeviceModel(modelName:'V9', manufacturer:vemco).save(failOnError: true)
        DeviceModel vemcoV9AP =
            new TagDeviceModel(modelName:'V9AP', manufacturer:vemco).save(failOnError: true)
        DeviceModel vemcoV13 =
            new TagDeviceModel(modelName:'V13', manufacturer:vemco).save(failOnError: true)
        DeviceModel vemcoV16 =
            new TagDeviceModel(modelName:'V16', manufacturer:vemco).save(failOnError: true)

        DeviceStatus newStatus = new DeviceStatus(status:'NEW').save(failOnError: true)
        DeviceStatus deployedStatus = new DeviceStatus(status:'DEPLOYED').save(failOnError: true)
        DeviceStatus recoveredStatus = new DeviceStatus(status:'RECOVERED').save(failOnError: true)
        DeviceStatus retiredStatus = new DeviceStatus(status:'RETIRED').save(failOnError: true)
        DeviceStatus lostStatus = new DeviceStatus(status:'LOST').save(failOnError: true)
        DeviceStatus stolenStatus = new DeviceStatus(status:'STOLEN').save(failOnError: true)
        DeviceStatus damagedStatus = new DeviceStatus(status:'DAMAGED').save(failOnError: true)
        DeviceStatus returnedToVendorStatus = new DeviceStatus(status:'RETURNED TO VENDOR').save(failOnError: true)

        TransmitterType pinger = new TransmitterType(transmitterTypeName:"PINGER").save(failOnError:true)
        TransmitterType pressure = new TransmitterType(transmitterTypeName:"PRESSURE").save(failOnError:true)
        TransmitterType temperature = new TransmitterType(transmitterTypeName:"TEMPERATURE").save(failOnError:true)
        TransmitterType accelerometer = new TransmitterType(transmitterTypeName:"ACCELEROMETER").save(failOnError:true)
        
        InstallationConfiguration array =
            new InstallationConfiguration(type:'ARRAY').save(failOnError:true)
        InstallationConfiguration curtain =
            new InstallationConfiguration(type:'CURTAIN').save(failOnError:true)
        InstallationConfiguration single =
            new InstallationConfiguration(type:'SINGLE').save(failOnError:true)
        
        MooringType fixedMooring = new MooringType(type:'FIXED').save(failOnError:true)
        MooringType floatingMooring = new MooringType(type:'FLOATING').save(failOnError:true)
        
        AnimalMeasurementType length = new AnimalMeasurementType(type:'LENGTH').save(failOnError:true)
        AnimalMeasurementType weight = new AnimalMeasurementType(type:'WEIGHT').save(failOnError:true)
        
        MeasurementUnit mm = new MeasurementUnit(unit:'mm').save(failOnError:true)
        MeasurementUnit gram = new MeasurementUnit(unit:'g').save(failOnError:true)
        
        CaptureMethod net = new CaptureMethod(name:'NET').save(failOnError:true)
        CaptureMethod line = new CaptureMethod(name:'LINE').save(failOnError:true)
        CaptureMethod longLine = new CaptureMethod(name:'LONG LINE').save(failOnError:true)
        CaptureMethod trap = new CaptureMethod(name:'TRAP').save(failOnError:true)
        CaptureMethod handCapture = new CaptureMethod(name:'HAND CAPTURE').save(failOnError:true)
        CaptureMethod freeSwimming = new CaptureMethod(name:'FREE SWIMMING').save(failOnError:true)
        
        Sex male = new Sex(sex:'MALE').save(failOnError:true)
        Sex female = new Sex(sex:'FEMALE').save(failOnError:true)
        Sex unknown = new Sex(sex:'UNKNOWN').save(failOnError:true)
        
        SurgeryType internal = new SurgeryType(type:'INTERNAL').save(failOnError:true)
        SurgeryType external = new SurgeryType(type:'EXTERNAL').save(failOnError:true)

        SurgeryTreatmentType anesthetic = new SurgeryTreatmentType(type:'ANESTHETIC').save(failOnError:true)
        SurgeryTreatmentType noAnesthetic = new SurgeryTreatmentType(type:'NO ANESTHETIC').save(failOnError:true)
    }
}
