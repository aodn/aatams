package au.org.emii.aatams.detection

import java.util.List;
import java.util.Map;

import au.org.emii.aatams.*
import grails.plugin.searchable.SearchableService
import grails.test.*
import org.joda.time.DateTime

abstract class AbstractVueDetectionFileProcessorServiceTests extends GrailsUnitTestCase 
{
    def detectionFactoryService
    def vueDetectionFileProcessorService
	
    def searchableService
    
    Receiver receiver
    ReceiverDeployment deployment
    ReceiverRecovery recovery
	
    protected void setUp() 
	{
        super.setUp()
		
        mockLogging(SearchableService, true)
        searchableService = new SearchableService()
        searchableService.metaClass.startMirroring = {}
        searchableService.metaClass.stopMirroring = {}
        assert(searchableService)
        
        mockLogging(Surgery)
        
        mockDomain(RawDetection)
        mockDomain(InvalidDetection)
        mockDomain(ValidDetection)
        
        mockDomain(DetectionSurgery)
        mockDomain(DeviceStatus)
        mockDomain(Sensor)
        mockDomain(Surgery)
        mockDomain(Tag)
        
		
		ReceiverDeviceModel vr3uwm = new ReceiverDeviceModel(modelName: "VR3UWM")
		mockDomain(ReceiverDeviceModel, [vr3uwm])
		vr3uwm.save()
		
        receiver = new Receiver(serialNumber:"354", model: vr3uwm)
        mockDomain(Receiver, [receiver])
        
        deployment = new ReceiverDeployment(receiver:receiver,
                                            deploymentDateTime:new DateTime("2008-12-08T06:44:24"))
                                        
        receiver.addToDeployments(deployment)
        mockDomain(ReceiverDeployment, [deployment])

        recovery = new ReceiverRecovery(deployment:deployment,
                                        recoveryDateTime:new DateTime("2010-12-08T06:44:24"))
        mockDomain(ReceiverRecovery, [recovery])
        recovery.save()
        
        receiver.save()
        
        deployment.recovery = recovery
        deployment.save()
		
		AbstractBatchProcessor.metaClass.getReader = { getReader(it) }
		AbstractBatchProcessor.metaClass.getNumRecords = { 7 }
		mockDomain(ReceiverDownloadFileProgress)
		ReceiverDownloadFileProgress.metaClass.static.withNewTransaction = { it() }
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

	public Reader getReader(downloadFile)
	{
		return new StringReader(getData())
	}
	
	protected String getData() 
	{
		return '''Date and Time (UTC),Receiver,Transmitter,Transmitter Name,Transmitter Serial,Sensor Value,Sensor Unit,Station Name,Latitude,Longitude,
2009-12-08 06:44:24,VR3UWM-354,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
2009-12-08 06:44:24,VR3UWM-354,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
2009-12-08 06:44:24,AAA-111,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
2009-12-08 06:44:24,BBB-111,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
2009-12-08 06:47:24,BBB-111,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
2007-12-08 06:44:24,VR3UWM-354,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234
2010-12-08 06:44:24,VR3UWM-354,A69-1303-62347,shark tag,1234,,,Neptune SW 1,-40.1234,45.1234'''
	}
}
