package au.org.emii.aatams.bulk

import grails.test.*

import au.org.emii.aatams.*

class AnimalReleaseLoaderTests extends AbstractLoaderTests
{
	def a69_1303
	def newStatus
	def v16
	def pingerType
	def pressureType
	
	def male
	def female
	
	def whiteShark
	
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
		pressureType = new TransmitterType(transmitterTypeName: "PRESSURE")
		mockDomain(TransmitterType, [pingerType, pressureType])
		[pingerType, pressureType].each {
			it.save()
		}
		
		male = new Sex(sex: "MALE")
		female = new Sex(sex: "FEMALE")
		mockDomain(Sex, [male, female])
		[male, female].each {
			it.save()
		}
		
		whiteShark = new CaabSpecies(spcode: '37010003')
		mockDomain(CaabSpecies, [whiteShark])
		whiteShark.save()
		
		def lineCapture = new CaptureMethod(name: 'LINE')
		mockDomain(CaptureMethod, [lineCapture])
		lineCapture.save()
		
		def noAnesthetic = new SurgeryTreatmentType( type: 'NO ANESTHETIC')
		mockDomain(SurgeryTreatmentType, [noAnesthetic])
		noAnesthetic.save()
		
		def internal = new SurgeryType(type: "INTERNAL")
		def external = new SurgeryType(type: "EXTERNAL")
		mockDomain(SurgeryType, [internal, external])
		[internal, external].each { it.save(failOnError: true) }
		
		def totalLength = new AnimalMeasurementType(type: "TOTAL LENGTH")
		mockDomain(AnimalMeasurementType, [totalLength])
		totalLength.save()
		
		def cm = new MeasurementUnit(unit: "cm")
		mockDomain(MeasurementUnit, [cm])
		cm.save()
					
		mockDomain(Animal)
		mockDomain(AnimalMeasurement)
		mockDomain(AnimalRelease)
		mockDomain(Project)
		mockDomain(Surgery)
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testCreateTag() 
	{
		assertEquals(0, Tag.count())
		assertEquals(0, Project.count())
		
		def releasesText = '''"ACO_ID","ACO_SERIAL_NUMBER","ACO_PINGER_CODE_ID","ACO_PINGER_TYPE","ACO_CODE_SPACE","ACO_MANUFACTURER","ACO_ATTACHMENT","ACO_OWNER","ACO_FREQUENCY","ACO_TEMP_SENSOR_YN","ACO_TEMP_CODE_SPACE","ACO_TEMP_PINGER_ID","ACO_TEMP_SLOPE","ACO_TEMP_INTERCEPT","ACO_PRES_SENSOR_YN","ACO_PRES_CODE_SPACE","ACO_PRES_PINGER_ID","ACO_PRES_SLOPE","ACO_PRES_INTERCEPT","ACO_TRANSMIT_INT_MIN","ACO_TRANSMIT_INT_MAX","REL_ID","REL_DATE","TIME","REL_TYPE","SPC_CAAB_CODE","SPC_COMMON_NAME","REL_LENGTH","REL_LENGTH_QUAL","REL_LENGTH_TYPE","REL_SEX","REL_COHORT_AGE","REL_LATITUDE","REL_LONGITUDE","TAG_ID","TAG_FIRST_NAME","TAG_FAMILY_NAME","VSL_ID","VSL_NAME","REL_CAPTURE","REL_FISH_COND","REL_INJ","REL_INJ_SEVERITY","REL_TIME_OOW","REL_STRONT_DOSE","REL_ABIOT_USED","REL_ABIOT_DOSE","REL_ASEPT_USED","REL_SCHOOL_NUM","REL_COUNT","REL_OBS_PROG_YN","NOTES"
904,1078955,62347,"Coded Pinger","R64K (Sync=320,Bin=20)","Vemco 16mm","External","Bruce",69,0,,,,,0,,,,,50,130,149291,10/12/2009 0:00:00,"22:15","into the wild",37010003,"White Shark",420,"guessed","Total Length","male",,-35.23,136.07,414,"Barry","Bruce",15365,"CALYPSO STAR","Shark Cage Dive","good condition","other","slight injury",0,,,,,,,,"Date and time are in UTC"
'''
		load([releasesText])
		
		def pinger = Sensor.findByPingCode(62347)
		assertNotNull(pinger)
		assertEquals(pingerType, pinger.transmitterType)
		
		def tag = pinger.tag
		assertNotNull(tag)
		assertEquals(a69_1303, tag.codeMap)
		assertEquals(newStatus, tag.status)
		assertEquals(v16, tag.model)
		assertEquals(1, Sensor.findAllByTag(tag).size())
		
		def project = tag.project
		assertEquals("Bruce", project.name)
		
		assertEquals(1, Tag.count())
		assertEquals(1, Project.count())
    }
	
