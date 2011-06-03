package au.org.emii.aatams.upload

/**
 *
 * @author jburgess
 */
enum FileProcessingStatus 
{
    PENDING('Pending'),
    PROCESSING('Processing'),
    PROCESSED('Processed'),
    ERROR('Error')
    
    String displayStatus
    
    FileProcessingStatus(String displayStatus)
    {
        this.displayStatus = displayStatus
    }
    
    static list()
    {
        [PENDING, PROCESSING, PROCESSED, ERROR]
    }
}

