package au.org.emii.aatams.bulk

import java.util.zip.ZipEntry
import java.util.zip.ZipException
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.springframework.web.multipart.MultipartFile;

class BulkImportService {

    static transactional = true

    void process(Long bulkImportId, MultipartFile multipartFile) throws BulkImportException
	{
		BulkImport bulkImport = BulkImport.get(bulkImportId)
		
		if (!bulkImport)
		{
			throw new BulkImportException("Invalid bulk import id: " + bulkImportId)
		}
		
		File bulkImportFile
		ZipFile bulkImportZipFile
		
		try
		{		
			bulkImportFile = writeFileToDisk(bulkImport, multipartFile)
			bulkImportZipFile = new ZipFile(bulkImportFile)
			
			ZipEntry receiversEntry = bulkImportZipFile.getEntry("RECEIVERS.csv")
			if (receiversEntry)
			{
				processReceivers([bulkImport: bulkImport], bulkImportZipFile.getInputStream(receiversEntry))
			}
		}
		catch (ZipException e)
		{
			throw new BulkImportException("Invalid zip file", e)
		}
		finally
		{
			bulkImportZipFile?.close()
		}
    }

	private File writeFileToDisk(BulkImport bulkImport, MultipartFile multipartFile) 
	{
		File outFile = new File(bulkImport.path)
		outFile.getParentFile().mkdirs()
		multipartFile.transferTo(outFile)
		
		return outFile
	}
	
	private void processReceivers(Map context, InputStream receiverStream) throws BulkImportException
	{
		if (receiverStream == null)
		{
			throw new BulkImportException("Invalid receivers data.")
		}
		
		new ReceiverLoader().load(context, receiverStream)
	}
}
