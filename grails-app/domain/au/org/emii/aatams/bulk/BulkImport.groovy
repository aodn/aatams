package au.org.emii.aatams.bulk

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.joda.time.DateTime

import au.org.emii.aatams.Organisation;

class BulkImport 
{
	Organisation organisation
	DateTime importStartDate
	DateTime importFinishDate
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
	
	String toString()
	{
		return "Organisation: " + String.valueOf(organisation) + " on " + String.valueOf(importStartDate)
	}
}
