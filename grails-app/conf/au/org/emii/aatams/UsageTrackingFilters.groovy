package au.org.emii.aatams

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

class UsageTrackingFilters 
{
	private static final Log log = LogFactory.getLog('usagetracking')

	def dependsOn = [LoggingFilters]
	
	def filters = 
	{
		all(controller:'*', action:'save|update|delete') 
		{
			before = 
			{
				log.info("$controllerName/$actionName")
			}
		}
		
		all(controller:'*', action:'list|create|show|edit') 
		{
			before = 
			{
				log.debug("$controllerName/$actionName")
			}
		}
	}
}