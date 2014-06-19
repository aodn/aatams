package au.org.emii.aatams.bulk

import org.grails.plugins.csv.CSVMapReader
import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory;

import au.org.emii.aatams.*
import grails.test.*

class ReceiverDeploymentLoaderTests extends AbstractLoaderTests
{
    Installation installation
    ProjectRole sealsPi
    
    protected void setUp()
    {
        super.setUp()
        
        loader = new ReceiverDeploymentLoader()
        
        mockDomain(InstallationStation)
        mockDomain(ReceiverDeployment)
        mockDomain(Receiver)
        
        MooringType fixedMooring = new MooringType(type: "FIXED")
        mockDomain(MooringType, [fixedMooring])
        fixedMooring.save()
        
        Project project = new Project(name: "seals")
        mockDomain(Project, [project])
        project.save()
        
        installation = new Installation(project: project)
        mockDomain(Installation, [installation])
        installation.save()
        
        mockDomain(ProjectRole)
        
        ProjectRoleType principalInvestigator = new ProjectRoleType(displayName: ProjectRoleType.PRINCIPAL_INVESTIGATOR)
        mockDomain(ProjectRoleType, [principalInvestigator])
        principalInvestigator.save()
        
        Person joeBloggs = new Person(name: "Joe Bloggs")
        mockDomain(Person, [joeBloggs])
        joeBloggs.save()

        sealsPi = new ProjectRole(person: joeBloggs, project: project, roleType: principalInvestigator, access: ProjectAccess.READ_WRITE)
        mockDomain(ProjectRole, [sealsPi])
        sealsPi.save()
        
        DeviceStatus recovered = new DeviceStatus(status: "RECOVERED")
        mockDomain(DeviceStatus, [recovered])
        recovered.save()
        
        mockDomain(ReceiverRecovery)
    }

    protected void tearDown()
    {
        super.tearDown()
    }
    
    void testNewDeployment()
    {
        InstallationStation station = createStation()
        Receiver receiver = createReceiver()
        
        def receiverDeploymentsText = '''"RCD_ID","RCV_ID","STA_ID","RCD_LATITUDE","RCD_LONGITUDE","RCD_MOORING_DEPTH","RCD_DATETIME_IN","RCD_DATETIME_OUT","RCD_FIRMWARE_VER","RCD_PC_DATETIME_AT_UPLOAD","RCD_PC_TIMEZONE_AT_UPLOAD","RCD_PC_TIMEZONE_AT_INIT","RCD_INIT_DATETIME","RCD_LOG_START_DATETIME","RCD_LOG_END_DATETIME","RCD_UPLOAD_DATETIME","RCD_CODE_MAP_CODE","RCD_BLANKING_INT","RCD_BATTERY_UPTIME","RCD_FILENAME","RCD_PERF_STATS","RCD_GAIN_MODE_CODE","RCD_GAIN","RCD_STRUCTURE_FAIL_YN","RCD_BATTERY_FAIL_YN","RCD_SOFTWARE_FAIL_YN","RCD_LOST_YN","RCD_OTHER_FAILURE","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
871,202,12,-21.96,113.92,,12/10/2008 6:09:08,19/1/2009 6:15:22,"1.2.7",19/1/2009 6:15:22,"UTC+10:00","UTC+08:00",12/10/2008 6:09:08,12/10/2008 6:09:08,19/1/2009 6:17:37,19/1/2009 6:17:37,583,240,0,,,,,,,,,,29/9/2010 16:47:36,"TAG",14/1/2011 12:28:20,"TAG"
'''
            
        assertSuccess(
            [receiverDeploymentsText], 
            [["type": BulkImportRecordType.NEW, 
             "srcPk": 871, 
             "srcTable": "RECEIVERDEPLOYMENTS", 
             "srcModifiedDate": ReceiverDeploymentLoader.DATE_TIME_FORMATTER.parseDateTime("14/1/2011 12:28:20"),
             "dstClass": "au.org.emii.aatams.ReceiverDeployment"]])
    }
    
