package au.org.emii.aatams

import java.util.List;
import java.util.Map;

import grails.plugin.searchable.SearchableService
import grails.test.GrailsUnitTestCase;
import org.joda.time.DateTime

abstract class AbstractVueEventFileProcessorServiceTests extends GrailsUnitTestCase 
{
	def eventFactoryService
	def searchableService
	def vueEventFileProcessorService
	
	ReceiverDownloadFile download
	
	protected void setUp()
	{
		super.setUp()
		mockLogging(SearchableService, true)
		searchableService = new SearchableService()
		searchableService.metaClass.startMirroring = {}
		searchableService.metaClass.stopMirroring = {}
		assert(searchableService)

		AbstractBatchProcessor.metaClass.getRecords = { getRecords(it) }
		
		Receiver receiver = new Receiver(codeName: "VR2W-103335")
		def receiverList = [receiver]
		mockDomain(Receiver, receiverList)

		ReceiverRecovery recovery = new ReceiverRecovery(recoveryDateTime: new DateTime("2010-12-08T01:45:29"))
		mockDomain(ReceiverRecovery, [recovery])
		recovery.save()
		
		ReceiverDeployment deployment = new ReceiverDeployment(recovery: recovery, receiver: receiver, 
															   deploymentDateTime: new DateTime("2009-12-08T01:45:29"),
															   initialisationDateTime: new DateTime("2009-12-08T00:45:29"))
		mockDomain(ReceiverDeployment, [deployment])
		deployment.save()
		
		recovery.deployment = deployment
		recovery.save()
		
		receiver.addToDeployments(deployment)
		receiverList.each { it.save() }
		
		mockDomain(ReceiverEvent)
		mockDomain(ValidReceiverEvent)
		mockDomain(InvalidReceiverEvent)
		
		download = new ReceiverDownloadFile()
		mockDomain(ReceiverDownloadFile, [download])
		download.save()
	}

	protected void tearDown()
	{
		super.tearDown()
	}

	public List<Map<String, String>> getRecords(downloadFile) 
	{
		def columnNames = 
			[0:EventFactoryService.DATE_AND_TIME_COLUMN, 
			 1:EventFactoryService.RECEIVER_COLUMN, 
			 2:EventFactoryService.DESCRIPTION_COLUMN, 
			 3:EventFactoryService.DATA_COLUMN, 
			 4:EventFactoryService.UNITS_COLUMN]

		def retList = []
		
		String data = '''2009-12-09 01:45:29,VR2W-103335,Initialization,,
2009-12-09 01:45:29,VR2W-103335,PC Time,2009-12-09 12:45:29+11:00,
2009-12-09 01:45:29,VR2W-103335,Map,MAP-110 (A69-1008; A69-1105; A69-1206; A69-1303),
2009-12-09 01:45:29,VR2W-103335,Blanking,240,ms
2009-12-09 01:45:29,VR2W-103335,Station,VR2W-NN-No_6,
2009-12-10 00:00:00,VR2W-103335,Memory Capacity,0.00,%
2009-12-10 00:00:00,VR2W-103335,Battery,3.50,V
2009-12-10 00:00:00,VR2W-103335,Daily Pings,0,
2009-12-10 00:00:00,VR2W-103335,Daily Syncs,0,
2009-12-10 00:00:00,VR2W-103335,Daily Rejects,0,
2009-12-11 00:00:00,VR2W-103335,Memory Capacity,0.00,%
2009-12-11 00:00:00,VR2W-103335,Battery,3.49,V
2009-12-11 00:00:00,VR2W-103335,Daily Pings,0,
2009-12-11 00:00:00,VR2W-103335,Daily Syncs,0,'''
		
		data.eachLine
		{
			line ->
			
			def record = [:]
			
			line.split(',').eachWithIndex
			{
				value, i ->
				
				record.put(columnNames[i], value)
			}
			
			retList.add(record)
		}
		
		return retList
	}
}
