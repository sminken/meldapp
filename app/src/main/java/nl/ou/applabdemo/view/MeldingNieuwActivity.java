package nl.ou.applabdemo.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import nl.ou.applabdemo.util.ExceptionVertaler;
import nl.ou.applabdemo.util.MeldAppException;
import nl.ou.applabdemo.viewmodel.GebruikersBeheerViewModel;
import nl.ou.applabdemo.viewmodel.MeldingBeheerViewModel;

/**
 * Actvity voor het maken van een nieuwe melding door de melder.
 */
@SuppressWarnings("SpellCheckingInspection")
public class MeldingNieuwActivity extends AppCompatActivity {

    // logging
    private static final String TAG = "MeldingNieuw";

    // Viewmodel voor het beheer van meldingen.
    private MeldingBeheerViewModel meldingBeheerViewModel = null;

    // Viewmodel voor het beheer van gebruikers.
    private GebruikersBeheerViewModel gebruikerBeheerViewModel = null;
    private GebruikerBeheer gebruikerBeheer = null;

    private String meldingGroep = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_melding_nieuw);

        // Haal input parameters op.
        getIncomingIntent();

        // Creëer de viewmodels voor gebruikers en meldingen.
        createViewModelMeldingBeheer();
        createViewModelGebruikerBeheer();
    }

    @Override
    public void onDestroy () {
        Log.v(TAG, "onDestroy");

        super.onDestroy();
    }

    /**
     * Haalt binnen gekomen paramters op.
     */
    private void getIncomingIntent() {

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            this.meldingGroep = bundle.getString("melding_groep");
        }
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
            }
        });
    }

    /**
     * Creeert de viewmodel voor gebruikers.
     */
    private void createViewModelGebruikerBeheer() {

        gebruikerBeheerViewModel = ViewModelProviders.of(this).get(GebruikersBeheerViewModel.class);

        // Voeg de observer aan de viewmodel toe.
        gebruikerBeheerViewModel.haalGebruikers().observe(this, new Observer<List<Gebruiker>>() {
            @Override
            public void onChanged(@Nullable List<Gebruiker> gebruikers) {
                gebruikerBeheer = new GebruikerBeheer(gebruikers);

                GebruikerInfo gebruikerInfo = gebruikerBeheer.haalAangemeldeGebruiker();

                initButtons();
                initTextViews(gebruikerInfo);
            }
        });
    }

    /**
     * Initialiseert de buttons van de view.
     */
    private void initButtons() {

        // Knop voor melding annuleren.
        final Button buttonMeldingAnnuleren = (Button) findViewById(R.id.button_annuleren);
        buttonMeldingAnnuleren.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Verwijder observers.
                removeObservers();

                // Start activity overzicht meldingen melder.
                Intent intent = new Intent(MeldingNieuwActivity.this, OverzichtMeldingenMelderActivity.class);
                intent.putExtra("melding_groep", meldingGroep);
                startActivity(intent);
                finish();
            }
        });

        // Knop voor nieuwe melding verzenden.
        final Button buttonMeldingVerzenden = (Button) findViewById(R.id.button_verzenden);
        buttonMeldingVerzenden.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //  Voeg melding via meldingbeheer toe.
                try {
                    // Haal de uid van de aangemelde gebruiker.
                    GebruikerInfo gebruikerInfo = gebruikerBeheer.haalAangemeldeGebruiker();
                    String uidMelder = gebruikerInfo.getUid();

                    // Haal onderwerp en inhoud van de melding.
                    TextView detailOnderwerp =  findViewById(R.id.detail_onderwerp);
                    String onderwerp = detailOnderwerp.getText().toString();

                    TextView detailInhoud = (TextView) findViewById(R.id.detail_inhoud);
                    String inhoud = detailInhoud.getText().toString();

                    // Voeg de nieuwe melding toe.
                    meldingBeheerViewModel.voegNieuweMeldingToe(uidMelder, onderwerp, inhoud);

                    // Melding is succesvol verzonden.
                    Toast.makeText(MeldingNieuwActivity.this, "Melding verzonden", Toast.LENGTH_SHORT).show();

                    // Verwijder observers.
                    removeObservers();

                    // Start activity overzicht met de meldingen van de melder.
                    Intent intent = new Intent(MeldingNieuwActivity.this, OverzichtMeldingenMelderActivity.class);
                    intent.putExtra("melding_groep", meldingGroep);
                    startActivity(intent);
                    finish();

                } catch (MeldAppException e) {
                    Toast.makeText(MeldingNieuwActivity.this, ExceptionVertaler.vertaalException(e.getMessage()), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Initialiseert de textviews van de view.
     *
     * @param gebruikerInfo de gebruiker informatie.
     */
    private void initTextViews(GebruikerInfo gebruikerInfo) {

        String melder = null;
        String telefoonnummer = null;

        // Haal de details van de melder.
        if (gebruikerInfo != null) {
            // Opgemaakte naam van de melder.
            melder = gebruikerInfo.getOpgemaakteNaam();
            telefoonnummer = gebruikerInfo.getTelefoonnummer();
        }

        TextView detailMelder = (TextView) findViewById(R.id.detail_melder);
        detailMelder.setText(melder);
        detailMelder.setEnabled(false);

        TextView detailTelefoonnummer = (TextView) findViewById(R.id.detail_telefoonnummer);
        detailTelefoonnummer.setText(telefoonnummer);
        detailTelefoonnummer.setEnabled(false);
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
