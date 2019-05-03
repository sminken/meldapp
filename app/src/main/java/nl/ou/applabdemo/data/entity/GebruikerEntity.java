package nl.ou.applabdemo.data.entity;

/**
 * Deze klasse beheert de instanties van de (database) klasse GebruikerEntity <br><br>
 *
 * Opmerking:<br>
 * Iedere instantie van deze klasse representeert één gebruiker (document)
 * zoals deze in de database is opgeslagen
 *
 */

@SuppressWarnings("SpellCheckingInspection")
public class GebruikerEntity {
    private String voornaam  = null;
    private String achternaam  = null;
    private String organisatie = null;
    private String uid = null;
    private String status = null;
    private String rol = null;
    private String telefoonnummer = null;
    private String deviceToken = null;
    private Boolean notification = false;

    // constructors
    public GebruikerEntity() { 
    }

    public GebruikerEntity(String voornaam, String achternaam, String organisatie, String uid,
                           String status, String rol, String telefoonnummer,
                           Boolean notification, String deviceToken) {
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.organisatie = organisatie;
        this.uid = uid;
        this.status = status;
        this.rol = rol;
        this.telefoonnummer = telefoonnummer;
        this.notification = notification;
        this.deviceToken = deviceToken;

    }

    // setters en getters:
    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public String getOrganisatie() {
        return organisatie;
    }

    public void setOrganisatie(String organisatie) {
        this.organisatie = organisatie;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getTelefoonnummer() {
        return telefoonnummer;
    }

    public void setTelefoonnummer(String telefoonnummer) {
        this.telefoonnummer = telefoonnummer;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Boolean getNotification() {
        return notification;
    }

    public void setNotification(Boolean notification) {
        this.notification = notification;
    }




    /*
    Override standard methods from Object
     */

    @Override
    public String toString() {
        return voornaam + " " + achternaam + " " + organisatie + " " + telefoonnummer;
    }
}
