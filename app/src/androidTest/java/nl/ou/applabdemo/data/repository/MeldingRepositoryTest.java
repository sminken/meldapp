package nl.ou.applabdemo.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import nl.ou.applabdemo.domain.Melding;
import nl.ou.applabdemo.util.MeldAppException;

import static org.junit.Assert.assertEquals;

/**
 * Klasse verantwoordelijk voor het testen van de klasse MeldingRepository
 * opmerking: deze klasse test alleen de methoden die een interactie hebben met de database
 */
public class MeldingRepositoryTest {

    public static final String TAG = "MeldingRepositoryTest: ";

    private static Melding meldingTest;
    private MeldingRepository meldingRepository;
    private Melding nieuweMelding;
    private Melding wijzigStatusMelding;
    private Melding toewijzenMelding;
    private Melding losOpMelding;
    private List<Melding> resultMeldingen;

    /**
     * Regel voor opgegooide excepties ten behoeve van de testen
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Setup voor iedere aanroep van een testmethode
     *
     * @throws MeldAppException
     */
    @Before
    public void setUp() throws MeldAppException {

        meldingRepository = new MeldingRepository();
        resultMeldingen = new ArrayList<>();
        nieuweMelding = new Melding("12345",
                "Dit is het te testen onderwerp voor meldingRepositoryTest", "Dit is de te testen inhoud voor meldingRepositoryTest");
        wijzigStatusMelding = new Melding("12345",
                "Gewijzigde statusmelding voor meldingRepositoryTest", "Dit is de te testen inhoud voor gewijzigde statusmelding voor meldingRepositoryTest");
        toewijzenMelding = new Melding("12345",
                "Dit is een toe te wijzen melding voor meldingRepositoryTest", "Dit is de te testen inhoud voor de toe te wijzen melding voor meldingRepositoryTest");
        losOpMelding = new Melding("12345",
                "Op te lossen melding voor meldingRepositoryTest", "Dit is de te op te lossen inhoud voor voor meldingRepositoryTest");

        meldingRepository.addListener(new FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<Melding>() {

            @Override
            public void onSuccess(List<Melding> result) {
                resultMeldingen = result;
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * Maakt geheugen vrij na het testen van een methode
     *
     */
    @After
    public void tearDown(){
        meldingRepository.removeListener();
        nieuweMelding = null;
        wijzigStatusMelding = null;
        toewijzenMelding = null;
        meldingTest = null;
    }

    /**
     * Test het toevoegen aan Firebase database
     */
    @Test
    public void voegMeldingToeAanDatabase() {
        Semaphore sync = new Semaphore(0);
        DatabaseReference.CompletionListener onCompletion = new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                sync.release();
            }
        };

        String meldingUid = meldingRepository.voegMeldingToeAanDatabase(nieuweMelding, onCompletion);

        // wait until melding is added
        sync.acquireUninterruptibly();

        for (Melding m : resultMeldingen) {
            if (m.getOnderwerp().equals("Dit is het te testen onderwerp voor meldingRepositoryTest")) {
                MeldingRepositoryTest.meldingTest = m;
                break;
            }
        }

        assertEquals("Dit is het te testen onderwerp voor meldingRepositoryTest", meldingTest.getOnderwerp());

        try {
            meldingRepository.verwijderMelding(meldingUid, onCompletion);
        } catch (MeldAppException e) {
            e.printStackTrace();
        }

        // wait until melding is deleted
        sync.acquireUninterruptibly();

    }

    /**
     * Test het sluiten van een melding in Firebase database
     * @throws MeldAppException
     */
    @Test
    public void sluitMelding() throws MeldAppException {

        Semaphore sync = new Semaphore(0);
        DatabaseReference.CompletionListener onCompletion = new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                sync.release();
            }
        };

        String meldingUid = meldingRepository.voegMeldingToeAanDatabase(nieuweMelding, onCompletion);

        sync.acquireUninterruptibly();

        for (Melding m : resultMeldingen) {
            if (m.getOnderwerp().equals("Dit is het te testen onderwerp voor meldingRepositoryTest")) {
                MeldingRepositoryTest.meldingTest = m;
                Log.d("meldingrepo", "de resultMelding is " + meldingTest.getStatus().toString());
                break;
            }
        }

        assertEquals("NIEUW", meldingTest.getStatus().toString());

        try {
            meldingRepository.sluitMelding(meldingUid, onCompletion);
        } catch (MeldAppException e) {
            e.printStackTrace();
        }

        sync.acquireUninterruptibly();

        for (Melding m : resultMeldingen) {
            if (m.getOnderwerp().equals("Dit is het te testen onderwerp")) {
                MeldingRepositoryTest.meldingTest = m;
                break;
            }
        }

        assertEquals("GESLOTEN", meldingTest.getStatus().toString());
        assertEquals(false, meldingTest.getNotificatieVerstuurd());

        try {
            meldingRepository.verwijderMelding(meldingUid, onCompletion);
        } catch (MeldAppException e) {
            e.printStackTrace();
        }

        sync.acquireUninterruptibly();

        thrown.expect(MeldAppException.class);
        thrown.expectMessage("Identificatie van de melding mag niet leeg zijn.");
        String nullMelding = null;
        meldingRepository.sluitMelding(nullMelding);

        thrown.expect(MeldAppException.class);
        thrown.expectMessage("Identificatie van de melding mag niet leeg zijn.");
        String legeMelding = "";
        meldingRepository.sluitMelding(legeMelding);

    }

