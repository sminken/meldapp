package nl.ou.applabdemo.data.mapper;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;

import nl.ou.applabdemo.data.entity.GebruikerEntity;
import nl.ou.applabdemo.domain.Gebruiker;
import nl.ou.applabdemo.domain.GebruikerStatus;
import nl.ou.applabdemo.domain.Rol;
import nl.ou.applabdemo.util.MeldAppException;

/**
 * Deze sub klasse is verantwoordelijk voor het beheren van instanties van GebruikerMapper
 *
 */

@SuppressWarnings("SpellCheckingInspection")
public class GebruikerMapper extends FirebaseMapper<GebruikerEntity, Gebruiker> {

    /*
    Tbv logging
     */
    private static final String TAG = "GebruikerMapper";

    /**
     * Deze methode zet (mapt) de database entities voor Gebruiker om
     *         in domain entities voor Gebruiker
     *
     * @param dataSnapshot : de database snapshot met entities die moeten worden omgezet
     * @return : een map met domain instanties
     */
    @Override
    public Gebruiker map(DataSnapshot dataSnapshot) throws MeldAppException {
        Log.v(TAG, "methode map aangeroepen");

        /*
        Maak entity instantie obv inhoud snapshot
         */
        if (dataSnapshot == null) {
            return null;
        }
        GebruikerEntity gebruikerEntity = dataSnapshot.getValue(GebruikerEntity.class);

        /*
         Validatie van waarden uit database
        */

        /*
        Uid mag niet null of leeg zijn
         */
        if (gebruikerEntity == null) {
            return null;
        }

        String gebruikerEntityUid = gebruikerEntity.getUid();
        String gebruikerEntityOrganisatie = gebruikerEntity.getOrganisatie();

        /*
          Gebruikers Status moet geldige waarde hebben
         */
        String gebruikerEntityStatus = gebruikerEntity.getStatus();
        GebruikerStatus gebruikerStatus = null;

        for (GebruikerStatus status : GebruikerStatus.values() ) {
            if ( status.toString().equals(gebruikerEntityStatus)) {
                gebruikerStatus = status;
                break;
            }
        }

        if ( gebruikerStatus == null) {
            throw new MeldAppException("Map van GebruikerUid: " + gebruikerEntityUid +
                    " Ongeldige waarde status: " + gebruikerEntityStatus);
        }

        /*
          Gebruikers Rol moet geldige waarde hebben
         */
        String gebruikerEntityRol = gebruikerEntity.getRol();
        Rol gebruikerRol = null;

        for (Rol rol : Rol.values() ) {
            if ( rol.toString().equals(gebruikerEntityRol)) {
                gebruikerRol = rol;
            }
        }

        if ( gebruikerRol == null) {
            throw new MeldAppException("Map van GebruikerUid: " + gebruikerEntityUid +
                    " Ongeldige waarde rol: " + gebruikerEntityRol);
        }


        // Geef nieuwe instantie
        return new Gebruiker(  gebruikerEntity.getVoornaam(), gebruikerEntity.getAchternaam(),
                               gebruikerEntityOrganisatie,
                               gebruikerEntityUid, gebruikerStatus,
                               gebruikerEntity.getTelefoonnummer(), gebruikerRol,
                               gebruikerEntity.getNotification(), gebruikerEntity.getDeviceToken());

    }

    /**
     * Met deze klasse methode kan een domain instantie van gebruiker worden omgezet (demap)
     * naar een database entity gebruiker
     *
     * @param gebruiker - instantie van gebruiker die moet worden omgezet
     * @return - instantie van GebruikerEntity
     */


    public static GebruikerEntity demap(Gebruiker gebruiker) {

        if (gebruiker == null) {
            return null;
        }

        return new GebruikerEntity(
                gebruiker.getVoornaam(), gebruiker.getAchternaam(), gebruiker.getOrganisatie(),
                gebruiker.getUid(), gebruiker.getGebruikerStatus().toString(),
                gebruiker.getRol().toString(), gebruiker.getTelefoonnummer(),
                gebruiker.getNotification(), gebruiker.getDeviceToken()
                );
    }

}