    void testUpdated()
    {
        InstallationStation station = createStation()
        Receiver receiver = createReceiver()
        
        ReceiverDeployment deployment = new ReceiverDeployment(
            station: new InstallationStation(),
            receiver: new Receiver(),
            deploymentNumber: 0,
            // initialisationDateTime - set this when loading detections?
            deploymentDateTime: new DateTime(),
            mooringType: MooringType.findByType("FIXED"),
            bottomDepthM: 0,
            receiverOrientation: ReceiverOrientation.UP,
            location: new GeometryFactory().createPoint(new Coordinate(0, 0)))
        
        deployment.save(failOnErorr: true)
        
        BulkImport bulkImport = new BulkImport(organisation: new Organisation(), importStartDate: new DateTime(), status: BulkImportStatus.IN_PROGRESS, filename: "some/path")
        bulkImport.save(failOnError: true)
        
        BulkImportRecord importRecord =
            new BulkImportRecord(
                bulkImport:bulkImport,
                srcTable: "RECEIVERDEPLOYMENTS",
                srcPk: 871,
                srcModifiedDate: ReceiverDeploymentLoader.DATE_TIME_FORMATTER.parseDateTime("15/5/2007 16:01:55"),
                dstClass: "au.org.emii.aatams.ReceiverDeployment",
                dstPk: deployment.id,
                type: BulkImportRecordType.NEW)
        importRecord.save(failOnError:true)
            
        assertEquals(1, ReceiverDeployment.count())
        assertEquals(1, BulkImport.count())
        assertEquals(3, BulkImportRecord.count())
        
        def receiverDeploymentsText = '''"RCD_ID","RCV_ID","STA_ID","RCD_LATITUDE","RCD_LONGITUDE","RCD_MOORING_DEPTH","RCD_DATETIME_IN","RCD_DATETIME_OUT","RCD_FIRMWARE_VER","RCD_PC_DATETIME_AT_UPLOAD","RCD_PC_TIMEZONE_AT_UPLOAD","RCD_PC_TIMEZONE_AT_INIT","RCD_INIT_DATETIME","RCD_LOG_START_DATETIME","RCD_LOG_END_DATETIME","RCD_UPLOAD_DATETIME","RCD_CODE_MAP_CODE","RCD_BLANKING_INT","RCD_BATTERY_UPTIME","RCD_FILENAME","RCD_PERF_STATS","RCD_GAIN_MODE_CODE","RCD_GAIN","RCD_STRUCTURE_FAIL_YN","RCD_BATTERY_FAIL_YN","RCD_SOFTWARE_FAIL_YN","RCD_LOST_YN","RCD_OTHER_FAILURE","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
871,202,12,-10.0,113.92,,12/10/2008 6:09:08,19/1/2009 6:15:22,"1.2.7",19/1/2009 6:15:22,"UTC+10:00","UTC+08:00",12/10/2008 6:09:08,12/10/2008 6:09:08,19/1/2009 6:17:37,19/1/2009 6:17:37,583,240,0,,,,,,,,,,29/9/2010 16:47:36,"TAG",14/1/2011 12:28:20,"TAG"
'''
        
        assertSuccess(
            [receiverDeploymentsText],
            [["type": BulkImportRecordType.UPDATED,
             "srcPk": 871,
             "srcTable": "RECEIVERDEPLOYMENTS",
             "srcModifiedDate": ReceiverDeploymentLoader.DATE_TIME_FORMATTER.parseDateTime("14/1/2011 12:28:20"),
             "dstClass": "au.org.emii.aatams.ReceiverDeployment"]])

        assertEquals(1, ReceiverDeployment.count())
        assertEquals(2, BulkImport.count())
        assertEquals(4, BulkImportRecord.count())
        assertEquals(-10.0f, deployment.location.y)
    }
    
