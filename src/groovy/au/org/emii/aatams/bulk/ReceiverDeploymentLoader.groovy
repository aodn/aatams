package au.org.emii.aatams.bulk

import java.io.InputStream
import java.util.List
import java.util.Map

import org.apache.log4j.Logger
import org.joda.time.DateTime

import au.org.emii.aatams.DeviceStatus
import au.org.emii.aatams.InstallationStation
import au.org.emii.aatams.MooringType
import au.org.emii.aatams.ProjectRole
import au.org.emii.aatams.ProjectRoleType
import au.org.emii.aatams.Receiver
import au.org.emii.aatams.ReceiverDeployment
import au.org.emii.aatams.ReceiverOrientation
import au.org.emii.aatams.ReceiverRecovery

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.Point

class ReceiverDeploymentLoader extends AbstractLoader 
{
    private static final Logger log = Logger.getLogger(ReceiverDeploymentLoader)
    
    static final String RCD_ID_COL = "RCD_ID"
    static final String RCV_ID_COL = "RCV_ID"
    static final String STA_ID_COL = "STA_ID"
    static final String RCD_DATETIME_IN_COL = "RCD_DATETIME_IN"
    static final String RCD_DATETIME_OUT_COL = "RCD_DATETIME_OUT"
    static final String RCD_INIT_DATETIME_COL = "RCD_INIT_DATETIME"
    static final String RCD_MOORING_DEPTH_COL = "RCD_MOORING_DEPTH"
    static final String RCD_LONGITUDE_COL = "RCD_LONGITUDE"
    static final String RCD_LATITUDE_COL = "RCD_LATITUDE"
    static final String RCD_OTHER_FAILURE_COL = "RCD_OTHER_FAILURE"
    
    void load(Map context, List<InputStream> streams) throws BulkImportException
    {
        def receiverDeploymentStream = streams[0]
        if (receiverDeploymentStream == null)
        {
            context.bulkImport.status = BulkImportStatus.ERROR
            throw new BulkImportException("Invalid receiver deployments data: data is empty.")
        }
        
        def records = getRecords(receiverDeploymentStream)
        
        log.info("Starting receiver deployment import...")
        
        records.each
        {
            processSingleRecord(context, it)
        }
    }
    
