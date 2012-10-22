package au.org.emii.aatams.bulk

import grails.test.*
import org.joda.time.DateTime

import au.org.emii.aatams.Receiver

class BulkImportRecordTests extends GrailsUnitTestCase 
{
	BulkImportRecord rec
	
    protected void setUp() 
	{
        super.setUp()
		 
		rec = new BulkImportRecord(
			srcTable: "RECEIVERS",
			srcPk: 123l,
			srcModifiedDate: new DateTime(),
			dstClass: "au.org.emii.aatams.Receiver",
			dstPk: 54l,
			type: BulkImportRecordType.NEW,
			bulkImport: new BulkImport())
			
		rec.grailsApplication = [ getClassForName: { dstClass -> return Receiver } ]
		
		mockDomain(BulkImportRecord, [rec])
		rec.save(failOnError: true)
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

	void testGetDstClazz()
	{
		assertEquals(Receiver.class, rec.getDstClazz())
	}
	
    void testGetDstUrl() 
	{
		assertEquals(new URL("http://localhost:8080/aatams/receiver/show/54"), rec.getDstUrl())
    }
	
	void testDstToString()
	{
		
	}
	
	
}
