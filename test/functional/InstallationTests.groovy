import org.junit.Test;

import pages.*

class InstallationTests extends GrailsCrudTest 
{
	def listPage = InstallationListPage
	def showPage = InstallationShowPage
	def createPage = InstallationCreatePage
	def editPage = InstallationEditPage
	
	@Test
	void testList()
	{
		doTestList(3,
				   [name:"Bondi Line", configuration:"CURTAIN", project:"Seal Count", numStations:"3"],
				   [],
				   [stations: ["Bondi SW1", "Bondi SW3", "Bondi SW2"]])
	}
	
	@Test
	void testShow()
	{
		doTestShow(
			"Bondi Line",
			[name:"Bondi Line",
			 configuration: "CURTAIN",
			 project: "Seal Count"])
	}
	
	@Test
	void testCreate()
	{
		doTestCreate(
			[nameTextField:"Derwent Array",
			 configurationSelect:"60",	// ARRAY
			 projectSelect:"13"],	// Tuna
			[name: "Derwent Array", project: "Tuna", configuration: "ARRAY"])
	}
	
	@Test
	void testEdit()
	{
		doTestEdit("Bondi Line")
	}
}
