package au.org.emii.aatams.bulk

class BulkImportJob 
{
	// Avoid concurrent modification exceptions (#1466).
	def concurrent = false
	
	// This job is triggered dynamically from the bulk import controller.
	static triggers = {}
	
	def bulkImportService
	
	def execute(context)
	{
		try
		{
			bulkImportService.process(context.mergedJobDataMap.get('bulkImportId'),
									  context.mergedJobDataMap.get('multipartFile'))
		}
		catch (BulkImportException e)
		{
			log.error(e)
			
			def bulkImport = BulkImportException.getAt(context.mergedJobDataMap.get('bulkImportId'))
			bulkImport?.status = BulkImportStatus.ERROR
			bulkImport?.save()
		}
	}
}
