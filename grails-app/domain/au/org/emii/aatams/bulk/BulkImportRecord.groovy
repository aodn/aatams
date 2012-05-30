package au.org.emii.aatams.bulk

class BulkImportRecord 
{
	String srcTable
	Long srcPk
	Date srcModifiedDate
	
	String dstClass
	Long dstPk
	BulkImportRecordType type
	
	static belongsTo = [bulkImport: BulkImport]
	
    static constraints = 
	{
		dstClass(nullable:true)
		dstPk(nullable:true)
    }
}