	void testExistingTagProject()
	{
		Project project = new Project(name: "Bruce")
		project.save(failOnError: true)
		
		Tag tag = new Tag(
			project: project,
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
904,1078955,62347,"Coded Pinger","R64K (Sync=320,Bin=20)","Vemco 16mm","External","Bruce",69,0,,,,,0,,,,,50,130,149291,10/12/2009 0:00:00,"22:15","into the wild",37010003,"White Shark",420,"guessed","Total Length","male",,-35.23,136.07,414,"Barry","Bruce",15365,"CALYPSO STAR","Shark Cage Dive","good condition","other","slight injury",0,,,,,,,,"Date and time are in UTC"
'''
		load([releasesText])
		
		assertEquals(1, Tag.count())
		assertEquals(1, Project.count())
	}

//    void testCreateTagWithPressure() 
//	{
//		assertEquals(0, Tag.count())
//		
//		def releasesText = '''"ACO_ID","ACO_SERIAL_NUMBER","ACO_PINGER_CODE_ID","ACO_PINGER_TYPE","ACO_CODE_SPACE","ACO_MANUFACTURER","ACO_ATTACHMENT","ACO_OWNER","ACO_FREQUENCY","ACO_TEMP_SENSOR_YN","ACO_TEMP_CODE_SPACE","ACO_TEMP_PINGER_ID","ACO_TEMP_SLOPE","ACO_TEMP_INTERCEPT","ACO_PRES_SENSOR_YN","ACO_PRES_CODE_SPACE","ACO_PRES_PINGER_ID","ACO_PRES_SLOPE","ACO_PRES_INTERCEPT","ACO_TRANSMIT_INT_MIN","ACO_TRANSMIT_INT_MAX","REL_ID","REL_DATE","TIME","REL_TYPE","SPC_CAAB_CODE","SPC_COMMON_NAME","REL_LENGTH","REL_LENGTH_QUAL","REL_LENGTH_TYPE","REL_SEX","REL_COHORT_AGE","REL_LATITUDE","REL_LONGITUDE","TAG_ID","TAG_FIRST_NAME","TAG_FAMILY_NAME","VSL_ID","VSL_NAME","REL_CAPTURE","REL_FISH_COND","REL_INJ","REL_INJ_SEVERITY","REL_TIME_OOW","REL_STRONT_DOSE","REL_ABIOT_USED","REL_ABIOT_DOSE","REL_ASEPT_USED","REL_SCHOOL_NUM","REL_COUNT","REL_OBS_PROG_YN","NOTES"
//497,5791,242,"Coded Pinger","S256 (Sync=360,Bin=20)","Vemco 16mm","Internal","Al Hobday",69,0,,,,,-1,,,0.69,-8.38,20,69,146417,4/12/2006 0:00:00,"","into the wild",37441004,"Southern Bluefin Tuna",58,"correctly measured","Length to Caudal Fork",,0,-34.57,118.09,99,"Alistair","Hobday",14337,"QUADRANT","Pole and line","good condition",,"slight injury",,,,,,,,,
//'''
//		assertSuccess(
//			[releasesText], 
//			[["type": BulkImportRecordType.NEW, 
//			 "srcPk": 497, 
//			 "srcTable": "RELEASES", 
//			 "srcModifiedDate": null,
//			 "dstClass": "au.org.emii.aatams.Tag"]])
//		
//		def pressureSensor = Sensor.findByPingCode(242)
//		assertNotNull(pressureSensor)
//		assertEquals(pressureType, pressureSensor.transmitterType)
//		
//		def tag = pressureSensor.tag
//		assertNotNull(tag)
//		assertEquals(a69_1303, tag.codeMap)
//		assertEquals(newStatus, tag.status)
//		assertEquals(v16, tag.model)
//		assertEquals(1, Sensor.findAllByTag(tag).size())
//		
//		assertEquals(1, Tag.count())
//	}
	
	void testCreateAnimalUnknownCaabCode()
	{
		def releasesText = '''"ACO_ID","ACO_SERIAL_NUMBER","ACO_PINGER_CODE_ID","ACO_PINGER_TYPE","ACO_CODE_SPACE","ACO_MANUFACTURER","ACO_ATTACHMENT","ACO_OWNER","ACO_FREQUENCY","ACO_TEMP_SENSOR_YN","ACO_TEMP_CODE_SPACE","ACO_TEMP_PINGER_ID","ACO_TEMP_SLOPE","ACO_TEMP_INTERCEPT","ACO_PRES_SENSOR_YN","ACO_PRES_CODE_SPACE","ACO_PRES_PINGER_ID","ACO_PRES_SLOPE","ACO_PRES_INTERCEPT","ACO_TRANSMIT_INT_MIN","ACO_TRANSMIT_INT_MAX","REL_ID","REL_DATE","TIME","REL_TYPE","SPC_CAAB_CODE","SPC_COMMON_NAME","REL_LENGTH","REL_LENGTH_QUAL","REL_LENGTH_TYPE","REL_SEX","REL_COHORT_AGE","REL_LATITUDE","REL_LONGITUDE","TAG_ID","TAG_FIRST_NAME","TAG_FAMILY_NAME","VSL_ID","VSL_NAME","REL_CAPTURE","REL_FISH_COND","REL_INJ","REL_INJ_SEVERITY","REL_TIME_OOW","REL_STRONT_DOSE","REL_ABIOT_USED","REL_ABIOT_DOSE","REL_ASEPT_USED","REL_SCHOOL_NUM","REL_COUNT","REL_OBS_PROG_YN","NOTES"
904,1078955,62347,"Coded Pinger","R64K (Sync=320,Bin=20)","Vemco 16mm","External","Bruce\\R-00656-02-015",69,0,,,,,0,,,,,50,130,149291,10/12/2009 0:00:00,"22:15","into the wild",123,"White Shark",420,"guessed","Total Length","male",,-35.23,136.07,414,"Barry","Bruce",15365,"CALYPSO STAR","Shark Cage Dive","good condition","other","slight injury",0,,,,,,,,"Date and time are in UTC"
'''
		try
		{
			load([releasesText])
			fail()
		}
		catch (BulkImportException e)
		{
			assertEquals("Unknown CAAB code: 123", e.message)
		}
	}
	
	void testCreateAnimalUnknownSex()
	{
		def releasesText = '''"ACO_ID","ACO_SERIAL_NUMBER","ACO_PINGER_CODE_ID","ACO_PINGER_TYPE","ACO_CODE_SPACE","ACO_MANUFACTURER","ACO_ATTACHMENT","ACO_OWNER","ACO_FREQUENCY","ACO_TEMP_SENSOR_YN","ACO_TEMP_CODE_SPACE","ACO_TEMP_PINGER_ID","ACO_TEMP_SLOPE","ACO_TEMP_INTERCEPT","ACO_PRES_SENSOR_YN","ACO_PRES_CODE_SPACE","ACO_PRES_PINGER_ID","ACO_PRES_SLOPE","ACO_PRES_INTERCEPT","ACO_TRANSMIT_INT_MIN","ACO_TRANSMIT_INT_MAX","REL_ID","REL_DATE","TIME","REL_TYPE","SPC_CAAB_CODE","SPC_COMMON_NAME","REL_LENGTH","REL_LENGTH_QUAL","REL_LENGTH_TYPE","REL_SEX","REL_COHORT_AGE","REL_LATITUDE","REL_LONGITUDE","TAG_ID","TAG_FIRST_NAME","TAG_FAMILY_NAME","VSL_ID","VSL_NAME","REL_CAPTURE","REL_FISH_COND","REL_INJ","REL_INJ_SEVERITY","REL_TIME_OOW","REL_STRONT_DOSE","REL_ABIOT_USED","REL_ABIOT_DOSE","REL_ASEPT_USED","REL_SCHOOL_NUM","REL_COUNT","REL_OBS_PROG_YN","NOTES"
904,1078955,62347,"Coded Pinger","R64K (Sync=320,Bin=20)","Vemco 16mm","External","Bruce\\R-00656-02-015",69,0,,,,,0,,,,,50,130,149291,10/12/2009 0:00:00,"22:15","into the wild",37010003,"White Shark",420,"guessed","Total Length","aaa",,-35.23,136.07,414,"Barry","Bruce",15365,"CALYPSO STAR","Shark Cage Dive","good condition","other","slight injury",0,,,,,,,,"Date and time are in UTC"
'''
		try
		{
			load([releasesText])
			fail()
		}
		catch (BulkImportException e)
		{
			assertEquals("Unknown sex: aaa", e.message)
		}
	}
	
	void testCreateAnimal()
	{
		assertEquals(0, Animal.count())
		
		def releasesText = '''"ACO_ID","ACO_SERIAL_NUMBER","ACO_PINGER_CODE_ID","ACO_PINGER_TYPE","ACO_CODE_SPACE","ACO_MANUFACTURER","ACO_ATTACHMENT","ACO_OWNER","ACO_FREQUENCY","ACO_TEMP_SENSOR_YN","ACO_TEMP_CODE_SPACE","ACO_TEMP_PINGER_ID","ACO_TEMP_SLOPE","ACO_TEMP_INTERCEPT","ACO_PRES_SENSOR_YN","ACO_PRES_CODE_SPACE","ACO_PRES_PINGER_ID","ACO_PRES_SLOPE","ACO_PRES_INTERCEPT","ACO_TRANSMIT_INT_MIN","ACO_TRANSMIT_INT_MAX","REL_ID","REL_DATE","TIME","REL_TYPE","SPC_CAAB_CODE","SPC_COMMON_NAME","REL_LENGTH","REL_LENGTH_QUAL","REL_LENGTH_TYPE","REL_SEX","REL_COHORT_AGE","REL_LATITUDE","REL_LONGITUDE","TAG_ID","TAG_FIRST_NAME","TAG_FAMILY_NAME","VSL_ID","VSL_NAME","REL_CAPTURE","REL_FISH_COND","REL_INJ","REL_INJ_SEVERITY","REL_TIME_OOW","REL_STRONT_DOSE","REL_ABIOT_USED","REL_ABIOT_DOSE","REL_ASEPT_USED","REL_SCHOOL_NUM","REL_COUNT","REL_OBS_PROG_YN","NOTES"
904,1078955,62347,"Coded Pinger","R64K (Sync=320,Bin=20)","Vemco 16mm","External","Bruce\\R-00656-02-015",69,0,,,,,0,,,,,50,130,149291,10/12/2009 0:00:00,"22:15","into the wild",37010003,"White Shark",420,"guessed","Total Length","male",,-35.23,136.07,414,"Barry","Bruce",15365,"CALYPSO STAR","Shark Cage Dive","good condition","other","slight injury",0,,,,,,,,"Date and time are in UTC"
'''
		load([releasesText])
		
		def whiteSharkAnimal = Animal.findBySpecies(CaabSpecies.findBySpcode('37010003'))
		assertEquals(male, whiteSharkAnimal.sex)

		assertEquals(1, Animal.count())
	}
	
	void testCreateAnimalRelease()
	{
		assertEquals(0, AnimalRelease.count())
		
		def releasesText = '''"ACO_ID","ACO_SERIAL_NUMBER","ACO_PINGER_CODE_ID","ACO_PINGER_TYPE","ACO_CODE_SPACE","ACO_MANUFACTURER","ACO_ATTACHMENT","ACO_OWNER","ACO_FREQUENCY","ACO_TEMP_SENSOR_YN","ACO_TEMP_CODE_SPACE","ACO_TEMP_PINGER_ID","ACO_TEMP_SLOPE","ACO_TEMP_INTERCEPT","ACO_PRES_SENSOR_YN","ACO_PRES_CODE_SPACE","ACO_PRES_PINGER_ID","ACO_PRES_SLOPE","ACO_PRES_INTERCEPT","ACO_TRANSMIT_INT_MIN","ACO_TRANSMIT_INT_MAX","REL_ID","REL_DATE","TIME","REL_TYPE","SPC_CAAB_CODE","SPC_COMMON_NAME","REL_LENGTH","REL_LENGTH_QUAL","REL_LENGTH_TYPE","REL_SEX","REL_COHORT_AGE","REL_LATITUDE","REL_LONGITUDE","TAG_ID","TAG_FIRST_NAME","TAG_FAMILY_NAME","VSL_ID","VSL_NAME","REL_CAPTURE","REL_FISH_COND","REL_INJ","REL_INJ_SEVERITY","REL_TIME_OOW","REL_STRONT_DOSE","REL_ABIOT_USED","REL_ABIOT_DOSE","REL_ASEPT_USED","REL_SCHOOL_NUM","REL_COUNT","REL_OBS_PROG_YN","NOTES"
904,1078955,62347,"Coded Pinger","R64K (Sync=320,Bin=20)","Vemco 16mm","External","Bruce\\R-00656-02-015",69,0,,,,,0,,,,,50,130,149291,10/12/2009 0:00:00,"22:15","into the wild",37010003,"White Shark",420,"guessed","Total Length","male",,-35.23,136.07,414,"Barry","Bruce",15365,"CALYPSO STAR","Shark Cage Dive","good condition","other","slight injury",0,,,,,,,,"Date and time are in UTC"
'''
		load([releasesText])
		
		def whiteSharkAnimal = Animal.findBySpecies(CaabSpecies.findBySpcode('37010003'))
		def release = AnimalRelease.findByAnimal(animal: whiteSharkAnimal)
		
		assertEquals(1, AnimalRelease.count())
	}
	
	void testCreateSurgery()
	{
		assertEquals(0, Surgery.count())
		
		def releasesText = '''"ACO_ID","ACO_SERIAL_NUMBER","ACO_PINGER_CODE_ID","ACO_PINGER_TYPE","ACO_CODE_SPACE","ACO_MANUFACTURER","ACO_ATTACHMENT","ACO_OWNER","ACO_FREQUENCY","ACO_TEMP_SENSOR_YN","ACO_TEMP_CODE_SPACE","ACO_TEMP_PINGER_ID","ACO_TEMP_SLOPE","ACO_TEMP_INTERCEPT","ACO_PRES_SENSOR_YN","ACO_PRES_CODE_SPACE","ACO_PRES_PINGER_ID","ACO_PRES_SLOPE","ACO_PRES_INTERCEPT","ACO_TRANSMIT_INT_MIN","ACO_TRANSMIT_INT_MAX","REL_ID","REL_DATE","TIME","REL_TYPE","SPC_CAAB_CODE","SPC_COMMON_NAME","REL_LENGTH","REL_LENGTH_QUAL","REL_LENGTH_TYPE","REL_SEX","REL_COHORT_AGE","REL_LATITUDE","REL_LONGITUDE","TAG_ID","TAG_FIRST_NAME","TAG_FAMILY_NAME","VSL_ID","VSL_NAME","REL_CAPTURE","REL_FISH_COND","REL_INJ","REL_INJ_SEVERITY","REL_TIME_OOW","REL_STRONT_DOSE","REL_ABIOT_USED","REL_ABIOT_DOSE","REL_ASEPT_USED","REL_SCHOOL_NUM","REL_COUNT","REL_OBS_PROG_YN","NOTES"
904,1078955,62347,"Coded Pinger","R64K (Sync=320,Bin=20)","Vemco 16mm","External","Bruce",69,0,,,,,0,,,,,50,130,149291,10/12/2009 0:00:00,"22:15","into the wild",37010003,"White Shark",420,"guessed","Total Length","male",,-35.23,136.07,414,"Barry","Bruce",15365,"CALYPSO STAR","Shark Cage Dive","good condition","other","slight injury",0,,,,,,,,"Date and time are in UTC"
'''
		load([releasesText])
		
		
		assertEquals(1, Surgery.count())
	}

	void testCreateMeasurement()
	{
		assertEquals(0, AnimalMeasurement.count())
		
		def releasesText = '''"ACO_ID","ACO_SERIAL_NUMBER","ACO_PINGER_CODE_ID","ACO_PINGER_TYPE","ACO_CODE_SPACE","ACO_MANUFACTURER","ACO_ATTACHMENT","ACO_OWNER","ACO_FREQUENCY","ACO_TEMP_SENSOR_YN","ACO_TEMP_CODE_SPACE","ACO_TEMP_PINGER_ID","ACO_TEMP_SLOPE","ACO_TEMP_INTERCEPT","ACO_PRES_SENSOR_YN","ACO_PRES_CODE_SPACE","ACO_PRES_PINGER_ID","ACO_PRES_SLOPE","ACO_PRES_INTERCEPT","ACO_TRANSMIT_INT_MIN","ACO_TRANSMIT_INT_MAX","REL_ID","REL_DATE","TIME","REL_TYPE","SPC_CAAB_CODE","SPC_COMMON_NAME","REL_LENGTH","REL_LENGTH_QUAL","REL_LENGTH_TYPE","REL_SEX","REL_COHORT_AGE","REL_LATITUDE","REL_LONGITUDE","TAG_ID","TAG_FIRST_NAME","TAG_FAMILY_NAME","VSL_ID","VSL_NAME","REL_CAPTURE","REL_FISH_COND","REL_INJ","REL_INJ_SEVERITY","REL_TIME_OOW","REL_STRONT_DOSE","REL_ABIOT_USED","REL_ABIOT_DOSE","REL_ASEPT_USED","REL_SCHOOL_NUM","REL_COUNT","REL_OBS_PROG_YN","NOTES"
904,1078955,62347,"Coded Pinger","R64K (Sync=320,Bin=20)","Vemco 16mm","External","Bruce",69,0,,,,,0,,,,,50,130,149291,10/12/2009 0:00:00,"22:15","into the wild",37010003,"White Shark",420,"guessed","Total Length","male",,-35.23,136.07,414,"Barry","Bruce",15365,"CALYPSO STAR","Shark Cage Dive","good condition","other","slight injury",0,,,,,,,,"Date and time are in UTC"
'''
		load([releasesText])
		
		
		assertEquals(1, AnimalMeasurement.count())
	}
}