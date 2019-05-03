package nl.ou.applabdemo.data.mapper;

import com.google.firebase.database.DataSnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import nl.ou.applabdemo.data.entity.MeldingEntity;
import nl.ou.applabdemo.domain.Melding;
import nl.ou.applabdemo.domain.Status;
import nl.ou.applabdemo.util.MeldAppException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Klasse om MeldingMapper te testen via een mockedDataSnapshot met Mockito
 */
public class MeldingMapperTest {

    @Mock
    private DataSnapshot mockedDataSnapshot;
    @Mock
    private MeldingEntity meldingEntityTest;
    @Mock
    private Melding meldingTest;

    /**
     * Initialiseert mockedsnapshot en andere attributen benodigd voor de test van meldingmapper
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        mockedDataSnapshot = mock(DataSnapshot.class);
        meldingEntityTest = maakMeldingEnityTest();
        meldingTest = maakMeldingTest();
    }

    /**
     * Maakt geheugen vrij na de testen
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        mockedDataSnapshot = null;
        meldingEntityTest = null;
        meldingTest = null;
    }

    /**
     * Test de methode map() in de meldingmapper
     */
    @Test
    public void map() throws MeldAppException {

        // mock stubbing
        when(mockedDataSnapshot.getValue(MeldingEntity.class)).thenReturn(meldingEntityTest);

        Melding meldingVanMapper = new MeldingMapper().map(mockedDataSnapshot);

        assertEquals("fout in meldingTerugVanMapper", meldingVanMapper.getUidGebruikerMelder(), meldingEntityTest.getUidGebruikerMelder());
        assertEquals("fout in meldingTerugVanMapper", meldingVanMapper.getOnderwerp(), meldingEntityTest.getOnderwerp());
        assertEquals("fout in meldingTerugVanMapper", meldingVanMapper.getInhoud(), meldingEntityTest.getInhoud());
        assertEquals("fout in meldingTerugVanMapper", meldingVanMapper.getDatumTijd(), meldingEntityTest.getDatumTijd());
        assertEquals("fout in meldingTerugVanMapper", meldingVanMapper.getStatus().toString(), meldingEntityTest.getStatus());
        assertEquals("fout in meldingTerugVanMapper", meldingVanMapper.getNotificatieVerstuurd(), meldingEntityTest.getNotificatieVerstuurd());

        assertFalse("meldingTerugVan Ontvanger dient fout te zijn", meldingVanMapper.getInhoud().equals("dit is de verkeerde inhoud"));

    }

    /**
     * Test de methode demap() in de meldingmapper
     */
    @Test
    public void demap() {

        MeldingEntity meldingEntityDemapTest = MeldingMapper.demap(meldingTest);

        assertEquals("fout in meldingEntityDemapTest", meldingEntityDemapTest.getUidGebruikerMelder(), meldingTest.getUidGebruikerMelder());
        assertEquals("fout in meldingEntityDemapTest", meldingEntityDemapTest.getOnderwerp(), meldingTest.getOnderwerp());
        assertEquals("fout in meldingEntityDemapTest", meldingEntityDemapTest.getInhoud(), meldingTest.getInhoud());
        assertEquals("fout in meldingEntityDemapTest", meldingEntityDemapTest.getDatumTijd(), meldingTest.getDatumTijd());
        assertEquals("fout in meldingEntityDemapTest", meldingEntityDemapTest.getStatus(), meldingTest.getStatus().toString());
        assertEquals("fout in meldingEntityDemapTest", meldingEntityDemapTest.getNotificatieVerstuurd(), meldingTest.getNotificatieVerstuurd());

        assertFalse("meldingTerugVan Ontvanger dient fout te zijn", meldingEntityDemapTest.getInhoud().equals("dit is de verkeerde inhoud"));

    }

    /**
     * Retourneert een MeldingEntity object benodigd voor de testen
     *
     * @return MeldingEntity de meldingentity
     */
    private MeldingEntity maakMeldingEnityTest() {
        meldingEntityTest = new MeldingEntity(
                "4321",
                "6666",
                "Onderwerp test",
                "Inhoud test",
                "16-08-2018 17:45:22",
                "NIEUW",
                false);

        return meldingEntityTest;
    }

    /**
     * Retourneert een Melding object benodigd voor de testen
     *
     * @return Melding de melding
     * @throws MeldAppException
     */
    private Melding maakMeldingTest() throws MeldAppException {
        meldingTest = new Melding(
                "12345",
                "4321",
                "6666",
                "Onderwerp test",
                "Inhoud test",
                "16-08-2018 17:45:22",
                Status.NIEUW.toString(),
                false);

        return meldingTest;
    }
}