    void testIgnored()
    {
        InstallationStation station = createStation()
        Receiver receiver = createReceiver()
        
        ReceiverDeployment deployment = new ReceiverDeployment(
            station: new InstallationStation(),
            receiver: new Receiver(),
            deploymentNumber: 0,
            // initialisationDateTime - set this when loading detections?
            deploymentDateTime: new DateTime(),
            mooringType: MooringType.findByType("FIXED"),
            bottomDepthM: 0,
            receiverOrientation: ReceiverOrientation.UP,
            location: new GeometryFactory().createPoint(new Coordinate(0, 0)))
        
        deployment.save(failOnErorr: true)
        
        BulkImport bulkImport = new BulkImport(organisation: new Organisation(), importStartDate: new DateTime(), status: BulkImportStatus.IN_PROGRESS, filename: "some/path")
        bulkImport.save(failOnError: true)
        
        BulkImportRecord importRecord =
            new BulkImportRecord(
                bulkImport:bulkImport,
                srcTable: "RECEIVERDEPLOYMENTS",
                srcPk: 871,
                srcModifiedDate: ReceiverDeploymentLoader.DATE_TIME_FORMATTER.parseDateTime("15/5/2012 16:01:55"),
                dstClass: "au.org.emii.aatams.ReceiverDeployment",
                dstPk: deployment.id,
                type: BulkImportRecordType.NEW)
        importRecord.save(failOnError:true)
            
        assertEquals(1, ReceiverDeployment.count())
        assertEquals(1, BulkImport.count())
        assertEquals(3, BulkImportRecord.count())
        
        def receiverDeploymentsText = '''"RCD_ID","RCV_ID","STA_ID","RCD_LATITUDE","RCD_LONGITUDE","RCD_MOORING_DEPTH","RCD_DATETIME_IN","RCD_DATETIME_OUT","RCD_FIRMWARE_VER","RCD_PC_DATETIME_AT_UPLOAD","RCD_PC_TIMEZONE_AT_UPLOAD","RCD_PC_TIMEZONE_AT_INIT","RCD_INIT_DATETIME","RCD_LOG_START_DATETIME","RCD_LOG_END_DATETIME","RCD_UPLOAD_DATETIME","RCD_CODE_MAP_CODE","RCD_BLANKING_INT","RCD_BATTERY_UPTIME","RCD_FILENAME","RCD_PERF_STATS","RCD_GAIN_MODE_CODE","RCD_GAIN","RCD_STRUCTURE_FAIL_YN","RCD_BATTERY_FAIL_YN","RCD_SOFTWARE_FAIL_YN","RCD_LOST_YN","RCD_OTHER_FAILURE","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
871,202,12,-10.0,113.92,,12/10/2008 6:09:08,19/1/2009 6:15:22,"1.2.7",19/1/2009 6:15:22,"UTC+10:00","UTC+08:00",12/10/2008 6:09:08,12/10/2008 6:09:08,19/1/2009 6:17:37,19/1/2009 6:17:37,583,240,0,,,,,,,,,,29/9/2010 16:47:36,"TAG",14/1/2011 12:28:20,"TAG"
'''
        
        assertSuccess(
            [receiverDeploymentsText],
            [["type": BulkImportRecordType.IGNORED,
             "srcPk": 871,
             "srcTable": "RECEIVERDEPLOYMENTS",
             "srcModifiedDate": ReceiverDeploymentLoader.DATE_TIME_FORMATTER.parseDateTime("14/1/2011 12:28:20"),
             "dstClass": "au.org.emii.aatams.ReceiverDeployment"]])

        assertEquals(1, ReceiverDeployment.count())
        assertEquals(2, BulkImport.count())
        assertEquals(4, BulkImportRecord.count())
        assertEquals(0.0f, deployment.location.y)
    }
    
