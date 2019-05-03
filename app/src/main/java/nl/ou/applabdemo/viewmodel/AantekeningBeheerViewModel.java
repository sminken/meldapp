package nl.ou.applabdemo.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import nl.ou.applabdemo.data.repository.AantekeningRepository;
import nl.ou.applabdemo.data.repository.FirebaseDatabaseRepository;
import nl.ou.applabdemo.domain.Aantekening;
//import nl.ou.applabdemo.domain.AantekeningBeheer;
import nl.ou.applabdemo.util.MeldAppException;

@SuppressWarnings("SpellCheckingInspection")
public class AantekeningBeheerViewModel extends ViewModel {
    private final String TAG = "VerwerkActivityViewMod";

    private MutableLiveData<List<Aantekening>> liveDataAantekeningen = null;
    //private AantekeningBeheer aantekeningBeheer = new AantekeningBeheer();
    private AantekeningRepository aantekeningRepository = new AantekeningRepository();

    /**
     * Haalt de meldingen op uit de database en slaat deze als livedata op
     * @return - livedata met de aantekeningen uit de database
     */
    public LiveData<List<Aantekening>> haalAantekeningenLijst( String meldingId) throws MeldAppException {
        Log.d(TAG,"haalAantekeningenLijst aangeroepen met meldingID " + meldingId );
        liveDataAantekeningen = new MutableLiveData<>();
        aantekeningRepository.addListener(new FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<Aantekening>() {
            @Override
            public void onSuccess(List<Aantekening> result) {
                List<Aantekening> geselecteerdeAantekeningenLijst = new ArrayList<Aantekening>();
                Log.d(TAG, "addListener.onSuccess aangeroepen");

                Collections.sort(result, new Comparator<Aantekening>() {
                    @Override
                    public int compare(Aantekening m1, Aantekening m2) {
                        try {

                            Date dateM1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(m1.getDatumTijd());
                            Date dateM2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(m2.getDatumTijd());
                            return dateM2.compareTo(dateM1);
                        } catch (ParseException e) {
                            Log.d("comparator: ", "parseException opgegooid in AantekeningViewModel");
                            return -1;
                        }
                    }
                });

                ListIterator itr = result.listIterator();
                Aantekening a;
                while ( itr.hasNext() ) {
                    a = (Aantekening) itr.next();
                    Log.d(TAG, "result meldinId=" + a.getMeldingId());
                    if ( a.getMeldingId().equals(meldingId) ) {
                        geselecteerdeAantekeningenLijst.add(a);
                    }
                }
                liveDataAantekeningen.setValue( geselecteerdeAantekeningenLijst );
            }

            @Override
            public void onError(Exception e) {
                Log.d("addListener","onError aangeroepn");
            }
        });
        return liveDataAantekeningen;
    }

    /**
     * Voegt nieuwe aantekening toe aan een geselecteerde melding
     * @param meldingId Geselecteerde melding
     * @param tekst Aantekening tekst
     */
    public static void voegNieuweAantekeningToe(String meldingId, String tekst) throws MeldAppException {

        if ( meldingId == null || tekst.isEmpty() ) {
            throw new MeldAppException("Aantekening mag niet leeg zijn");
        }
        Aantekening aantekening = new Aantekening(meldingId,tekst );
        //  voeg toe aan database
        voegAantekeningToeAanDatabase(aantekening);
    }

    /**
     * Voegt aantekening toe aan repository
     * @param aantekening Referentie naar aantekening object.
     */
    private static void voegAantekeningToeAanDatabase(Aantekening aantekening) {
        AantekeningRepository aantekeningRepository = new AantekeningRepository();
        aantekeningRepository.voegAantekeningToeAanDatabase(aantekening);
    }

    @Override
    protected void onCleared()
    {
        //aantekeningBeheer.onCleared();
    }


}

