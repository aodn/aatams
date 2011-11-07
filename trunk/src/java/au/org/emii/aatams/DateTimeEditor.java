package au.org.emii.aatams;

import java.beans.PropertyEditorSupport;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author jburgess
 */
public class DateTimeEditor extends PropertyEditorSupport
{
    private DateTime dateTime;
    
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
    
    @Override
    public String getAsText()
    {
        System.out.println("getAsText()");
        return formatter.print(dateTime);
    }
    
    @Override
    public void setAsText(String text) throws IllegalArgumentException
    {
        System.out.println("setAsText()");
        dateTime = formatter.parseDateTime(text);
        super.setValue(dateTime);
    }
}
