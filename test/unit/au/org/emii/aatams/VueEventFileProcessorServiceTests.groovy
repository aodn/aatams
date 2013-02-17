package au.org.emii.aatams


import org.joda.time.DateTime;

import grails.plugin.searchable.SearchableService
import grails.test.*


class VueEventFileProcessorServiceTests extends AbstractVueEventFileProcessorServiceTests 
{
    protected void setUp() 
	{
        super.setUp()
		
		mockLogging(EventFactoryService, true)
		eventFactoryService = new EventFactoryService()
		
		mockLogging(VueEventFileProcessorService, true)
		vueEventFileProcessorService = new VueEventFileProcessorService()
		vueEventFileProcessorService.eventFactoryService = eventFactoryService
		vueEventFileProcessorService.searchableService = searchableService
		vueEventFileProcessorService.metaClass.getReader = { getReader(it) }
	}

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testProcess() 
	{
		vueEventFileProcessorService.process(download)

		def records = vueEventFileProcessorService.getRecords(download)
		
		assertEquals (records.size(), ValidReceiverEvent.count())
		
		records.eachWithIndex
		{
			record, i ->
			
			assertEquals(record[EventFactoryService.RECEIVER_COLUMN], ValidReceiverEvent.list()[i].receiverDeployment.receiver.name)
			assertEquals(record[EventFactoryService.DESCRIPTION_COLUMN], ValidReceiverEvent.list()[i].description)
			assertEquals(record[EventFactoryService.DATA_COLUMN], ValidReceiverEvent.list()[i].data ?: "")
			
			// Special case for #1016
			if (ValidReceiverEvent.list()[i].units?.startsWith("Shad Bay"))
			{
				assertEquals("Shad Bay, NS, Canada", ValidReceiverEvent.list()[i].units)
			}
			else
			{
				assertEquals(record[EventFactoryService.UNITS_COLUMN], ValidReceiverEvent.list()[i].units ?: "")
			}
		}
    }
}
