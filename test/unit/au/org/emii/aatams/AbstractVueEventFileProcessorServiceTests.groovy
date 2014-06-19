package au.org.emii.aatams

import java.util.List;
import java.util.Map;

import grails.plugin.searchable.SearchableService
import grails.test.GrailsUnitTestCase;
import org.joda.time.DateTime

abstract class AbstractVueEventFileProcessorServiceTests extends GrailsUnitTestCase 
{
    def eventFactoryService
    def searchableService
    def vueEventFileProcessorService
    
    ReceiverDownloadFile download
    
    protected void setUp()
    {
        super.setUp()
        mockLogging(SearchableService, true)
        searchableService = new SearchableService()
        searchableService.metaClass.startMirroring = {}
        searchableService.metaClass.stopMirroring = {}
        assert(searchableService)

        registerMetaClass AbstractBatchProcessor
        AbstractBatchProcessor.metaClass.getReader = { getReader(it) }
        AbstractBatchProcessor.metaClass.getNumRecords = { 16 }
        AbstractBatchProcessor.metaClass.flushSession = {  }
        
        mockDomain(ReceiverDownloadFileProgress)
        ReceiverDownloadFileProgress.metaClass.static.withNewTransaction = { it() }
        
        ReceiverDeviceModel model = new ReceiverDeviceModel(modelName:"VR2W")
        mockDomain(ReceiverDeviceModel, [model])
        model.save()
        
        Receiver receiver = new Receiver(model: model, serialNumber: "103335")
        def receiverList = [receiver]
        mockDomain(Receiver, receiverList)

        ReceiverRecovery recovery = new ReceiverRecovery(recoveryDateTime: new DateTime("2010-12-08T01:45:29"))
        mockDomain(ReceiverRecovery, [recovery])
        recovery.save()
        
        ReceiverDeployment deployment = new ReceiverDeployment(recovery: recovery, receiver: receiver, 
                                                               deploymentDateTime: new DateTime("2009-12-08T01:45:29"),
                                                               initialisationDateTime: new DateTime("2009-12-08T00:45:29"))
        mockDomain(ReceiverDeployment, [deployment])
        deployment.save()
        
        recovery.deployment = deployment
        recovery.save()
        
        receiver.addToDeployments(deployment)
        receiverList.each { it.save() }
        
        mockDomain(ReceiverEvent)
        mockDomain(ValidReceiverEvent)
        mockDomain(InvalidReceiverEvent)
        
        download = new ReceiverDownloadFile()
        mockDomain(ReceiverDownloadFile, [download])
        download.importDate = new Date()
        download.name = "My Download File"
        download.type = ReceiverDownloadFileType.EVENTS_CSV
        download.metaClass.toString = { super.toString() }
        download.save()
    }

    protected void tearDown()
    {
        super.tearDown()
    }

    public Reader getReader(downloadFile)
    {
        return new StringReader('''Date/Time,Receiver,Description,Data,Units,
2009-12-09 01:45:29,VR2W-103335,Initialization,,
2009-12-09 01:45:29,VR2W-103335,PC Time,2009-12-09 12:45:29+11:00,
2009-12-09 01:45:29,VR2W-103335,Map,MAP-110 (A69-1008; A69-1105; A69-1206; A69-1303),
2009-12-09 01:45:29,VR2W-103335,Blanking,240,ms
2009-12-09 01:45:29,VR2W-103335,Station,VR2W-NN-No_6,
2009-12-10 00:00:00,VR2W-103335,Memory Capacity,0.00,%
2009-12-10 00:00:00,VR2W-103335,Battery,3.50,V
2009-12-10 00:00:00,VR2W-103335,Daily Pings,0,
2009-12-10 00:00:00,VR2W-103335,Daily Syncs,0,
2009-12-10 00:00:00,VR2W-103335,Daily Rejects,0,
2009-12-11 00:00:00,VR2W-103335,Memory Capacity,0.00,%
2009-12-11 00:00:00,VR2W-103335,Battery,3.49,V
2009-12-11 00:00:00,VR2W-103335,Daily Pings,0,
2009-12-11 00:00:00,VR2W-103335,Daily Syncs,0,
2009-12-11 16:47:33,VR2W-103335,Initialization,VEMCO,Shad Bay, NS, Canada,
2009-12-11 16:47:33,VR2W-103335,Map,Custom Map (A69-1105; A69-1303),''')
    }
}
