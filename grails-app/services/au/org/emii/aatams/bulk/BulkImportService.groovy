package au.org.emii.aatams.bulk

import java.util.zip.ZipEntry
import java.util.zip.ZipException
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.joda.time.DateTime
import org.springframework.web.multipart.MultipartFile;

class BulkImportService {

    static transactional = true

    def jdbcTemplateVueDetectionFileProcessorService
    
    void process(Long bulkImportId, MultipartFile multipartFile) throws BulkImportException
    {
        BulkImport bulkImport = BulkImport.get(bulkImportId)
        
        if (!bulkImport)
        {
            throw new BulkImportException("Invalid bulk import id: " + bulkImportId)
        }
        
        File bulkImportFile
        ZipFile bulkImportZipFile
        
        long startTime = System.currentTimeMillis()
        log.info("Bulk import started: " + bulkImport.path)
         
        try
        {        
            bulkImportFile = writeFileToDisk(bulkImport, multipartFile)
            bulkImportZipFile = new ZipFile(bulkImportFile)
            
            load(bulkImportZipFile, bulkImport)
        }
        catch (ZipException e)
        {
            bulkImport.status = BulkImportStatus.ERROR
            throw new BulkImportException("Invalid zip file", e)
        }
        catch (Throwable e)
        {
            log.error("Error importing", e)
            bulkImport.status = BulkImportStatus.ERROR
            throw e
        }
        finally
        {
            bulkImportZipFile?.close()
            bulkImport.importFinishDate = new DateTime()
            bulkImport.save()
            
            log.info("Bulk import finished, elapsed time (ms): " + (System.currentTimeMillis() - startTime))
        }
    }

    private void load(ZipFile bulkImportZipFile, BulkImport bulkImport) 
    {
        [(new ReceiverLoader()): ["RECEIVERS.csv"], 
         (new InstallationLoader()): ["GROUPINGS.csv", "GROUPINGDETAIL.csv", "STATIONS.csv"],
         (new ReceiverDeploymentLoader()): ["RECEIVERDEPLOYMENTS.csv"],
         (new AnimalReleaseLoader()): ["RELEASES.csv"],
         (new DetectionLoader(jdbcTemplateVueDetectionFileProcessorService: jdbcTemplateVueDetectionFileProcessorService)): ["DETECTIONS.csv"]].each
        {
            loader, entryNames ->

            def entries = entryNames.collect { bulkImportZipFile.getEntry(it) }

            def allNonNull = true
            entries.each { allNonNull &= (it != null) }

            if (allNonNull)
            {
                log.info "loading ${entryNames}..."
                loader.load([bulkImport: bulkImport], entries.collect { bulkImportZipFile.getInputStream(it) } )
            }
        }

        bulkImport.status = BulkImportStatus.SUCCESS
    }

    private File writeFileToDisk(BulkImport bulkImport, MultipartFile multipartFile) 
    {
        File outFile = new File(bulkImport.path)
        outFile.getParentFile().mkdirs()
        multipartFile.transferTo(outFile)
        
        return outFile
    }
}
