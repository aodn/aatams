package au.org.emii.aatams.report

import au.org.emii.aatams.*
import de.micromata.opengis.kml.v_2_2_0.*
import groovy.sql.Sql

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import org.apache.commons.io.IOUtils
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class KmlService implements ApplicationContextAware {
    static transactional = false

    ApplicationContext applicationContext
    def dataSource
    def detectionExtractService
    def grailsApplication
    def queryService

    void export(Class clazz, Map params, OutputStream out) {
        assert(isSupportedFormat(params.format))

        if (params.format == 'KMZ') {
            generateKmz(generateKml(clazz, params), out)
        }
        else if (params.format == "KML") {
            def kml = generateKml(clazz, params)
            kml.marshal(out)
        }
    }

    Kml toKml(List<InstallationStation> stations) {
        final Kml kml = new Kml()
        Document doc = kml.createAndSetDocument()

        doc.createAndAddStyle()
            .withId("defaultStationStyle")
            .withIconStyle(new IconStyle().withScale(1.0).withHeading(0.0).withIcon(new Icon().withHref("files/station.png")))
            .withLabelStyle(new LabelStyle().withScale(0.0))

        def projects = stations*.installation*.project.unique().sort() {
            a, b ->

            a.name <=> b.name
        }

        projects.each {
            project ->

            Folder projectFolder = project.toKmlFolder()
            doc.getFeature().add(projectFolder)
        }

        return kml
    }

    public boolean isSupportedFormat(String format) {
        return ["KML", "KMZ"].contains(format)
    }

    public Kml generateKml(clazz, params) {
        def result = queryService.query(clazz, params, true).results
        return toKml(result)
    }

    public void generateKmz(kml, out) {
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

        [
            "files/": null,
            "files/main.css": getMainCssStream(),
            "files/IMOS-logo.png": getImosLogoStream(),
            "files/fish.png": getFishIconStream(),
            "files/red_fish.png": getRedFishIconStream(),
            "files/station.png": getStationIconStream(),
            "files/circle.png": getCircleIconStream()
        ].each {
            k, v ->

            addZipEntry(kmzStream, k, v)
        }

        kmzStream.close()
    }

    private addZipEntry(kmzStream, entryName, entryStream) {
        ZipEntry entry = new ZipEntry(entryName)
        kmzStream.putNextEntry(entry)

        if (entryStream) {
            IOUtils.copy(entryStream, kmzStream)
            kmzStream.closeEntry()
        }
    }

    private InputStream getMainCssStream() {
        return applicationContext.getResource("/css/main.css").getInputStream()
    }

    private InputStream getImosLogoStream() {
        return applicationContext.getResource("/images/IMOS-logo.png").getInputStream()
    }

    private InputStream getFishIconStream() {
        return applicationContext.getResource("/images/fish.png").getInputStream()
    }

    private InputStream getRedFishIconStream() {
        return applicationContext.getResource("/images/red_fish.png").getInputStream()
    }

    private InputStream getStationIconStream() {
        return applicationContext.getResource("/images/station.png").getInputStream()
    }

    private InputStream getCircleIconStream() {
        return applicationContext.getResource("/images/circle.png").getInputStream()
    }
}
