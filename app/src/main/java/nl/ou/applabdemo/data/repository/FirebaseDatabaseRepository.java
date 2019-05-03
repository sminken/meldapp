package nl.ou.applabdemo.data.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import nl.ou.applabdemo.data.mapper.FirebaseMapper;

/**
 * Deze generieke klasse beheert repositories van het type Model
 * @param <Model> - Type parameter voor de domein klasse instantie
 */
public abstract class FirebaseDatabaseRepository<Model> {

    // Referentie naar de firebase database
    protected DatabaseReference databaseReference;

    /* Referentie naar de instantie callback methode om hiermee wijzigingen
       op de firebase datbase te verwerken in de betreffende ViewModel
     */
    protected FirebaseDatabaseRepositoryCallback<Model> firebaseCallback;

    /* Referentie naar de listener op de firebase database om op de hoogte
       te worden gebracht van wijzigingen op de database     */
    private BaseValueEventListener listener;

    // Referentie naar mapper om database documenten te kunnen omzetten naar domain instanties
    private FirebaseMapper mapper;

    // Referentie naar naam van collectie in database
    protected abstract String getRootNode();

    /**
     * Constructor waarin database referentie en mapper worden vastgelegd
     * @param mapper : mapper om database te kunnen omzetten naar domain instanties
     */
    public FirebaseDatabaseRepository(FirebaseMapper mapper) {
        databaseReference = FirebaseDatabase.getInstance().getReference(getRootNode());
        this.mapper = mapper;
    }

    /**
     * Methode waarmee listener op de Firebase database wordt aangemaakt en geregistreerd
     * @param firebaseCallback - methode die door de listener moet worden gebruikt igv wijzigingen op database
     */

    public void addListener(FirebaseDatabaseRepositoryCallback<Model> firebaseCallback) {
        this.firebaseCallback = firebaseCallback;
        listener = new BaseValueEventListener(mapper, firebaseCallback);
        databaseReference.addValueEventListener(listener);
    }

    /**
     * Methode om de listener op de database te verwijderen
     */
    public void removeListener() {
        databaseReference.removeEventListener(listener);
    }

    /**
     * Interface die de twee methoden voorschrijft die voor de callback methode moeten worden geimplementeerd
     * @param <T> - type parameter voor domain klasse
     */

    public interface FirebaseDatabaseRepositoryCallback<T> {
        void onSuccess(List<T> result);

        void onError(Exception e);
    }
}
