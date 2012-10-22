package au.org.emii.aatams.bulk

import au.org.emii.aatams.*
import grails.test.*

import org.hibernate.SessionFactory;
import org.joda.time.DateTime

class BulkImportRecordTests extends GrailsUnitTestCase 
{
	Receiver receiver
	BulkImport bulkImport
	SessionFactory sessionFactory
	
    protected void setUp() 
	{
        super.setUp()
		
		sessionFactory.getCurrentSession().flush()
		sessionFactory.getCurrentSession().clear()
		
		receiver = 
			new Receiver(
				organisation: Organisation.findByName('CSIRO'), 
				model: ReceiverDeviceModel.findByModelName('VR2W'),
				serialNumber: '112233')
		receiver.save(failOnError: true, flush: true)
		
		bulkImport =
			new BulkImport(
				organisation: Organisation.findByName('CSIRO'),
				importStartDate: new DateTime(),
				status: BulkImportStatus.SUCCESS,
				filename: "some/path")
		bulkImport.save(flush: true)	
    }

    protected void tearDown() 
	{
		receiver?.delete()
		
        super.tearDown()
    }

    void testDeleteNewExistingReceiver() 
	{
		BulkImportRecord newRecord = 
			new BulkImportRecord(
				srcTable: "RECEIVERS", 
				srcPk: 123, 
				srcModifiedDate: new DateTime(),
				dstClass: "au.org.emii.aatams.Receiver", 
				dstPk: receiver.id, 
				type: BulkImportRecordType.NEW, 
				bulkImport: bulkImport)
			
		newRecord.save(flush: true, failOnError: true)
		assertNotNull(Receiver.get(receiver.id))

		bulkImport.refresh()
		bulkImport.delete(flush: true, failOnError: true)
		
		sessionFactory.getCurrentSession().clear()
		receiver = Receiver.get(receiver.id)
		assertNull(receiver)
    }
	
    void testDeleteNewNonexistentReceiver()
	{
		try
		{
			BulkImportRecord newRecord = 
				new BulkImportRecord(
					srcTable: "RECEIVERS", 
					srcPk: 123, 
					srcModifiedDate: new DateTime(),
					dstClass: "au.org.emii.aatams.Receiver", 
					dstPk: 123, 
					type: BulkImportRecordType.NEW, 
					bulkImport: bulkImport)
				
			newRecord.save(flush: true, failOnError: true)
			assertNotNull(Receiver.get(receiver.id))
			
			bulkImport.refresh()
			bulkImport.delete(flush: true)
			assertNull(Receiver.get(receiver.id))
			
			fail()
		}
		catch (Throwable e)
		{
			
		}
    }
	
	void testDeleteUpdated()
	{
		BulkImportRecord newRecord = 
			new BulkImportRecord(
				srcTable: "RECEIVERS", 
				srcPk: 123, 
				srcModifiedDate: new DateTime(),
				dstClass: "au.org.emii.aatams.Receiver", 
				dstPk: receiver.id, 
				type: BulkImportRecordType.UPDATED, 
				bulkImport: bulkImport)
			
		newRecord.save(flush: true, failOnError: true)
		assertNotNull(Receiver.get(receiver.id))
		
		bulkImport.refresh()
		bulkImport.delete(flush: true)
		
		sessionFactory.getCurrentSession().clear()
		receiver = Receiver.get(receiver.id)
		assertNotNull(receiver)
    }
	
	void testDeleteIgnored()
	{
		BulkImportRecord newRecord =
			new BulkImportRecord(
				srcTable: "RECEIVERS",
				srcPk: 123,
				srcModifiedDate: new DateTime(),
				dstClass: "au.org.emii.aatams.Receiver",
				dstPk: receiver.id,
				type: BulkImportRecordType.IGNORED,
				bulkImport: bulkImport)
			
		newRecord.save(flush: true, failOnError: true)
		assertNotNull(Receiver.get(receiver.id))
		
		bulkImport.refresh()
		bulkImport.delete(flush: true)
		
		sessionFactory.getCurrentSession().clear()
		receiver = Receiver.get(receiver.id)
		assertNotNull(receiver)
	}
	
	void testGetDstClazz()
	{
		assertEquals(Receiver.class, getRecord().getDstClazz())
	}
	
	void testGetDstController()
	{
		assertEquals("receiver", getRecord().getDstController())
	}
	
	void testGetDstObject()
	{
		assertEquals(receiver, getRecord().getDstObject())
	}

	private BulkImportRecord getRecord()
	{
		BulkImportRecord newRecord =
			new BulkImportRecord(
				srcTable: "RECEIVERS",
				srcPk: 123,
				srcModifiedDate: new DateTime(),
				dstClass: "au.org.emii.aatams.Receiver",
				dstPk: receiver.id,
				type: BulkImportRecordType.NEW,
				bulkImport: bulkImport)
		newRecord.save(flush: true, failOnError: true)
		
		return newRecord
	}
}
