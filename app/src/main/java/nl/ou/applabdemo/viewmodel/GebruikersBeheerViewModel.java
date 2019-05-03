package nl.ou.applabdemo.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import nl.ou.applabdemo.data.repository.FirebaseDatabaseRepository;
import nl.ou.applabdemo.data.repository.GebruikerRepository;
import nl.ou.applabdemo.domain.Gebruiker;
import nl.ou.applabdemo.domain.GebruikerInfo;
import nl.ou.applabdemo.util.MeldAppException;

/**
 * Deze klasse beheert de instantie van een viewmodel voor gebruikers
 * Voor meer info zie: https://developer.android.com/reference/android/arch/lifecycle/ViewModel
 */

@SuppressWarnings("SpellCheckingInspection")
public class GebruikersBeheerViewModel extends ViewModel{
    // logging
    private static final String TAG = "GebBeheerViewModel";

    /*
    Livedata klasse voor gebruikers aanmaken
     */
    private MutableLiveData<List<Gebruiker>> liveDataGebruiker;

    /*
    Maak repo aan voor gebruikers
     */
    private final GebruikerRepository gebruikerRepository = new GebruikerRepository();

    public LiveData<List<Gebruiker>> haalGebruikers() {
        if (liveDataGebruiker == null) {
            liveDataGebruiker = new MutableLiveData<>();
            laadUsers();
        }
        return liveDataGebruiker;
    }

    @Override
    /*
    When the owner activity is finished, the framework calls the ViewModel objects's onCleared() method so that it can clean up resources.
     */
    protected void onCleared() {
        gebruikerRepository.removeListener();
    }

    private void laadUsers() {
        /*
        Listener toevoegen aan repo voor users
         */
        gebruikerRepository.addListener(new FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<Gebruiker>() {
            @Override
            public void onSuccess(List<Gebruiker> result) {
                Log.v(TAG, "Laad Users - on Success");

                liveDataGebruiker.setValue(result);
            }

            @Override
            public void onError(Exception e) {
                liveDataGebruiker.setValue(null);
            }
        });
    }


    /**
     * Deze methode voegt een gebruiker toe aan de database
     * @param gebruikerInfo - instantie waarvoor een gebruiker moet worden toegevoegd
     * @throws MeldAppException
     */
    public void voegUserToe(GebruikerInfo gebruikerInfo) throws MeldAppException {
        gebruikerRepository.voegGebruikerToeAanDatabase(gebruikerInfo);

    }

    /**
     * Deze methode wijzigt de notification van een gegeven gebruiker
     * @param uid - uid van de gegeven gebruiker
     * @param notificatie - nieuwe waarde notification
     * @throws MeldAppException - als uid niet is gevuld of gebruiker met uid komt niet voor
     */
    public void wijzigNotificatieGebruiker( String uid, boolean notificatie) throws MeldAppException {

        if ( uid == null) {
            throw new MeldAppException("Uid  gebruiker is niet gevuld");
        }
        gebruikerRepository.wijzigNotificatieGebruiker (uid, notificatie);
    }

    /**
     *  Deze methode wijzigt de deviceToken van een gegeven gebruiker
     * @param uid - uid van de gegeven gebruiker
     * @param deviceToken - nieuwe waarde notification
     * @throws MeldAppException - als uid niet is gevuld of gebruiker met uid komt niet voor
     */

    public void updateDeviceToken( String uid, String deviceToken) throws MeldAppException {

        if ( uid == null) {
            throw new MeldAppException("Uid  gebruiker is niet gevuld");
        }
        gebruikerRepository.updateDeviceTokenGebruiker (uid, deviceToken);
    }
}
