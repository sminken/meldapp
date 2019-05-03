package nl.ou.applabdemo.data.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import static junit.framework.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({FirebaseDatabase.class})

public class MeldingRepositoryTest {

    @Mock
    private DatabaseReference mockedDatabaseReference;
    MeldingRepository meldingRepository;

    /**
     * Initialiseert Mockito en PowerMockito attributen
     *
     * @throws Exception
     */
    @Before
    public void Before() throws Exception {

        mockedDatabaseReference = Mockito.mock(DatabaseReference.class);
        FirebaseDatabase mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        when(mockedFirebaseDatabase.getReference()).thenReturn(mockedDatabaseReference);
        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFirebaseDatabase);

    }

    /**
     * Initialiseert een meldingRepository ten behoeve van de testen
     */
    @Before
    public void setUp() throws Exception {
        meldingRepository = new MeldingRepository();
    }

    /**
     * Maakt geheugen vrij na de testen
     */
    @After
    public void tearDown() {
        mockedDatabaseReference = null;
        meldingRepository = null;
    }

    /**
     * Test of rootNode op de juiste manier wordt geretourneerd
     */
    @Test
    public void getRootNode() {

        String rootNode = meldingRepository.getRootNode();
        assertEquals("meldingen", rootNode);

    }
}