package au.org.emii.aatams.util

import java.text.DateFormat
import java.text.SimpleDateFormat

class SqlUtils {
    public static String formatTimestamp(timestamp, format) {
        if (timestamp) {
            def formatter = new SimpleDateFormat(format)
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

            return formatter.format(timestamp)
        }

        return null
    }
}
