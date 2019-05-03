package nl.ou.applabdemo.domain;

import nl.ou.applabdemo.util.MeldAppException;
import static nl.ou.applabdemo.util.Date.getDateTimeStamp;

/**
 * Deze klasse is verantwoordelijk voor het aanmaken en beheren van instanties van Meldingen
 *
 */
@SuppressWarnings("SpellCheckingInspection")
public class Melding {

    // public static final String TAG = "Melding ";

    private String uidMelding;
    private String uidGebruikerMelder;
    private String uidGebruikerBehandelaar;
    private String onderwerp;
    private String inhoud;
    private Status status;
    private String datumTijd;
    private boolean notificatieVerstuurd;

    /**
     * Constructor voor een Melding
     *
     * @param uidMelding uid van de melding
     * @param uidGebruikerMelder naam van de melder
     * @param onderwerp onderwerp van de melding
     * @param inhoud inhoud van de melding
     * @param datumTijd datum en tijd van de melding
     * @param status status van de melding
     * @param notificatieVerstuurd  indicator of notificatie voor deze melding is verstuurd
     *
     */
    public Melding(String uidMelding, String uidGebruikerMelder, String uidGebruikerBehandelaar, String onderwerp,
                   String inhoud, String datumTijd, String status, boolean notificatieVerstuurd) throws MeldAppException {

        // Controleer de geldigheid van de waarden in de melding.
        isUidGebruikerMelderGeldig(uidGebruikerMelder);
        isOnderwerpGeldig(onderwerp);
        isInhoudGeldig(inhoud);
        isDatumTijdGeldig(datumTijd);
        Status statusGeldig = isStatusGeldig(status);

        // Waarden zijn geldig.
        this.uidMelding = uidMelding;
        this.uidGebruikerMelder = uidGebruikerMelder;
        this.uidGebruikerBehandelaar = uidGebruikerBehandelaar;
        this.onderwerp = onderwerp;
        this.inhoud = inhoud;
        this.datumTijd = datumTijd;
        this.status = statusGeldig;
        this.notificatieVerstuurd = notificatieVerstuurd;
    }

    /**
     * Constructor voor een nieuwe melding, aangemaakt door een melder.
     *
     * @param uidGebruikerMelder Uid van de melder.
     * @param onderwerp Onderwerp van de melding.
     * @param inhoud Inhoud van de melding.
     */
    public Melding(String uidGebruikerMelder, String onderwerp, String inhoud) throws MeldAppException {

        // Controleer de geldigheid van de waarden in de melding.
        isUidGebruikerMelderGeldig(uidGebruikerMelder);
        isOnderwerpGeldig(onderwerp);
        isInhoudGeldig(inhoud);

        // Waarden zijn geldig.
        this.uidGebruikerMelder = uidGebruikerMelder;
        this.uidGebruikerBehandelaar = null;
        this.onderwerp = onderwerp;
        this.inhoud = inhoud;
        this.datumTijd = getDateTimeStamp();
        this.status = Status.NIEUW;
        this.notificatieVerstuurd = false;
    }

    /**
     * Retourneert de firebase uid van de melding
     *
     * @return String uid van de melding
     */
    public String getUidMelding() {
        return uidMelding;
    }

    /**
     * Retourneert datum en tijd van de melding
     *
     * @return String datum en tijd
     */
    public String getDatumTijd() {
        return datumTijd;
    }

    /**
     * Retourneert naam van de gebruiker
     *
     * @return String naam van de gebruiker
     */
    public String getUidGebruikerMelder() {
        return uidGebruikerMelder;
    }

     /**
     * Retourneert uid van de toegewezen behandelaar
     *
     * @return String de uid van de behandelaar
     */
    public String getUidGebruikerBehandelaar() {
        return uidGebruikerBehandelaar;
    }

     /**
     * Retourneert onderwerp van de melding
     *
     * @return String onderwerp van de melding
     */
    public String getOnderwerp() {
        return onderwerp;
    }

