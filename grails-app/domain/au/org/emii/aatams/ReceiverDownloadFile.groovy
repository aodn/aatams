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
    String path
    
    String name
    Date importDate
    
    FileProcessingStatus status
    
    String errMsg
    
    Person requestingUser
    
    static belongsTo = [receiverDownload: ReceiverDownload]

    static constraints =
    {
        type()
        path()
    }
    
    static mapping =
    {
        errMsg type: 'text'
    }
    
//    ReceiverDownloadFile(String fullPath, String name)
//    {
//        this.importDate = new Date()
//        this.name = name
//        this.path = fullPath
//        this.type = ReceiverDownloadFileType.fromPath(fullPath)
//        this.status = FileProcessingStatus.PENDING
//    }
    
    String toString()
    {
        return String.valueOf(path)
    }
}
