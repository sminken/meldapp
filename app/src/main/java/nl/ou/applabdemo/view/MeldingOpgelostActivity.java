package nl.ou.applabdemo.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import nl.ou.applabdemo.R;
import nl.ou.applabdemo.domain.Melding;
import nl.ou.applabdemo.domain.MeldingBeheer;
import nl.ou.applabdemo.domain.MeldingInfo;
import nl.ou.applabdemo.util.ExceptionVertaler;
import nl.ou.applabdemo.util.MeldAppException;
import nl.ou.applabdemo.util.StatusVertaler;
import nl.ou.applabdemo.viewmodel.MeldingBeheerViewModel;

@SuppressWarnings("SpellCheckingInspection")
public class MeldingOpgelostActivity extends AppCompatActivity {
    // logging
    private static final String TAG = "MeldingOpgelost";

    // Viewmodel voor het beheer van meldingen.
    private MeldingBeheerViewModel meldingBeheerViewModel = null;
    private MeldingBeheer meldingBeheer = null;

    private String uidMelding = null;
    private String meldingGroep = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_melding_opgelost);

        // Haal input parameters op.
        getIncomingIntent();

        // Creëer de viewmodels voor meldingen.
        createViewModelMeldingBeheer();
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
            this.uidMelding = bundle.getString("uid_melding");
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
                meldingBeheer = new MeldingBeheer(meldingen);

                // Haal de gevraagde melding.
                MeldingInfo meldingInfo = meldingBeheer.getMelding(uidMelding);

                initButtons(meldingInfo);
                initTextViews(meldingInfo);
                initPopupView();
            }
        });
    }

    /**
     * Initialiseert de buttons van de view.
     *
     * @param meldingInfo de melding informatie
     */
    private void initButtons(MeldingInfo meldingInfo) {

        // Knop voor melding annuleren.
        final Button buttonMeldingAnnuleren = findViewById(R.id.button_annuleren);
        //noinspection Convert2Lambda
        buttonMeldingAnnuleren.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Verwijder observers.
                removeObservers();

                // Start activity details meldingen.
                Intent intent = new Intent(MeldingOpgelostActivity.this, MeldingDetailsBehandelaarActivity.class);
                intent.putExtra("melding_groep", meldingGroep);
                intent.putExtra("uid_melding", meldingInfo.getUidMelding());
                startActivity(intent);
                finish();
            }
        });

        // Knop voor melding opgelost.
        final Button buttonMeldingOpgelost = findViewById(R.id.button_opgelost);
        //noinspection Convert2Lambda
        buttonMeldingOpgelost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // Registreer de melding als opgelost.
                try {
                    TextView aanvullingTextView = findViewById(R.id.detail_aanvulling);
                    String aanvulling = aanvullingTextView.getText().toString();

                    // Voeg de aanvulling toe aan de inhoud.
                    String inhoud = meldingBeheer.addAanvullingAanInhoud(meldingInfo.getInhoud(), aanvulling);

                    // De melding is opgelost.
                    meldingBeheerViewModel.opgelosteMelding(meldingInfo.getUidMelding(), inhoud);

                    // Verwijder observers.
                    removeObservers();

                    // Start activity overzicht meldingen behandelaar.
                    Intent intent = new Intent(MeldingOpgelostActivity.this, OverzichtMeldingenBehandelaarActivity.class);
                    intent.putExtra("melding_groep", meldingGroep);
                    startActivity(intent);
                    finish();
                }
                catch (MeldAppException e) {
                    Toast.makeText(MeldingOpgelostActivity.this, ExceptionVertaler.vertaalException(e.getMessage()), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Definieer de setting van de knop opgelost. Deze kan alsnog gedisabled worden als de status van deze
        // melding tussentijds mocht wijzigen.
        // Activeer en deactiveer deze knop op basis van de status van de melding.
        switch (meldingInfo.getStatus()) {
            case NIEUW:
            case HEROPEND:
            case INBEHANDELING:
                buttonMeldingOpgelost.setEnabled(true);
                break;
            case OPGELOST:
            case GESLOTEN:
                buttonMeldingOpgelost.setEnabled(false);
                break;
            default:
                buttonMeldingOpgelost.setEnabled(false);
                break;
        }
    }

    /**
     * Initialiseert de textviews van de view.
     *
     * @param meldingInfo de melding informatie
     */
    private void initTextViews(MeldingInfo meldingInfo) {

        String onderwerp = null;
        String datum = null;
        String tijd = null;
        String status = null;

        // Haal de details van de melding.
        if (meldingInfo != null) {
            onderwerp = meldingInfo.getOnderwerp();
            datum = meldingInfo.getDatum();
            tijd = meldingInfo.getTijd();
            status = StatusVertaler.vertaalStatus(meldingInfo.getStatusTekst());
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
    }

    /**
     * Toont pop up scherm voor de opgeloste melding.
     */
    private void initPopupView() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int hight = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*0.80),(int)(hight*0.64));
    }

    /**
     * Verwijder de observers voor gebruikers en meldingen.
     */
    private void removeObservers() {
        Log.v(TAG, "Remove meldingen");
        meldingBeheerViewModel.haalMeldingen().removeObservers(this);
    }

}
