package au.org.emii.aatams.bulk

import au.org.emii.aatams.Organisation;

class BulkImport 
{
	Organisation organisation
	Date importStartDate
	Date importFinishDate
	BulkImportStatus status
	
	static hasMany = [records: BulkImportRecord]
	
    static constraints = 
	{
		importFinishDate(nullable:true)
    }
}