    void testInvalidDeploymentWithNoStation()
    {
        def receiverDeploymentsText = '''"RCD_ID","RCV_ID","STA_ID","RCD_LATITUDE","RCD_LONGITUDE","RCD_MOORING_DEPTH","RCD_DATETIME_IN","RCD_DATETIME_OUT","RCD_FIRMWARE_VER","RCD_PC_DATETIME_AT_UPLOAD","RCD_PC_TIMEZONE_AT_UPLOAD","RCD_PC_TIMEZONE_AT_INIT","RCD_INIT_DATETIME","RCD_LOG_START_DATETIME","RCD_LOG_END_DATETIME","RCD_UPLOAD_DATETIME","RCD_CODE_MAP_CODE","RCD_BLANKING_INT","RCD_BATTERY_UPTIME","RCD_FILENAME","RCD_PERF_STATS","RCD_GAIN_MODE_CODE","RCD_GAIN","RCD_STRUCTURE_FAIL_YN","RCD_BATTERY_FAIL_YN","RCD_SOFTWARE_FAIL_YN","RCD_LOST_YN","RCD_OTHER_FAILURE","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
256,108,,,,,,,,,,,1/1/1990 0:00:00,,,,,,,,,,,-1,,,,,31/3/2009 15:05:22,"TAG",27/9/2010 10:29:27,"TAG"
'''
            
        assertSuccess(
            [receiverDeploymentsText], 
            [["type": BulkImportRecordType.INVALID, 
             "srcPk": 256, 
             "srcTable": "RECEIVERDEPLOYMENTS", 
             "srcModifiedDate": ReceiverDeploymentLoader.DATE_TIME_FORMATTER.parseDateTime("27/9/2010 10:29:27"),
             "dstClass": "au.org.emii.aatams.ReceiverDeployment"]])
    }
    
    void testInvalidDeploymentNoDeploymentTime()
    {
        InstallationStation station = createStation()
        Receiver receiver = createReceiver()
        
        def receiverDeploymentsText = '''"RCD_ID","RCV_ID","STA_ID","RCD_LATITUDE","RCD_LONGITUDE","RCD_MOORING_DEPTH","RCD_DATETIME_IN","RCD_DATETIME_OUT","RCD_FIRMWARE_VER","RCD_PC_DATETIME_AT_UPLOAD","RCD_PC_TIMEZONE_AT_UPLOAD","RCD_PC_TIMEZONE_AT_INIT","RCD_INIT_DATETIME","RCD_LOG_START_DATETIME","RCD_LOG_END_DATETIME","RCD_UPLOAD_DATETIME","RCD_CODE_MAP_CODE","RCD_BLANKING_INT","RCD_BATTERY_UPTIME","RCD_FILENAME","RCD_PERF_STATS","RCD_GAIN_MODE_CODE","RCD_GAIN","RCD_STRUCTURE_FAIL_YN","RCD_BATTERY_FAIL_YN","RCD_SOFTWARE_FAIL_YN","RCD_LOST_YN","RCD_OTHER_FAILURE","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
871,202,12,-21.96,113.92,,,19/1/2009 6:15:22,"1.2.7",19/1/2009 6:15:22,"UTC+10:00","UTC+08:00",12/10/2008 6:09:08,12/10/2008 6:09:08,19/1/2009 6:17:37,19/1/2009 6:17:37,583,240,0,,,,,,,,,,29/9/2010 16:47:36,"TAG",14/1/2011 12:28:20,"TAG"
'''
        
        assertSuccess(
            [receiverDeploymentsText], 
            [["type": BulkImportRecordType.INVALID, 
             "srcPk": 871, 
             "srcTable": "RECEIVERDEPLOYMENTS", 
             "srcModifiedDate": ReceiverDeploymentLoader.DATE_TIME_FORMATTER.parseDateTime("14/1/2011 12:28:20"),
             "dstClass": "au.org.emii.aatams.ReceiverDeployment"]])
    }
    
