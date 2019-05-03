package nl.ou.applabdemo.comparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import nl.ou.applabdemo.domain.MeldingInfo;
import nl.ou.applabdemo.domain.Status;

/**
 * Comparator voor meldingen info.
 */
public abstract class MeldingInfoComparator implements Comparator<MeldingInfo> {

    /**
     * Vergelijkt meldingInfo's.
     * @param mi1 MeldingInfo om te vergelijken.
     * @param mi2 MeldingInfo om te vergelijken.
     * @return Vergeleken MeldingInfo resultaat.
     */
    @Override
    public int compare(MeldingInfo mi1, MeldingInfo mi2) {

        if (mi1 == null && mi2 == null) {
            return 0;
        } else if (mi1 == null) {
            return -1;
        } else if (mi2 == null) {
            return 1;
        }
        // Vergelijk de statussen van de meldingen.
        return compareStatus(mi1, mi2);
    }

    /**
     * Vergelijkt de status van de meldingen.
     *
     * @param mi1 MeldingInfo om te vergelijken.
     * @param mi2 MeldingInfo om te vergelijken.
     * @return Vergeleken MeldingInfo resultaat.
     */
    public int compareStatus(MeldingInfo mi1, MeldingInfo mi2) {

        Status status1 = mi1.getStatus();
        Status status2 = mi2.getStatus();

        if (status1 == null && status2 == null) {
            return 0;
        } else if (status1 == null) {
            return -1;
        } else if (status2 == null) {
            return 1;
        } else if (status1.equals(status2)){
            // Statussen zijn gelijk. Vergelijk datum en tijd van de meldingen.
            return compareDatumTijd(mi1, mi2);
        }
        // Statussen zijn ongelijk.
        return status1.compareTo(status2);
    }

    /**
     * Vergelijkt de datum en tijd van de meldingen. Meest recente bovenaan.
     *
     * @param mi1 MeldingInfo om te vergelijken.
     * @param mi2 MeldingInfo om te vergelijken.
     * @return Vergeleken MeldingInfo resultaat.
     */
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
            compareDate = date2.compareTo(date1);
        }
        catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        // Vergelijk de date van de meldingen.
        return compareDate;
    }
}
