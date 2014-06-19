package au.org.emii.aatams.bulk

import au.org.emii.aatams.CodeMap;
import au.org.emii.aatams.DeviceStatus;
import au.org.emii.aatams.InstallationStation
import au.org.emii.aatams.MooringType
import au.org.emii.aatams.Organisation
import au.org.emii.aatams.Receiver
import au.org.emii.aatams.ReceiverDeployment;
import au.org.emii.aatams.ReceiverDeviceModel
import au.org.emii.aatams.Sensor
import au.org.emii.aatams.Tag
import au.org.emii.aatams.TagDeviceModel;
import au.org.emii.aatams.detection.CsiroDetectionFormat
import grails.test.*
import org.grails.plugins.csv.CSVMapReader
import org.joda.time.DateTime

import java.text.ParseException
import java.text.SimpleDateFormat

class CsiroDetectionFormatTests extends GrailsUnitTestCase 
{
    CsiroDetectionFormat format
    
    String detectionsText = '''"DET_ID","ACO_ID","RCD_ID","DET_DATETIME","DET_QUALITY_CODE","DET_TEMP","DET_DEPTH","DET_RAW","DET_RECORD_ID","DET_CORR_STATUS_CODE"
98472,1063,344,3/6/2008 8:01:53,,0,0,0,,
'''
    
    protected void setUp() 
    {
        super.setUp()
        
        format = new CsiroDetectionFormat()
        
        mockDomain(BulkImportRecord)
        mockDomain(Receiver)
        mockDomain(ReceiverDeployment)
        mockDomain(Sensor)
        mockDomain(Tag)
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    //        def detectionsText = '''"DET_ID","ACO_ID","RCD_ID","DET_DATETIME","DET_QUALITY_CODE","DET_TEMP","DET_DEPTH","DET_RAW","DET_RECORD_ID","DET_CORR_STATUS_CODE"
    //        98472,1063,344,3/6/2008 8:01:53,,0,0,0,,
    //        '''

    def detectionReader() {
        return new CSVMapReader(new StringReader(detectionsText))
    }

    private void assertException(List<String> expectedMessages)
    {
        detectionReader().eachWithIndex
        {
            it, i ->
            
            try
            {
                format.parseRow(it)
                fail()
            }
            catch (BulkImportException e)
            {
                assertEquals(expectedMessages[i], e.message)    
            }
        }
    }

    private void assertSuccess(List<Map> expectedValues)
    {
        detectionReader().eachWithIndex
        {
            it, i ->
            
            def parsedValues = format.parseRow(it)
            
            expectedValues[i].each 
            {
                k, v ->
                
                assertEquals(v, parsedValues[k])    
            }
        }    
    }
    
    private Tag createTag()
    {
        def tag = new Tag(
            codeMap: new CodeMap(codeMap: "A69-1303"),
            model: new TagDeviceModel(),
            serialNumber: "1234",
            status: new DeviceStatus())
        
        def sensor = new Sensor(tag: tag, pingCode: 1111)
        
        tag.addToSensors(sensor)
        tag.save(failOnError: true)
        
        return tag
    }
    
    private BulkImportRecord createTagImportRecord(tagId)
    {
        BulkImportRecord tagRecord =
            new BulkImportRecord(
                bulkImport: new BulkImport(),
                srcTable: "RELEASES",
                srcPk: 1063,
                dstClass: "au.org.emii.aatams.Tag",
                dstPk: tagId,
                type: BulkImportRecordType.NEW)
        tagRecord.save(failOnError: true)
    }
    
    private ReceiverDeployment createReceiverDeployment()
    {
        def receiver = new Receiver(
            model: new ReceiverDeviceModel(modelName: "VR2W"),
            organisation: new Organisation(),
            serialNumber: "5678")
        
        receiver.save(failOnError: true)
        
        def receiverDeployment = new ReceiverDeployment(
            receiver: receiver,
            station: new InstallationStation(),
            deploymentDateTime: new DateTime(),
            mooringType: new MooringType())
        
        receiverDeployment.save(failOnError: true)
            
        return receiverDeployment
    }
    
    private BulkImportRecord createReceiverImportRecord(receiverDeploymentId)
    {
        BulkImportRecord receiverRecord =
            new BulkImportRecord(
                bulkImport: new BulkImport(),
                srcTable: "RECEIVERS",
                srcPk: 344,
                dstClass: "au.org.emii.aatams.ReceiverDeployment",
                dstPk: receiverDeploymentId,
                type: BulkImportRecordType.NEW)
        receiverRecord.save(failOnError: true)
    }

    void testUnknownTagRecord()
    {
        try
        {
            format.tagRecord(['ACO_ID': '1063'])
            fail()
        }
        catch (BulkImportException e)
        {
            assertEquals("No bulk import record for tag with ACO_ID = 1063", e.message)
        }
    }

    void testUnknownTag()
    {
        createTagImportRecord(1)
        try
        {
            format.tag(['ACO_ID': '1063'])
            fail()
        }
        catch (BulkImportException e)
        {
            assertEquals("No tag corresponding to bulk import record with ACO_ID = 1063", e.message)
        }
    }

    void testUnknownReceiverDeployment()
    {
        try
        {
            format.receiverDeployment(['RCD_ID': '344'])
        }
        catch (BulkImportException e)
        {
            assertEquals("No bulk import record for receiver with RCD_ID = 344", e.message)
        }
    }

    void testUnknownReceiver()
    {
        createReceiverImportRecord(null)

        try
        {
            format.receiver(['RCD_ID': "344"])
        }
        catch (BulkImportException e)
        {
            assertEquals("No receiver corresponding to bulk import record with RCD_ID = 344", e.message)
        }
    }

    void testUnknownTagBulkImport()
    {
        createReceiverImportRecord(createReceiverDeployment().id)
        assertException(["No bulk import record for tag with ACO_ID = 1063"])
    }

    void testUnknownTagThroughParse()
    {
        createTagImportRecord(1)
        createReceiverImportRecord(createReceiverDeployment().id)
        assertException(["No tag corresponding to bulk import record with ACO_ID = 1063"])
    }

    void testUnknownReceiverBulkImport()
    {
        createTagImportRecord(createTag().id)
        assertException(["No bulk import record for receiver with RCD_ID = 344"])
    }
    
    void testUnknownReceiverThroughParse()
    {
        createTagImportRecord(createTag().id)
        createReceiverImportRecord(null)
        
        assertException(["No receiver corresponding to bulk import record with RCD_ID = 344"])    
    }

    void testPinger()
    {
        createTagImportRecord(createTag().id)
        createReceiverImportRecord(createReceiverDeployment().id)
        
        assertSuccess([
            [timestamp: new DateTime("2008-06-03T08:01:53Z").toDate(),
             receiverName: "VR2W-5678",
             transmitterId: "A69-1303-1111"]
        ])
    }
    
    void testSensor()
    {
        createTagImportRecord(createTag().id)
        createReceiverImportRecord(createReceiverDeployment().id)
        
        assertSuccess([
            [timestamp: new DateTime("2008-06-03T08:01:53Z").toDate(),
             receiverName: "VR2W-5678",
             transmitterId: "A69-1303-1111"]
//             sensorValue: TODO
        ])
    }
}
