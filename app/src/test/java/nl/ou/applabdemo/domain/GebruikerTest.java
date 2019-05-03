package nl.ou.applabdemo.domain;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nl.ou.applabdemo.util.MeldAppException;

import static org.junit.Assert.*;

/**
 * Unit testen voor klasse Gebruikers
 *
 */

@SuppressWarnings("ALL")
public class GebruikerTest {

    private Gebruiker gebruiker1;
    private final String voornaam1 = "Karel";
    private final String achternaam1 = "Klaasen";
    private final String organisatie1 = "";
    private final String uid1 = "88880000999";
    private final GebruikerStatus status1 = GebruikerStatus.ACTIEF;
    private final String telefoonNummer1 = "0987654321";
    private final Rol rol1 = Rol.BEHANDELAAR;
    private final boolean notification = false;
    private final String deviceToken = "jasdJHh298`N";


    @Before
    public void setUp() throws Exception {
        gebruiker1 = new Gebruiker(voornaam1, achternaam1, organisatie1, uid1,
                                   status1, telefoonNummer1,
                                   rol1, notification, deviceToken);
    }

    @After
    public void tearDown() {
        gebruiker1 = null;
    }

    @Test
    public void geenAchternaam () {

        String verwachteMelding = "Achternaam moet ingevuld zijn.";

        try {
            @SuppressWarnings("unused") Gebruiker resultaatGebruiker = new Gebruiker("", "", "", "9876",
                    GebruikerStatus.NIEUW, "" , Rol.MELDER, false, "aggaqo");
            Assert.fail("Meldapp exception verwacht" + verwachteMelding);
        } catch (MeldAppException meldAppEx) {
            Assert.assertTrue(verwachteMelding.equals(meldAppEx.getMessage()));
        }
    }

    @Test
    public void welOrganisatie () {

        String verwachteMelding = "Organisatie-waarde niet toegestaan.";

        try {
            @SuppressWarnings("unused") Gebruiker resultaatGebruiker = new Gebruiker("", "Jansen",
                    null, "9876", GebruikerStatus.NIEUW, "010-1234567", Rol.MELDER,
                    false, "jjhqhpq890asdl");
            Assert.fail("Meldapp exception verwacht" + verwachteMelding);
        } catch (MeldAppException meldAppEx) {
            Assert.assertTrue(verwachteMelding.equals(meldAppEx.getMessage()));
        }

        try {
            @SuppressWarnings("unused") Gebruiker resultaatGebruiker = new Gebruiker("", "Jansen",
                    "a", "9876", GebruikerStatus.NIEUW, "010-1234567", Rol.MELDER,
                    false, "jjhqhpq890asdl");
            Assert.fail("Meldapp exception verwacht" + verwachteMelding);
        } catch (MeldAppException meldAppEx) {
            Assert.assertTrue(verwachteMelding.equals(meldAppEx.getMessage()));
        }

        try {
            @SuppressWarnings("unused") Gebruiker resultaatGebruiker = new Gebruiker("", "Jansen",
                    "", "9876", GebruikerStatus.NIEUW, "010-1234567", Rol.MELDER,
                    false, "jjhqhpq890asdl");
        } catch (MeldAppException meldAppEx) {
            Assert.fail("Meldapp exception niet verwacht");;
        }
    }

    @Test
    public void geenUid () {

        String verwachteMelding = "Uid moet ingevuld zijn.";

        try {
            @SuppressWarnings("unused") Gebruiker resultaatGebruiker = new Gebruiker("", "Jansen",
                    "", null, GebruikerStatus.NIEUW, "", Rol.MELDER, false, "jjhqhpq890asdl");
            Assert.fail("Meldapp exception verwacht" + verwachteMelding);
        } catch (MeldAppException meldAppEx) {
            Assert.assertTrue(verwachteMelding.equals(meldAppEx.getMessage()));
        }

        try {
            @SuppressWarnings("unused") Gebruiker resultaatGebruiker = new Gebruiker("", "Jansen",
                    "", "", GebruikerStatus.NIEUW, "", Rol.MELDER, false, "jjhqhpq890asdl");
            Assert.fail("Meldapp exception verwacht" + verwachteMelding);
        } catch (MeldAppException meldAppEx) {
            Assert.assertTrue(verwachteMelding.equals(meldAppEx.getMessage()));
        }

        try {
            @SuppressWarnings("unused") Gebruiker resultaatGebruiker = new Gebruiker("", "Jansen",
                    "", "9876", GebruikerStatus.NIEUW, "010-1234567", Rol.MELDER,
                    false, "jjhqhpq890asdl");
        } catch (MeldAppException meldAppEx) {
            Assert.fail("Meldapp exception niet verwacht");;
        }

    }

    @Test
    public void getVoornaam() {
        assertEquals("voornaam van de gebruiker is correct", voornaam1, gebruiker1.getVoornaam());
    }

    @Test
    public void getAchternaam() {
        assertEquals("achternaam van de gebruiker is correct", achternaam1,
                gebruiker1.getAchternaam());
    }

    @Test
    public void getOrganisatie() {
        assertEquals("naam van de organisatie is correct", organisatie1,
                gebruiker1.getOrganisatie());
    }

    @Test
    public void getUid() {
        assertEquals("uid is correct", uid1, gebruiker1.getUid());
    }

    @Test
    public void getGebruikerStatus() {
        assertEquals("status is correct", status1, gebruiker1.getGebruikerStatus());
    }