    void testRecovery()
    {
        InstallationStation station = createStation()
        Receiver receiver = createReceiver()
        
        def receiverDeploymentsText = '''"RCD_ID","RCV_ID","STA_ID","RCD_LATITUDE","RCD_LONGITUDE","RCD_MOORING_DEPTH","RCD_DATETIME_IN","RCD_DATETIME_OUT","RCD_FIRMWARE_VER","RCD_PC_DATETIME_AT_UPLOAD","RCD_PC_TIMEZONE_AT_UPLOAD","RCD_PC_TIMEZONE_AT_INIT","RCD_INIT_DATETIME","RCD_LOG_START_DATETIME","RCD_LOG_END_DATETIME","RCD_UPLOAD_DATETIME","RCD_CODE_MAP_CODE","RCD_BLANKING_INT","RCD_BATTERY_UPTIME","RCD_FILENAME","RCD_PERF_STATS","RCD_GAIN_MODE_CODE","RCD_GAIN","RCD_STRUCTURE_FAIL_YN","RCD_BATTERY_FAIL_YN","RCD_SOFTWARE_FAIL_YN","RCD_LOST_YN","RCD_OTHER_FAILURE","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
871,202,12,-21.96,113.92,,12/10/2008 6:09:08,19/1/2009 6:15:22,"1.2.7",19/1/2009 6:15:22,"UTC+10:00","UTC+08:00",12/10/2008 6:09:08,12/10/2008 6:09:08,19/1/2009 6:17:37,19/1/2009 6:17:37,583,240,0,,,,,,,,,"hello world",29/9/2010 16:47:36,"TAG",14/1/2011 12:28:20,"TAG"
'''
        assertSuccess(
            [receiverDeploymentsText], 
            [["type": BulkImportRecordType.NEW, 
             "srcPk": 871, 
             "srcTable": "RECEIVERDEPLOYMENTS", 
             "srcModifiedDate": ReceiverDeploymentLoader.DATE_TIME_FORMATTER.parseDateTime("14/1/2011 12:28:20"),
             "dstClass": "au.org.emii.aatams.ReceiverDeployment"]])
        
        BulkImportRecord importRecord = BulkImportRecord.findBySrcPk(871)
        assertNotNull(importRecord)
        
        ReceiverDeployment deployment = ReceiverDeployment.get(importRecord.dstPk)
        assertNotNull(deployment)
        
        ReceiverRecovery recovery = ReceiverRecovery.findByDeployment(deployment)
        assertNotNull(recovery)
        assertEquals(deployment.location, recovery.location)
        assertEquals(ReceiverDeploymentLoader.DATE_TIME_FORMATTER.parseDateTime("19/1/2009 6:15:22"), recovery.recoveryDateTime)
        assertEquals(DeviceStatus.findByStatus("RECOVERED"), recovery.status)
        assertEquals("hello world", recovery.comments)
        assertEquals(sealsPi, recovery.recoverer)
    }
    
    void testFindStationExisting()
    {
        InstallationStation station = createStation()
        
        def receiverDeploymentsText = '''"RCD_ID","RCV_ID","STA_ID","RCD_LATITUDE","RCD_LONGITUDE","RCD_MOORING_DEPTH","RCD_DATETIME_IN","RCD_DATETIME_OUT","RCD_FIRMWARE_VER","RCD_PC_DATETIME_AT_UPLOAD","RCD_PC_TIMEZONE_AT_UPLOAD","RCD_PC_TIMEZONE_AT_INIT","RCD_INIT_DATETIME","RCD_LOG_START_DATETIME","RCD_LOG_END_DATETIME","RCD_UPLOAD_DATETIME","RCD_CODE_MAP_CODE","RCD_BLANKING_INT","RCD_BATTERY_UPTIME","RCD_FILENAME","RCD_PERF_STATS","RCD_GAIN_MODE_CODE","RCD_GAIN","RCD_STRUCTURE_FAIL_YN","RCD_BATTERY_FAIL_YN","RCD_SOFTWARE_FAIL_YN","RCD_LOST_YN","RCD_OTHER_FAILURE","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
871,202,12,-21.96,113.92,,12/10/2008 6:09:08,19/1/2009 6:15:22,"1.2.7",19/1/2009 6:15:22,"UTC+10:00","UTC+08:00",12/10/2008 6:09:08,12/10/2008 6:09:08,19/1/2009 6:17:37,19/1/2009 6:17:37,583,240,0,,,,,,,,,,29/9/2010 16:47:36,"TAG",14/1/2011 12:28:20,"TAG"
'''
        def receiverDeploymentRecord = new CSVMapReader(new InputStreamReader(new ByteArrayInputStream(receiverDeploymentsText.bytes))).toList()
        
        InstallationStation foundStation = loader.findStation(receiverDeploymentRecord)
        assertNotNull(foundStation)
        assertEquals(station.name, foundStation.name)
    }

