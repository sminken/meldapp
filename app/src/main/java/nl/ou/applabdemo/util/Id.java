package nl.ou.applabdemo.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Date;

/**
 * Klasse voor speciale identificaties in de MeldApp.
 *
 */

public class Id {

    /**
     * Generates an id using the current date, time and random number.
     * @return Generated id
     */
    public static String getGeneratedId(){

        // Get the current date and time with thoudends of milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date date = Calendar.getInstance().getTime();
        String dateId = sdf.format(date);
        dateId = dateId.replaceAll(".","");

        // Generate a random number of five digits
        Random rd = new Random();
        int random = rd.nextInt((100000));
        String randomId = Integer.toString(random);

        String id = dateId + randomId;
        return id.trim();
    }
}