    private void processSingleRecord(Map context, Map record) throws BulkImportException
    {
        log.debug("Processing record: " + record)
        
        record.modifiedDate = DATE_TIME_FORMATTER.parseDateTime(record[MODIFIED_DATETIME_COL])
        
        def stationId = record[STA_ID_COL]
        if (   !stationId || (stationId == "")
            || (record[RCD_DATETIME_IN_COL] == null) || (record[RCD_DATETIME_IN_COL] == "")
            || (record[RCD_INIT_DATETIME_COL] == null) || (record[RCD_INIT_DATETIME_COL] == ""))
        {
            record.id = null
            record.status = BulkImportRecordType.INVALID
        }
        else
        {
            Point location = 
                new GeometryFactory().createPoint(
                    new Coordinate(
                        Float.valueOf(record[RCD_LONGITUDE_COL]), 
                        Float.valueOf(record[RCD_LATITUDE_COL])))
            location.setSRID(4326)
            
            def station = findStation(record)
            def receiver = findReceiver(record)
            
            def deploymentDateTime = DATE_TIME_FORMATTER.parseDateTime(record[RCD_DATETIME_IN_COL])
            record.deploymentDateTime = deploymentDateTime
            
            def initDateTime = DATE_TIME_FORMATTER.parseDateTime(record[RCD_INIT_DATETIME_COL])
            record.initDateTime = initDateTime
            
            def recoveryDateTime
            
            if (record[RCD_DATETIME_OUT_COL] && (record[RCD_DATETIME_OUT_COL] != ""))
            { 
                recoveryDateTime = DATE_TIME_FORMATTER.parseDateTime(record[RCD_DATETIME_OUT_COL])
                record.recoveryDateTime = recoveryDateTime
                
                if (recoveryDateTime.isBefore(deploymentDateTime))
                {
                    log.warn("Record ${record[RCD_ID_COL]}: recovery date (${recoveryDateTime}) is before deployment date (${deploymentDateTime})")
                }
            }
            
            log.debug("Loading deployment record: " + record)
            log.debug("station: " + station)
            log.debug("receiver: " + receiver)
            
            // New, update or ignore?
            List<BulkImportRecord> existingImportRecords = BulkImportRecord.findAllBySrcPkAndSrcTable(Integer.valueOf(record[RCD_ID_COL]), "RECEIVERDEPLOYMENTS", [sort: "srcModifiedDate", order: "desc"])
    
            if (existingImportRecords.isEmpty())
            {
                ReceiverDeployment deployment = 
                    new ReceiverDeployment(
                        station: station,
                        receiver: receiver,
                        deploymentNumber: 0,
                        initialisationDateTime: initDateTime,
                        deploymentDateTime: deploymentDateTime,
                        mooringType: MooringType.findByType("FIXED"),
                        bottomDepthM: record[RCD_MOORING_DEPTH_COL],
                        receiverOrientation: ReceiverOrientation.UP,
                        location: location)
                
                deployment.save(failOnError: true)
                record.deployment = deployment
                
                station.location = location
                station.save()
                record.station = station
                
                if (recoveryDateTime)
                {
                    createOrUpdateRecovery(record)
                }
                
                record.id = deployment.id
                record.status = BulkImportRecordType.NEW
            }
            else
            {
                BulkImportRecord lastImport = existingImportRecords[0]
                DateTime lastModifiedDate = lastImport.srcModifiedDate
                
                if (record.modifiedDate > lastModifiedDate)
                {
                    ReceiverDeployment existingDeployment = ReceiverDeployment.get(lastImport.dstPk)
                    existingDeployment.station = station
                    existingDeployment.receiver = receiver
                    existingDeployment.deploymentNumber = 0
                    existingDeployment.initialisationDateTime = initDateTime
                    existingDeployment.deploymentDateTime = deploymentDateTime
                    existingDeployment.mooringType = MooringType.findByType("FIXED")
                    
                    if (record[RCD_MOORING_DEPTH_COL] && record[RCD_MOORING_DEPTH_COL] != "")
                    {
                        existingDeployment.bottomDepthM = Float.valueOf(record[RCD_MOORING_DEPTH_COL])
                    }
                    
                    existingDeployment.receiverOrientation = ReceiverOrientation.UP
                    existingDeployment.location = location
                    existingDeployment.save(failOnError:true)
                    record.deployment = existingDeployment
                    
                    station.location = location
                    station.save()
                    record.station = station
                    
                    if (recoveryDateTime)
                    {
                        createOrUpdateRecovery(record)
                    }
                
                    // update
                    record.id = existingDeployment.id
                    record.status = BulkImportRecordType.UPDATED
                }
                else
                {
                    // ignore
                    record.id = null
                    record.status = BulkImportRecordType.IGNORED
                }
            }
        }
        
        BulkImportRecord importRecord =
            new BulkImportRecord(
                bulkImport: context.bulkImport,
                srcTable: "RECEIVERDEPLOYMENTS",
                srcPk: record[RCD_ID_COL],
                srcModifiedDate: record.modifiedDate,
                dstClass: "au.org.emii.aatams.ReceiverDeployment",
                dstPk: record.id,
                type: record.status)
            
        importRecord.save(failOnError: true)
    }
    
    private ReceiverRecovery createOrUpdateRecovery(Map record)
    {
        ReceiverRecovery recovery
        
        if (record.deployment.recovery)
        {
            recovery = record.deployment.recovery
        }
        else
        {
            recovery = new ReceiverRecovery()
        }
        
        recovery.deployment = record.deployment
        recovery.recoverer = ProjectRole.findByRoleTypeAndProject(ProjectRoleType.findByDisplayName(ProjectRoleType.PRINCIPAL_INVESTIGATOR), record.station.installation.project)
        recovery.recoveryDateTime = record.recoveryDateTime
        recovery.location = record.deployment.location
        recovery.status = DeviceStatus.findByStatus("RECOVERED")
        recovery.comments = record[RCD_OTHER_FAILURE_COL]
            
        recovery.save(failOnError: true)
    }
    private InstallationStation findStation(Map record)
    {
        Long stationId = Long.valueOf(record[STA_ID_COL])
        BulkImportRecord stationImportRecord = BulkImportRecord.findBySrcTableAndSrcPk("STATIONS", stationId)
        
        if (!stationImportRecord)
        {
            throw new BulkImportException("Unknown station ID in import: " + record[STA_ID_COL])    
        }
        
        return InstallationStation.get(stationImportRecord.dstPk)
    }
    
    private Receiver findReceiver(Map record)
    {
        Long receiverId = Long.valueOf(record[RCV_ID_COL])
        BulkImportRecord receiverImportRecord = BulkImportRecord.findBySrcTableAndSrcPk("RECEIVERS", receiverId)
        
        if (!receiverImportRecord)
        {
            throw new BulkImportException("Unknown receiver ID in import: " + record[RCV_ID_COL])
        }
        
        return Receiver.get(receiverImportRecord.dstPk)
    }
}
