package au.org.emii.aatams

/**
 *
 * @author jburgess
 */
enum FileProcessingStatus 
{
    ARCHIVED('Archived'),   // But not processed, as such.
    PENDING('Pending'),
    PROCESSING('Processing'),
    PROCESSED('Processed'),
    ERROR('Error'),
    DELETING('Deleting')
    
    String displayStatus
    
    FileProcessingStatus(String displayStatus)
    {
        this.displayStatus = displayStatus
    }
    
    static list()
    {
        [ARCHIVED, PENDING, PROCESSING, PROCESSED, ERROR, DELETING]
    }
}

