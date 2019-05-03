package nl.ou.applabdemo.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Klasse met speciale datums in de MeldApp.
 *
 */

public class Date {

    /**
     * Gives the current date and time in the format dd-MM-yyyy HH:mm:ss.
     * @return Current date and time in the given format.
     */
    public static String getDateTimeStamp(){
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    }
}
