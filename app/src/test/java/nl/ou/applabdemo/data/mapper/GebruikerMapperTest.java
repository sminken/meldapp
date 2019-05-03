package nl.ou.applabdemo.data.mapper;

import com.google.firebase.database.DataSnapshot;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nl.ou.applabdemo.data.entity.GebruikerEntity;
import nl.ou.applabdemo.domain.Gebruiker;
import nl.ou.applabdemo.domain.GebruikerStatus;
import nl.ou.applabdemo.domain.Rol;
import nl.ou.applabdemo.util.MeldAppException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit testen voor klasse GebruikerMapper
 *
 */
@SuppressWarnings("SpellCheckingInspection")
public class GebruikerMapperTest {
    private DataSnapshot mockedDataSnapshot;
    private GebruikerEntity gebruikerEntityTest;

    private final String voornaamTestWaarde = "Ron";
    private final String achternaamTestWaarde = "van den Enden";
    private final String telefoonnummerTestWaarde = "0612345678";
    private final String uidTestWaarde = "1HaAHHHHHAAAAA";
    private final String organisatieTestWaarde = "";
    private final String statusTestWaarde = "NIEUW";
    private final String rolTestWaarde = "MELDER";
    private final boolean notificationTestWaarde = true;
    private final String deviceTokenTestWaarde = "8237basdfafas";

    @Before
    public void setUp() {

        // mock creation
        mockedDataSnapshot = mock(DataSnapshot.class);

    }

    @After
    public void tearDown() {

        gebruikerEntityTest = null;
        mockedDataSnapshot = null;
    }

