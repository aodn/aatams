package au.org.emii.aatams.export

import grails.plugin.executor.SessionBinderUtils;
import grails.plugin.executor.SessionBoundRunnable

import java.io.OutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

import org.codehaus.groovy.grails.commons.ConfigurationHolder

abstract class AbstractStreamingExporterService 
{
	protected abstract void writeCsvHeader(OutputStream out)
	protected abstract def writeCsvChunk(resultList, OutputStream out)
	protected void applyEmbargo(results, params) {}
	protected abstract List readData(filterParams)
	protected abstract String getReportName()
	
	protected generateReport(params, req, res)
	{
		long startTime = System.currentTimeMillis()
		
		OutputStream out = null
		
		// Select the appropriate content encoding based on the
		// client's Accept-Encoding header. Choose GZIP if the header
		// includes "gzip". Choose ZIP if the header includes "compress".
		// Choose no compression otherwise.
		String encodings = req.getHeader("Accept-Encoding");
		if (encodings != null && encodings.indexOf("gzip") != -1)
		{
			// Go with GZIP
			res.setHeader("Content-Encoding", "gzip");
			out = new GZIPOutputStream(res.getOutputStream());
		}
		else if (encodings != null && encodings.indexOf("compress") != -1)
		{
			// Go with ZIP
			res.setHeader("Content-Encoding", "x-compress");
			out = new ZipOutputStream(res.getOutputStream());
			((ZipOutputStream)out).putNextEntry(new ZipEntry("dummy name"));
		}
		else
		{
			// No compression
			out = res.getOutputStream();
		}

		res.setHeader("Vary", "Accept-Encoding");
		res.setHeader("Content-disposition", "attachment; filename=" + getReportName() + ".csv")
		res.contentType = "text/csv"
		res.characterEncoding = "UTF-8"

		try
		{
			writeCsvHeader(out)
			writeCsvData(params, out)
		}
		finally
		{
			// Write the compression trailer and close the output stream
			out.close()
			log.info("Elapsed time (ms): " + (System.currentTimeMillis() - startTime))
		}
	}

	protected int getLimit()
	{
		return ConfigurationHolder.config.rawDetection.extract.limit
	}
	
	protected void writeCsvData(final filterParams, OutputStream out)
	{
		filterParams.max = getLimit()
		filterParams.offset = 0

		def results = readData(filterParams)
		filterParams.offset = filterParams.offset + results.size()
		
		while (results.size() > 0)
		{
			applyEmbargo(results, filterParams)
			writeCsvChunk(results, out)
			
			results = readData(filterParams)
			filterParams.offset = filterParams.offset + results.size()
		}		
	}
}
