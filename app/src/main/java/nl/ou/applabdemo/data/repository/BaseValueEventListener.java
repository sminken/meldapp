package nl.ou.applabdemo.data.repository;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import nl.ou.applabdemo.data.mapper.FirebaseMapper;
import nl.ou.applabdemo.util.MeldAppException;

/**
 * Deze klasse is verantwoordelijk voor het afhandelen van wijzigingen op de firebase database
 * @param <Model> - domein klasse die in de database wordt vastgelegd
 * @param <Entity> - entity (database) klasse zoals deze wordt opgeslagen in de database
 *
 */
public class BaseValueEventListener<Model, Entity> implements ValueEventListener {

    /* Referentie naar de mapper waarnee instantie van de domein klasse worden omgezet naar
       instanties van de (database) entity klassen
     */
    private FirebaseMapper<Entity, Model> mapper;

    // Referentie naar de call back methode voor de event afhandeling
    private FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback callback;

    public BaseValueEventListener(FirebaseMapper<Entity, Model> mapper,
                                  FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<Model> callback) {
        this.mapper = mapper;
        this.callback = callback;
    }

    /**
     * Overwrite van de onDataChange methode die wordt uitgevoerd na het wijzigen van de database in Firebase
     * @param dataSnapshot - snapshot van de database na wijziging
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        // TODO Nog nagaan hoe dit netter kan
        try {
            List<Model> data = mapper.mapList(dataSnapshot);
            callback.onSuccess(data);
        }
        catch (MeldAppException exception) {}
    }

    /**
     * Overwrite van de onCancelled methode die wordt uitgevoerd na het mislukken van de database wijziging
     * @param databaseError - bevat de specificatie van de foutsituatie. <br>
     *                        Zie https://firebase.google.com/docs/reference/android/com/google/firebase/database/DatabaseError
     *
     */
    @Override
    public void onCancelled(DatabaseError databaseError) {
        callback.onError(databaseError.toException());
    }
}