    @Test
    public void map() {

        // test met leeg snapshot
        GebruikerMapper gebruikerMapperNull = new GebruikerMapper();
        Gebruiker resultaatGebruiker = null;
        try {
            resultaatGebruiker = gebruikerMapperNull.map(null);
        } catch (MeldAppException meldAppEx) {
            Assert.fail("Meldapp exception niet verwacht" + meldAppEx.getMessage());
        }
        assertNull(resultaatGebruiker);

        // mock stubbing: snapshot bevat geen GebruikerEntity
        when(mockedDataSnapshot.getValue(GebruikerEntity.class)).thenReturn(null);
        try {
            resultaatGebruiker = gebruikerMapperNull.map(mockedDataSnapshot);
        } catch (MeldAppException meldAppEx) {
            Assert.fail("Meldapp exception niet verwacht" + meldAppEx.getMessage());
        }
        assertNull(resultaatGebruiker);


        /*
         Test - Uid mag niet null zijn
          */
        gebruikerEntityTest = geefTestGebruikersEntity();
        gebruikerEntityTest.setUid(null);

        // mock stubbing
        when(mockedDataSnapshot.getValue(GebruikerEntity.class)).thenReturn(gebruikerEntityTest);

        // voer test uit
        String verwachteMelding = "Uid moet ingevuld zijn.";
        testNegatiefResultaat(mockedDataSnapshot, verwachteMelding);

        verify(mockedDataSnapshot, times(2)).getValue(GebruikerEntity.class);

        /*
         Test - Uid mag niet leeg zijn
          */
        gebruikerEntityTest = geefTestGebruikersEntity();
        gebruikerEntityTest.setUid("");

        // mock stubbing:
        when(mockedDataSnapshot.getValue(GebruikerEntity.class)).thenReturn(gebruikerEntityTest);

        // voer test uit
        verwachteMelding = "Uid moet ingevuld zijn.";
        testNegatiefResultaat(mockedDataSnapshot, verwachteMelding);

         /*
         Test - Organisatie mag niet null zijn
          */
        gebruikerEntityTest = geefTestGebruikersEntity();
        gebruikerEntityTest.setOrganisatie(null);

        // mock stubbing
        when(mockedDataSnapshot.getValue(GebruikerEntity.class)).thenReturn(gebruikerEntityTest);

        // voer test uit
        verwachteMelding = "Organisatie-waarde niet toegestaan.";
        testNegatiefResultaat(mockedDataSnapshot, verwachteMelding);

        /*
         Test - Organisatie moet leeg zijn
          */
        gebruikerEntityTest = geefTestGebruikersEntity();
        gebruikerEntityTest.setOrganisatie("AAA");

        // mock stubbing
        when(mockedDataSnapshot.getValue(GebruikerEntity.class)).thenReturn(gebruikerEntityTest);

        // voer test uit
        verwachteMelding = "Organisatie-waarde niet toegestaan.";
        testNegatiefResultaat(mockedDataSnapshot, verwachteMelding);

         /*
        Test ongeldige waarde Status
         */
        gebruikerEntityTest = geefTestGebruikersEntity();
        gebruikerEntityTest.setStatus("AAA");

        // mock stubbing
        when(mockedDataSnapshot.getValue(GebruikerEntity.class)).thenReturn(gebruikerEntityTest);

        // voer test uit
        verwachteMelding = "Map van GebruikerUid: " + gebruikerEntityTest.getUid() + " Ongeldige waarde status: " + "AAA";
        testNegatiefResultaat(mockedDataSnapshot, verwachteMelding);

        /*
        Test ongeldige waarde Rol
         */

        gebruikerEntityTest = geefTestGebruikersEntity();
        gebruikerEntityTest.setRol("AAA");

        // mock stubbing
        when(mockedDataSnapshot.getValue(GebruikerEntity.class)).thenReturn(gebruikerEntityTest);

        // voer test uit
        verwachteMelding = "Map van GebruikerUid: " + gebruikerEntityTest.getUid() + " Ongeldige waarde rol: " + "AAA";
        testNegatiefResultaat(mockedDataSnapshot, verwachteMelding);


          /*
        Test gebruiker met juiste attributen
         */
        gebruikerEntityTest = geefTestGebruikersEntity();
        // mock stubbing
        when(mockedDataSnapshot.getValue(GebruikerEntity.class)).thenReturn(gebruikerEntityTest);

        // voer test uit
        GebruikerMapper gebruikerMapper = new GebruikerMapper();

        // controle op juiste verwerking van alle mogelijke Enum instances voor Status en Rol
        for (GebruikerStatus gebruikerStatus: GebruikerStatus.values()) {
            for (Rol rol : Rol.values()) {
                Gebruiker resultaatGebruikerValue;
                try {
                    gebruikerEntityTest.setRol(rol.toString());
                    gebruikerEntityTest.setStatus(gebruikerStatus.toString());
                    resultaatGebruikerValue = gebruikerMapper.map(mockedDataSnapshot);
                    controleerWaardenGebruiker(resultaatGebruikerValue, gebruikerStatus, rol,
                            notificationTestWaarde);

                } catch (MeldAppException meldAppEx) {
                    Assert.fail("Meldapp exception niet verwacht");
                }
            }
        }

        // test met notificationwaarde false

        try {
            gebruikerEntityTest.setRol(Rol.MELDER.toString());
            gebruikerEntityTest.setStatus(GebruikerStatus.NIEUW.toString());
            gebruikerEntityTest.setNotification(false);
            Gebruiker resultaatGebruikerValue = gebruikerMapper.map(mockedDataSnapshot);
            controleerWaardenGebruiker(resultaatGebruikerValue, GebruikerStatus.NIEUW,
                    Rol.MELDER, false);

        } catch (MeldAppException meldAppEx) {
            Assert.fail("Meldapp exception niet verwacht");
        }

        /*
        Controle aantal malen mock uitgevoerd
         */
        verify(mockedDataSnapshot, times(17)).getValue(GebruikerEntity.class);
    }

    @Test
    public void demap() {

        GebruikerEntity gebruikerEntityNull = GebruikerMapper.demap(null);
        assertNull(gebruikerEntityNull);

        // controle op juiste verwerking van alle mogelijke Enum instances voor Status en Rol

        for (GebruikerStatus gebruikerStatus: GebruikerStatus.values()) {
            for (Rol rol : Rol.values()) {

                try {
                    Gebruiker testGebruiker = new Gebruiker(voornaamTestWaarde,
                            achternaamTestWaarde, organisatieTestWaarde, uidTestWaarde,
                            gebruikerStatus, telefoonnummerTestWaarde, rol,
                            notificationTestWaarde, deviceTokenTestWaarde);

                    GebruikerEntity gebruikerEntity = GebruikerMapper.demap(testGebruiker);
                    controleerWaardenGebruikerEntity(gebruikerEntity, gebruikerStatus, rol);

                } catch (MeldAppException meldAppEx) {
                    Assert.fail("Meldapp exception niet verwacht bij: " + gebruikerEntityTest.toString());
                }
            }
        }

    }

