package au.org.emii.aatams.bulk

import org.apache.commons.io.IOUtils;

import au.org.emii.aatams.ReceiverDownloadFile
import au.org.emii.aatams.ReceiverDownloadFileType

class DetectionLoader extends AbstractLoader
{
    def jdbcTemplateVueDetectionFileProcessorService
    
    void load(Map context, List<InputStream> streams) throws BulkImportException
    {
        // Create a ReceiverDownloadFile...
        ReceiverDownloadFile download
        
        ReceiverDownloadFile.withNewTransaction
        {
            download = new ReceiverDownloadFile(type: ReceiverDownloadFileType.CSIRO_DETECTIONS_CSV)
            download.initialiseForProcessing("CSIRO_bulk_upload")
            download.save(flush: true, failOnError: true)
        }

        download = ReceiverDownloadFile.read(download.id)
                
        BulkImportRecord importRecord = 
            new BulkImportRecord(
                bulkImport: context.bulkImport,
                srcTable: "DETECTIONS",
                dstClass: "au.org.emii.aatams.ReceiverDownloadFile",
                dstPk: download.id,
                type: BulkImportRecordType.NEW)
        
        importRecord.save(failOnError: true)    
            
        // Save stream to file...
        File outfile = new File(download.path)
        outfile.getParentFile().mkdirs()
        outfile.createNewFile()
        
        IOUtils.copy(streams[0], new BufferedOutputStream(new FileOutputStream(outfile)))
        
        // Delegate to fileProcessorService
        jdbcTemplateVueDetectionFileProcessorService.process(download)
        
    }
}