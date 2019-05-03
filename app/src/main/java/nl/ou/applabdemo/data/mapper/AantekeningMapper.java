package nl.ou.applabdemo.data.mapper;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import nl.ou.applabdemo.data.entity.AantekeningEntity;
import nl.ou.applabdemo.domain.Aantekening;

/**
 * Kasse voor het mappen van een aantekening van domein naar entiteit en visa versa.
 *
 */
public class AantekeningMapper extends FirebaseMapper<AantekeningEntity, Aantekening> {

    /**
     * Deze methode zet (mapt) de database entities voor Meldingen om in domain entities voor Meldingen
     *
     * @param dataSnapshot : de database snapshot met entities die moeten worden omgezet
     * @return : een map met domain instanties
     */
    @Override
    public Aantekening map(DataSnapshot dataSnapshot) {
        AantekeningEntity aantekeningEntity = dataSnapshot.getValue(AantekeningEntity.class);
        String aantekeningId = aantekeningEntity.getAantekeningId();
        String meldingId = aantekeningEntity.getMeldingId();
        String datumTijd = aantekeningEntity.getDatumTijd();
        String tekst = aantekeningEntity.getTekst();
        Aantekening aantekening = new Aantekening(aantekeningId,meldingId,datumTijd,tekst);
        Log.d("Aantekeningmapper: ", "Aantekening " + aantekening.getAantekeningId() + " voor " + aantekening.getMeldingId() +  " mapt");
        return aantekening;
    }

    /**
     * Met deze klasse methode kan een domain instantie van melding worden omgezet (demap) naar een database entity melding
     *
     * @param aantekening - instantie van melding die moet worden omgezet
     * @return - instantie van aantekening entity
     */
    public static AantekeningEntity demap(Aantekening aantekening) {

        AantekeningEntity aantekeningEntity = new AantekeningEntity(
                aantekening.getAantekeningId(),
                aantekening.getMeldingId(),
                aantekening.getDatumTijd(),
                aantekening.getTekst());
        Log.d("Aantekeningmapper: ", "Aantekening " + aantekening.getAantekeningId() + " voor " + aantekening.getMeldingId() +  " demapt");

        return aantekeningEntity;
    }
}
