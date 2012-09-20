package au.org.emii.aatams.bulk

import grails.test.*

import au.org.emii.aatams.*

class AnimalReleaseLoaderTests extends AbstractLoaderTests
{
	def a69_1303
	def newStatus
	def v16
	def pingerType
	
    protected void setUp() 
	{
        super.setUp()
		
		loader = new AnimalReleaseLoader()
		
		a69_1303 = new CodeMap(codeMap: "A69-1303")
		mockDomain(CodeMap, [a69_1303])
		[a69_1303].each { it.save() }
		
		newStatus = new DeviceStatus(status: DeviceStatus.NEW)
		mockDomain(DeviceStatus, [newStatus])
		[newStatus].each { it.save() }
		
		mockDomain(Sensor)
		mockDomain(Tag)

		v16 = new TagDeviceModel(modelName: "V16", manufacturer: new DeviceManufacturer())
		mockDomain(TagDeviceModel, [v16])
		[v16].each {
			it.save()
		}
		
		pingerType = new TransmitterType(transmitterTypeName: "PINGER")
		mockDomain(TransmitterType, [pingerType])
		[pingerType].each {
			it.save()
		}
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testCreateTag() 
	{
		assertEquals(0, Tag.count())
		
		def releasesText = '''"ACO_ID","ACO_SERIAL_NUMBER","ACO_PINGER_CODE_ID","ACO_PINGER_TYPE","ACO_CODE_SPACE","ACO_MANUFACTURER","ACO_ATTACHMENT","ACO_OWNER","ACO_FREQUENCY","ACO_TEMP_SENSOR_YN","ACO_TEMP_CODE_SPACE","ACO_TEMP_PINGER_ID","ACO_TEMP_SLOPE","ACO_TEMP_INTERCEPT","ACO_PRES_SENSOR_YN","ACO_PRES_CODE_SPACE","ACO_PRES_PINGER_ID","ACO_PRES_SLOPE","ACO_PRES_INTERCEPT","ACO_TRANSMIT_INT_MIN","ACO_TRANSMIT_INT_MAX","REL_ID","REL_DATE","TIME","REL_TYPE","SPC_CAAB_CODE","SPC_COMMON_NAME","REL_LENGTH","REL_LENGTH_QUAL","REL_LENGTH_TYPE","REL_SEX","REL_COHORT_AGE","REL_LATITUDE","REL_LONGITUDE","TAG_ID","TAG_FIRST_NAME","TAG_FAMILY_NAME","VSL_ID","VSL_NAME","REL_CAPTURE","REL_FISH_COND","REL_INJ","REL_INJ_SEVERITY","REL_TIME_OOW","REL_STRONT_DOSE","REL_ABIOT_USED","REL_ABIOT_DOSE","REL_ASEPT_USED","REL_SCHOOL_NUM","REL_COUNT","REL_OBS_PROG_YN","NOTES"
904,1078955,62347,"Coded Pinger","R64K (Sync=320,Bin=20)","Vemco 16mm","External","Bruce\\R-00656-02-015",69,0,,,,,0,,,,,50,130,149291,10/12/2009 0:00:00,"22:15","into the wild",37010003,"White Shark",420,"guessed","Total Length","male",,-35.23,136.07,414,"Barry","Bruce",15365,"CALYPSO STAR","Shark Cage Dive","good condition","other","slight injury",0,,,,,,,,"Date and time are in UTC"
'''
		assertSuccess(
			[releasesText], 
			[["type": BulkImportRecordType.NEW, 
			 "srcPk": 904, 
			 "srcTable": "RELEASES", 
			 "srcModifiedDate": null,
			 "dstClass": "au.org.emii.aatams.Tag"]])
		
		def pinger = Sensor.findByPingCode(62347)
		assertNotNull(pinger)
		assertEquals(pingerType, pinger.transmitterType)
		
		def tag = pinger.tag
		assertNotNull(tag)
		assertEquals(a69_1303, tag.codeMap)
		assertEquals(newStatus, tag.status)
		assertEquals(v16, tag.model)
		
		assertEquals(1, Tag.count())
    }
	
	void testExistingTag()
	{
		Tag tag = new Tag(
			model: v16,
			serialNumber: "1078955",
			status: newStatus,
			codeMap: a69_1303)
		tag.save(failOnError: true)
		
		Sensor pinger = new Sensor(
			tag: tag,
			pingCode: 62347,
			transmitterType: pingerType)
		pinger.save(failOnError: true)
		
		tag.addToSensors(pinger)
		
		assertEquals(1, Tag.count())
		
		def releasesText = '''"ACO_ID","ACO_SERIAL_NUMBER","ACO_PINGER_CODE_ID","ACO_PINGER_TYPE","ACO_CODE_SPACE","ACO_MANUFACTURER","ACO_ATTACHMENT","ACO_OWNER","ACO_FREQUENCY","ACO_TEMP_SENSOR_YN","ACO_TEMP_CODE_SPACE","ACO_TEMP_PINGER_ID","ACO_TEMP_SLOPE","ACO_TEMP_INTERCEPT","ACO_PRES_SENSOR_YN","ACO_PRES_CODE_SPACE","ACO_PRES_PINGER_ID","ACO_PRES_SLOPE","ACO_PRES_INTERCEPT","ACO_TRANSMIT_INT_MIN","ACO_TRANSMIT_INT_MAX","REL_ID","REL_DATE","TIME","REL_TYPE","SPC_CAAB_CODE","SPC_COMMON_NAME","REL_LENGTH","REL_LENGTH_QUAL","REL_LENGTH_TYPE","REL_SEX","REL_COHORT_AGE","REL_LATITUDE","REL_LONGITUDE","TAG_ID","TAG_FIRST_NAME","TAG_FAMILY_NAME","VSL_ID","VSL_NAME","REL_CAPTURE","REL_FISH_COND","REL_INJ","REL_INJ_SEVERITY","REL_TIME_OOW","REL_STRONT_DOSE","REL_ABIOT_USED","REL_ABIOT_DOSE","REL_ASEPT_USED","REL_SCHOOL_NUM","REL_COUNT","REL_OBS_PROG_YN","NOTES"
904,1078955,62347,"Coded Pinger","R64K (Sync=320,Bin=20)","Vemco 16mm","External","Bruce\\R-00656-02-015",69,0,,,,,0,,,,,50,130,149291,10/12/2009 0:00:00,"22:15","into the wild",37010003,"White Shark",420,"guessed","Total Length","male",,-35.23,136.07,414,"Barry","Bruce",15365,"CALYPSO STAR","Shark Cage Dive","good condition","other","slight injury",0,,,,,,,,"Date and time are in UTC"
'''
		assertSuccess(
			[releasesText], 
			[])
		
		assertEquals(1, Tag.count())
	}

//    void testCreateTagWithPressure() 
//    void testCreateTagWithTemp() 
//	void testCreateTagWithTempAndPressure()
//	void testCreateTagWithTempAndPressureNoPinger()
//	
}