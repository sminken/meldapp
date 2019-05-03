package nl.ou.applabdemo.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import nl.ou.applabdemo.R;
import nl.ou.applabdemo.domain.Gebruiker;
import nl.ou.applabdemo.domain.GebruikerBeheer;
import nl.ou.applabdemo.domain.GebruikerInfo;
import nl.ou.applabdemo.domain.Melding;
import nl.ou.applabdemo.domain.MeldingBeheer;
import nl.ou.applabdemo.domain.MeldingInfo;
import nl.ou.applabdemo.util.ExceptionVertaler;
import nl.ou.applabdemo.util.MeldAppException;
import nl.ou.applabdemo.util.StatusVertaler;
import nl.ou.applabdemo.viewmodel.GebruikersBeheerViewModel;
import nl.ou.applabdemo.viewmodel.MeldingBeheerViewModel;

/**
 * Actvity voor het weergeven van de details van een melding van de behandelaar
 */
@SuppressWarnings("SpellCheckingInspection")
public class MeldingDetailsBehandelaarActivity extends AppCompatActivity {

    // logging
    private static final String TAG = "MeldingDetail";

    // Viewmodel voor het beheer van meldingen.
    private MeldingBeheerViewModel meldingBeheerViewModel = null;
    private MeldingBeheer meldingBeheer = null;

    // Viewmodel voor het beheer van gebruikers.
    private GebruikersBeheerViewModel gebruikerBeheerViewModel = null;
    private GebruikerBeheer gebruikerBeheer = null;