    void testFindStationNonExisting()
    {
        def receiverDeploymentsText = '''"RCD_ID","RCV_ID","STA_ID","RCD_LATITUDE","RCD_LONGITUDE","RCD_MOORING_DEPTH","RCD_DATETIME_IN","RCD_DATETIME_OUT","RCD_FIRMWARE_VER","RCD_PC_DATETIME_AT_UPLOAD","RCD_PC_TIMEZONE_AT_UPLOAD","RCD_PC_TIMEZONE_AT_INIT","RCD_INIT_DATETIME","RCD_LOG_START_DATETIME","RCD_LOG_END_DATETIME","RCD_UPLOAD_DATETIME","RCD_CODE_MAP_CODE","RCD_BLANKING_INT","RCD_BATTERY_UPTIME","RCD_FILENAME","RCD_PERF_STATS","RCD_GAIN_MODE_CODE","RCD_GAIN","RCD_STRUCTURE_FAIL_YN","RCD_BATTERY_FAIL_YN","RCD_SOFTWARE_FAIL_YN","RCD_LOST_YN","RCD_OTHER_FAILURE","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
871,202,12,-21.96,113.92,,12/10/2008 6:09:08,19/1/2009 6:15:22,"1.2.7",19/1/2009 6:15:22,"UTC+10:00","UTC+08:00",12/10/2008 6:09:08,12/10/2008 6:09:08,19/1/2009 6:17:37,19/1/2009 6:17:37,583,240,0,,,,,,,,,,29/9/2010 16:47:36,"TAG",14/1/2011 12:28:20,"TAG"
'''
        def receiverDeploymentRecord = new CSVMapReader(new InputStreamReader(new ByteArrayInputStream(receiverDeploymentsText.bytes))).toList()
        
        try
        {
            InstallationStation foundStation = loader.findStation(receiverDeploymentRecord)
            fail()
        }
        catch (BulkImportException e)
        {
            assertEquals("Unknown station ID in import: " + 12, e.message)
        }
    }    
    
    void testFindReceiverExisting()
    {
        Receiver receiver = createReceiver()
        
        def receiverDeploymentsText = '''"RCD_ID","RCV_ID","STA_ID","RCD_LATITUDE","RCD_LONGITUDE","RCD_MOORING_DEPTH","RCD_DATETIME_IN","RCD_DATETIME_OUT","RCD_FIRMWARE_VER","RCD_PC_DATETIME_AT_UPLOAD","RCD_PC_TIMEZONE_AT_UPLOAD","RCD_PC_TIMEZONE_AT_INIT","RCD_INIT_DATETIME","RCD_LOG_START_DATETIME","RCD_LOG_END_DATETIME","RCD_UPLOAD_DATETIME","RCD_CODE_MAP_CODE","RCD_BLANKING_INT","RCD_BATTERY_UPTIME","RCD_FILENAME","RCD_PERF_STATS","RCD_GAIN_MODE_CODE","RCD_GAIN","RCD_STRUCTURE_FAIL_YN","RCD_BATTERY_FAIL_YN","RCD_SOFTWARE_FAIL_YN","RCD_LOST_YN","RCD_OTHER_FAILURE","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
871,202,12,-21.96,113.92,,12/10/2008 6:09:08,19/1/2009 6:15:22,"1.2.7",19/1/2009 6:15:22,"UTC+10:00","UTC+08:00",12/10/2008 6:09:08,12/10/2008 6:09:08,19/1/2009 6:17:37,19/1/2009 6:17:37,583,240,0,,,,,,,,,,29/9/2010 16:47:36,"TAG",14/1/2011 12:28:20,"TAG"
'''
        def receiverDeploymentRecord = new CSVMapReader(new InputStreamReader(new ByteArrayInputStream(receiverDeploymentsText.bytes))).toList()
        
        Receiver foundReceiver = loader.findReceiver(receiverDeploymentRecord)
        assertNotNull(foundReceiver)
        assertEquals(receiver.serialNumber, foundReceiver.serialNumber)
    }

