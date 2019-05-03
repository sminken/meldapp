package nl.ou.applabdemo.data.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Unit testen voor klasse GebruikerEntity
 *
 */
@SuppressWarnings("SpellCheckingInspection")
public class GebruikerEntityTest {

    private GebruikerEntity gebruikerEntity1;
    private GebruikerEntity gebruikerEntity2;
    private final String gebruikerVoornaam1 = "Karel";
    private final String gebruikerAchternaam1 = "Jansen";
    private final String organisatieNaam1 = "Organisatie Een";
    private final String uid1 = "9999eeeeeq99dhhahsd";
    private final String status1 = "Actief";
    private final String rol1 = "Behandelaar";
    private final String telefoonNummer1 = "123-1234568";
    private final Boolean notification = true;
    private final String deviceToken = "126t9alaflal";


    @Before
    public void setUp(){
        gebruikerEntity1 = new GebruikerEntity(
                gebruikerVoornaam1, gebruikerAchternaam1, organisatieNaam1, uid1, status1,
                rol1, telefoonNummer1, notification, deviceToken );
        gebruikerEntity2 = new GebruikerEntity();
    }

    @After
    public void tearDown() {
        gebruikerEntity1 = null;
        gebruikerEntity2 = null;
    }



    @Test
    public void getVoornaam() {
        assertEquals("voornaam van de gebruiker1 is correct", gebruikerVoornaam1,
                gebruikerEntity1.getVoornaam());
        assertFalse("voornaam van de gebruiker1 is niet correct",
                gebruikerEntity1.getVoornaam().equals("Niet goed"));
        assertEquals("voornaam van de gebruiker2 is correct", null,
                gebruikerEntity2.getVoornaam());
    }

    @Test
    public void setVoornaam() {
        String nieuweNaam = "Iemand anders";
        gebruikerEntity1.setVoornaam(nieuweNaam);
        assertEquals("voornaam van de gebruiker is correct", nieuweNaam,
                gebruikerEntity1.getVoornaam());
    }

    @Test
    public void getAchternaam() {
        assertEquals("achternaam van de gebruiker is correct",
                gebruikerAchternaam1, gebruikerEntity1.getAchternaam());
        assertFalse("achyernaam van de gebruiker is niet correct",
                gebruikerEntity1.getVoornaam().equals("Niet goed"));
        assertEquals("achternaam van de gebruiker2 is correct", null,
                gebruikerEntity2.getAchternaam());
    }

    @Test
    public void setAchternaam() {
        String nieuweNaam = "Iemand anders";
        gebruikerEntity1.setAchternaam(nieuweNaam);
        assertEquals("naam van de gebruiker 1 is correct", nieuweNaam,
                gebruikerEntity1.getAchternaam());
    }
    @Test
    public void getOrganisatie() {
        assertEquals("naam van de organisatie 1 is correct", organisatieNaam1,
                gebruikerEntity1.getOrganisatie());
        assertEquals("naam van de organisatie 1 is correct", null,
                gebruikerEntity2.getOrganisatie());

    }

    @Test
    public void setOrganisatie() {
        String nieuweOrganisatieNaam = "Organisatie 1b";
        gebruikerEntity1.setOrganisatie(nieuweOrganisatieNaam);
        assertEquals("naam van de organisatie 1 is correct", nieuweOrganisatieNaam,
                gebruikerEntity1.getOrganisatie());
    }

    @Test
    public void getUid() {
        assertEquals("uid 1 is correct", uid1, gebruikerEntity1.getUid());
        assertEquals("uid 2 is correct", null, gebruikerEntity2.getUid());
    }

    @Test
    public void setUid() {
        String nieuweUid = "4445DahHDHeeeq99dhhahsd";
        gebruikerEntity1.setUid(nieuweUid);
        assertEquals("uid 1 is correct", nieuweUid, gebruikerEntity1.getUid());
    }

    @Test
    public void getStatus() {
        assertEquals("status 1 is correct", status1, gebruikerEntity1.getStatus());
        assertEquals("status 2 is correct", null, gebruikerEntity2.getStatus());
    }

    @Test
    public void setStatus() {
        String nieuweStatus = "Inactief";
        gebruikerEntity1.setStatus(nieuweStatus);
        assertEquals("status 1 is correct", nieuweStatus, gebruikerEntity1.getStatus());
    }

    @Test
    public void getRol() {
        assertEquals("rol 1 is correct", rol1, gebruikerEntity1.getRol());
        assertEquals("rol 2 is correct", null, gebruikerEntity2.getRol());

    }

    @Test
    public void setRol() {
        String nieuweRol = "Melder";
        gebruikerEntity1.setRol(nieuweRol);
        assertEquals("rol 1 is correct", nieuweRol, gebruikerEntity1.getRol());
    }

    @Test
    public void getTelefoonnummer() {
        assertEquals("telefoonNummer 1 is correct", telefoonNummer1,
                gebruikerEntity1.getTelefoonnummer());
        assertEquals("telefoonNummer 2 is correct", null, gebruikerEntity2.getTelefoonnummer());
    }

    @Test
    public void setTelefoonnummer() {
        String nieuweTelefoonNummer = "010-99999";
        gebruikerEntity1.setTelefoonnummer(nieuweTelefoonNummer);
        assertEquals("telefoonNummer 1 is correct", nieuweTelefoonNummer,
                gebruikerEntity1.getTelefoonnummer());
    }

    @Test
    public void setNotification () {
        gebruikerEntity1.setNotification(false);
        assertEquals("notification 1 is correct", false,
                gebruikerEntity1.getNotification());
    }

    @Test
    public void getNotification () {
        assertEquals("notification 1 is correct", true,
                gebruikerEntity1.getNotification());
    }

    @Test
    public void setDeviceToken () {
        String nieuwToken = "h898ua0asokpoda";
        gebruikerEntity1.setDeviceToken(nieuwToken);
        assertEquals("deviceToken 1 is correct", nieuwToken, gebruikerEntity1.getDeviceToken());
    }

    @Test
    public void getDeviceToken () {
        assertEquals("deviceToken 1 is correct", deviceToken, gebruikerEntity1.getDeviceToken());
    }

    @Test
    public void toStringTest() {

        assertEquals("String 1 is correct", gebruikerEntity1.getVoornaam() + " " +
                gebruikerEntity1.getAchternaam() + " " + gebruikerEntity1.getOrganisatie() + " " +
                gebruikerEntity1.getTelefoonnummer() , gebruikerEntity1.toString());
        assertEquals("String 2 is correct", null + " " + null + " " + null + " " + null ,
                gebruikerEntity2.toString());
    }
}