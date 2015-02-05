package au.org.emii.aatams.export

import au.org.emii.aatams.sql.Sql

import java.util.zip.GZIPOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.servlet.http.Cookie

import org.codehaus.groovy.grails.commons.ConfigurationHolder

abstract class AbstractStreamingExporterService
{
    def dataSource

    protected abstract void writeCsvHeader(OutputStream out)
    protected abstract def writeCsvRow(resultList, OutputStream out)
    protected def applyEmbargo(results, params) { return results }
    protected abstract def eachRow(filterParams, closure)
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

    protected int getFetchSize()
    {
        return ConfigurationHolder.config.detection.extract.fetchSize
    }

    protected void writeCsvData(final params, OutputStream out)
    {
        params.sql = new Sql(dataSource)
        params.sql.fetchSize = getFetchSize()
        params.sql.autoCommit = false

        indicateExportStart(params)
        writeCsvHeader(out)

        eachRow(params) { row ->
            writeCsvRow(row, out)
        }
    }

    protected void indicateExportStart(params)
    {
        // Indicate to the client that we have received the export request.
        // See: http://geekswithblogs.net/GruffCode/archive/2010/10/28/detecting-the-file-download-dialog-in-the-browser.aspx
        params.response.addCookie(new Cookie("fileDownloadToken", params.downloadTokenValue))
    }
}
