package au.org.emii.aatams.bulk

import org.joda.time.DateTime

class BulkImportRecord 
{
	def grailsApplication
	
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

	def afterDelete()
	{
		if (this.type == BulkImportRecordType.NEW)
		{
			Class dstClazz = grailsApplication.getClassForName(dstClass)
			log.debug "Deleting for class: " + dstClazz + ", pk: " + dstPk
			
			def dstObject = dstClazz.get(dstPk)
				
			assert(dstObject)
			dstObject.delete(failOnError:true)
		}
	}
}
