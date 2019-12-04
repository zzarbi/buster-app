package busterapp.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateHelper {

    /**
     * Format a date into ISO 8601 format
     * http://en.wikipedia.org/wiki/ISO_8601 
     * 
     * @param Date date
     * @return String
     */
    public static String formatISO8601(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        return df.format(date);
    }
}