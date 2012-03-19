package au.org.emii.aatams

class RecoveryTagLib 
{
	def recoveryList =
	{
		attrs, body ->
		
//		println("attrs: " + attrs)
//		def model = [entityList: attrs.entityList, params: attrs.params
//		model += attrs.model
//		model += attrs.hideReceiverColumn
		 
		out << render(template: "/receiverRecovery/recoveryList",
					  model: attrs)
	}
}
