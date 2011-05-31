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
    
    static constraints =
    {
        type()
        path()
    }
    
    String toString()
    {
        return path.getFile()
    }
}
