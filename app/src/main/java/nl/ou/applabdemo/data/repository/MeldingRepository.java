package nl.ou.applabdemo.data.repository;

import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import nl.ou.applabdemo.data.entity.MeldingEntity;
import nl.ou.applabdemo.data.mapper.MeldingMapper;
import nl.ou.applabdemo.domain.Melding;
import nl.ou.applabdemo.domain.Status;
import nl.ou.applabdemo.util.MeldAppException;

/**
 * Klasse verantwoordelijk voor de interactie met Firebase
 *
 */
public class MeldingRepository extends FirebaseDatabaseRepository<Melding> {

    /**
     * Constructor waarbij de mapper voor Meldingen wordt aangemaakt en vastgelegd
     */
    public MeldingRepository() {
        super(new MeldingMapper());
    }

    /**
     * Methode voor het toevoegen van een instantie van Melding aan de database
     *
     * @param melding      melding melding die aan de database moet worden toegevoegd
     * @param onCompletion luisteraar op de firebase database
     * @return String de nieuwe Firebase Uid van de melding
     */
    public String voegMeldingToeAanDatabase(Melding melding, @Nullable DatabaseReference.CompletionListener onCompletion) {
        MeldingEntity meldingEntity = MeldingMapper.demap(melding);
        String meldingUid = databaseReference.push().getKey();
        assert meldingUid != null;
        databaseReference.child(meldingUid).setValue(meldingEntity, onCompletion);

        return meldingUid;
    }

    /**
     * Methode voor het toevoegen van een instantie van Melding aan de database
     *
     * @param melding melding die aan de database moet worden toegevoegd
     * @return String de nieuwe Firebase Uid van de melding
     */
    public String voegMeldingToeAanDatabase(Melding melding) {
        return voegMeldingToeAanDatabase(melding, null);
    }

    /**
     * Sluit een melding en registeert dit in de database
     *
     * @param uidMelding   De uid van de melding die wordt gesloten.
     * @param onCompletion luisteraar op de firebase database
     * @throws MeldAppException
     */
    public void sluitMelding(String uidMelding, @Nullable DatabaseReference.CompletionListener onCompletion) throws MeldAppException {

        // Is uid van de melding gevuld?
        if (uidMelding == null || uidMelding.isEmpty()) {
            throw new MeldAppException("Identificatie van de melding mag niet leeg zijn.");
        }

        DatabaseReference dbRef = databaseReference.child(uidMelding);

        // Wijzig de status van de melding naar Gesloten.
        Map<String, Object> statusUpdate = new HashMap<>();
        statusUpdate.put("status", Status.GESLOTEN.toString());
        dbRef.updateChildren(statusUpdate);

        // Wijzig de indicator voor het versturen van een notificatie voor
        // deze melding.
        Map<String, Object> notificatieVerstuurdUpdate = new HashMap<>();
        notificatieVerstuurdUpdate.put("notificatieVerstuurd", false);
        dbRef.updateChildren(notificatieVerstuurdUpdate, onCompletion);
    }

    /**
     * Sluit een melding en registreert dit in de database
     *
     * @param uidMelding De uid van de melding die wordt gesloten.
     */
    public void sluitMelding(String uidMelding) throws MeldAppException {
        sluitMelding(uidMelding, null);
    }

    /**
     * Verwijdert de melding uit de database
     *
     * @param uidMelding   de uid van de melding
     * @param onCompletion luisteraar op de firebase database
     */
    public void verwijderMelding(String uidMelding, @Nullable DatabaseReference.CompletionListener onCompletion) throws MeldAppException {
        // Is uid van de melding gevuld?
        if (uidMelding == null || uidMelding.isEmpty()) {
            // make sure on complete is always called
            if (onCompletion != null)
                onCompletion.onComplete(null, null);
            throw new MeldAppException("Identificatie van de melding mag niet leeg zijn.");
        }

        DatabaseReference dbRef = databaseReference.child(uidMelding);
        dbRef.removeValue(onCompletion);
    }

    /**
     * Verwijdert de melding uit de database
     *
     * @param uidMelding de uid van de melding
     */
    public void verwijderMelding(String uidMelding) throws MeldAppException {
        verwijderMelding(uidMelding, null);
    }

