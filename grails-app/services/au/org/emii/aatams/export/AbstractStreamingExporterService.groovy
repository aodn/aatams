package au.org.emii.aatams.export

import java.util.zip.GZIPOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.servlet.http.Cookie

import org.codehaus.groovy.grails.commons.ConfigurationHolder

abstract class AbstractStreamingExporterService
{
    protected abstract void writeCsvHeader(OutputStream out)
    protected abstract def writeCsvChunk(resultList, OutputStream out)
    protected def applyEmbargo(results, params) { return results }
    protected abstract def readData(filterParams)
    protected abstract String getReportName()

    protected generateReport(params, req, res)
    {
        long startTime = System.currentTimeMillis()

        res.reset()
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
            params.response = res
            writeCsvData(params, out)
        }
        finally
        {
            // Write the compression trailer and close the output stream
            out.close()
            res.flushBuffer()

            log.info("Elapsed time (ms): " + (System.currentTimeMillis() - startTime))
        }
    }

    protected int getLimit()
    {
        return ConfigurationHolder.config.detection.extract.limit
    }

    protected void writeCsvData(final params, OutputStream out)
    {
        params.limit = getLimit()
        params.offset = 0

        def data = readData(params)
        def results = data.results
        def rowCount = data.rowCount
        params.offset = params.offset + rowCount

        indicateExportStart(params)
        writeCsvHeader(out)

        while (results.size() > 0)
        {
            writeCsvChunk(results, out)

            data = readData(params)
            results = data.results
            rowCount = data.rowCount
            params.offset = params.offset + rowCount
        }
    }

    protected void indicateExportStart(params)
    {
        // Indicate to the client that we have received the export request.
        // See: http://geekswithblogs.net/GruffCode/archive/2010/10/28/detecting-the-file-download-dialog-in-the-browser.aspx
        params.response.addCookie(new Cookie("fileDownloadToken", params.downloadTokenValue))
    }
}
