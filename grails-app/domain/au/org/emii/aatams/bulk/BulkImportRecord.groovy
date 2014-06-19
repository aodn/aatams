package au.org.emii.aatams.bulk

import grails.util.GrailsNameUtils;

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
    
    static transients = ['dstClazz', 'dstController', 'dstObject']

    def beforeDelete()
    {
        if (this.type == BulkImportRecordType.NEW)
        {
            dstClazz.withNewSession
            {            
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
    
    Class getDstClazz()
    {
        return grailsApplication.getClassForName(dstClass)
    }
    
    Object getDstObject()
    {
        return dstClazz.get(dstPk)
    }
    
    /**
     * Trick to make 'g' resolve to the ApplicationTagLib.
     */
    private getG() 
    {
        grailsApplication.mainContext.getBean(
            'org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')
    }
    
    String getDstController()
    {
        return GrailsNameUtils.getPropertyNameRepresentation(dstClazz)
    }
 
    String toString()
    {
        return "[srcTable: ${srcTable}, srcPk: ${srcPk}, dstPk: ${dstPk}, dstClass: ${dstClass}, type: ${type}]"
    }
}
