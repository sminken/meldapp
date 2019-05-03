package nl.ou.applabdemo.domain;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
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
import nl.ou.applabdemo.util.MeldAppException;

/**
 * Klasse voor het beheer van een aantekening.
 *
 */
@SuppressWarnings("SpellCheckingInspection")
public class AantekeningBeheer {
    private static final String TAG = "AantekeningBeheer ";

    /**
     * Constructor
     */
    public AantekeningBeheer() {
        Log.d(TAG,"Constructor aangeroepen");
    }

    /**
     * Voegt nieuwe aantekening toe aan een geselecteerde melding
     * @param meldingId Geselecteerde melding
     * @param tekst Aantekening tekst
     */
    public void voegNieuweAantekeningToe(String meldingId, String tekst) {

        Aantekening aantekening = new Aantekening(meldingId,tekst );
        //  voeg toe aan database
        voegAantekeningToeAanDatabase(aantekening);
    }

    /**
     * Voegt aantekening toe aan repository
     * @param aantekening Referentie naar aantekening object.
     */
    private void voegAantekeningToeAanDatabase(Aantekening aantekening) {
        AantekeningRepository aantekeningRepository = new AantekeningRepository();
        aantekeningRepository.voegAantekeningToeAanDatabase(aantekening);
    }

    public void onCleared() {

    }

}
