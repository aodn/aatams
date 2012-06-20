package au.org.emii.aatams

import org.springframework.jdbc.core.JdbcTemplate

class JdbcTemplateVueEventFileProcessorService extends VueEventFileProcessorService
{
    static transactional = true
	
	def dataSource
	def jdbcTemplateEventFactoryService
	
//	List<Map> eventBatch
	
	protected int getBatchSize()
	{
		return 10000
	}
	
	protected void startBatch(context)
	{
		log.debug("Start batch")
		
		// Create lists
		context.eventBatch = new ArrayList<Map>()
	}
	
	protected void endBatch(context)
	{
		log.debug("End batch, inserting events...")
		insertEvents(context)
	}
	
	private void insertEvents(context)
	{
		def insertStatementList = context.eventBatch.collect
		{
			ReceiverEvent.toSqlInsert(it)
		}
		
		batchUpdate(insertStatementList.toArray(new String[0]))
	}
	
	void batchUpdate(String[] statements)
	{
		log.debug("Inserting " + statements.size() + " records...")
		JdbcTemplate insert = new JdbcTemplate(dataSource)
		insert.batchUpdate(statements)
		log.debug("Batch successfully inserted")
	}
	
    void processSingleRecord(downloadFile, map, context)
    {
		def event = jdbcTemplateEventFactoryService.newEvent(downloadFile, map)
        context.eventBatch.addAll(event)
    }
}
