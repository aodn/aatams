package au.org.emii.aatams

class RecoveryTagLib 
{
	def recoveryList =
	{
		attrs, body ->
		
		out << render(template: "/receiverRecovery/recoveryList",
					  model: attrs)
	}
}
