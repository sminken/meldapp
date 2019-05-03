package nl.ou.applabdemo.data.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nl.ou.applabdemo.domain.Status;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 * Klasse verantwoordelijk voor het testen van de klasse MeldingEntity
 */
public class MeldingEntityTest {

    MeldingEntity meldingEntity;

    /**
     * Initialiseert een meldingEnitity ten behoeve van de testen
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        meldingEntity = new MeldingEntity(
                "1234",
                "6666",
                "avontureneiland",
                "een lange reis rond de wereld",
                "13-07-2018 11:57:24",
                "OPEN",
                false);
    }

    /**
     * Stelt Melding op null na de test zodat geheugen kan worden vrijgemaakt
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        meldingEntity = null;
    }

    /**
     * Test of de juiste naam van de gebruiker wordt geretourneerd
     */
    @Test
    public void getUidGebruikerMelder() {
        String uidGebruikerMelder = meldingEntity.getUidGebruikerMelder();
        assertEquals("naam gebruiker is niet correct", "1234", uidGebruikerMelder);
        assertFalse("naam gebruiker zou niet goed moeten zijn", uidGebruikerMelder.equals("Steven van de OU"));
    }

    /**
     * Test of de juiste naam van de gebruiker wordt geretourneerd
     */
    @Test
    public void getUidGebruikerBehandelaar() {
        String uidGebruikerBehandelaar = meldingEntity.getUidGebruikerBehandelaar();
        assertEquals("naam gebruiker is niet correct", "6666", uidGebruikerBehandelaar);
        assertFalse("naam gebruiker zou niet goed moeten zijn", uidGebruikerBehandelaar.equals("Steven van de OU"));
    }

    /**
     * Test of het juiste onderwerp wordt geretourneerd
     */
    @Test
    public void getOnderwerp() {
        String onderwerp = meldingEntity.getOnderwerp();
        assertEquals("onderwerp is niet correct", "avontureneiland", onderwerp);
        assertFalse("onderwerp zou niet goed moeten zijn", onderwerp.equals("drakeneiland"));
    }

    /**
     * Test of de juiste inhoud wordt geretourneerd
     */
    @Test
    public void getInhoud() {
        String inhoud = meldingEntity.getInhoud();
        String verwachteInhoud = "een lange reis rond de wereld";
        String verkeerdeInhoud = "het loopt slecht af met de draak";

        assertEquals("inhoud is niet correct", verwachteInhoud, inhoud);
        assertFalse("inhoud zou niet goed moeten zijn", inhoud.equals(verkeerdeInhoud));
    }

    /**
     * Test of de juiste status wordt geretourneerd
     */
    @Test
    public void getStatus() {
        String status = meldingEntity.getStatus();

        assertEquals("status is niet correct", "OPEN", status);
        assertFalse("status zou niet goed moeten zijn", status.equals(Status.GESLOTEN));
    }

    /**
     * Test of de juiste datum en tijd wordt geretourneerd
     */
    @Test
    public void getDatumTijd() {
        String datumTijd = meldingEntity.getDatumTijd();
        assertEquals("melding datumTijd is niet correct", "13-07-2018 11:57:24", datumTijd);
        assertFalse("datumTijd zou niet goed moeten zijn", datumTijd.equals("13-03-2021 11:44:24"));
    }
}