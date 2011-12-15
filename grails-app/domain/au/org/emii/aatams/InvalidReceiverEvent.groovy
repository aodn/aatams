package au.org.emii.aatams

import au.org.emii.aatams.detection.InvalidDetectionReason

class InvalidReceiverEvent extends ReceiverEvent 
{
	InvalidDetectionReason reason
	String message
	
	static constraints =
	{
		reason(nullable:false)
		message(nullable:true, blank:true)
	}
	
	String toString()
	{
		StringBuilder buf = new StringBuilder(String.valueOf(reason))
		if (message)
		{
			buf.append(": ")
			buf.append(message)
		}
		
		return buf.toString()
	}
}
