package pages

class OrganisationCreateEditBasePage extends LayoutPage
{
	static content =
	{
		nameTextField { $("input", name: "organisation.name") }
		departmentTextField { $("input", name: "organisation.department") }
		phoneNumberTextField { $("input", name: "organisation.phoneNumber") }
		faxNumberTextField { $("input", name: "organisation.faxNumber") }
		
		streetAddressStreetAddressTextField { $("input", name: "streetAddress.streetAddress") }
		streetAddressSuburbTownTextField { $("input", name: "streetAddress.suburbTown") }
		streetAddressStateTextField { $("input", name: "streetAddress.state") }
		streetAddressPostcodeTextField { $("input", name: "streetAddress.postcode") }
		streetAddressCountryTextField { $("input", name: "streetAddress.country") }

		postalAddressStreetAddressTextField { $("input", name: "postalAddress.streetAddress") }
		postalAddressSuburbTownTextField { $("input", name: "postalAddress.suburbTown") }
		postalAddressStateTextField { $("input", name: "postalAddress.state") }
		postalAddressPostcodeTextField { $("input", name: "postalAddress.postcode") }
		postalAddressCountryTextField { $("input", name: "postalAddress.country") }
	}
}
