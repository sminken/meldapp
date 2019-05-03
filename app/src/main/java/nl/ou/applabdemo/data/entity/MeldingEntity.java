package nl.ou.applabdemo.data.entity;

/**
 * Klasse die de (database) MeldingEntity beheert. <br><br>
 * <p>
 * Opmerking<br>
 * Iedere instantie van deze klasse correspondeert met één Melding uit de collectie
 * van Meldingen in de Firebase database
 */
@SuppressWarnings("SpellCheckingInspection")
public class MeldingEntity {

    private String uidGebruikerMelder;
    private String uidGebruikerBehandelaar;
    private String onderwerp;
    private String inhoud;
    private String status;
    private String datumTijd;
    private boolean notificatieVerstuurd;

    public MeldingEntity() { }

    /**
     * Constructor voor de MeldingEntity
     *
     * @param uidGebruikerMelder Uid van de gebruiker
     * @param onderwerp          onderwerp van de melding
     * @param inhoud             inhoud van de melding
     * @param datumTijd          datum en tijd van de melding
     * @param status             status van de melding
     * @param notificatieVerstuurd  indicator of notificatie voor deze melding is verstuurd
     *
     */
    public MeldingEntity(String uidGebruikerMelder, String uidGebruikerBehandelaar, String onderwerp, String inhoud,
                         String datumTijd, String status, boolean notificatieVerstuurd ) {
        this.uidGebruikerMelder = uidGebruikerMelder;
        this.uidGebruikerBehandelaar = uidGebruikerBehandelaar;
        this.onderwerp = onderwerp;
        this.inhoud = inhoud;
        this.datumTijd = datumTijd;
        this.status = status;
        this.notificatieVerstuurd = notificatieVerstuurd;
    }

    /**
     * Retourneert uid van de melder
     *
     * @return String uid van de melder
     */
    public String getUidGebruikerMelder() {
        return uidGebruikerMelder;
    }

    /**
     * Retourneert de uid van de behandelaar
     *
     * @return String uid van de behandelaar
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
     * Retourneert datum en tijd van de melding
     *
     * @return String datum en tijd van de melding
     */
    public String getDatumTijd() {
        return datumTijd;
    }

    /**
     * Retourneert status van de melding
     *
     * @return String status van de melding
     */
    public String getStatus() {
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

}
