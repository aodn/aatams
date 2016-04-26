package au.org.emii.aatams.export
import au.org.emii.aatams.*
import au.org.emii.aatams.filter.QueryService
import au.org.emii.aatams.report.KmlService
import au.org.emii.aatams.report.ReportInfoService
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase
import net.sf.jasperreports.engine.export.JRCsvExporter
import net.sf.jasperreports.engine.export.JRPdfExporter

class ExportServiceTests extends AbstractGrailsUnitTestCase {
    def exportService
    def permissionUtilsService
    def queryService
    def receiverList
    def reportInfoService

    protected void setUp() {
        super.setUp()

        mockLogging(ExportService, true)
        exportService = new ExportService()

        mockLogging(QueryService, true)
        queryService = new QueryService()

        mockLogging(PermissionUtilsService, true)
        permissionUtilsService = new PermissionUtilsService()
        permissionUtilsService.metaClass.principal = {
            return [name: "Joe Bloggs"]
        }

        mockLogging(ReportInfoService, true)
        reportInfoService = new ReportInfoService()

        exportService.reportInfoService = reportInfoService
        exportService.queryService = queryService
        exportService.permissionUtilsService = permissionUtilsService
        exportService.kmlService = new KmlService()

        exportService.metaClass.getReportStream = {
            clazz, params ->

            return new FileInputStream(new File("web-app/reports/receiverList.jrxml"))
        }

        exportService.metaClass.getSubreportDir = {
            return "web-app/reports/"
        }

        def vemco = new DeviceManufacturer(manufacturerName: "Vemco")
        mockDomain(DeviceManufacturer, [vemco])
        vemco.save()

        def vr2w = new ReceiverDeviceModel(modelName: "vr2w", manufacturer: vemco)
        mockDomain(ReceiverDeviceModel, [vr2w])
        vr2w.save()

        def imos = new Organisation(name: "IMOS")
        mockDomain(Organisation, [imos])
        imos.save()

        receiverList = []
        20.times {
            def rxr = new Receiver(model: vr2w, serialNumber: it + 1, organisation: imos)
            receiverList.add(rxr)
        }

        mockDomain(Receiver, receiverList)
        receiverList.each { it.save() }
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testExporterNoFormat() {
        try {
            exportService.getExporter([:])
            fail()
        }
        catch (IllegalArgumentException e) {
            assertEquals("Export format not specified.", e.getMessage())
        }
    }

    void testExporterUnsupportedFormat() {
        try {
            exportService.getExporter([format: "XYZ"])
            fail()
        }
        catch (IllegalArgumentException e) {
            assertEquals("Unsupported export format: XYZ", e.getMessage())
        }
    }

    void testExporterCSV() {
        assertEquals(JRCsvExporter.class, exportService.getExporter([format: "CSV"]).class)
    }

    void testExporterPDF() {
        assertEquals(JRPdfExporter.class, exportService.getExporter([format: "PDF"]).class)
    }

    void testExportReceiversOneRecord() {
        def expectedOutput = ''',Parameters,,,,,,,,,,,,
,user:,,,,Joe Bloggs,,,,,,,,
,,code name,,,,status,,manufacturer,,model,serial number,,
,organisation:,,,IMOS,,,total receivers:,,0,,,,
,,vr2w-1,,,,NEW,,Vemco,,vr2w,1,,
'''
        assertExport(expectedOutput, [receiverList[0]])
    }

    void testExportReceiversTwoRecords() {
        def expectedOutput = ''',Parameters,,,,,,,,,,,,
,user:,,,,Joe Bloggs,,,,,,,,
,,code name,,,,status,,manufacturer,,model,serial number,,
,organisation:,,,IMOS,,,total receivers:,,0,,,,
,,vr2w-1,,,,NEW,,Vemco,,vr2w,1,,
,,vr2w-2,,,,NEW,,Vemco,,vr2w,2,,
'''

        assertExport(expectedOutput, [receiverList[0], receiverList[1]])
    }

    private assertExport(expectedOutput, results) {
        exportService.metaClass.getDataSource = {
            queryService, clazz, params ->

            PagedBeanDataSource ds = new PagedBeanDataSource(queryService, clazz, params)
            ds.metaClass.query = {
                theclazz, filterParams ->

                List queryResults

                if (filterParams.offset >= results.size()) {
                    queryResults = []
                }
                else {
                    queryResults = results[filterParams.offset..Math.min(filterParams.offset + filterParams.max, results.size - 1)]
                }

                return queryResults
            }

            return ds
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream()

        def params = [format: "CSV", filter: [:]]

        exportService.export(Receiver.class, params, out)

        assertTrue(removePageFooter(out.toString()).contains(expectedOutput));
    }

    private String removePageFooter(String s) {
        def lineCount = 0
        s.eachLine { lineCount ++}

        def retString = ""
        int index = 0

        s.eachLine {
            if (it.contains("Page")) {
                // remove page footer
            }
            else {
                retString += it + '\n'
            }

            index++
        }

        return retString
    }
}
