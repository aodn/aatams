package au.org.emii.aatams.bulk

import grails.test.*

import au.org.emii.aatams.*

class AnimalReleaseLoaderTests extends AbstractLoaderTests
{
    def a69_1303
    def v16, v13, v9, v8
    def pingerType
    def pressureType

    def male
    def female
    def unknown

    def whiteShark

    def bruceProject
    def nretaProject
    def bluefinTunaProject
    def daleyProject

    def expectedCaptureMapping

    protected void setUp()
    {
        super.setUp()

        loader = new AnimalReleaseLoader()

        a69_1303 = new CodeMap(codeMap: "A69-1303")
        mockDomain(CodeMap, [a69_1303])
        [a69_1303].each { it.save() }

        mockDomain(Sensor)
        mockDomain(Tag)

        v16 = new TagDeviceModel(modelName: "V16", manufacturer: new DeviceManufacturer())
        v13 = new TagDeviceModel(modelName: "V13", manufacturer: new DeviceManufacturer())
        v9 = new TagDeviceModel(modelName: "V9", manufacturer: new DeviceManufacturer())
        v8 = new TagDeviceModel(modelName: "V8", manufacturer: new DeviceManufacturer())
        mockDomain(TagDeviceModel, [v16, v13, v9, v8])

        [v16, v13, v9, v8].each {
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
        unknown = new Sex(sex: "UNKNOWN")
        mockDomain(Sex, [male, female, unknown])
        [male, female, unknown].each {
            it.save()
        }

        loadCaabCodes()

        def noAnesthetic = new SurgeryTreatmentType( type: 'NO ANESTHETIC')
        mockDomain(SurgeryTreatmentType, [noAnesthetic])
        noAnesthetic.save()

        def internal = new SurgeryType(type: "INTERNAL")
        def external = new SurgeryType(type: "EXTERNAL")
        mockDomain(SurgeryType, [internal, external])
        [internal, external].each { it.save(failOnError: true) }

        def totalLength = new AnimalMeasurementType(type: "TOTAL LENGTH")
        def forkLength = new AnimalMeasurementType(type: "FORK LENGTH")
        mockDomain(AnimalMeasurementType, [totalLength, forkLength])
        [totalLength, forkLength].each { it.save() }

        def cm = new MeasurementUnit(unit: "cm")
        mockDomain(MeasurementUnit, [cm])
        cm.save()

        bruceProject = new Project(name: "CSIRO: Shark Monitoring Network")
        nretaProject = new Project(name: "Ningaloo Reef Ecosystem Tracking Array (NRETA)")
        bluefinTunaProject = new Project(name: "Bluefin tuna WA")
        daleyProject = new Project(name: "CSIRO: Ross Daley")

        def projectList = [bruceProject, nretaProject, bluefinTunaProject, daleyProject]
        mockDomain(Project, projectList)
        projectList.each { it.save() }

        mockDomain(CaptureMethod)
        expectedCaptureMapping = [
            "Shark Cage Dive": "FREE-SWIMMING",
            "": "OTHER",
            "Other/unknown": "OTHER",
            "Handlining": "HAND CAPTURE",
            "Trolling": "TRAWL",
            "Pole and line": "POLE AND LINE"]

        // Create capture methods...
        expectedCaptureMapping.values().each {
            def captureMethod = new CaptureMethod(name: it)
            captureMethod.save()
        }

        mockDomain(Animal)
        mockDomain(AnimalMeasurement)
        mockDomain(AnimalRelease)
        mockDomain(Surgery)
    }

    private void loadCaabCodes()
    {
        def codesAsString =
            '''37010003
            37311010
            37351008
            37337037
            37346027
            37018036
            37018033
            37311057
            37361001
            37311166
            37351013
            37386027
            37437031
            37386030
            37384090
            37018030
            37014001
            37311022
            37311078
            37311012
            37386008
            37386001
            37018029
            37018034
            37035011
            37027010
            37018022
            37035027
            37335001
            37018038
            37018039
            37441004'''.split('\n').collect { it.trim() }



        def speciesList = codesAsString.collect {
            new CaabSpecies(spcode: it)
        }
        whiteShark = speciesList[0]

        mockDomain(CaabSpecies, speciesList)
        speciesList.each { it.save() }


    }

    protected void tearDown()
    {
        super.tearDown()
    }

    void testCreateTag()
    {
        assertEquals(0, Tag.count())

        def releasesText = '''"ACO_ID","ACO_SERIAL_NUMBER","ACO_PINGER_CODE_ID","ACO_PINGER_TYPE","ACO_CODE_SPACE","ACO_MANUFACTURER","ACO_ATTACHMENT","ACO_OWNER","ACO_FREQUENCY","ACO_TEMP_SENSOR_YN","ACO_TEMP_CODE_SPACE","ACO_TEMP_PINGER_ID","ACO_TEMP_SLOPE","ACO_TEMP_INTERCEPT","ACO_PRES_SENSOR_YN","ACO_PRES_CODE_SPACE","ACO_PRES_PINGER_ID","ACO_PRES_SLOPE","ACO_PRES_INTERCEPT","ACO_TRANSMIT_INT_MIN","ACO_TRANSMIT_INT_MAX","REL_ID","REL_DATE","TIME","REL_TYPE","SPC_CAAB_CODE","SPC_COMMON_NAME","REL_LENGTH","REL_LENGTH_QUAL","REL_LENGTH_TYPE","REL_SEX","REL_COHORT_AGE","REL_LATITUDE","REL_LONGITUDE","TAG_ID","TAG_FIRST_NAME","TAG_FAMILY_NAME","VSL_ID","VSL_NAME","REL_CAPTURE","REL_FISH_COND","REL_INJ","REL_INJ_SEVERITY","REL_TIME_OOW","REL_STRONT_DOSE","REL_ABIOT_USED","REL_ABIOT_DOSE","REL_ASEPT_USED","REL_SCHOOL_NUM","REL_COUNT","REL_OBS_PROG_YN","NOTES"
904,1078955,62347,"Coded Pinger","R64K (Sync=320,Bin=20)","Vemco 16mm","External","Bruce",69,0,,,,,0,,,,,50,130,149291,10/12/2009,"22:15","into the wild",37010003,"White Shark",420,"guessed","Total Length","male",,-35.23,136.07,414,"Barry","Bruce",15365,"CALYPSO STAR","Shark Cage Dive","good condition","other","slight injury",0,,,,,,,,"Date and time are in UTC"
'''
        load([releasesText])

        def pinger = Sensor.findByPingCode(62347)
        assertNotNull(pinger)
        assertEquals(pingerType, pinger.transmitterType)

        def tag = pinger.tag
        assertNotNull(tag)
        assertEquals(a69_1303, tag.codeMap)
        assertEquals(DeviceStatus.NEW, tag.status)
        assertEquals(v16, tag.model)
        assertEquals(1, Sensor.findAllByTag(tag).size())

        def project = tag.project
        assertEquals("CSIRO: Shark Monitoring Network", project.name)

        assertEquals(1, Tag.count())
    }

    void testCreateAnimalUnknownCaabCode()
    {
        def releasesText = '''"ACO_ID","ACO_SERIAL_NUMBER","ACO_PINGER_CODE_ID","ACO_PINGER_TYPE","ACO_CODE_SPACE","ACO_MANUFACTURER","ACO_ATTACHMENT","ACO_OWNER","ACO_FREQUENCY","ACO_TEMP_SENSOR_YN","ACO_TEMP_CODE_SPACE","ACO_TEMP_PINGER_ID","ACO_TEMP_SLOPE","ACO_TEMP_INTERCEPT","ACO_PRES_SENSOR_YN","ACO_PRES_CODE_SPACE","ACO_PRES_PINGER_ID","ACO_PRES_SLOPE","ACO_PRES_INTERCEPT","ACO_TRANSMIT_INT_MIN","ACO_TRANSMIT_INT_MAX","REL_ID","REL_DATE","TIME","REL_TYPE","SPC_CAAB_CODE","SPC_COMMON_NAME","REL_LENGTH","REL_LENGTH_QUAL","REL_LENGTH_TYPE","REL_SEX","REL_COHORT_AGE","REL_LATITUDE","REL_LONGITUDE","TAG_ID","TAG_FIRST_NAME","TAG_FAMILY_NAME","VSL_ID","VSL_NAME","REL_CAPTURE","REL_FISH_COND","REL_INJ","REL_INJ_SEVERITY","REL_TIME_OOW","REL_STRONT_DOSE","REL_ABIOT_USED","REL_ABIOT_DOSE","REL_ASEPT_USED","REL_SCHOOL_NUM","REL_COUNT","REL_OBS_PROG_YN","NOTES"
904,1078955,62347,"Coded Pinger","R64K (Sync=320,Bin=20)","Vemco 16mm","External","Bruce",69,0,,,,,0,,,,,50,130,149291,10/12/2009,"22:15","into the wild",123,"White Shark",420,"guessed","Total Length","male",,-35.23,136.07,414,"Barry","Bruce",15365,"CALYPSO STAR","Shark Cage Dive","good condition","other","slight injury",0,,,,,,,,"Date and time are in UTC"
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
904,1078955,62347,"Coded Pinger","R64K (Sync=320,Bin=20)","Vemco 16mm","External","Bruce",69,0,,,,,0,,,,,50,130,149291,10/12/2009,"22:15","into the wild",37010003,"White Shark",420,"guessed","Total Length","aaa",,-35.23,136.07,414,"Barry","Bruce",15365,"CALYPSO STAR","Shark Cage Dive","good condition","other","slight injury",0,,,,,,,,"Date and time are in UTC"
'''
        try
        {
            load([releasesText])
            fail()
        }
        catch (BulkImportException e)
        {
            assertEquals("Unknown sex: 'aaa'", e.message)
        }
    }

    void testCreateAnimal()
    {
        assertEquals(0, Animal.count())

        def releasesText = '''"ACO_ID","ACO_SERIAL_NUMBER","ACO_PINGER_CODE_ID","ACO_PINGER_TYPE","ACO_CODE_SPACE","ACO_MANUFACTURER","ACO_ATTACHMENT","ACO_OWNER","ACO_FREQUENCY","ACO_TEMP_SENSOR_YN","ACO_TEMP_CODE_SPACE","ACO_TEMP_PINGER_ID","ACO_TEMP_SLOPE","ACO_TEMP_INTERCEPT","ACO_PRES_SENSOR_YN","ACO_PRES_CODE_SPACE","ACO_PRES_PINGER_ID","ACO_PRES_SLOPE","ACO_PRES_INTERCEPT","ACO_TRANSMIT_INT_MIN","ACO_TRANSMIT_INT_MAX","REL_ID","REL_DATE","TIME","REL_TYPE","SPC_CAAB_CODE","SPC_COMMON_NAME","REL_LENGTH","REL_LENGTH_QUAL","REL_LENGTH_TYPE","REL_SEX","REL_COHORT_AGE","REL_LATITUDE","REL_LONGITUDE","TAG_ID","TAG_FIRST_NAME","TAG_FAMILY_NAME","VSL_ID","VSL_NAME","REL_CAPTURE","REL_FISH_COND","REL_INJ","REL_INJ_SEVERITY","REL_TIME_OOW","REL_STRONT_DOSE","REL_ABIOT_USED","REL_ABIOT_DOSE","REL_ASEPT_USED","REL_SCHOOL_NUM","REL_COUNT","REL_OBS_PROG_YN","NOTES"
904,1078955,62347,"Coded Pinger","R64K (Sync=320,Bin=20)","Vemco 16mm","External","Bruce",69,0,,,,,0,,,,,50,130,149291,10/12/2009,"22:15","into the wild",37010003,"White Shark",420,"guessed","Total Length","male",,-35.23,136.07,414,"Barry","Bruce",15365,"CALYPSO STAR","Shark Cage Dive","good condition","other","slight injury",0,,,,,,,,"Date and time are in UTC"
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
904,1078955,62347,"Coded Pinger","R64K (Sync=320,Bin=20)","Vemco 16mm","External","Bruce",69,0,,,,,0,,,,,50,130,149291,10/12/2009,"22:15","into the wild",37010003,"White Shark",420,"guessed","Total Length","male",,-35.23,136.07,414,"Barry","Bruce",15365,"CALYPSO STAR","Shark Cage Dive","good condition","other","slight injury",0,,,,,,,,"Date and time are in UTC"
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
904,1078955,62347,"Coded Pinger","R64K (Sync=320,Bin=20)","Vemco 16mm","External","Bruce",69,0,,,,,0,,,,,50,130,149291,10/12/2009,"22:15","into the wild",37010003,"White Shark",420,"guessed","Total Length","male",,-35.23,136.07,414,"Barry","Bruce",15365,"CALYPSO STAR","Shark Cage Dive","good condition","other","slight injury",0,,,,,,,,"Date and time are in UTC"
'''
        load([releasesText])


        assertEquals(1, Surgery.count())
    }

    void testCreateMeasurement()
    {
        assertEquals(0, AnimalMeasurement.count())

        def releasesText = '''"ACO_ID","ACO_SERIAL_NUMBER","ACO_PINGER_CODE_ID","ACO_PINGER_TYPE","ACO_CODE_SPACE","ACO_MANUFACTURER","ACO_ATTACHMENT","ACO_OWNER","ACO_FREQUENCY","ACO_TEMP_SENSOR_YN","ACO_TEMP_CODE_SPACE","ACO_TEMP_PINGER_ID","ACO_TEMP_SLOPE","ACO_TEMP_INTERCEPT","ACO_PRES_SENSOR_YN","ACO_PRES_CODE_SPACE","ACO_PRES_PINGER_ID","ACO_PRES_SLOPE","ACO_PRES_INTERCEPT","ACO_TRANSMIT_INT_MIN","ACO_TRANSMIT_INT_MAX","REL_ID","REL_DATE","TIME","REL_TYPE","SPC_CAAB_CODE","SPC_COMMON_NAME","REL_LENGTH","REL_LENGTH_QUAL","REL_LENGTH_TYPE","REL_SEX","REL_COHORT_AGE","REL_LATITUDE","REL_LONGITUDE","TAG_ID","TAG_FIRST_NAME","TAG_FAMILY_NAME","VSL_ID","VSL_NAME","REL_CAPTURE","REL_FISH_COND","REL_INJ","REL_INJ_SEVERITY","REL_TIME_OOW","REL_STRONT_DOSE","REL_ABIOT_USED","REL_ABIOT_DOSE","REL_ASEPT_USED","REL_SCHOOL_NUM","REL_COUNT","REL_OBS_PROG_YN","NOTES"
904,1078955,62347,"Coded Pinger","R64K (Sync=320,Bin=20)","Vemco 16mm","External","Bruce",69,0,,,,,0,,,,,50,130,149291,10/12/2009,"22:15","into the wild",37010003,"White Shark",420,"guessed","Total Length","male",,-35.23,136.07,414,"Barry","Bruce",15365,"CALYPSO STAR","Shark Cage Dive","good condition","other","slight injury",0,,,,,,,,"Date and time are in UTC"
'''
        load([releasesText])


        assertEquals(1, AnimalMeasurement.count())
    }

    void testFindProjectUnknownName()
    {
        ["asdf", "", "Auto Entry"].each
        {
            try
            {
                loader.findProject(it)
                fail()
            }
            catch (BulkImportException e)
            {
                assertEquals("No mapping to project for owner: ${it}", e.message)
            }
        }
    }

    void testFindProjectBruce()
    {
        [
            "BRU007 / White shark collaboration",
            "Bruce\\R-00656-02-015",
            "Bruce",
            "Bruce \\ KU30A",
            "Bruce \\ SMN",
            "Bruce\\KU30A",
            "Bruce / KU30A",
            "KU30A / Bruce",
            "Bruce / KU30",
            "Bruce\\Effects of berley",
            "BRU-00656-02-015",
            "BRU080\\R-00656-02-015",
            "Bruce \\ Effects of berley",
            "Bruce \\ Juv white sharks",
            "Bruce \\ white sharks",
            "Bruce \\ white shark",
            "Fox A\\none",
            "Fox A/ none",
            "Fox A / none"
        ].each
        {
            assertEquals(bruceProject, loader.findProject(it))
        }
    }

    void testFindProject()
    {
        [
            "Babcock/Pillans": nretaProject,
            "Al Hobday" : bluefinTunaProject,
            "Daley \\ R656-02-014": daleyProject
        ].each
        {
            k, v ->

            assertEquals(v, loader.findProject(k))
        }
    }

    void testIgnoreBySerialNumber()
    {
        def defaultVals = ["SPC_CAAB_CODE": "310", "ACO_OWNER": "Bruce", "ACO_MANUFACTURER": "Vemco 16mm"]

        ["", "0", "1", "2"].each
        {
            assertTrue(loader.shouldIgnore(defaultVals + ["ACO_SERIAL_NUMBER": it]))
        }

        ["3", "12"].each
        {
            assertFalse(loader.shouldIgnore(defaultVals + ["ACO_SERIAL_NUMBER": it]))
        }
    }

    void testIgnoreBySpecies()
    {
        def defaultVals = ["ACO_OWNER": "Bruce", "ACO_MANUFACTURER": "Vemco 16mm", "ACO_SERIAL_NUMBER": "12"]

        ["", "0"].each
        {
            assertTrue(loader.shouldIgnore(defaultVals + ["SPC_CAAB_CODE": it]))
        }

        ["312"].each
        {
            assertFalse(loader.shouldIgnore(defaultVals + ["SPC_CAAB_CODE": it]))
        }
    }

    void testIgnoreByOwner()
    {
        def defaultVals = ["SPC_CAAB_CODE": "310", "ACO_MANUFACTURER": "Vemco 16mm", "ACO_SERIAL_NUMBER": "12"]

        ["", "Auto Entry", "Auto Entry "].each
        {
            assertTrue(loader.shouldIgnore(defaultVals + ["ACO_OWNER": it]))
        }

        ["Bruce"].each
        {
            assertFalse(loader.shouldIgnore(defaultVals + ["ACO_OWNER": it]))
        }
    }

    void testCaptureMethod()
    {
        expectedCaptureMapping.each
        {
            k, v ->

            assertEquals(CaptureMethod.findByName(v), loader.getCaptureMethod(k))
        }
    }
}
