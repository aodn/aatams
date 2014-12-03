package au.org.emii.aatams.util

import java.text.DateFormat
import java.text.SimpleDateFormat

class SqlUtils
{
    public static void appendIntegerParams(buff, params, paramNames)
    {
        paramNames.each
        {
            buff.append(params[it])
            buff.append(",")
        }
    }

    public static void appendBooleanParams(buff, params, paramNames)
    {
        appendIntegerParams(buff, params, paramNames)
    }

    public static  void appendStringParams(buff, params, paramNames)
    {
        paramNames.each
        {
            if (params[it] != null)
            {
                buff.append("'")
                buff.append(params[it])
                buff.append("'")
            }
            else
            {
                buff.append(params[it])
            }
            buff.append(",")
        }
    }

    public static  void removeTrailingCommaAndAddBracket(buff)
    {
        buff.deleteCharAt(buff.length() - 1)
        buff.append(')')
    }

    public static String formatTimestamp(timestamp, format)
    {
        if (timestamp)
        {
            def formatter = new SimpleDateFormat(format)
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

            return formatter.format(timestamp)
        }

        return null
    }
}
