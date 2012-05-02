package au.org.emii.aatams

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter

import au.org.emii.aatams.export.ExportService;
import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*

class ReceiverControllerTests extends AbstractControllerUnitTestCase 
{
    protected void setUp() 
	{
        super.setUp()
		controller.params.format = "PDF"
		
		ExportService.metaClass.getExporter =
		{
			params ->
			
			return new JRCsvExporter()
		}
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testSaveWithWithspaceAroundSerialNumber() 
	{
		controller.params.serialNumber = " 234 "
		controller.params.organisation = Organisation.findByName("IMOS")
		controller.params.model = ReceiverDeviceModel.findByModelName("VR2W")
		controller.save()
		
		def receiver = Receiver.get(controller.redirectArgs.id)
		assertEquals("234", receiver.serialNumber)
		
		receiver.delete(failOnError:true)
    }
	
	void testExportNoFilter()
	{
		assertExport([:], "testExecuteReceiverNoFilter")
	}
}
