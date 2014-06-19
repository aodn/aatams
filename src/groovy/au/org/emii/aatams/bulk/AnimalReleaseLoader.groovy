package au.org.emii.aatams.bulk

import java.util.Date;

import au.org.emii.aatams.*

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

class AnimalReleaseLoader extends AbstractLoader 
{
    private static final Logger log = Logger.getLogger(AnimalReleaseLoader)
    
    static final String RCD_ID_COL = "RCD_ID"
    static final String RCV_ID_COL = "RCV_ID"
    static final String STA_ID_COL = "STA_ID"

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
        
        if (shouldIgnore(record))
        {
            
        }
        else
        {
            def tag = loadTag(context, record)
            
            if (!record["REL_LATITUDE"].isEmpty() && !record["REL_LONGITUDE"].isEmpty())
            {
                def animal = loadAnimal(context, record)
                def release = loadRelease(context, record, tag.project, animal)
                def surgery = loadSurgery(context, record, tag, release)
                def measurement = loadMeasurement(context, record, release)
            }
        }
    }
    
    private boolean shouldIgnore(record)
    {
        boolean shouldIgnore = false
        
        [record[ACO_MANUFACTURER_COL], record[ACO_SERIAL_NUMBER_COL], record["ACO_OWNER"]].each {
            
            if (!it || it.isEmpty())
            { 
                shouldIgnore = true
            }
        }
        
        ["0", "1", "2"].each
        {
            if (record[ACO_SERIAL_NUMBER_COL] == it)
            {
                log.warn("Ignoring tag with serial number '${record[ACO_SERIAL_NUMBER_COL]}'")
                shouldIgnore =  true
            }
        }

        ["", "0"].each
        {
            if (record["SPC_CAAB_CODE"] == it)
            {
                log.warn("Ignoring tag with species code \'${record['SPC_CAAB_CODE']}\'")
                shouldIgnore =  true
            }
        }
        
        ["", "Auto Entry"].each
        {
            if (record["ACO_OWNER"].trim() == it)
            {
                log.warn("Ignoring tag with owner code \'${record['ACO_OWNER']}\'")
                shouldIgnore =  true
            }
        }

        return shouldIgnore
    }
    
    private Tag loadTag(Map context, Map record) throws BulkImportException
    {
        def serialNumber = record[ACO_SERIAL_NUMBER_COL]
        
        Tag tag = Tag.findBySerialNumber(serialNumber)
        if (tag)
        {
            log.warn("Tag with serial number ${serialNumber} already exists.")
            return tag
        }
        
        tag = new Tag(
            model: getModel(record[ACO_MANUFACTURER_COL]),
            serialNumber: serialNumber,
            
            status: DeviceStatus.findByStatus(DeviceStatus.NEW, [cache: true]),
            project: findProject(record["ACO_OWNER"]),
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
        
        return tag
    }
    
    private Project findProject(owner)
    {
        def ownerToProjectNameMapping = [:]
        
        [
            "BRU007 / White shark collaboration",
            "Bruce\\R-00656-02-015",
            "BruceR-00656-02-015",
            "Bruce",
            "Bruce \\ KU30A",
            "Bruce  KU30A",
            "Bruce \\ SMN",
            "Bruce  SMN",
            "Bruce\\KU30A",
            "BruceKU30A",
            "Bruce / KU30A",
            "KU30A / Bruce",
            "Bruce / KU30",
            "Bruce\\Effects of berley",
            "BruceEffects of berley",
            "BRU-00656-02-015",
            "BRU080\\R-00656-02-015",
            "Bruce \\ Effects of berley",
            "Bruce \\ Juv white sharks",
            "Bruce \\ white sharks",
            "Bruce \\ white shark",
            "Fox A\\none",
            "BRU080R-00656-02-015",
            "Bruce  Effects of berley",
            "Bruce  Juv white sharks",
            "Bruce  white sharks",
            "Bruce  white shark",
            "Fox Anone",
            "Fox A/ none",
            "Fox A / none"
        ].each {
        
            ownerToProjectNameMapping[it] = "CSIRO: Shark Monitoring Network"
        }
        
        ownerToProjectNameMapping["Babcock/Pillans"] = "Ningaloo Reef Ecosystem Tracking Array (NRETA)"
        ownerToProjectNameMapping["Richard Pillans/TERN"] = "Ningaloo Reef Ecosystem Tracking Array (NRETA)"
        ownerToProjectNameMapping["Al Hobday"] = "Bluefin tuna WA"
        ownerToProjectNameMapping["Hobday"] = "Bluefin tuna WA"
        ownerToProjectNameMapping["Daley \\ R656-02-014"] = "CSIRO: Ross Daley"
            
        def name = ownerToProjectNameMapping[owner]
        
        if (!name)
        {
            throw new BulkImportException("No mapping to project for owner: ${owner}")
        }
        
        Project project = Project.findByName(name, [cache: true])
        
        if (!project && owner == "Daley \\ R656-02-014")
        {
            project = new Project(name: owner)
            project.save()
        }
        
        return project
    }
    
    private TagDeviceModel getModel(modelAsString)
    {
        if (modelAsString == "Vemco 16mm")
        {
            return TagDeviceModel.findByModelName("V16")
        }
        else if (modelAsString == "Vemco 13mm")
        {
            return TagDeviceModel.findByModelName("V13")
        }
        else if (modelAsString == "Vemco 9mm")
        {
            return TagDeviceModel.findByModelName("V9")
        }
        else if (modelAsString == "Vemco 8mm")
        {
            return TagDeviceModel.findByModelName("V8")
        }

        assert(false): "Unkown model: " + modelAsString
    }
    
    private CodeMap getCodeMap(codeMapAsString)
    {
        return CodeMap.findByCodeMap("A69-1303")
    }
    
    private Animal loadAnimal(Map context, Map record) throws BulkImportException
    {
        CaabSpecies species = CaabSpecies.findBySpcode(record["SPC_CAAB_CODE"])
        
        if (!species)
        {
            throw new BulkImportException("Unknown CAAB code: ${record['SPC_CAAB_CODE']}")
        }
        
        def sexMapping = [
            "male": Sex.findBySex("MALE"), 
            "female": Sex.findBySex("FEMALE"), 
            null: Sex.findBySex("UNKNOWN"), 
            '': Sex.findBySex("UNKNOWN"), 
            "indeterminate": Sex.findBySex("UNKNOWN")]
        def sex = sexMapping[record["REL_SEX"]]
        
        if (!sex)
        {
            throw new BulkImportException("Unknown sex: '${record['REL_SEX']}'")
        }
        
        Animal animal = new Animal(sex: sex, species: species)
        animal.save(failOnError:true)
        
        BulkImportRecord importRecord =
            new BulkImportRecord(
                bulkImport: context.bulkImport,
                srcTable: "RELEASES",
                srcPk: record[ACO_ID_COL],
                dstClass: "au.org.emii.aatams.Animal",
                dstPk: animal.id,
                type: BulkImportRecordType.NEW)
        
        importRecord.save(failOnError: true)
        
        return animal
    }
    
    private AnimalRelease loadRelease(Map context, Map record, Project project, Animal animal)
    {
        assert(record)
        assert(project)
        assert(animal)    
    
        Point location =  new GeometryFactory().createPoint(new Coordinate(Double.valueOf(record['REL_LONGITUDE']), Double.valueOf(record['REL_LATITUDE'])))
        location.setSRID(4326)
        AnimalRelease release = new AnimalRelease(
            project: project,
            animal: animal,
            captureLocality: "not recorded",
            captureLocation: location,
            captureDateTime: constructDateTime(record['REL_DATE'], record['TIME']),
            captureMethod: getCaptureMethod(record['REL_CAPTURE']),
            releaseLocality: "not recorded",
            releaseLocation: location,
            releaseDateTime: constructDateTime(record['REL_DATE'], record['TIME']),
            comments: record['NOTES'],
            embargoDate: new DateTime().plusYears(1).toDate(),
            status: AnimalReleaseStatus.CURRENT)
    
        release.save(failOnError: true)
        
        return release
    }
    
    private CaptureMethod getCaptureMethod(captureMethodName)
    {
        def mapping = [
            "Shark Cage Dive": "FREE-SWIMMING",
            "": "OTHER",
            "Other/unknown": "OTHER",
            "Handlining": "HAND CAPTURE",
            "Trolling": "TRAWL",
            "Pole and line": "POLE AND LINE",
            "Cast Net": "OTHER",
            "Rod and reel": "POLE AND LINE"]
        
        def captureMethod = CaptureMethod.findByName(mapping[captureMethodName])
        
        if (!captureMethod)
        {
            throw new BulkImportException("Unknown capture method: ${captureMethodName}")
        }
        
        return captureMethod
    }
    
    private static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("d/M/yyyy")
    
    private DateTime constructDateTime(dateAsString, timeAsString)
    {
        // 4/12/2007 0:00:00,"13:04"
        DateTime date = dateFormatter.parseDateTime(dateAsString)
        
        if (!timeAsString?.isEmpty())
        {
            LocalTime time = new LocalTime(timeAsString)
            date = date.plusHours(time.hourOfDay)
            date = date.plusMinutes(time.minuteOfHour)
        }
        
        return date
    }
    
    private Surgery loadSurgery(context, record, tag, release)
    {
        assert(tag)
        assert(release)
        
        Surgery surgery = new Surgery(
            release: release, 
            tag: tag,
            timestamp: release.captureDateTime,
            type: getSurgeryType(record['ACO_ATTACHMENT']),
            treatmentType: SurgeryTreatmentType.findByType('NO ANESTHETIC'),
            comments: "")
        
        surgery.save(failOnError: true)
        
        return surgery
    }
    
    private SurgeryType getSurgeryType(surgeryTypeAsString)
    {
        def mapping = [
            "": SurgeryType.findByType("EXTERNAL", [cache: true]),
            "External": SurgeryType.findByType("EXTERNAL", [cache: true]),
            "Internal": SurgeryType.findByType("INTERNAL", [cache: true]),
            "Archival Tag Attachment Quality": SurgeryType.findByType("EXTERNAL", [cache: true])]
        
        assert(mapping.containsKey(surgeryTypeAsString))
        return mapping[surgeryTypeAsString]
    }
    
    private AnimalMeasurement loadMeasurement(context, record, release)
    {
        assert(release)
        
        def type = getMeasurementType(record['REL_LENGTH_TYPE'])
        def valueAsString = record['REL_LENGTH']
        if (type && !valueAsString.isEmpty())
        {
            AnimalMeasurement measurement = new AnimalMeasurement(
                release: release,
                type: type,
                value: Float.valueOf(valueAsString),
                unit: MeasurementUnit.findByUnit("cm", [cache: true]),
                estimate: isEstimatedMeasurement(record['REL_LENGTH_QUALITY']))
            
            measurement.save(failOnError: true)
            return measurement
        }
        
        return null
    }
    
    private AnimalMeasurementType getMeasurementType(typeAsString)
    {
        def mapping = [
            "": null,
            "Total Length": AnimalMeasurementType.findByType("TOTAL LENGTH", [cache: true]),
            "Length to Caudal Fork": AnimalMeasurementType.findByType("FORK LENGTH", [cache: true]),
            "Lower Jaw to Caudal Fork Length": AnimalMeasurementType.findByType("FORK LENGTH", [cache: true])]
        
        assert(mapping.containsKey(typeAsString))
        return mapping[typeAsString]
    }
    
    private boolean isEstimatedMeasurement(lengthQuality)
    {
        def mapping = [
            "": false,
            "guessed": true,
            "correctly measured": false,
            "unknown": false]
        
        return mapping[lengthQuality]
    }
}
