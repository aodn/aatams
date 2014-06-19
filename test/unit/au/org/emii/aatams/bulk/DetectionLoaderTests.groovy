package au.org.emii.aatams.bulk

import au.org.emii.aatams.ReceiverDownloadFile

class DetectionLoaderTests extends AbstractLoaderTests 
{
    protected void setUp() 
    {
        super.setUp()
        
        loader = new DetectionLoader()

        mockDomain(ReceiverDownloadFile)
        
        mockConfig("fileimport.path = '/tmp/fileProcessorServiceTests'")
        ReceiverDownloadFile.metaClass.getPath = { '/tmp/detectionLoaderTests' }
        ReceiverDownloadFile.metaClass.static.withNewTransaction = { it.call() }
    }
    
    protected void tearDown()
    {
        super.tearDown()
    }
    
    void testLoadDetections()
    {
        def detectionsText = '''"DET_ID","ACO_ID","RCD_ID","DET_DATETIME","DET_QUALITY_CODE","DET_TEMP","DET_DEPTH","DET_RAW","DET_RECORD_ID","DET_CORR_STATUS_CODE"
98472,1063,344,3/6/2008 8:01:53,,0,0,0,,
'''
        boolean processCalled = false
        
        def detectionProcessor = new Object()
        detectionProcessor.metaClass.process = {
            
            ReceiverDownloadFile download ->
            
            assertNotNull(download)
            processCalled = true
        }
        
        loader.jdbcTemplateVueDetectionFileProcessorService = detectionProcessor
        
        load([detectionsText])
        assertTrue(processCalled)
    }
}
