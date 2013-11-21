package au.org.emii.aatams

import org.apache.shiro.SecurityUtils;
import org.springframework.web.context.request.RequestContextHolder
import org.slf4j.MDC

class LoggingFilters
{
    def filters =
	{
        all(controller:'*', action:'*')
		{
            before =
			{
				def username = Person.get(SecurityUtils?.subject?.principal)?.username
				if (username)
				{
					MDC.put('username', username)
				}
				else
				{
					MDC.put('username', 'unathenticated')
				}
            }

            afterView =
			{
				MDC.remove('username')
            }
        }
    }
}
