package au.org.emii.aatams


/**
 * Process an event file downloaded from receiver's VUE application.
 */
class VueEventFileProcessorService extends AbstractBatchProcessor
{
    def eventFactoryService
    
    static transactional = true
    
    void processSingleRecord(downloadFile, map)
    {
        eventFactoryService.newEvent(downloadFile, map)
    }
}
