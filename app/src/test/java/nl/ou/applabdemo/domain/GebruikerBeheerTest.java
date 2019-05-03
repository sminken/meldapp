package nl.ou.applabdemo.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit testen voor klasse GebruikerBeheer
 *
 */
@SuppressWarnings("SpellCheckingInspection")
public class GebruikerBeheerTest {

    private GebruikerBeheer gebruikerBeheerGemengd;
    private GebruikerBeheer gebruikerBeheerAlleenBehandelaars;
    private GebruikerBeheer leegBeheer;
    private GebruikerBeheer nulElementenBeheer;
    private Gebruiker gebruiker1;
    private Gebruiker gebruiker2;
    private Gebruiker gebruiker3;
    private GebruikerInfo gebruikerInfo1;
    private GebruikerInfo gebruikerInfo2;
    private GebruikerInfo gebruikerInfo3;

    @SuppressWarnings("Convert2Diamond")
    @Before
    public void setUp() throws Exception {
        leegBeheer = new GebruikerBeheer(null);

        nulElementenBeheer = new GebruikerBeheer(new ArrayList<Gebruiker>());

        gebruiker1 = new Gebruiker("Piet", "Klaasen", "", "1234", GebruikerStatus.NIEUW,
                                    "013-1234567", Rol.MELDER, false, "jjhqhpq890asdl" );
        gebruiker2 = new Gebruiker("Karel", "Egberts", "", "7337", GebruikerStatus.ACTIEF,
                "020-8888888", Rol.BEHANDELAAR, false, "jjhqhpq890asdl" );
        gebruiker3 = new Gebruiker("Karel", "Egberts", "", "9876" , GebruikerStatus.ACTIEF,
                "020-8888888", Rol.MELDER, false, "jjhqhpq890asdl" );

        List<Gebruiker> lijstGebruikers = new ArrayList<Gebruiker>();
        lijstGebruikers.add(gebruiker1);
        lijstGebruikers.add(gebruiker2);
        lijstGebruikers.add(gebruiker3);
        lijstGebruikers.add(null);

        gebruikerBeheerGemengd = new GebruikerBeheer(lijstGebruikers);

        lijstGebruikers = new ArrayList<Gebruiker>();
        lijstGebruikers.add(gebruiker2);
        lijstGebruikers.add(gebruiker2);
        lijstGebruikers.add(gebruiker2);

        gebruikerBeheerAlleenBehandelaars = new GebruikerBeheer(lijstGebruikers);

        gebruikerInfo1 = new GebruikerInfo(gebruiker1.getVoornaam(), gebruiker1.getAchternaam(),
                gebruiker1.getOrganisatie(), gebruiker1.getUid(), gebruiker1.getGebruikerStatus(),
                gebruiker1.getTelefoonnummer(), gebruiker1.getRol(),
                gebruiker1.getNotification(), gebruiker1.getDeviceToken());

        gebruikerInfo2 = new GebruikerInfo(gebruiker2.getVoornaam(), gebruiker2.getAchternaam(),
                gebruiker2.getOrganisatie(), gebruiker2.getUid(), gebruiker2.getGebruikerStatus(),
                gebruiker2.getTelefoonnummer(), gebruiker2.getRol(),
                gebruiker2.getNotification(), gebruiker2.getDeviceToken());

        gebruikerInfo3 = new GebruikerInfo(gebruiker3.getVoornaam(), gebruiker3.getAchternaam(),
                gebruiker3.getOrganisatie(), gebruiker3.getUid(), gebruiker3.getGebruikerStatus(),
                gebruiker3.getTelefoonnummer(), gebruiker3.getRol(),
                gebruiker3.getNotification(), gebruiker3.getDeviceToken());

    }

    @After
    public void tearDown() {

        leegBeheer = null;
        nulElementenBeheer = null;
        gebruikerBeheerGemengd = null;

        gebruiker1 = null;
        gebruiker2 = null;
        gebruiker3 = null;
    }

