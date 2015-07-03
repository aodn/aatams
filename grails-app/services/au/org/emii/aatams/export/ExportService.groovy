package au.org.emii.aatams.export

import net.sf.jasperreports.engine.JRDataSource
import net.sf.jasperreports.engine.JRExporter
import net.sf.jasperreports.engine.JRExporterParameter
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.design.JasperDesign
import net.sf.jasperreports.engine.export.JRCsvExporter
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.engine.xml.JRXmlLoader

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH

import au.org.emii.aatams.Person

class ExportService implements ApplicationContextAware {
    ApplicationContext applicationContext
    
    def grailsApplication
    def kmlService
    def permissionUtilsService
    def reportInfoService
    def queryService
    
    void export(Class clazz, Map params, OutputStream out)  {
        long startTimestamp = System.currentTimeMillis()
        
        if (kmlService.isSupportedFormat(params.format)) {
            kmlService.export(clazz, params, out)    
        }
        else {
            params.putAll([REPORT_USER: permissionUtilsService.principal()?.username, 
                           FILTER_PARAMS: getFilterParamsInReportFormat(params).entrySet(), 
                           SUBREPORT_DIR: getSubreportDir()])
            
            InputStream reportStream = getReportStream(clazz, params)
            assert(reportStream)
            JasperDesign jasperDesign = JRXmlLoader.load(reportStream)
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign)
            assert(jasperReport)
            
            JRDataSource ds = getDataSource(queryService, clazz, params)
            
            // Copy the map, otherwise we get a self reference for one of the entries (which causes
            // stackoverflow when traversing the map).
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(params), ds)
            
            JRExporter exporter = getExporter(params)
            
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint)
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out)
            
            exporter.exportReport()
        }
        
        log.info("Export completed in (ms): " + (System.currentTimeMillis() - startTimestamp))
    }
    
    private String getSubreportDir() {
        def servletContext = SCH.servletContext
        return servletContext.getRealPath('/reports') + "/"
    }
    
    private JRDataSource getDataSource(queryService, clazz, params) {
        // Don't use this for now, as filtering out of embargoed entities
        // is not being handled properly (it's causing duplicates to appear in 
        // the report).
        //
        // Both of the big reports (detection and event) are not handled
        // by this class in any case.
//        return new PagedBeanDataSource(queryService, clazz, params)
        return new JRBeanCollectionDataSource(queryService.query(clazz, params).results)
    }
    
    private Map getFilterParamsInReportFormat(params) {
        // Insert the user in to filter params passed to Jasper.
        def filterParams = [:]

        Person person = permissionUtilsService.principal()
        if (person) {
            filterParams.user = person.name
        }

        filterParams = filterParams + reportInfoService.filterParamsToReportFormat(params.filter)

        return filterParams
    }
    
    private JRExporter getExporter(params) {
        if (!params.format) {
            throw new IllegalArgumentException("Export format not specified.")
        }
        else if (params.format == "CSV") {
            return new JRCsvExporter()
        }
        else if (params.format == "PDF") {
            return new JRPdfExporter()
        }
        else {
            throw new IllegalArgumentException("Unsupported export format: " + params.format)
        }
    }
    
    private InputStream getReportStream(clazz, params) {
        return new FileInputStream(applicationContext.getResource("/reports/" + reportInfoService.getReportInfo(clazz).jrxmlFilename[params.format] + ".jrxml")?.getFile())
    }
}
