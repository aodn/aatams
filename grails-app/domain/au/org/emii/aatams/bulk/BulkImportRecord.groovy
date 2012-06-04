package au.org.emii.aatams.bulk

import org.joda.time.DateTime

class BulkImportRecord 
{
	String srcTable
	Long srcPk
	DateTime srcModifiedDate
	
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
