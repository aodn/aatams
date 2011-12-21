import org.junit.Test;

import pages.*

class InstallationStationTests extends GrailsCrudTest 
{
	def listPage = InstallationStationListPage
	def showPage = InstallationStationShowPage
	def createPage = InstallationStationCreatePage
	def editPage = InstallationStationEditPage
	
	@Test
	void testList()
	{
		doTestList(7,
				   [name:"Ningaloo S1", arrayPosition:"1", /*location:"10.12¡N 10.12¡E (datum:null)",*/ active:"yes"],
				   [installationLink:"Ningaloo Array", projectLink:"Tuna"],
				   [])
	}
	
	@Test
	void testShow()
	{
		doTestShow(
			"Ningaloo S1",
			[name:"Ningaloo S1",
			 arrayPosition:"1", 
			 location:"10.12\u00b0N 10.12\u00b0E (datum:null)",
			 installation: "Ningaloo Array",
			 project: "Tuna"])
	}

	@Test
	void testCreate()
	{
		doTestCreate(
			[nameTextField:"Some New Station",
			 arrayPositionTextField: "57",
			 installationSelect: "63"],
		    [name: "Some New Station", arrayPosition: "57", location:"10.12\u00b0S 10.12\u00b0E (datum:WGS 84)", installation: "Ningaloo Array"])
	}
	
	protected void doCustomCreateEntry()
	{
		assert at(getCreatePage())
		
		locationTextField.click()
		locationDialog.latitudeTextField.value("10.12")
		locationDialog.longitudeTextField.value("10.12")
		locationDialog.datumSelect.value("4326")	// WGS 84
		locationDialog.okButton.click()
	}

	@Test
	void testEdit()
	{
		doTestEdit("Ningaloo S2")
	}
}
