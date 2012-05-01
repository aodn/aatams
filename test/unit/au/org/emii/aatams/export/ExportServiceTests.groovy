package au.org.emii.aatams.export

import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import au.org.emii.aatams.filter.QueryService;
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase
import au.org.emii.aatams.*


import grails.test.*

class ExportServiceTests extends AbstractGrailsUnitTestCase 
{
	def exportService
	def queryService
	def receiverList
	
    protected void setUp() 
	{
        super.setUp()
		
		mockLogging(ExportService, true)
		exportService = new ExportService()
		
		mockLogging(QueryService, true)
		queryService = new QueryService()
		exportService.queryService = queryService
		
		exportService.metaClass.getReportStream =
		{
			return new FileInputStream(new File("web-app/reports/receiverList.jrxml"))
		}
		
		def deployed = new DeviceStatus(status: "DEPLOYED")
		mockDomain(DeviceStatus, [deployed])
		deployed.save()
		
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
		20.times
		{
			def rxr = new Receiver(model: vr2w, serialNumber: it + 1, organisation: imos)
			receiverList.add(rxr)
		}
		
		mockDomain(Receiver, receiverList)
		receiverList.each { it.save() }
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

	void testExporterNoFormat()
	{
		try
		{
			exportService.getExporter([:])
			fail()
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Export format not specified.", e.getMessage())
		}	
	}
	
	void testExporterUnsupportedFormat()
	{
		try
		{
			exportService.getExporter([format: "XYZ"])
			fail()
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Unsupported export format: XYZ", e.getMessage())
		}	
	}
	
	void testExporterCSV()
	{
		assertEquals(JRCsvExporter.class, exportService.getExporter([format: "CSV"]).class)
	}
	
	void testExporterPDF()
	{
		assertEquals(JRPdfExporter.class, exportService.getExporter([format: "PDF"]).class)
	}
	
//    void testExportReceiversOneRecord() 
//	{
//		def expectedOutput = ''',,AATAMS Receivers,,,,,,,,,,,
//,Parameters,,,,,,,,,,,,
//,user:,,,,Joe Bloggs,,,,,,,,
//,,,code name,,,status,,manufacturer,,model,serial number,,
//,organisation:,,,IMOS,,,total receivers:,,0,,,,
//,,,vr2w-1,,,null,,Vemco,,vr2w,1,,
//'''
//		assertExport(expectedOutput, [receiverList[0]])
//    }
	
	void testExportReceiversTwoRecords()
	{
		def expectedOutput = ''',,AATAMS Receivers,,,,,,,,,,,
,Parameters,,,,,,,,,,,,
,user:,,,,Joe Bloggs,,,,,,,,
,,,code name,,,status,,manufacturer,,model,serial number,,
,organisation:,,,IMOS,,,total receivers:,,0,,,,
,,,vr2w-1,,,null,,Vemco,,vr2w,1,,
,,,vr2w-2,,,null,,Vemco,,vr2w,2,,
'''
		
		assertExport(expectedOutput, [receiverList[0], receiverList[1]])
	}

	private assertExport(expectedOutput, results)
	{
		PagedBeanDataSource.metaClass.getResults =
		{
			return results
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream()
		
		def params = [format: "CSV", filter: [user: "Joe Bloggs"]]
		
		exportService.export(Receiver.class, params, out)
		
		assertEquals(expectedOutput, removePageFooter(out.toString()))
	}
	
	private String removePageFooter(String s)
    {
        def lineCount = 0
        s.eachLine { lineCount ++}
        
        def retString = ""
        int index = 0
        
        s.eachLine
        {
            if (it.contains("Page"))
			{
				// remove page footer
			}
            else
            {
                retString += it + '\n'
            }
            
            index++
        }
        
        return retString 
    }
}