    /**
     * Deze methode controleert voor klasse Gebruiker of het resultaat ook gelijk is
     * aan het verwachte resultaat
     * @param resultaatGebruiker - resultaat van mapper
     * @param verwachteStatus - verwachte Status resultaat
     * @param verwachteRol - verachte Rol resultaat
     */

    private void controleerWaardenGebruiker(Gebruiker resultaatGebruiker,
                                            GebruikerStatus verwachteStatus, Rol verwachteRol,
                                            boolean verwachteNotification
                                            ) {
        assertEquals(voornaamTestWaarde, resultaatGebruiker.getVoornaam());
        assertEquals(achternaamTestWaarde, resultaatGebruiker.getAchternaam());
        assertEquals(organisatieTestWaarde, resultaatGebruiker.getOrganisatie());
        assertEquals(uidTestWaarde, resultaatGebruiker.getUid());
        assertEquals(verwachteStatus, resultaatGebruiker.getGebruikerStatus());
        assertEquals(telefoonnummerTestWaarde, resultaatGebruiker.getTelefoonnummer());
        assertEquals(verwachteRol, resultaatGebruiker.getRol());
        assertEquals(verwachteNotification, resultaatGebruiker.getNotification());
        assertEquals(deviceTokenTestWaarde, resultaatGebruiker.getDeviceToken());
    }

    /**
     * Deze methode gaat na dat de verwachte exception ook daadwerkelijk optreedt.
     *        Als deze excpetion niet optreedt, wordt een foutmelding gegeven
     *
     * @param dataSnapshot - snapshot met daarin document uit de collectie van gebruikers
     *                     uit de database
     * @param verwachteMelding - de foutmelding die wordt verwacht
     */
    private void testNegatiefResultaat(DataSnapshot dataSnapshot, String verwachteMelding) {
        GebruikerMapper gebruikerMapper = new GebruikerMapper();
        try {
            //noinspection unused
            Gebruiker resultaatGebruiker = gebruikerMapper.map(dataSnapshot);
            Assert.fail("Meldapp exception verwacht" + verwachteMelding);
        } catch (MeldAppException meldAppEx) {

            Assert.assertTrue(verwachteMelding.equals(meldAppEx.getMessage()));
        }

    }


    /**
     * Deze methode controleert voor klasse GebruikerEntity of het resultaat ook gelijk is
     * aan het verwachte resultaat
     * @param resultaatGebruiker - resultaat van demapper
     * @param verwachteStatus - verwachte Status resultaat
     * @param verwachteRol - verachte Rol resultaat
     */

    private void controleerWaardenGebruikerEntity(GebruikerEntity resultaatGebruiker,
                                                  GebruikerStatus verwachteStatus,
                                                  Rol verwachteRol) {
        assertEquals(voornaamTestWaarde, resultaatGebruiker.getVoornaam());
        assertEquals(achternaamTestWaarde, resultaatGebruiker.getAchternaam());
        assertEquals(organisatieTestWaarde, resultaatGebruiker.getOrganisatie());
        assertEquals(uidTestWaarde, resultaatGebruiker.getUid());
        assertEquals(verwachteStatus.toString(), resultaatGebruiker.getStatus());
        assertEquals(telefoonnummerTestWaarde, resultaatGebruiker.getTelefoonnummer());
        assertEquals(verwachteRol.toString(), resultaatGebruiker.getRol());
        assertEquals(notificationTestWaarde, resultaatGebruiker.getNotification());
        assertEquals(deviceTokenTestWaarde, resultaatGebruiker.getDeviceToken());

    }

    /**
     * Deze methode geeft een instantie van GebruikersEntit met de vaste test waarden terug
     * @return - de instantie met vaste waarden
     */
    private GebruikerEntity geefTestGebruikersEntity() {
        GebruikerEntity gebruikerEntityTest = new GebruikerEntity();
        gebruikerEntityTest.setVoornaam(voornaamTestWaarde);
        gebruikerEntityTest.setAchternaam(achternaamTestWaarde);
        gebruikerEntityTest.setTelefoonnummer(telefoonnummerTestWaarde);
        gebruikerEntityTest.setUid(uidTestWaarde);
        gebruikerEntityTest.setOrganisatie(organisatieTestWaarde);
        gebruikerEntityTest.setStatus(statusTestWaarde);
        gebruikerEntityTest.setRol(rolTestWaarde);
        gebruikerEntityTest.setNotification(notificationTestWaarde);
        gebruikerEntityTest.setDeviceToken(deviceTokenTestWaarde);
        return gebruikerEntityTest;

    }
}