    private String uidMelding = null;
    private String meldingGroep = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_melding_details_behandelaar);
        getIncomingIntent();

        // Creëer de viewmodels voor gebruikers en meldingen.
        createViewModelGebruikerBeheer();
        createViewModelMeldingBeheer();
    }

    @Override
    public void onDestroy () {
        Log.v(TAG, "onDestroy");

        super.onDestroy();
    }

    /**
     * Haalt binnen gekomen parameters op.
     */
    private void getIncomingIntent() {

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            this.uidMelding = bundle.getString("uid_melding");
            this.meldingGroep = bundle.getString("melding_groep");
        }
    }

    /**
     * Creeert de viewmodel voor gebruikers.
     */
    private void createViewModelGebruikerBeheer() {

        // Viewmodel voor het beheer van gebruikers.
        gebruikerBeheerViewModel = ViewModelProviders.of(this).get(GebruikersBeheerViewModel.class);

        // Voeg de observer aan de viewmodel toe.
        //noinspection Convert2Lambda
        gebruikerBeheerViewModel.haalGebruikers().observe(this, new Observer<List<Gebruiker>>() {
            @Override
            public void onChanged(@Nullable List<Gebruiker> gebruikers) {
                gebruikerBeheer = new GebruikerBeheer(gebruikers);
            }
        });
    }

    /**
     * Creeert de viewmodel voor meldingen.
     */
    private void createViewModelMeldingBeheer() {

        // Viewmodel voor het beheer van meldingen.
        meldingBeheerViewModel = ViewModelProviders.of(this).get(MeldingBeheerViewModel.class);

        // Voeg de observer aan de viewmodel toe.
        //noinspection Convert2Lambda
        meldingBeheerViewModel.haalMeldingen().observe(this, new Observer<List<Melding>>() {
            @Override
            public void onChanged(@Nullable List<Melding> meldingen) {
                meldingBeheer = new MeldingBeheer(meldingen);

                // Haal de gevraagde melding.
                MeldingInfo meldingInfo = meldingBeheer.getMelding(uidMelding);

                // Haal de gebruiker gegevens van de melder op.
                String uidGebruikerMelder = meldingInfo.getUidGebruikerMelder();
                GebruikerInfo gebruikerInfoMelder = gebruikerBeheer.haalGebruikerOp(uidGebruikerMelder);

                // Haal de gebruiker gegevens van de behandelaar op.
                String uidGebruikerBehandelaar = meldingInfo.getUidGebruikerBehandelaar();
                GebruikerInfo gebruikerInfoBehandelaar = gebruikerBeheer.haalGebruikerOp(uidGebruikerBehandelaar);

                initButtons(meldingInfo);
                initTextViews(meldingInfo, gebruikerInfoMelder, gebruikerInfoBehandelaar);
            }
        });
    }

    /**
     * Initialiseert de buttons van de view.
     */
    private void initButtons(MeldingInfo meldingInfo) {

        // Knop voor melding annuleren.
        final Button buttonMeldingAnnuleren = findViewById(R.id.button_annuleren);
        //noinspection Convert2Lambda
        buttonMeldingAnnuleren.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Verwijder observers.
                removeObservers();

                // Start parent activity.
                finish();
            }
        });

        // Knop voor melding aantekeningen.
        final Button buttonMeldingAantekeningen = findViewById(R.id.button_aantekeningen);
        //noinspection Convert2Lambda
        buttonMeldingAantekeningen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Verwijder observers.
                removeObservers();

                // Start activity aantekeningen.
                Intent intent = new Intent(MeldingDetailsBehandelaarActivity.this, OverzichtAantekeningenActivity.class);
                intent.putExtra("uid_melding", meldingInfo.getUidMelding());
                startActivity(intent);
                finish();
            }
        });

        // Knop voor melding aan mij toewijzen.
        final Button buttonMeldingMijToewijzen = findViewById(R.id.button_mij_toewijzen);
        //noinspection Convert2Lambda
        buttonMeldingMijToewijzen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                try {
                    // De melding toewijzen aan de aangemelde behandelaar.
                    GebruikerInfo gebruikerInfo = gebruikerBeheer.haalAangemeldeGebruiker();
                    meldingBeheerViewModel.toewijzenMelding(meldingInfo.getUidMelding(), gebruikerInfo.getUid());
                }
                catch (MeldAppException e) {
                    Toast.makeText(MeldingDetailsBehandelaarActivity.this, ExceptionVertaler.vertaalException(e.getMessage()), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Knop voor melding opgelost.
        final Button buttonMeldingOpgelost = findViewById(R.id.button_opgelost);
        //noinspection Convert2Lambda
        buttonMeldingOpgelost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Verwijder observers.
                removeObservers();

                // Start activity opgeloste melding.
                Intent intent = new Intent(MeldingDetailsBehandelaarActivity.this, MeldingOpgelostActivity.class);
                intent.putExtra("uid_melding", meldingInfo.getUidMelding());
                intent.putExtra("melding_groep", meldingGroep);
                startActivity(intent);
                finish();
            }
        });

        // Definieer de settings van de knoppen toewijzen en opgelost.
        // Activeer en deactiveer op basis van de status van de melding.
        switch (meldingInfo.getStatus()) {
            case NIEUW:
                buttonMeldingAnnuleren.setVisibility(View.VISIBLE);
                buttonMeldingMijToewijzen.setVisibility(View.VISIBLE);
                buttonMeldingOpgelost.setVisibility(View.INVISIBLE);
                buttonMeldingAantekeningen.setVisibility(View.INVISIBLE);
                break;
            case HEROPEND:
            case INBEHANDELING:
                buttonMeldingAnnuleren.setVisibility(View.VISIBLE);
                buttonMeldingMijToewijzen.setVisibility(View.INVISIBLE);
                buttonMeldingOpgelost.setVisibility(View.VISIBLE);
                buttonMeldingAantekeningen.setVisibility(View.VISIBLE);
                break;
            case OPGELOST:
            case GESLOTEN:
                buttonMeldingAnnuleren.setVisibility(View.VISIBLE);
                buttonMeldingMijToewijzen.setVisibility(View.INVISIBLE);
                buttonMeldingOpgelost.setVisibility(View.INVISIBLE);
                buttonMeldingAantekeningen.setVisibility(View.VISIBLE);
                break;
            default:
                buttonMeldingAnnuleren.setVisibility(View.VISIBLE);
                buttonMeldingMijToewijzen.setVisibility(View.INVISIBLE);
                buttonMeldingOpgelost.setVisibility(View.INVISIBLE);
                buttonMeldingAantekeningen.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
    * Initialiseert de textviews van de view.
    *
    * @param meldingInfo de melding informatie
    * @param gebruikerInfoMelder de gebruikers informatie van de melder.
    * @param gebruikerInfoBehandelaar de gebruikers informatie van de behandelaar.
    */
    private void initTextViews(MeldingInfo meldingInfo, GebruikerInfo gebruikerInfoMelder, GebruikerInfo gebruikerInfoBehandelaar ) {

        String onderwerp = null;
        String datum = null;
        String tijd = null;
        String status = null;
        String melder = null;
        String telefoonnummer = null;
        String behandelaar;
        String inhoud = null;

        // Haal de details van de melding.
        if (meldingInfo != null) {
            onderwerp = meldingInfo.getOnderwerp();
            datum = meldingInfo.getDatum();
            tijd = meldingInfo.getTijd();
            status = StatusVertaler.vertaalStatus(meldingInfo.getStatusTekst());
            inhoud = meldingInfo.getInhoud();
        }

        // Haal de details van de melder.
        if (gebruikerInfoMelder != null) {
            // Opgemaakte naam van de melder.
            melder = gebruikerInfoMelder.getOpgemaakteNaam();
            telefoonnummer = gebruikerInfoMelder.getTelefoonnummer();
        }

        // Haal de details van de behandelaar.
        if (gebruikerInfoBehandelaar != null) {
            // Opgemaakte naam van de behandelaar.
            behandelaar = gebruikerInfoBehandelaar.getOpgemaakteNaam();
        } else {
            behandelaar = getString(R.string.behandelaar_niet_toegekend);
        }

        // Zet de details van de textviews.
        TextView detailOnderwerp = findViewById(R.id.detail_onderwerp);
        detailOnderwerp.setText(onderwerp);

        TextView detailDatum = findViewById(R.id.detail_datum);
        detailDatum.setText(datum);

        TextView detailTijd = findViewById(R.id.detail_tijd);
        detailTijd.setText(tijd);

        TextView detailStatus = findViewById(R.id.detail_status);
        detailStatus.setText(status);

        TextView detailMelder = findViewById(R.id.detail_melder);
        detailMelder.setText(melder);

        TextView detailTelefoonnummer = findViewById(R.id.detail_telefoonnummer);
        detailTelefoonnummer.setText(telefoonnummer);

        TextView detailBehandelaar = findViewById(R.id.detail_behandelaar);
        detailBehandelaar.setText(behandelaar);

        TextView detailInhoud = findViewById(R.id.detail_inhoud);
        detailInhoud.setMovementMethod(new ScrollingMovementMethod());
        detailInhoud.setText(inhoud);
    }

    /**
     * Verwijder de observers voor gebruikers en meldingen.
     */
    private void removeObservers() {
        Log.v(TAG, "Remove gebruikers");
        gebruikerBeheerViewModel.haalGebruikers().removeObservers(this);

        Log.v(TAG, "Remove meldingen");
        meldingBeheerViewModel.haalMeldingen().removeObservers(this);
    }

}