    void testFindReceiverNonExisting()
    {
        def receiverDeploymentsText = '''"RCD_ID","RCV_ID","STA_ID","RCD_LATITUDE","RCD_LONGITUDE","RCD_MOORING_DEPTH","RCD_DATETIME_IN","RCD_DATETIME_OUT","RCD_FIRMWARE_VER","RCD_PC_DATETIME_AT_UPLOAD","RCD_PC_TIMEZONE_AT_UPLOAD","RCD_PC_TIMEZONE_AT_INIT","RCD_INIT_DATETIME","RCD_LOG_START_DATETIME","RCD_LOG_END_DATETIME","RCD_UPLOAD_DATETIME","RCD_CODE_MAP_CODE","RCD_BLANKING_INT","RCD_BATTERY_UPTIME","RCD_FILENAME","RCD_PERF_STATS","RCD_GAIN_MODE_CODE","RCD_GAIN","RCD_STRUCTURE_FAIL_YN","RCD_BATTERY_FAIL_YN","RCD_SOFTWARE_FAIL_YN","RCD_LOST_YN","RCD_OTHER_FAILURE","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
871,202,12,-21.96,113.92,,12/10/2008 6:09:08,19/1/2009 6:15:22,"1.2.7",19/1/2009 6:15:22,"UTC+10:00","UTC+08:00",12/10/2008 6:09:08,12/10/2008 6:09:08,19/1/2009 6:17:37,19/1/2009 6:17:37,583,240,0,,,,,,,,,,29/9/2010 16:47:36,"TAG",14/1/2011 12:28:20,"TAG"
'''
        def receiverDeploymentRecord = new CSVMapReader(new InputStreamReader(new ByteArrayInputStream(receiverDeploymentsText.bytes))).toList()
        
        try
        {
            Receiver foundReceiver = loader.findReceiver(receiverDeploymentRecord)
            fail()
        }
        catch (BulkImportException e)
        {
            assertEquals("Unknown receiver ID in import: " + 202, e.message)
        }
    }

    protected void assertSuccessDetail(it, importRecord)
    {
        if (importRecord.dstPk)
        {
            ReceiverDeployment deployment = ReceiverDeployment.get(importRecord.dstPk)
            assertNotNull(deployment)
            
            assertEquals(deployment.location, deployment.station.location)
        }
    }
    
    private InstallationStation createStation()
    {
        InstallationStation station =
            new InstallationStation(installation: installation,
                                    name: "BL1",
                                    location: new GeometryFactory().createPoint(new Coordinate(0f, 0f)))
        station.save(failOnError: true)
        
        BulkImportRecord importRecord = new BulkImportRecord(
            bulkImport: new BulkImport(),
            srcTable: "STATIONS",
            srcPk: Long.valueOf(12),
            srcModifiedDate: new DateTime(),
            dstClass: "au.org.emii.aatams.InstallationStation",
            dstPk: station.id,
            type: BulkImportRecordType.NEW)
        
        importRecord.save(failOnError: true)
        
        return station
    }
    
    private Receiver createReceiver()
    {
        Receiver receiver =
            new Receiver(organisation: csiro,
                         serialNumber: "1234",
                         model: new ReceiverDeviceModel())
        receiver.save(failOnError: true)
        
        BulkImportRecord importRecord = new BulkImportRecord(
            bulkImport: new BulkImport(),
            srcTable: "RECEIVERS",
            srcPk: Long.valueOf(202),
            srcModifiedDate: new DateTime(),
            dstClass: "au.org.emii.aatams.Receiver",
            dstPk: receiver.id,
            type: BulkImportRecordType.NEW)
        
        importRecord.save(failOnError: true)
        
        return receiver
    }
}
