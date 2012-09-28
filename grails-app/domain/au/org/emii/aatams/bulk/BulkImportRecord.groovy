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
		dstClass(nullable: true)
		dstPk(nullable: true)
		srcPk(nullable: true)
		srcModifiedDate(nullable: true)
    }

	def beforeDelete()
	{
		if (this.type == BulkImportRecordType.NEW)
		{
			Class dstClazz = grailsApplication.getClassForName(dstClass)

			dstClazz.withNewSession
			{			
				def dstObject = dstClazz.get(dstPk)
				
				if (!dstObject)
				{
					// This is ok, the destination object has already been deleted as part of a cascade
					// delete of parent entity.
					log.debug("No destination object, dstClass: ${dstClass}, pk: ${dstPk}")
				}
				else
				{
					log.debug("Deleting: " + String.valueOf(dstObject))
					dstObject.delete(failOnError:true, flush: true)
				}
			}
		}
	}
	
	String toString()
	{
		return "[srcTable: ${srcTable}, srcPk: ${srcPk}, dstPk: ${dstPk}, type: ${type}]"
	}
}
