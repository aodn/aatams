package au.org.emii.aatams.bulk

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
		
		writeFileToDisk(bulkImport, multipartFile)

		ZipArchiveInputStream zipStream
		try
		{		
			InputStream multipartStream = multipartFile.getInputStream()
			zipStream = new ZipArchiveInputStream(multipartStream)
			
			ZipArchiveEntry zipEntry = zipStream.getNextZipEntry()
			while (zipEntry != null)
			{
				if (zipEntry.getName() == "RECEIVERS.csv")
				{
					processReceivers(null)
				}
				
				zipEntry = zipStream.getNextZipEntry()
			}
		}
		finally
		{
			zipStream.close()
		}
    }

	private void writeFileToDisk(BulkImport bulkImport, MultipartFile multipartFile) 
	{
		File outFile = new File(bulkImport.path)
		outFile.getParentFile().mkdirs()
		multipartFile.transferTo(outFile)
	}
	
	private void processReceivers(InputStream receiverStream)
	{
		
	}
}
