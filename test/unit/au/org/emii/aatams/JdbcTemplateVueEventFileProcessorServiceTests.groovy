package au.org.emii.aatams

import grails.test.*

class JdbcTemplateVueEventFileProcessorServiceTests extends AbstractVueEventFileProcessorServiceTests 
{
    protected void setUp() 
	{
        super.setUp()
		
		mockLogging(JdbcTemplateEventFactoryService, true)
		eventFactoryService = new JdbcTemplateEventFactoryService()
		
		mockLogging(JdbcTemplateVueEventFileProcessorService, true)
		vueEventFileProcessorService = new JdbcTemplateVueEventFileProcessorService()
		vueEventFileProcessorService.jdbcTemplateEventFactoryService = eventFactoryService
		vueEventFileProcessorService.searchableService = searchableService
		vueEventFileProcessorService.metaClass.getRecords = { getRecords(it) }
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testProcess() 
	{
		vueEventFileProcessorService.metaClass.batchUpdate = { String[] statements -> batchUpdate(statements) }
		vueEventFileProcessorService.metaClass.getBatchSize = { 10000 }
		vueEventFileProcessorService.process(download)
    }
	
	private void batchUpdate(String[] statementList)
	{
		statementList.each { println it }
		
		assertEquals("INSERT INTO RECEIVER_EVENT (ID, VERSION, TIMESTAMP, RECEIVER_DEPLOYMENT_ID, RECEIVER_DOWNLOAD_ID, DATA, DESCRIPTION, UNITS) VALUES(nextval('hibernate_sequence'),0,'2009-12-09 12:45:29.0',1,1,null,'Initialization',null)", statementList[0])
		assertEquals("INSERT INTO RECEIVER_EVENT (ID, VERSION, TIMESTAMP, RECEIVER_DEPLOYMENT_ID, RECEIVER_DOWNLOAD_ID, DATA, DESCRIPTION, UNITS) VALUES(nextval('hibernate_sequence'),0,'2009-12-09 12:45:29.0',1,1,'2009-12-09 12:45:29+11:00','PC Time',null)", statementList[1])
		assertEquals("INSERT INTO RECEIVER_EVENT (ID, VERSION, TIMESTAMP, RECEIVER_DEPLOYMENT_ID, RECEIVER_DOWNLOAD_ID, DATA, DESCRIPTION, UNITS) VALUES(nextval('hibernate_sequence'),0,'2009-12-09 12:45:29.0',1,1,'MAP-110 (A69-1008; A69-1105; A69-1206; A69-1303)','Map',null)", statementList[2])
		assertEquals("INSERT INTO RECEIVER_EVENT (ID, VERSION, TIMESTAMP, RECEIVER_DEPLOYMENT_ID, RECEIVER_DOWNLOAD_ID, DATA, DESCRIPTION, UNITS) VALUES(nextval('hibernate_sequence'),0,'2009-12-09 12:45:29.0',1,1,'240','Blanking','ms')", statementList[3])
		assertEquals("INSERT INTO RECEIVER_EVENT (ID, VERSION, TIMESTAMP, RECEIVER_DEPLOYMENT_ID, RECEIVER_DOWNLOAD_ID, DATA, DESCRIPTION, UNITS) VALUES(nextval('hibernate_sequence'),0,'2009-12-09 12:45:29.0',1,1,'VR2W-NN-No_6','Station',null)", statementList[4])
		assertEquals("INSERT INTO RECEIVER_EVENT (ID, VERSION, TIMESTAMP, RECEIVER_DEPLOYMENT_ID, RECEIVER_DOWNLOAD_ID, DATA, DESCRIPTION, UNITS) VALUES(nextval('hibernate_sequence'),0,'2009-12-10 11:00:00.0',1,1,'0.00','Memory Capacity','%')", statementList[5])
		assertEquals("INSERT INTO RECEIVER_EVENT (ID, VERSION, TIMESTAMP, RECEIVER_DEPLOYMENT_ID, RECEIVER_DOWNLOAD_ID, DATA, DESCRIPTION, UNITS) VALUES(nextval('hibernate_sequence'),0,'2009-12-10 11:00:00.0',1,1,'3.50','Battery','V')", statementList[6])
		assertEquals("INSERT INTO RECEIVER_EVENT (ID, VERSION, TIMESTAMP, RECEIVER_DEPLOYMENT_ID, RECEIVER_DOWNLOAD_ID, DATA, DESCRIPTION, UNITS) VALUES(nextval('hibernate_sequence'),0,'2009-12-10 11:00:00.0',1,1,'0','Daily Pings',null)", statementList[7])
		assertEquals("INSERT INTO RECEIVER_EVENT (ID, VERSION, TIMESTAMP, RECEIVER_DEPLOYMENT_ID, RECEIVER_DOWNLOAD_ID, DATA, DESCRIPTION, UNITS) VALUES(nextval('hibernate_sequence'),0,'2009-12-10 11:00:00.0',1,1,'0','Daily Syncs',null)", statementList[8])
		assertEquals("INSERT INTO RECEIVER_EVENT (ID, VERSION, TIMESTAMP, RECEIVER_DEPLOYMENT_ID, RECEIVER_DOWNLOAD_ID, DATA, DESCRIPTION, UNITS) VALUES(nextval('hibernate_sequence'),0,'2009-12-10 11:00:00.0',1,1,'0','Daily Rejects',null)", statementList[9])
		assertEquals("INSERT INTO RECEIVER_EVENT (ID, VERSION, TIMESTAMP, RECEIVER_DEPLOYMENT_ID, RECEIVER_DOWNLOAD_ID, DATA, DESCRIPTION, UNITS) VALUES(nextval('hibernate_sequence'),0,'2009-12-11 11:00:00.0',1,1,'0.00','Memory Capacity','%')", statementList[10])
		assertEquals("INSERT INTO RECEIVER_EVENT (ID, VERSION, TIMESTAMP, RECEIVER_DEPLOYMENT_ID, RECEIVER_DOWNLOAD_ID, DATA, DESCRIPTION, UNITS) VALUES(nextval('hibernate_sequence'),0,'2009-12-11 11:00:00.0',1,1,'3.49','Battery','V')", statementList[11])
		assertEquals("INSERT INTO RECEIVER_EVENT (ID, VERSION, TIMESTAMP, RECEIVER_DEPLOYMENT_ID, RECEIVER_DOWNLOAD_ID, DATA, DESCRIPTION, UNITS) VALUES(nextval('hibernate_sequence'),0,'2009-12-11 11:00:00.0',1,1,'0','Daily Pings',null)", statementList[12])
		assertEquals("INSERT INTO RECEIVER_EVENT (ID, VERSION, TIMESTAMP, RECEIVER_DEPLOYMENT_ID, RECEIVER_DOWNLOAD_ID, DATA, DESCRIPTION, UNITS) VALUES(nextval('hibernate_sequence'),0,'2009-12-11 11:00:00.0',1,1,'0','Daily Syncs',null)", statementList[13])
	}
}
