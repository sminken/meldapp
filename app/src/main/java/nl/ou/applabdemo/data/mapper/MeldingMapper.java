package nl.ou.applabdemo.data.mapper;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import nl.ou.applabdemo.data.entity.MeldingEntity;
import nl.ou.applabdemo.domain.Melding;
import nl.ou.applabdemo.domain.Status;
import nl.ou.applabdemo.util.MeldAppException;

/**
 * Deze subklasse van FirebaseMapper is verantwoordelijk voor het beheren van instanties van MeldingenMapper
 */
public class MeldingMapper extends FirebaseMapper<MeldingEntity, Melding> {

    public static final String TAG = "Meldingmapper ";

    /**
     * Deze methode zet (mapt) de database entities voor Meldingen om in domein entities voor Meldingen
     *
     * @param dataSnapshot : de database snapshot met entities die moeten worden omgezet
     * @return : Een melding of null als één van de attributen in de database een ongeldige waarde bevat.
     */
    @Override
    public Melding map(DataSnapshot dataSnapshot)  {

        MeldingEntity meldingEntity = dataSnapshot.getValue(MeldingEntity.class);

        try {
            String uidMelding = dataSnapshot.getKey();
            return new Melding(
                    uidMelding,
                    meldingEntity.getUidGebruikerMelder(),
                    meldingEntity.getUidGebruikerBehandelaar(),
                    meldingEntity.getOnderwerp(),
                    meldingEntity.getInhoud(),
                    meldingEntity.getDatumTijd(),
                    meldingEntity.getStatus(),
                    meldingEntity.getNotificatieVerstuurd());
        }
        catch (MeldAppException e) {
            // Eén van de attributen van melding in de database heeft een ongeldige waarde.
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Met deze klasse methode kan een domain instantie van melding worden omgezet (demap) naar een database entity melding
     *
     * @param melding - instantie van melding die moet worden omgezet
     * @return - instantie van Melding entity
     */
    public static MeldingEntity demap(Melding melding) {
        Log.d(TAG, "demap aangeroepen");

        MeldingEntity meldingEntity = new MeldingEntity(
                melding.getUidGebruikerMelder(),
                melding.getUidGebruikerBehandelaar(),
                melding.getOnderwerp(),
                melding.getInhoud(),
                melding.getDatumTijd(),
                melding.getStatus().toString(),
                melding.getNotificatieVerstuurd());
        Log.d(TAG, "MeldingEntity aangemaakt");

        return meldingEntity;
    }
}