    @Test
    public void getTelefoonnummer() {
        assertEquals("telefoonNummer is correct", telefoonNummer1,
                gebruiker1.getTelefoonnummer());
    }

    @Test
    public void getRol() {
        assertEquals("rol is correct", rol1, gebruiker1.getRol());
    }

    @Test
    public void getNotification () {
        assertEquals("notification is correct", notification, gebruiker1.getNotification());
    }

    @Test
    public void setNotification () {
        boolean nieuweNotificationWaarde = true;
        gebruiker1.setNotification(nieuweNotificationWaarde);
        assertEquals("notification nieuw is correct", nieuweNotificationWaarde,
                gebruiker1.getNotification());
    }

    @Test
    public void getDeviceToken () {
        assertEquals("deviceToken is correct", deviceToken, gebruiker1.getDeviceToken());
    }

    @Test
    public void setDeviceToken () {
        String nieuweDeviceToken = "jkajklglglfsqlllflq";
        gebruiker1.setDeviceToken(nieuweDeviceToken);
        assertEquals("deviceToken nieuw is correct", nieuweDeviceToken,
                gebruiker1.getDeviceToken());
    }

    @Test
    public void toStringTest() {

        assertEquals("String is correct", voornaam1 + " " + achternaam1 + " " + organisatie1 +
                " " + telefoonNummer1 , gebruiker1.toString());
    }

    @Test
    public void testGebruikersInfo () {
        GebruikerInfo resultaatGebruikerInfo = gebruiker1.geefGebruikerInfoInstantie();

        assertEquals("GebruikersInfo voornaam is correct", resultaatGebruikerInfo.getVoornaam(),
                gebruiker1.getVoornaam());
        assertEquals("GebruikersInfo achternaam is correct", resultaatGebruikerInfo.getAchternaam(),
                gebruiker1.getAchternaam());
        assertEquals("GebruikersInfo organisatie is correct", resultaatGebruikerInfo.getOrganisatie(),
                gebruiker1.getOrganisatie());
        assertEquals("GebruikersInfo uid is correct", resultaatGebruikerInfo.getUid(),
                gebruiker1.getUid());
        assertEquals("GebruikersInfo status is correct", resultaatGebruikerInfo.getGebruikerStatus(),
                gebruiker1.getGebruikerStatus());
        assertEquals("GebruikersInfo telefoonnummer is correct",
                resultaatGebruikerInfo.getTelefoonnummer(),
                gebruiker1.getTelefoonnummer());
        assertEquals("GebruikersInfo rol is correct", resultaatGebruikerInfo.getRol(),
                gebruiker1.getRol());
        assertEquals("GebruikersInfo notification is correct",
                resultaatGebruikerInfo.getNotification(), gebruiker1.getNotification());
        assertEquals("GebruikersInfo deviceToken is correct",
                resultaatGebruikerInfo.getDeviceToken(), gebruiker1.getDeviceToken());

    }

    @Test
    public void testTelefoonnummmerControles() {
        String verwachteMelding = "Alleen getallen en - zijn toegestaan";

        try {
             @SuppressWarnings("unused") Gebruiker resultaatGebruiker = new Gebruiker("", "Jansen", "", "9876",
                     GebruikerStatus.NIEUW, "a", Rol.MELDER, false, "jjhqhpq890asdl");
            Assert.fail("Meldapp exception verwacht" + verwachteMelding);
        } catch (MeldAppException meldAppEx) {
            Assert.assertTrue(verwachteMelding.equals(meldAppEx.getMessage()));
        }

        try {
            @SuppressWarnings("unused") Gebruiker resultaatGebruiker = new Gebruiker("", "Jansen", "", "9876",
                    GebruikerStatus.NIEUW, "1-a", Rol.MELDER, false, "jjhqhpq890asdl");
            Assert.fail("Meldapp exception verwacht" + verwachteMelding);
        } catch (MeldAppException meldAppEx) {
            Assert.assertTrue(verwachteMelding.equals(meldAppEx.getMessage()));
        }

        verwachteMelding = "Geef een geldig telefoonnummer op bestaande uit 10 cijfers";

        try {
            @SuppressWarnings("unused") Gebruiker resultaatGebruiker = new Gebruiker("", "Jansen", "", "9876",
                    GebruikerStatus.NIEUW, "123-4", Rol.MELDER, false, "jjhqhpq890asdl");
            Assert.fail("Meldapp exception verwacht" + verwachteMelding);
        } catch (MeldAppException meldAppEx) {
            Assert.assertTrue(verwachteMelding.equals(meldAppEx.getMessage()));
        }

        try {
            @SuppressWarnings("unused") Gebruiker resultaatGebruiker = new Gebruiker("", "Jansen", "", "9876",
                    GebruikerStatus.NIEUW, "123-4567890", Rol.MELDER, false, "jjhqhpq890asdl");
            Assert.fail("Meldapp exception verwacht" + verwachteMelding);
        } catch (MeldAppException meldAppEx) {
            Assert.assertTrue(verwachteMelding.equals(meldAppEx.getMessage()));
        }

        try {
            @SuppressWarnings("unused") Gebruiker resultaatGebruiker = new Gebruiker("", "Jansen",
                    "", "9876", GebruikerStatus.NIEUW, "012-4567890", Rol.MELDER,
                    false, "jjhqhpq890asdl");
        } catch (MeldAppException meldAppEx) {
            Assert.fail("Meldapp exception niet verwacht");

        }
    }
}