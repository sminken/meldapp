package nl.ou.applabdemo.comparator;

import nl.ou.applabdemo.domain.MeldingInfo;
import nl.ou.applabdemo.domain.Status;

/**
 * Comparator voor de Gesloten meldingen van de behandelaar.
 */
public class MeldingInfoBehandelaarGeslotenComparator extends MeldingInfoComparator {

    /**
     * Vergelijkt de status van de meldingen.
     *
     * @param mi1 MeldingInfo om te vergelijken.
     * @param mi2 MeldingInfo om te vergelijken.
     * @return Vergeleken MeldingInfo resultaat.
     */
    @Override
    public int compareStatus(MeldingInfo mi1, MeldingInfo mi2) {

        Status status1 = mi1.getStatus();
        Status status2 = mi2.getStatus();

        if (status1 == null && status2 == null) {
            return 0;
        } else if (status1 == null) {
            return -1;
        } else if (status2 == null) {
            return 1;
            // Status Heropend komt voor status In Behandeling.
        } else if (status1.equals(Status.OPGELOST) && status2.equals(Status.GESLOTEN)) {
            return -1;
        } else if (status1.equals(Status.GESLOTEN) && status2.equals(Status.OPGELOST)) {
            return 1;
        } else if (status1.equals(status2)){
            // Statussen zijn gelijk. Vergelijk datum en tijd van de meldingen.
            return compareDatumTijd(mi1, mi2);
        }
        // Statussen zijn ongelijk.
        return status1.compareTo(status2);
    }

}
