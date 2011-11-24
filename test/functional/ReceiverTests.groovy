import org.junit.Test;

import groovy.lang.MetaClass;
import pages.*

class ReceiverTests extends GrailsCrudTest 
{
	def listPage = ReceiverListPage
	def showPage = ReceiverShowPage
	def createPage = ReceiverCreatePage
	def editPage = ReceiverEditPage

	@Test
	void testList()
	{
		doTestList(
			6, 
			[name:"VR2W-101336", model: "VR2", serialNumber: "12345678", organisation: "CSIRO", status: "DEPLOYED"],
			[], [])
	}
	
	@Test
	void testShow()
	{
		doTestShow("VR2W-101338",  
				   [name:"VR2W-101338", 
					organisation: "IMOS (eMII)",
					model: "VR2",
					serialNumber: "1111r",
					status: "NEW",
					comment: ""])
	}

	@Test
	void testEdit()
	{
		doTestEdit("CSIRO", "serialNumber", "serialNumberTextField")
	}
	
	@Test
	void testCreate()
	{
		doTestCreate(
			[nameTextField:"Some New Organisation",
			 departmentTextField:"Marine",
			 phoneNumberTextField:"1234",
			 faxNumberTextField:"4321",
			 streetAddressStreetAddressTextField:"12 Smith Street", 
			 streetAddressSuburbTownTextField:"Hobart", 
			 streetAddressStateTextField:"TAS", 
			 streetAddressPostcodeTextField:"7000", 
			 streetAddressCountryTextField:"Australia", 
			 postalAddressStreetAddressTextField:"PO Box 1234", 
			 postalAddressSuburbTownTextField:"Melbourne",
			 postalAddressStateTextField:"VIC", 
			 postalAddressPostcodeTextField:"3000",
			 postalAddressCountryTextField:"Australia"],
		 	[])
	}
	
	private void listOrganisationsAsUnauthorised()
	{
		doTestList(
			1,
			[name:"CSIRO", 
			 department:"CMAR", 
			 phoneNumber:"1234", 
			 faxNumber:"1234", 
			 streetAddress:"12 Smith Street, Hobart, TAS, Australia, 7000", 
			 postalAddress:"34 Queen Street, Melbourne, VIC, Australia, 3000"],
		    [],
			[])
	}
	
	private void listOrganisationsAsSysAdmin()
	{
		loginAsSysAdmin()
		doTestList(3, [name:"CSIRO"], [], [])
		doTestList(3, [name:"IMOS"], [], [])
		doTestList(3, [name:"IMOS 2"], [], [])
	}
}
