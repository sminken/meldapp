package nl.ou.applabdemo.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nl.ou.applabdemo.util.MeldAppException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 * Klasse verantwoordelijk voor het testen van de klasse Melding
 *
 */
public class MeldingTest {

    Melding melding;

    /**
     * Initialiseert een melding ten behoeve van de testen
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        melding = new Melding("3", "4321", "7777", "de sprookjesboom",
                "lang verhaal maar ze leefden nog lang en gelukkig", "13-07-2018 11:57:24", Status.NIEUW.toString(),
                false);
    }

    /**
     * Stelt Melding op null na de test zodat geheugen kan worden vrijgemaakt
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        melding = null;
    }

    /**
     * Test of de juiste uid wordt geretourneert van de melding
     */
    @Test
    public void getUidMelding() {
        String uid = melding.getUidMelding();
        assertEquals("melding uid is niet correct", "3", uid);
    }

    /**
     * Test of de juiste datum en tijd worden geretourneerd
     */
    @Test
    public void getDatumTijd() {
        String datumTijd = melding.getDatumTijd();
        assertEquals("melding datumTijd is niet correct", "13-07-2018 11:57:24", datumTijd);
        assertFalse("datumTijd zou niet goed moeten zijn", datumTijd.equals("13-03-2021 11:44:24"));
    }

    /**
     * Test of de juiste uid van de melder wordt geretourneerd
     */
    @Test
    public void getUidGebruikerMelder() {
        String uidGebruikerMelder = melding.getUidGebruikerMelder();
        assertEquals("uid gebruiker is niet correct", "4321", uidGebruikerMelder);
        assertFalse("uid gebruiker zou niet goed moeten zijn", uidGebruikerMelder.equals("345234"));
    }

    /**
     * Test of de juiste id van de behandelaar wordt geretourneerd
     */
    @Test
    public void getUidGebruikerBehandelaar() {
        String uidGebruikerBehandelaar = melding.getUidGebruikerBehandelaar();
        assertEquals("uid gebruiker is niet correct", "7777", uidGebruikerBehandelaar);
        assertFalse("uid gebruiker zou niet goed moeten zijn", uidGebruikerBehandelaar.equals("345234"));
    }

    /**
     * Test of het juiste onderwerp wordt geretourneerd
     */
    @Test
    public void getOnderwerp() {
        String onderwerp = melding.getOnderwerp();
        assertEquals("onderwerp is niet correct", "de sprookjesboom", onderwerp);
        assertFalse("onderwerp zou niet goed moeten zijn", onderwerp.equals("drakeneiland"));
    }

    /**
     * Test of de juiste inhoud wordt geretourneerd
     */
    @Test
    public void getInhoud() {
        String inhoud = melding.getInhoud();
        String verwachteInhoud = "lang verhaal maar ze leefden nog lang en gelukkig";
        String verkeerdeInhoud = "het loopt slecht af met de draak";

        assertEquals("inhoud is niet correct", verwachteInhoud, inhoud);
        assertFalse("inhoud zou niet goed moeten zijn", inhoud.equals(verkeerdeInhoud));
    }

    /**
     * Test of de juiste status wordt weergegeven
     */
    @Test
    public void getStatus() {
        Status status = melding.getStatus();

        assertEquals("status is niet correct", Status.NIEUW, status);
        assertFalse("status zou niet goed moeten zijn", status.equals(Status.GESLOTEN));
    }

    /**
     * Test of de juiste notification wordt weergegeven
     */
    @Test
    public void getNotification() {
        Boolean notification = melding.getNotificatieVerstuurd();

        assertFalse("notification is niet correct", notification);
    }


    /**
     * Test of twee melding gelijk zijn
     */
    @Test
    public void equals() throws MeldAppException {
        Melding meldingAndere = new Melding("3", "4321", "6666","de sprookjesboom",
                "lang verhaal maar ze leefden nog lang en gelukkig", "13-07-2018 11:57:24", Status.NIEUW.toString(),
                false);
        Melding meldingVerkeerd = new Melding("4", "Pieter Jan", "6666","computer stuk",
                "ik heb dringend hulp nodig", "14-07-2013 11:57:24", Status.NIEUW.toString(),
                false);
        Boolean gelijk = melding.equals(meldingAndere);
        assertEquals("de meldingen zouden gelijk dienen te zijn", true, melding.equals(meldingAndere));
        assertFalse("de meldingen zouden niet gelijk dienen te zijn", melding.equals(meldingVerkeerd));
    }

    /**
     * Test of de hashcode van twee indentieke objecten gelijk is
     */
    @Test
    public void hashCodeTest() throws MeldAppException {
        Melding meldingTwee = new Melding("3", "4321", "6666","de sprookjesboom",
                "lang verhaal maar ze leefden nog lang en gelukkig", "13-07-2018 11:57:24", Status.NIEUW.toString(),
                false);
        Melding meldingVerkeerd = new Melding("4", "Pieter Jan", "6666","computer stuk",
                "ik heb dringend hulp nodig", "14-07-2013 11:57:24", Status.NIEUW.toString(),
                false);

        assertEquals("de hashcode zou gelijk dienen te zijn", melding.hashCode(), meldingTwee.hashCode());
        assertFalse("de hashcode zou verschillend dienen te zijn", melding.hashCode() == meldingVerkeerd.hashCode());
    }

    /**
     * Test of de juiste toString text wordt geretourneerd
     */
    @Test
    public void toStringTest() {
        String text = "melding gedaan door melder uid: 4321 met als onderwerp: de sprookjesboom en als inhoud: lang verhaal maar ze leefden nog lang en gelukkig";
        assertEquals("de toString zou gelijk dienen te zijn", melding.toString(), text);
    }
}