package nl.ou.applabdemo.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import nl.ou.applabdemo.data.repository.FirebaseDatabaseRepository;
import nl.ou.applabdemo.data.repository.MeldingRepository;
import nl.ou.applabdemo.domain.Melding;
import nl.ou.applabdemo.util.MeldAppException;

/**
 * Deze klasse beheert de instantie van een viewmodel voor meldingen.
 */
@SuppressWarnings("SpellCheckingInspection")
public class MeldingBeheerViewModel extends ViewModel{

    // Livedata voor meldingen.
    private MutableLiveData<List<Melding>> liveDataMeldingen;

    // Repository voor meldingen.
    private final MeldingRepository meldingRepository = new MeldingRepository();

    public LiveData<List<Melding>> haalMeldingen() {
        if (liveDataMeldingen == null) {
            liveDataMeldingen = new MutableLiveData<>();
            laadMeldingen();
        }
        return liveDataMeldingen;
    }

    @Override
    // Bij het beëindigen van de activity wordt de onCleared voor het opschonen van resources aangeroepen.
    protected void onCleared() {
        meldingRepository.removeListener();
    }

    private void laadMeldingen() {
        // Listener voor meldingen aan repository koppelen.
        meldingRepository.addListener(new FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<Melding>() {
            @Override
            public void onSuccess(List<Melding> result) {
                 liveDataMeldingen.setValue(result);
            }

            @Override
            public void onError(Exception e) {
                liveDataMeldingen.setValue(null);
            }
        });
    }

    /**
     * Voegt een nieuwe melding voor de gegeven melder toe.
     *
     * @param uidMelder De uid van de melder.
     * @param onderwerp Onderwerp van de melding.
     * @param inhoud    Inhoud van de melding.
     */
    public void voegNieuweMeldingToe(String uidMelder, String onderwerp, String inhoud) throws MeldAppException {

        Melding melding = new Melding(uidMelder, onderwerp, inhoud);
        meldingRepository.voegMeldingToeAanDatabase(melding);
    }

    /**
     * Sluit een melding met de opgegeven melding identificatie.
     *
     * @param uidMelding De uid van de melding.
     */
    public void sluitMelding(String uidMelding) throws MeldAppException {

        meldingRepository.sluitMelding(uidMelding);
    }

    /**
     * Heropent een melding met de opgegeven melding identificatie en wijzigt de inhoud.
     *
     * @param uidMelding de uid van de melding
     * @param inhoud de (nieuwe) inhoud van de melding
     */
    public void heropenMelding(String uidMelding, String inhoud) throws MeldAppException {

        meldingRepository.heropenMelding(uidMelding, inhoud);
    }

    /**
     * Wijst de melding aan de gegeven behandelaar toe.
     *
     * @param uidMelding de uid van de melding
     * @param uidBehandelaar de uid van de behandelaar waaraan de melding wordt toegewezen.
     */
    public void toewijzenMelding(String uidMelding, String uidBehandelaar) throws MeldAppException {

        meldingRepository.toewijzenMelding(uidMelding, uidBehandelaar);
    }

    /**
     * Registreert opgelost bij de melding en wijzigt de inhoud.
     * Registreert een melding met de opgegeven melding identificatie als opgelost en wijzigt de inhoud.
     *
     * @param uidMelding de uid van de melding
     * @param inhoud de (nieuwe) inhoud van de melding
     */
    public void opgelosteMelding(String uidMelding, String inhoud) throws MeldAppException {

        meldingRepository.opgelosteMelding(uidMelding, inhoud);
    }

}
