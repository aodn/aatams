package au.org.emii.aatams.bulk

import org.apache.log4j.Logger
import org.grails.plugins.csv.CSVMapReader
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

import au.org.emii.aatams.Receiver
import au.org.emii.aatams.ReceiverDeviceModel

class ReceiverLoader extends AbstractLoader
{
    private static final Logger log = Logger.getLogger(ReceiverLoader)
    
    static final String RCV_ID_COL = "RCV_ID"
    static final String RCV_SERIAL_NO_COL = "RCV_SERIAL_NO"
    static final String RCV_MODEL_CODE_COL = "RCV_MODEL_CODE"
    static final String RCV_OWNER_COL = "RCV_OWNER"
    static final String RCV_COMMENTS_COL = "RCV_COMMENTS"
    static final String ENTRY_DATETIME_COL = "ENTRY_DATETIME"
    static final String ENTRY_BY_COL = "ENTRY_BY"
    
    static final List<String> HEADERS = 
        [RCV_ID_COL, RCV_SERIAL_NO_COL, RCV_MODEL_CODE_COL, 
         RCV_OWNER_COL, RCV_COMMENTS_COL, ENTRY_DATETIME_COL, 
         ENTRY_BY_COL, MODIFIED_DATETIME_COL, MODIFIED_BY_COL]
        
    void load(Map context, List<InputStream> streams) throws BulkImportException
    {
        def receiverStream = streams[0]
        if (receiverStream == null)
        {
            context.bulkImport.status = BulkImportStatus.ERROR
            throw new BulkImportException("Invalid receivers data: data is empty.")
        }
        
        def records = getRecords(receiverStream)
        
        log.info("Starting receiver import...")
        
        records.each
        {
            processSingleRecord(context, it)
        }
    }
    
    private void processSingleRecord(Map context, Map record) throws BulkImportException
    {
        log.debug("Processing record: " + record)
        
        validateRecord(context, record)
        createOrUpdateReceiver(context, record)
        createBulkImportRecord(context, record)
    }

    private void validateRecord(Map context, Map record) 
    {
        if (record.size() != HEADERS.size())
        {
            context.bulkImport.status = BulkImportStatus.ERROR
            throw new BulkImportException("Invalid receivers data format.")
        }

        def receiverModel = ReceiverDeviceModel.findByModelName(record[RCV_MODEL_CODE_COL])

        if (!receiverModel)
        {
            context.bulkImport.status = BulkImportStatus.ERROR
            throw new BulkImportException("Unknown model code: " + record[RCV_MODEL_CODE_COL])
        }
        
        // Store in the map for convenience.
        record.receiverModel = receiverModel
        record.modifiedDate = DATE_TIME_FORMATTER.parseDateTime(record[MODIFIED_DATETIME_COL])
    }
    
    private void createOrUpdateReceiver(Map context, Map record)
    {
        // New, update or ignore?
        List<BulkImportRecord> existingImportRecords = BulkImportRecord.findAllBySrcPkAndSrcTable(Integer.valueOf(record[RCV_ID_COL]), "RECEIVERS", [sort: "srcModifiedDate", order: "desc"])

        if (existingImportRecords.isEmpty())
        {
            def existingReceiver = Receiver.findBySerialNumber(record[RCV_SERIAL_NO_COL])
            if (existingReceiver)
            {
                // duplicate - this receiver has already be entered (manually) in to the
                // database.
                record.id = existingReceiver.id
                record.status = BulkImportRecordType.DUPLICATE
            }
            else
            {
                Receiver receiver =
                    new Receiver(
                        organisation: context.bulkImport.organisation,
                        model: record.receiverModel,
                        serialNumber: record[RCV_SERIAL_NO_COL],
                        comment: record[RCV_COMMENTS_COL])
                receiver.save(failOnError:true)
                record.id = receiver.id
                record.status = BulkImportRecordType.NEW
            }
        }
        else
        {
            BulkImportRecord lastImport = existingImportRecords[0]
            DateTime lastModifiedDate = lastImport.srcModifiedDate
            
            if (record.modifiedDate > lastModifiedDate)
            {
                Receiver existingReceiver = Receiver.get(lastImport.dstPk)
                existingReceiver.organisation = context.bulkImport.organisation
                existingReceiver.model = record.receiverModel
                existingReceiver.serialNumber = record[RCV_SERIAL_NO_COL]
                existingReceiver.comment = record[RCV_COMMENTS_COL]
                existingReceiver.save(failOnError:true)

                // update
                record.id = existingReceiver.id
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
    
    private void createBulkImportRecord(context, record)
    {
        BulkImportRecord importRecord = 
            new BulkImportRecord(
                bulkImport: context.bulkImport,
                srcTable: "RECEIVERS", 
                srcPk: record[RCV_ID_COL],
                srcModifiedDate: record.modifiedDate,
                dstClass: "au.org.emii.aatams.Receiver",
                dstPk: record.id,
                type: record.status)
            
        importRecord.save(failOnError: true)
    }
}
