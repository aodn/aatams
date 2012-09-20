package au.org.emii.aatams.bulk

import au.org.emii.aatams.*

import org.apache.log4j.Logger;

class AnimalReleaseLoader extends AbstractLoader 
{
	private static final Logger log = Logger.getLogger(AnimalReleaseLoader)
	
	static final String RCD_ID_COL = "RCD_ID"
	static final String RCV_ID_COL = "RCV_ID"
	static final String STA_ID_COL = "STA_ID"

	//"","ACO_PINGER_TYPE",
	//"","","ACO_ATTACHMENT","ACO_OWNER","ACO_FREQUENCY","ACO_TEMP_SENSOR_YN","ACO_TEMP_CODE_SPACE","ACO_TEMP_PINGER_ID","ACO_TEMP_SLOPE","ACO_TEMP_INTERCEPT","ACO_PRES_SENSOR_YN","ACO_PRES_CODE_SPACE","ACO_PRES_PINGER_ID","ACO_PRES_SLOPE","ACO_PRES_INTERCEPT","ACO_TRANSMIT_INT_MIN","ACO_TRANSMIT_INT_MAX"
	
	static final String ACO_ID_COL = "ACO_ID"
	static final String ACO_SERIAL_NUMBER_COL = "ACO_SERIAL_NUMBER"
	static final String ACO_PINGER_CODE_ID_COL = "ACO_PINGER_CODE_ID"
	static final String ACO_CODE_SPACE_COL = "ACO_CODE_SPACE"
	static final String ACO_MANUFACTURER_COL = "ACO_MANUFACTURER"
	
	void load(Map context, List<InputStream> streams) throws BulkImportException
	{
		def releaseStream = streams[0]
		if (releaseStream == null)
		{
			context.bulkImport.status = BulkImportStatus.ERROR
			throw new BulkImportException("Invalid release data: data is empty.")
		}
		
		def records = getRecords(releaseStream)
		
		log.info("Starting release import...")
		
		records.each
		{
			processSingleRecord(context, it)
		}
	}
	
	private void processSingleRecord(Map context, Map record) throws BulkImportException
	{
		log.debug("Processing record: " + record)

		def serialNumber = record[ACO_SERIAL_NUMBER_COL]
		
		if (Tag.findBySerialNumber(serialNumber))
		{
			log.warn("Tag with serial number ${serialNumber} already exists.")
			return
		}
		
		Tag tag = new Tag(
			model: getModel(record[ACO_MANUFACTURER_COL]),
			serialNumber: serialNumber,
			
			status: DeviceStatus.findByStatus(DeviceStatus.NEW, [cache: true]),
//			project: 
			codeMap: getCodeMap(record[ACO_CODE_SPACE_COL]))
		tag.save(failOnError: true)
		
		Sensor pinger = new Sensor(
			tag: tag,
			pingCode: record[ACO_PINGER_CODE_ID_COL],
			transmitterType: TransmitterType.findByTransmitterTypeName("PINGER", [cache: true]))
		pinger.save(failOnError: true)	
		
		BulkImportRecord importRecord =
			new BulkImportRecord(
				bulkImport: context.bulkImport,
				srcTable: "RELEASES",
				srcPk: record[ACO_ID_COL],
				dstClass: "au.org.emii.aatams.Tag",
				dstPk: tag.id,
				type: BulkImportRecordType.NEW)
		
		importRecord.save(failOnError: true)
	}
	
	private TagDeviceModel getModel(modelAsString)
	{
		if (modelAsString == "Vemco 16mm")
		{
			return TagDeviceModel.findByModelName("V16")
		}
		
		assert(false): "Unkown model: " + modelAsString
	}
	
	private CodeMap getCodeMap(codeMapAsString)
	{
		if (codeMapAsString == "R64K (Sync=320,Bin=20)")
		{
			return CodeMap.findByCodeMap("A69-1303")
		}
		
		assert(false): "Unkown code map: " + codeMapAsString
	}
}
