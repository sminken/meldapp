package nl.ou.applabdemo.comparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import nl.ou.applabdemo.domain.MeldingInfo;

/**
 * Comparator voor de openstaande meldingen van de behandelaar.
 */
public class MeldingInfoBehandelaarOpenstaandComparator extends MeldingInfoComparator {

    /**
     * Vergelijkt de datum en tijd van de meldingen. Meest oudste bovenaan.
     *
     * @param mi1 MeldingInfo om te vergelijken.
     * @param mi2 MeldingInfo om te vergelijken.
     * @return Vergeleken MeldingInfo resultaat.
     */
    @Override
    public int compareDatumTijd(MeldingInfo mi1, MeldingInfo mi2) {

        String datumTijd1 = mi1.getDatumTijd();
        String datumTijd2 = mi2.getDatumTijd();

        if (datumTijd1 == null && datumTijd2 == null) {
            return 0;
        } else if (datumTijd1 == null) {
            return -1;
        } else if (datumTijd2 == null) {
            return 1;
        }

        int compareDate = 0;
        try {
            Date date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(datumTijd1);
            Date date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(datumTijd2);
            compareDate = date1.compareTo(date2);
        }
        catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        // Vergelijk de date van de meldingen.
        return compareDate;
    }

}