     /**
     * Retourneert inhoud van de melding
     *
     * @return String inhoud van de melding
     */
    public String getInhoud() {
        return inhoud;
    }

    /**
     * Retourneert status van de melding
     *
     * @return Status status van de melding
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Retourneert de indicatie van het versturen van een notificatie voor deze melding
     *
     * @return boolean true als notificatie voor de melding is verstuurd. Anders false.
     */
    public boolean getNotificatieVerstuurd() {
        return notificatieVerstuurd;
    }

    /**
     * Retourneert string representatie van de melding
     *
     * @return String string representatie van de melding
     */
    @Override
    public String toString() {
        return "melding gedaan door melder uid: " + uidGebruikerMelder + " met als onderwerp: " + onderwerp + " en als inhoud: " + inhoud;
    }

    /**
     * Retourneert of twee Meldingen gelijk zijn
     *
     * @param obj het melding object
     * @return true indien objecten gelijk zijn anders false
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Melding))
            return false;
        Melding andereMelding = (Melding) obj;
        return (uidMelding.equals(andereMelding.getUidMelding()) && datumTijd.equals(andereMelding.getDatumTijd()));
    }

    /**
     * Retourneert unieke hashCode van dit Melding object
     *
     * @return int de hashcode
     */
    @Override
    public int hashCode() {
        return uidMelding.hashCode() * datumTijd.hashCode();
    }

    /**
     * Controleert de waarde van het onderwerp in de melding.
     *
     * @param onderwerp Het onderwerp van de melding.
     * @throws MeldAppException Het onderwerp heeft geen geldige waarde.
     */
    private void isOnderwerpGeldig(String onderwerp) throws MeldAppException {

        // Controleer het onderwerp.
        if (onderwerp == null || onderwerp.isEmpty()) {
            throw new MeldAppException("Onderwerp mag niet leeg zijn.");
        }
    }

    /**
     * Controleert de waarde van de inhoud in de melding.
     *
     * @param inhoud De inhoud van de melding.
     * @throws MeldAppException De inhoud heeft geen geldige waarde.
     */
    private void isInhoudGeldig(String inhoud) throws MeldAppException {

        // Controleer de inhoud.
        if (inhoud == null || inhoud.isEmpty()) {
            throw new MeldAppException("Inhoud mag niet leeg zijn.");
        }
    }

    /**
     * Controleert de waarde van de datum en de tijd in de melding.
     *
     * @param datumTijd De datum en tijd van de melding.
     * @throws MeldAppException De datum en tijd hebben geen geldige waarde.
     */
    private void isDatumTijdGeldig(String datumTijd) throws MeldAppException {

        // Controleer de datum en tijd.
        if (datumTijd == null || datumTijd.isEmpty()) {
            throw new MeldAppException("Datum en tijd mogen mogen niet leeg zijn.");
        }
    }

    /**
     * Controleert de waarde van de uid van de melder in de melding.
     *
     * @param uidGebruikerMelder De uid van de melder in de melding.
     * @throws MeldAppException De uid vam de melder heeft geen geldige waarde.
     */
    private void isUidGebruikerMelderGeldig(String uidGebruikerMelder) throws MeldAppException {

        // Controleer de uid van de melder.
        if (uidGebruikerMelder == null || uidGebruikerMelder.isEmpty()) {
            throw new MeldAppException("Identificatie van de melder mag niet leeg zijn.");
        }
    }

    /**
     * Controleert de waarde van de status in de melding.
     *
     * @param status De status in de melding.
     * @throws MeldAppException De status heeft geen geldige waarde.
     */
    private Status isStatusGeldig(String status) throws MeldAppException {

        // Controleer de inhoud.
        if (status == null || status.isEmpty()) {
            throw new MeldAppException("Status mag niet leeg zijn.");
        }

        for(Status statusGeldig: Status.values()) {
            if(status.equals(statusGeldig.toString())) {
                // Status is geldig.
                return statusGeldig;
            }
        }

        // De status is onbekend en niet geldig.
        throw new MeldAppException("Status " + status + " is niet toegestaan.");
    }

}
