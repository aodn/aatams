package au.org.emii.aatams.export

import au.org.emii.aatams.Receiver
import au.org.emii.aatams.report.ReportInfoService

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporter
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperFillManager 
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.JRExporterParameter
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.design.JasperDesign
import net.sf.jasperreports.engine.export.JRCsvExporter
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRCsvMetadataExporter;
import net.sf.jasperreports.engine.export.JRCsvMetadataExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class ExportService implements ApplicationContextAware
{
	ApplicationContext applicationContext
	def reportInfoService
	def queryService
	
    void export(Class clazz, Map params, OutputStream out) 
	{
		params.putAll([REPORT_USER: "jbloggs", FILTER_PARAMS: params.filter?.entrySet(), SUBREPORT_DIR: "web-app/reports/"])
		
		InputStream reportStream = getReportStream()
		assert(reportStream)
		JasperDesign jasperDesign = JRXmlLoader.load(reportStream)
		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign)
		assert(jasperReport)
		
		JRDataSource ds = new PagedBeanDataSource(queryService, clazz, params)
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds)
		
		JRExporter exporter = getExporter(params)
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint)
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out)
		
		exporter.exportReport()
    }
	
	private JRExporter getExporter(params)
	{
		if (!params.format)
		{
			throw new IllegalArgumentException("Export format not specified.")
		}
		else if (params.format == "CSV")
		{
			return new JRCsvExporter()
		}
		else if (params.format == "PDF")
		{
			return new JRPdfExporter()
		}
		else
		{
			throw new IllegalArgumentException("Unsupported export format: " + params.format)
		}
	}
	
	private InputStream getReportStream()
	{
		return new FileInputStream(applicationContext.getResource("/WEB-INF/reports/receiverList.jrxml")?.getFile())
	}
}