    @Test
    public void haalGebruikerOp() {

        GebruikerInfo gevondenGebruiker;

        /*
        Test met gebruikersbeheer null
         */
        gevondenGebruiker = leegBeheer.haalGebruikerOp(null);
        assertEquals(gevondenGebruiker, null);

        gevondenGebruiker = leegBeheer.haalGebruikerOp("1234");
        assertEquals(gevondenGebruiker, null);

        /*
        Test met gebruikersbeheer met geen elementen
         */
        gevondenGebruiker = nulElementenBeheer.haalGebruikerOp(null);
        assertEquals(gevondenGebruiker, null);

        gevondenGebruiker = nulElementenBeheer.haalGebruikerOp("4567");
        assertEquals(gevondenGebruiker, null);

        /*
        Test met gevuld gebruikersbeheer
         */
        gevondenGebruiker = gebruikerBeheerGemengd.haalGebruikerOp(null);
        assertEquals(gevondenGebruiker, null);

        gevondenGebruiker = gebruikerBeheerGemengd.haalGebruikerOp("");
        assertEquals(gevondenGebruiker, null);

        gevondenGebruiker = gebruikerBeheerGemengd.haalGebruikerOp("9999");
        assertEquals(gevondenGebruiker, null);

        gevondenGebruiker = gebruikerBeheerGemengd.haalGebruikerOp("1234");
        controleerResultaat(gevondenGebruiker, gebruikerInfo1);


        gevondenGebruiker = gebruikerBeheerGemengd.haalGebruikerOp("7337");
        controleerResultaat(gevondenGebruiker, gebruikerInfo2);

    }

    private void controleerResultaat(GebruikerInfo gevondenGebruiker, GebruikerInfo gebruikerInfo) {
        assertEquals(gevondenGebruiker.getVoornaam(), gebruikerInfo.getVoornaam());
        assertEquals(gevondenGebruiker.getAchternaam(), gebruikerInfo.getAchternaam());
        assertEquals(gevondenGebruiker.getOrganisatie(), gebruikerInfo.getOrganisatie());
        assertEquals(gevondenGebruiker.getUid(), gebruikerInfo.getUid());
        assertEquals(gevondenGebruiker.getGebruikerStatus(), gebruikerInfo.getGebruikerStatus());
        assertEquals(gevondenGebruiker.getTelefoonnummer(), gebruikerInfo.getTelefoonnummer());
        assertEquals(gevondenGebruiker.getRol(), gebruikerInfo.getRol());
        assertEquals(gevondenGebruiker.getOpgemaakteNaam(), gebruikerInfo.getOpgemaakteNaam());

    }

    @SuppressWarnings("EmptyMethod")
    @Test
    public void haalAangemeldeGebruiker() {

        //TODO java.lang.IllegalStateException: Default FirebaseApp is not initialized in this process null.
        // Make sure to call FirebaseApp.initializeApp(Context) first.
         // mock stubbing
//        when(mockedFirebaseUser.getUid()).thenReturn("1234");
//
//        GebruikerInfo gevondenGebruiker;
//        gevondenGebruiker = gebruikerBeheer.haalAangemeldeGebruiker();
//        controleerResultaat(gevondenGebruiker, gebruikerInfo1);


    }

    @Test
    public void geefAlleMelders() {

        List<GebruikerInfo> opgehaaldeMelders;

        opgehaaldeMelders = leegBeheer.geefAlleMelders();
        assertEquals(0, opgehaaldeMelders.size());

        opgehaaldeMelders = nulElementenBeheer.geefAlleMelders();
        assertEquals(0, opgehaaldeMelders.size());

        opgehaaldeMelders = gebruikerBeheerAlleenBehandelaars.geefAlleMelders();
        assertEquals(0, opgehaaldeMelders.size());

        opgehaaldeMelders = gebruikerBeheerGemengd.geefAlleMelders();
        assertEquals(2, opgehaaldeMelders.size());
        controleerResultaat(opgehaaldeMelders.get(0), gebruikerInfo1);
        controleerResultaat(opgehaaldeMelders.get(1), gebruikerInfo3);




    }


    }