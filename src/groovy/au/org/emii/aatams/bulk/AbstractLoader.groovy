package au.org.emii.aatams.bulk

import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

abstract class AbstractLoader 
{
	static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("d/m/YYYY HH:mm:ss")
}
