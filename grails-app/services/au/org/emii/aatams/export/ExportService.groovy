package au.org.emii.aatams.export

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import au.org.emii.aatams.InstallationStation
import au.org.emii.aatams.Person
import au.org.emii.aatams.Receiver
import au.org.emii.aatams.report.KmlService
import au.org.emii.aatams.report.ReportInfoService
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.Kml
import de.micromata.opengis.kml.v_2_2_0.ScreenOverlay;
import de.micromata.opengis.kml.v_2_2_0.Units;
import de.micromata.opengis.kml.v_2_2_0.Vec2

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

import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class ExportService implements ApplicationContextAware
{
	ApplicationContext applicationContext
	
	def grailsApplication
	def kmlService
	def permissionUtilsService
	def reportInfoService
	def queryService
	
    void export(Class clazz, Map params, OutputStream out) 
	{
		long startTimestamp = System.currentTimeMillis()
		
		if (params.format == "KMZ")
		{
			generateKmz(clazz, params, out)
		}
		else
		{
			params.putAll([REPORT_USER: permissionUtilsService.principal()?.username, FILTER_PARAMS: getFilterParamsInReportFormat(params).entrySet(), SUBREPORT_DIR: "web-app/reports/"])
			
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
	
	private JRDataSource getDataSource(queryService, clazz, params)
	{
		// Don't use this for now, as filtering out of embargoed entities
		// is not being handled properly (it's causing duplicates to appear in 
		// the report).
		//
		// Both of the big reports (detection and event) are not handled
		// by this class in any case.
//		return new PagedBeanDataSource(queryService, clazz, params)
		return new JRBeanCollectionDataSource(queryService.query(clazz, params).results)
	}
	
	private Map getFilterParamsInReportFormat(params)
	{
		// Insert the user in to filter params passed to Jasper.
		def filterParams = [:]

		Person person = permissionUtilsService.principal()
		if (person)
		{
			filterParams.user = person.name
		}

		filterParams = filterParams + reportInfoService.filterParamsToReportFormat(params.filter)

		return filterParams
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
	
	private InputStream getReportStream(clazz, params)
	{
		return new FileInputStream(applicationContext.getResource("/reports/" + reportInfoService.getReportInfo(clazz).jrxmlFilename[params.format] + ".jrxml")?.getFile())
	}
	
	private Kml generateKml(clazz, params, out)
	{
		def kml
		def result = queryService.query(clazz, params).results
		if (clazz == InstallationStation)
		{
			InstallationStation.refreshDetectionCounts()
			kml = kmlService.toKml(result)
		}
		else
		{
			kml = clazz.toKml(result, grailsApplication.config.grails.serverURL)	
		}
		
		return kml
	}
	
	private generateKmz(clazz, params, out)
	{
		ZipOutputStream kmzStream = new ZipOutputStream(out)
		
		// Write KML file itself.
		ZipEntry kmlEntry = new ZipEntry("doc.kml")
		kmzStream.putNextEntry(kmlEntry)
		def result = queryService.query(clazz, params).results
		def kml = generateKml(clazz, params, out)
		
		Icon imosIcon = new Icon().withHref("files/IMOS-logo.png")
		ScreenOverlay imosOverlay = 
			kml.getFeature().createAndAddScreenOverlay()
		imosOverlay.setIcon(imosIcon)	
		imosOverlay.setOverlayXY(new Vec2().withX(0).withY(0).withXunits(Units.FRACTION).withYunits(Units.FRACTION))
		imosOverlay.setScreenXY(new Vec2().withX(0.025).withY(0.05).withXunits(Units.FRACTION).withYunits(Units.FRACTION))
		
		kml.marshal(kmzStream)
		kmzStream.closeEntry()
		
		// "files" directory.
		ZipEntry filesEntry = new ZipEntry("files/")
		kmzStream.putNextEntry(filesEntry)
		kmzStream.closeEntry()
		
		// Style sheet.
		ZipEntry mainCssEntry = new ZipEntry("files/main.css")
		kmzStream.putNextEntry(mainCssEntry)
		def mainCssInputStream = applicationContext.getResource("/css/main.css").getInputStream()
		IOUtils.copy(mainCssInputStream, kmzStream)
		kmzStream.closeEntry()
		
		// IMOS logo.
		ZipEntry imosLogoEntry = new ZipEntry("files/IMOS-logo.png")
		kmzStream.putNextEntry(imosLogoEntry)
		def imosLogoInputStream = applicationContext.getResource("/images/IMOS-logo.png").getInputStream()
		IOUtils.copy(imosLogoInputStream, kmzStream)
		kmzStream.closeEntry()
		
		kmzStream.close()
	}
}
