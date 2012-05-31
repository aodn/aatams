package au.org.emii.aatams.bulk

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import au.org.emii.aatams.Organisation;

class BulkImport 
{
	Organisation organisation
	Date importStartDate
	Date importFinishDate
	BulkImportStatus status
	String filename
	
	static hasMany = [records: BulkImportRecord]
	static transients = ['path']
	
    static constraints = 
	{
		importFinishDate(nullable:true)
    }
	
	String getPath()
	{
		return ConfigurationHolder.config.bulkimport.path + File.separator + id + File.separator + filename
	}
}
