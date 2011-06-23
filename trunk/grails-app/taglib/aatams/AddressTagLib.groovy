package aatams

import au.org.emii.aatams.Address

class AddressTagLib 
{
    def addressDetail = {attrs, body ->
                        
        out << render(template:"/address/addressTemplate", model:[addressName:attrs.addressName, address:attrs.address, body:body()])
    }
}
