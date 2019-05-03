package nl.ou.applabdemo.domain;

import nl.ou.applabdemo.util.MeldAppException;

/**
 * Deze klasse beheert de instanties van de domein klasse Gebruiker
 *
 */
@SuppressWarnings("SpellCheckingInspection")
public class Gebruiker {

    private final String voornaam;
    private final String achternaam;
    private final String organisatie;
    private final String uid;
    private final GebruikerStatus gebruikerStatus;
    private final String telefoonnummer;
    private final Rol rol;

    // wijzigbare attributen:
    private boolean notification;
    private String deviceToken;

    /**
     * Standaard constructor voor een Gebruiker
     *
     * @param voornaam        - voornaam van de gebruiker
     * @param achternaam      - achternaam van de gebruiker
     * @param organisatie     - organisatie waarvan de gebruiker deel uit maakt
     * @param uid             - uid uit Firebase database die
     *                          aan gebruiker is toegekend bij eerste aanmelding
     * @param gebruikerStatus - status gebruiker
     * @param telefoonnummer  - telefoonnummer van gebruiker
     * @param rol             - rol gebruiker
     */
    public Gebruiker(String voornaam, String achternaam, String organisatie, String uid,
                     GebruikerStatus gebruikerStatus, String telefoonnummer,
                     Rol rol, boolean notification, String deviceToken )
        throws MeldAppException {

        this.voornaam = voornaam;

        // achternaam is verplicht veld
        if (achternaam == null || achternaam.equals("")) {
            throw new MeldAppException("Achternaam moet ingevuld zijn.");
        }
        this.achternaam = achternaam;

        /*
        In de eerste versie van de meldApp wordt geen gebruik gemaakt van organisatie
        Deze moet daarom leeg zijn
         */
        if ( organisatie == null || !organisatie.isEmpty()) {
            throw new MeldAppException("Organisatie-waarde niet toegestaan.");
        }
        this.organisatie = organisatie;

        // uid is verplicht veld
        if (uid == null || uid.isEmpty()) {
            throw new MeldAppException("Uid moet ingevuld zijn.");
        }
        this.uid = uid;

        this.gebruikerStatus = gebruikerStatus;

        // controle geldigheid telefoonnummer
        telefoonnummer = telefoonnummer.trim();
        int size = 0;
        for (char c : telefoonnummer.toCharArray()) {
            if (Character.isDigit(c)) {
                size++;
            }
            else if ( c != '-') {
                throw new MeldAppException("Alleen getallen en - zijn toegestaan");
            }
        }
        if (size == 10 && telefoonnummer.charAt(0) == '0') {
            this.telefoonnummer = telefoonnummer;
        }   else {
            throw new MeldAppException("Geef een geldig telefoonnummer op bestaande uit 10 cijfers");
        }

        this.rol = rol;
        this.notification = notification;
        this.deviceToken = deviceToken;

    }

    // getters

    public String getVoornaam() {
        return voornaam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public String getOrganisatie() {
        return organisatie;
    }

    public String getUid() {
        return uid;
    }

    public GebruikerStatus getGebruikerStatus() {
        return gebruikerStatus;
    }

    public String getTelefoonnummer() {
        return telefoonnummer;
    }

    public Rol getRol() {
        return rol;
    }

    public boolean getNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    /**
     * Deze methode geeft een instantie van GebruikersInfo terug met daarin de gegevens
     *         van deze instantie van Gebruiker
     * @return - de GebruikersInfo instantie
     */
    public GebruikerInfo geefGebruikerInfoInstantie () {
        return new GebruikerInfo( voornaam, achternaam, organisatie, uid,
                gebruikerStatus, telefoonnummer, rol,
                notification, deviceToken);
    }

    /*
    Override standard methods from Object
     */
    @Override
    public String toString() {
        return voornaam + " " + achternaam + " " + organisatie + " " + telefoonnummer;
    }

}
