package au.org.emii.aatams

/**
 * Index (and meta-data) to a file which has been downloaded from a receiver as
 * part of the recovery process.
 */
class ReceiverDownloadFile 
{
    ReceiverDownloadFileType type
    
    /**
     * Path (including filename) to file.
     */
    URL path
    
    String name
    Date importDate
    
    FileProcessingStatus status

    static constraints =
    {
        type()
        path()
    }
    
    ReceiverDownloadFile(String fullPath, String name)
    {
        this.importDate = new Date()
        this.name = name
        this.path = new URL("file://" + fullPath)
        this.type = ReceiverDownloadFileType.fromPath(fullPath)
        this.status = FileProcessingStatus.PENDING
    }
    
    String toString()
    {
        return String.valueOf(path)
    }
}
