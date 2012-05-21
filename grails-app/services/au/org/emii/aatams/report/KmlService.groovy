package au.org.emii.aatams.report

import au.org.emii.aatams.*
import au.org.emii.aatams.detection.ValidDetection
import de.micromata.opengis.kml.v_2_2_0.Document
import de.micromata.opengis.kml.v_2_2_0.Folder
import de.micromata.opengis.kml.v_2_2_0.Icon
import de.micromata.opengis.kml.v_2_2_0.Kml
import de.micromata.opengis.kml.v_2_2_0.Placemark
import de.micromata.opengis.kml.v_2_2_0.ScreenOverlay
import de.micromata.opengis.kml.v_2_2_0.Units
import de.micromata.opengis.kml.v_2_2_0.Vec2

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class KmlService implements ApplicationContextAware 
{
    static transactional = false

	ApplicationContext applicationContext
	def grailsApplication
	def queryService
	
	void export(Class clazz, Map params, OutputStream out)
	{
		assert(isSupportedFormat(params.format))

		if (params.format == "KMZ")
		{
			generateKmz(generateKml(clazz, params), out)
		}
		else if (params.format == "KML")
		{
			def kml = generateKml(clazz, params)
			kml.marshal(out)
		}
	}

    Kml toKml(List<InstallationStation> stations) 
	{
		final Kml kml = new Kml()
		Document doc = kml.createAndSetDocument()

		def projects = stations*.installation*.project.unique().sort()
		{
			a, b ->
			
			a.name <=> b.name
		}
		
		projects.each
		{
			project ->
			
			Folder projectFolder = project.toKmlFolder()
			doc.getFeature().add(projectFolder)
		}
		
		return kml 
    }
	
	public boolean isSupportedFormat(String format)
	{
		return ((format == "KML") || (format == "KMZ"))
	}
	
	public Kml generateKml(clazz, params)
	{
		def kml
		def result = queryService.query(clazz, params).results
		if (clazz == InstallationStation)
		{
			InstallationStation.refreshDetectionCounts()
			kml = toKml(result)
		}
		else
		{
			kml = clazz.toKml(result, grailsApplication.config.grails.serverURL)
		}
		
		return kml
	}
	
	public void generateKmz(kml, out)
	{
		ZipOutputStream kmzStream = new ZipOutputStream(out)
		
		// Write KML file itself.
		ZipEntry kmlEntry = new ZipEntry("doc.kml")
		kmzStream.putNextEntry(kmlEntry)
		
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
		IOUtils.copy(getMainCssStream(), kmzStream)
		kmzStream.closeEntry()
		
		// IMOS logo.
		ZipEntry imosLogoEntry = new ZipEntry("files/IMOS-logo.png")
		kmzStream.putNextEntry(imosLogoEntry)
		IOUtils.copy(getImosLogoStream(), kmzStream)
		kmzStream.closeEntry()
		
		kmzStream.close()
	}
	
	private InputStream getMainCssStream()
	{
		return applicationContext.getResource("/css/main.css").getInputStream()
	}

	private InputStream getImosLogoStream()
	{
		return applicationContext.getResource("/images/IMOS-logo.png").getInputStream()
	}
}
