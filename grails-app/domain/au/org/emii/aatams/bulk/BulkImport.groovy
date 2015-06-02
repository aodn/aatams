package au.org.emii.aatams.bulk

import org.joda.time.DateTime

import au.org.emii.aatams.Organisation

class BulkImport
{
    def grailsApplication
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
        return grailsApplication.config.bulkimport.path + File.separator + id + File.separator + filename
    }

    String toString()
    {
        return "Organisation: " + String.valueOf(organisation) + " on " + String.valueOf(importStartDate)
    }
}