    /**
     * Heropent een melding, waarbij de inhoud van de melding kan worden aangevuld met aanvullende
     * informatie
     *
     * @param uidMelding   De uid van de melding die wordt heropent.
     * @param inhoud       De nieuwe/aangevulde inhoud van de melding.
     * @param onCompletion luisteraar op de firebase database
     */
    public void heropenMelding(String uidMelding, String inhoud, @Nullable DatabaseReference.CompletionListener onCompletion) throws MeldAppException {
        wijzigStatusEnInhoudMelding(uidMelding, Status.HEROPEND, inhoud, onCompletion);
    }

    /**
     * Heropent een melding, waarbij de inhoud van de melding kan worden aangevuld met aanvullende
     * informatie
     *
     * @param uidMelding De uid van de melding die wordt heropent.
     * @param inhoud     De nieuwe/aangevulde inhoud van de melding.
     */
    public void heropenMelding(String uidMelding, String inhoud) throws MeldAppException {
        heropenMelding(uidMelding, inhoud, null);
    }

    /**
     * Een opgeloste melding, waarbij de inhoud van de melding kan worden aangevuld met aanvullende
     * informatie.
     *
     * @param uidMelding   De uid van de melding die is opgelost.
     * @param inhoud       De nieuwe/aangevulde inhoud van de melding.
     * @param onCompletion luisteraar op de firebase database
     */
    public void opgelosteMelding(String uidMelding, String inhoud, @Nullable DatabaseReference.CompletionListener onCompletion) throws MeldAppException {
        wijzigStatusEnInhoudMelding(uidMelding, Status.OPGELOST, inhoud, onCompletion);
    }

    /**
     * Een opgeloste melding, waarbij de inhoud van de melding kan worden aangevuld met aanvullende
     * informatie.
     *
     * @param uidMelding De uid van de melding die is opgelost.
     * @param inhoud     De nieuwe/aangevulde inhoud van de melding.
     */
    public void opgelosteMelding(String uidMelding, String inhoud) throws MeldAppException {
        opgelosteMelding(uidMelding, inhoud, null);
    }

    /**
     * Toewijzen melding, waarbij de uid van de behandelaar in de melding en de status worden gewijzigd.
     *
     * @param uidMelding     De uid van de melding die wordt toegewezen.
     * @param uidBehandelaar De uid van de behandelaar die de melding krijgt toegewezen.
     * @param onCompletion   luisteraar op de firebase database
     */
    public void toewijzenMelding(String uidMelding, String uidBehandelaar, @Nullable DatabaseReference.CompletionListener onCompletion) throws MeldAppException {

        // Is de uid van de melding gevuld?
        if (uidMelding == null || uidMelding.isEmpty()) {
            throw new MeldAppException("Identificatie van de melding mag niet leeg zijn.");
        }

        // Is de uid van de behandelaar gevuld?
        if (uidBehandelaar == null || uidBehandelaar.isEmpty()) {
            throw new MeldAppException("Identificatie van de behandelaar mag niet leeg zijn.");
        }

        DatabaseReference dbRef = databaseReference.child(uidMelding);

        // Wijzig de uid van de behandelaar van de melding.
        Map<String, Object> uidBehandelaarUpdate = new HashMap<>();
        uidBehandelaarUpdate.put("uidGebruikerBehandelaar", uidBehandelaar);
        dbRef.updateChildren(uidBehandelaarUpdate);

        // Wijzig de status van de melding naar Gesloten.
        Map<String, Object> statusUpdate = new HashMap<>();
        statusUpdate.put("status", Status.INBEHANDELING.toString());
        dbRef.updateChildren(statusUpdate);

        // Wijzig de indicator voor het versturen van een notificatie voor
        // deze melding.
        Map<String, Object> notificatieVerstuurdUpdate = new HashMap<>();
        notificatieVerstuurdUpdate.put("notificatieVerstuurd", false);
        dbRef.updateChildren(notificatieVerstuurdUpdate, onCompletion);
    }

    /**
     * Toewijzen melding, waarbij de uid van de behandelaar in de melding en de status worden gewijzigd.
     *
     * @param uidMelding     De uid van de melding die wordt toegewezen.
     * @param uidBehandelaar De uid van de behandelaar die de melding krijgt toegewezen.
     */
    public void toewijzenMelding(String uidMelding, String uidBehandelaar) throws MeldAppException {
        toewijzenMelding(uidMelding, uidBehandelaar, null);
    }


