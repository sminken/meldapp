package nl.ou.applabdemo.data.mapper;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import nl.ou.applabdemo.util.MeldAppException;

/**
 * Deze generieke klasse is verantwoordelijk voor het (de)mappen van database entities op domain entity <br>
 *     volgens Data Mapper Pattern
 * @param <Entity> : de  database entity klasse
 * @param <Model> : de domein klasse
 *
 */
public abstract class FirebaseMapper<Entity, Model> {

    /**
     * Methode signatuur voor het mappen van database entities op domain instanties
     * @param dataSnapshot  de database snapshot met entities die moeten worden omgezet
     * @return : een map met domain instanties
     */
    public abstract Model map (DataSnapshot dataSnapshot) throws MeldAppException;

    /**
     * Deze methode zet de documenten uit de collectie uit de database snapshot <br>
     *     om in een lijst van domain instanties. <br>
     *     Hierbij stelt ieder document een database entity voor.
     * @param dataSnapshot - snapshot met de collectie van documenten
     * @return : de lijst met domain instanties
     */
    public List<Model> mapList(DataSnapshot dataSnapshot) throws MeldAppException {
        List<Model> list = new ArrayList<>();
        for (DataSnapshot item : dataSnapshot.getChildren()) {
            if (map(item) != null) {
                list.add(map(item));
            }
        }
        return list;
    }

}