    /**
     * Test het heropenen van een melding in Firebase database
     * @throws MeldAppException
     */
    @Test
    public void heropenMelding() throws MeldAppException {

        Semaphore sync = new Semaphore(0);
        DatabaseReference.CompletionListener onCompletion = new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                sync.release();
            }
        };

        String meldingUid = meldingRepository.voegMeldingToeAanDatabase(wijzigStatusMelding, onCompletion);

        sync.acquireUninterruptibly();

        for (Melding m : resultMeldingen) {
            if (m.getOnderwerp().equals("Gewijzigde statusmelding voor meldingRepositoryTest")) {
                MeldingRepositoryTest.meldingTest = m;
                break;
            }
        }

        assertEquals("NIEUW", meldingTest.getStatus().toString());

        try {
            meldingRepository.heropenMelding(meldingUid, "inhoud heropen melding voor meldingRepositoryTest", onCompletion);
        } catch (MeldAppException e) {
            e.printStackTrace();
        }

        sync.acquireUninterruptibly();

        for (Melding m : resultMeldingen) {
            if (m.getInhoud().equals("inhoud heropen melding voor meldingRepositoryTest")) {
                MeldingRepositoryTest.meldingTest = m;
                break;
            }
        }

        assertEquals("Gewijzigde statusmelding voor meldingRepositoryTest", meldingTest.getOnderwerp());
        assertEquals("HEROPEND", meldingTest.getStatus().toString());
        assertEquals("inhoud heropen melding voor meldingRepositoryTest", meldingTest.getInhoud());
        assertEquals(false, meldingTest.getNotificatieVerstuurd());

        try {
            meldingRepository.verwijderMelding(meldingUid, onCompletion);
        } catch (MeldAppException e) {
            e.printStackTrace();
        }

        sync.acquireUninterruptibly();

        String nullMelding = null;
        String nullInhoud = null;
        String uid = "123";
        String inhoud = "inhoud";
        String legeInhoud = "";
        String legeUid = "";

        thrown.expect(MeldAppException.class);
        thrown.expectMessage("Identificatie van de melding mag niet leeg zijn.");
        meldingRepository.heropenMelding(nullMelding, inhoud);

        thrown.expect(MeldAppException.class);
        thrown.expectMessage("Identificatie van de melding mag niet leeg zijn.");
        meldingRepository.heropenMelding(legeUid, inhoud);

        thrown.expect(MeldAppException.class);
        thrown.expectMessage("Inhoud van de melding mag niet leeg zijn.");
        meldingRepository.heropenMelding(uid, nullInhoud);

        thrown.expect(MeldAppException.class);
        thrown.expectMessage("Inhoud van de melding mag niet leeg zijn.");
        meldingRepository.heropenMelding(uid, legeInhoud);

    }

    /**
     * Test het oplossen van een melding in Firebase database
     * @throws MeldAppException
     */
    @Test
    public void opgelosteMelding() throws MeldAppException {

        Semaphore sync = new Semaphore(0);
        DatabaseReference.CompletionListener onCompletion = new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                sync.release();
            }
        };

        String meldingUid = meldingRepository.voegMeldingToeAanDatabase(losOpMelding, onCompletion);

        sync.acquireUninterruptibly();

        for (Melding m : resultMeldingen) {
            if (m.getOnderwerp().equals("Op te lossen melding voor meldingRepositoryTest")) {
                MeldingRepositoryTest.meldingTest = m;
                break;
            }
        }

        assertEquals("NIEUW", meldingTest.getStatus().toString());

        try {
            meldingRepository.opgelosteMelding(meldingUid, "nieuwe inhoud opgelost melding voor meldingRepositoryTest", onCompletion);
        } catch (MeldAppException e) {
            e.printStackTrace();
        }

        sync.acquireUninterruptibly();

        for (Melding m : resultMeldingen) {
            if (m.getInhoud().equals("nieuwe inhoud opgelost melding voor meldingRepositoryTest")) {
                MeldingRepositoryTest.meldingTest = m;
                break;
            }
        }

        assertEquals("Op te lossen melding voor meldingRepositoryTest", meldingTest.getOnderwerp());
        assertEquals("OPGELOST", meldingTest.getStatus().toString());
        assertEquals("nieuwe inhoud opgelost melding voor meldingRepositoryTest", meldingTest.getInhoud());
        assertEquals(false, meldingTest.getNotificatieVerstuurd());

        try {
            meldingRepository.verwijderMelding(meldingUid, onCompletion);
        } catch (MeldAppException e) {
            e.printStackTrace();
        }

        sync.acquireUninterruptibly();

        String nullMelding = null;
        String nullInhoud = null;
        String uid = "123";
        String inhoud = "inhoud";
        String legeInhoud = "";
        String legeUid = "";

        thrown.expect(MeldAppException.class);
        thrown.expectMessage("Identificatie van de melding mag niet leeg zijn.");
        meldingRepository.opgelosteMelding(nullMelding, inhoud);

        thrown.expect(MeldAppException.class);
        thrown.expectMessage("Identificatie van de melding mag niet leeg zijn.");
        meldingRepository.opgelosteMelding(legeUid, inhoud);

        thrown.expect(MeldAppException.class);
        thrown.expectMessage("Inhoud van de melding mag niet leeg zijn.");
        meldingRepository.opgelosteMelding(uid, nullInhoud);

        thrown.expect(MeldAppException.class);
        thrown.expectMessage("Inhoud van de melding mag niet leeg zijn.");
        meldingRepository.opgelosteMelding(uid, legeInhoud);

    }

    /**
     * Test het toewijzen van een melding in Firebase database
     * @throws MeldAppException
     */
    @Test
    public void toewijzenMelding() throws MeldAppException {

        Semaphore sync = new Semaphore(0);
        DatabaseReference.CompletionListener onCompletion = new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                sync.release();
            }
        };

        String meldingUid = meldingRepository.voegMeldingToeAanDatabase(toewijzenMelding, onCompletion);

        sync.acquireUninterruptibly();

        for (Melding m : resultMeldingen) {
            if (m.getOnderwerp().equals("Dit is een toe te wijzen melding voor meldingRepositoryTest")) {
                MeldingRepositoryTest.meldingTest = m;
                break;
            }
        }

        assertEquals(null, meldingTest.getUidGebruikerBehandelaar());

        meldingRepository.wijsBehandelaarToe(meldingUid, "777777", onCompletion);

        sync.acquireUninterruptibly();

        for (Melding m : resultMeldingen) {
            if (m.getOnderwerp().equals("Dit is een toe te wijzen melding voor meldingRepositoryTest")) {
                MeldingRepositoryTest.meldingTest = m;
                break;
            }
        }

        assertEquals("Dit is een toe te wijzen melding voor meldingRepositoryTest", meldingTest.getOnderwerp());
        assertEquals("777777", meldingTest.getUidGebruikerBehandelaar());
        assertEquals(false, meldingTest.getNotificatieVerstuurd());

        try {
            meldingRepository.verwijderMelding(meldingUid, onCompletion);
        } catch (MeldAppException e) {
            e.printStackTrace();
        }

        sync.acquireUninterruptibly();

        String nullMelding = null;
        String nullBehandelaar = null;
        String uid = "123";
        String legeUid = "";
        String behandelaar = "behandelaar";
        String legeBehandelaar = "";

        thrown.expect(MeldAppException.class);
        thrown.expectMessage("Identificatie van de behandelaar mag niet leeg zijn.");
        meldingRepository.toewijzenMelding(uid, nullBehandelaar);

        thrown.expect(MeldAppException.class);
        thrown.expectMessage("Identificatie van de behandelaar mag niet leeg zijn.");
        meldingRepository.toewijzenMelding(uid, legeBehandelaar);

        thrown.expect(MeldAppException.class);
        thrown.expectMessage("Identificatie van de melding mag niet leeg zijn.");
        meldingRepository.toewijzenMelding(nullMelding, behandelaar);

        thrown.expect(MeldAppException.class);
        thrown.expectMessage("Identificatie van de melding mag niet leeg zijn.");
        meldingRepository.toewijzenMelding(legeUid, behandelaar);
    }

    /**
     * test het toewijzen van een behandelaar in Firebase database
     */
    @Test
    public void wijsBehandelaarToe() {

        Semaphore sync = new Semaphore(0);
        DatabaseReference.CompletionListener onCompletion = new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                sync.release();
            }
        };

        String meldingUid = meldingRepository.voegMeldingToeAanDatabase(toewijzenMelding, onCompletion);

        sync.acquireUninterruptibly();

        for (Melding m : resultMeldingen) {
            if (m.getOnderwerp().equals("Dit is een toe te wijzen melding voor meldingRepositoryTest")) {
                MeldingRepositoryTest.meldingTest = m;
                Log.d("meldingrepo", "de resultMelding is " + meldingTest.getStatus().toString());
                break;
            }
        }

        assertEquals(null, meldingTest.getUidGebruikerBehandelaar());

        meldingRepository.wijsBehandelaarToe(meldingUid, "555", onCompletion);

        sync.acquireUninterruptibly();

        for (Melding m : resultMeldingen) {
            if (m.getOnderwerp().equals("Dit is een toe te wijzen melding voor meldingRepositoryTest")) {
                MeldingRepositoryTest.meldingTest = m;
                break;
            }
        }

        assertEquals("Dit is een toe te wijzen melding voor meldingRepositoryTest", meldingTest.getOnderwerp());
        assertEquals("555", meldingTest.getUidGebruikerBehandelaar());

        try {
            meldingRepository.verwijderMelding(meldingUid, onCompletion);
        } catch (MeldAppException e) {
            e.printStackTrace();
        }

        sync.acquireUninterruptibly();
    }

    /**
     * Test het verwijderen van een melding in Firebase database
     */
    @Test
    public void verwijderMelding() {

        Semaphore sync = new Semaphore(0);
        DatabaseReference.CompletionListener onCompletion = new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                sync.release();
            }
        };

        String meldingUid = meldingRepository.voegMeldingToeAanDatabase(nieuweMelding, onCompletion);

        sync.acquireUninterruptibly();

        for (Melding m : resultMeldingen) {
            if (m.getOnderwerp().equals("Dit is het te testen onderwerp voor meldingRepositoryTest")) {
                MeldingRepositoryTest.meldingTest = m;
                break;
            }
        }

        assertEquals("Dit is het te testen onderwerp voor meldingRepositoryTest", meldingTest.getOnderwerp());

        try {
            meldingRepository.verwijderMelding(meldingUid, onCompletion);
        } catch (MeldAppException e) {
            e.printStackTrace();
        }

        sync.acquireUninterruptibly();

        for (Melding m : resultMeldingen) {
            if (m.getOnderwerp().equals("Dit is het te testen onderwerp voor meldingRepositoryTest")) {
                MeldingRepositoryTest.meldingTest = m;
                break;
            } else {
                meldingTest = null;
            }
        }

        assertEquals(null, meldingTest);

    }
}