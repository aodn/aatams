package au.org.emii.aatams.bulk

import au.org.emii.aatams.*
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase;
import grails.test.GrailsUnitTestCase;
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils

abstract class AbstractLoaderTests extends AbstractGrailsUnitTestCase 
{
    protected def currentDateTime = new DateTime("2012-01-01T12:00:00")
    protected AbstractLoader loader
    Organisation csiro
    
    protected void setUp()
    {
        super.setUp()
        
        mockDomain(BulkImport)
        mockDomain(BulkImportRecord)
        
        DateTimeUtils.setCurrentMillisFixed(currentDateTime.toInstant().getMillis())
        mockDomain(Organisation)
        csiro = new Organisation(name: 'CSIRO', department: 'CMAR')
        csiro.save()
    }

    protected void tearDown()
    {
        DateTimeUtils.setCurrentMillisSystem()
        super.tearDown()
    }

    protected def assertSuccess(texts, expectedImportRecords)
    {
        assertSuccess(texts, expectedImportRecords, true)
    }
    
    protected BulkImport load(List<String> texts)
    {
        return loadStreams(texts.collect { new ByteArrayInputStream(it.bytes) })
    }

    protected BulkImport loadStreams(List<InputStream> streams)
    {
        BulkImport bulkImport = new BulkImport(organisation: csiro, importStartDate: new DateTime(), status: BulkImportStatus.IN_PROGRESS, filename: "some/path")
        bulkImport.save(failOnError: true)
    
        loader.load([bulkImport: bulkImport, organisation: csiro], streams)
        
        return bulkImport
    }

    protected def assertSuccess(texts, expectedImportRecords, checkAllExpected)
    {
        def bulkImport = load(texts)
        
        def importRecords = BulkImportRecord.findAllByBulkImport(bulkImport)

        if (checkAllExpected)
        {
            assertEquals(expectedImportRecords.size(), importRecords.size())
        }
        
        expectedImportRecords.eachWithIndex
        {
            it, i ->
            
            def importRecord = importRecords[i]
            assertNotNull(importRecord)
            assertEquals(bulkImport, importRecord.bulkImport)
            
            assertEquals(it.type, importRecord.type)
            assertEquals(it.srcPk, importRecord.srcPk)
            assertEquals(it.srcTable, importRecord.srcTable)
            assertEquals(it.srcModifiedDate, importRecord.srcModifiedDate)
            assertEquals(it.dstClass, importRecord.dstClass)
            
            assertSuccessDetail(it, importRecord)
        }
    }
    
    protected void assertSuccessDetail(it, importRecord)
    {
        
    }
}