    /**
     * Wijzigt de status en de inhoud van de gegeven melding.
     * informatie
     *
     * @param uidMelding   De uid van de melding die wijzigt.
     * @param status       De nieuwe status van de melding.
     * @param inhoud       De nieuwe/aangevulde inhoud van de melding.
     * @param onCompletion luisteraar op de firebase database
     */
    private void wijzigStatusEnInhoudMelding(String uidMelding, Status status, String inhoud, @Nullable DatabaseReference.CompletionListener onCompletion) throws MeldAppException {

        // Is de uid van de melding gevuld?
        if (uidMelding == null || uidMelding.isEmpty()) {
            throw new MeldAppException("Identificatie van de melding mag niet leeg zijn.");
        }

        // Is de status van de melding gevuld?
        if (!isGeldigeStatus(status)) {
            throw new MeldAppException("Status " + status + " van de melding is onjuist.");
        }

        // Is de inhoud van de melding gevuld?
        if (inhoud == null || inhoud.isEmpty()) {
            throw new MeldAppException("Inhoud van de melding mag niet leeg zijn.");
        }

        DatabaseReference dbRef = databaseReference.child(uidMelding);

        // Wijzig de inhoud van de melding.
        Map<String, Object> inhoudUpdate = new HashMap<>();
        inhoudUpdate.put("inhoud", inhoud);
        dbRef.updateChildren(inhoudUpdate);

        // Wijzig de status van de melding.
        Map<String, Object> statusUpdate = new HashMap<>();
        statusUpdate.put("status", status.toString());
        dbRef.updateChildren(statusUpdate);

        // Wijzig de indicator voor het versturen van een notificatie voor
        // deze melding.
        Map<String, Object> notificatieVerstuurdUpdate = new HashMap<>();
        notificatieVerstuurdUpdate.put("notificatieVerstuurd", false);
        dbRef.updateChildren(notificatieVerstuurdUpdate, onCompletion);
    }

    /**
     * Wijzigt de status en de inhoud van de gegeven melding.
     * informatie
     *
     * @param uidMelding De uid van de melding die wijzigt.
     * @param status     De nieuwe status van de melding.
     * @param inhoud     De nieuwe/aangevulde inhoud van de melding.
     */
    private void wijzigStatusEnInhoudMelding(String uidMelding, Status status, String inhoud) throws MeldAppException {
        wijzigStatusEnInhoudMelding(uidMelding, status, inhoud, null);
    }

    /**
     * Controleert of de gegeven status geldig is.
     *
     * @param status De status die op geldigheid moet worden gecontroleerd.
     * @return true als status geldig is. Anders false.
     */
    private boolean isGeldigeStatus(Status status) {

        boolean geldigeStatus = false;

        for (Status oneStatus : Status.values()) {
            if (oneStatus.equals(status)) {
                geldigeStatus = true;
                break;
            }
        }

        return geldigeStatus;
    }


    /**
     * Methode om een behandelaar aan de melding toe te wijzen
     *
     * @param meldingUid     de melding
     * @param uidBehandelaar de uid van de behandelaar
     * @param onCompletion   luisteraar op de firebase database
     */
    public void wijsBehandelaarToe(String meldingUid, String uidBehandelaar, @Nullable DatabaseReference.CompletionListener onCompletion) {
        DatabaseReference behandelaarMeldingRef = databaseReference.child(meldingUid);
        Map<String, Object> behandelaarUpdates = new HashMap<>();
        behandelaarUpdates.put("uidGebruikerBehandelaar", uidBehandelaar);

        behandelaarMeldingRef.updateChildren(behandelaarUpdates, onCompletion);
    }

    /**
     * Methode om een behandelaar aan de melding toe te wijzen
     *
     * @param meldingUid     de melding
     * @param uidBehandelaar de uid van de behandelaar
     */
    public void wijsBehandelaarToe(String meldingUid, String uidBehandelaar) {
        wijsBehandelaarToe(meldingUid, uidBehandelaar, null);
    }

    /**
     * Definitie van de naam van de collectie voor Meldingen in de database
     *
     * @return - de naam van die collectie
     */
    @Override
    protected String getRootNode() {
        return "meldingen";
    }
}
