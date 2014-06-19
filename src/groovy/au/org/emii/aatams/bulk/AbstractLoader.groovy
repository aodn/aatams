package au.org.emii.aatams.bulk

import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.grails.plugins.csv.CSVMapReader

abstract class AbstractLoader 
{
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss")

    static final String MODIFIED_DATETIME_COL = "MODIFIED_DATETIME"
    static final String MODIFIED_BY_COL = "MODIFIED_BY"

    protected List<Map<String, String>> getRecords(InputStream stream)
    {
        return new CSVMapReader(new InputStreamReader(stream)).toList()
    }
}
