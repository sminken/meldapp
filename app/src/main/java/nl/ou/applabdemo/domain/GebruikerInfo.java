package nl.ou.applabdemo.domain;

/**
 * Deze klasse beheert de instanties van de domein klasse Gebruiker Info
 *
 */
@SuppressWarnings("SpellCheckingInspection")
public class GebruikerInfo {
    private final String voornaam;
    private final String achternaam;
    private final String organisatie;
    private final String uid;
    private final GebruikerStatus gebruikerStatus;
    private final String telefoonnummer;
    private final Rol rol;
    private final boolean notification;
    private final String deviceToken;

    public GebruikerInfo (String voornaam, String achternaam, String organisatie,
                          String uid, GebruikerStatus gebruikerStatus, String telefoonnummer,
                          Rol rol, boolean notification, String deviceToken) {
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.organisatie = organisatie;
        this.uid = uid;
        this.gebruikerStatus = gebruikerStatus;
        this.telefoonnummer = telefoonnummer;
        this.rol = rol;
        this.notification = notification;
        this.deviceToken = deviceToken;
    }


    public String getVoornaam() {
        return voornaam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public String getOpgemaakteNaam() {
        return voornaam + ' ' + achternaam;
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

    public Boolean getNotification() {
        return notification;
    }

    public String getDeviceToken() {
        return deviceToken;
    }
}
