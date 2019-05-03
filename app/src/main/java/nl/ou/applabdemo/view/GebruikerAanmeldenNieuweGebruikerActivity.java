package nl.ou.applabdemo.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import nl.ou.applabdemo.R;
import nl.ou.applabdemo.domain.Gebruiker;
import nl.ou.applabdemo.domain.GebruikerInfo;
import nl.ou.applabdemo.domain.GebruikerStatus;
import nl.ou.applabdemo.domain.Rol;
import nl.ou.applabdemo.util.ExceptionVertaler;
import nl.ou.applabdemo.util.MeldAppException;
import nl.ou.applabdemo.viewmodel.GebruikersBeheerViewModel;

/**
 * Activity voor het aanmelden van een nieuwe gebruiker in de meldApp <br>
 *     <B>Voorwaarde:</B> : Er moet een gebruiker zijn aangemeld in de Firebase database <br>
 *
 *         Deze UID moet via de intent worden doorgegeven.
 *
 */
@SuppressWarnings("SpellCheckingInspection")
public class GebruikerAanmeldenNieuweGebruikerActivity extends AppCompatActivity
        implements View.OnClickListener{

    // logging
    private static final String TAG = "AanmeldNieuweGebruiker";

    private TextView voornaamTextView;
    private TextView achternaamTextView;
    private TextView telefoonnummerTextView;
    private TextView foutmeldingView;
    private String gebruikerUidUitIntent;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;

    // tbv ViewModel
    private GebruikersBeheerViewModel gebruikerBeheerViewModel = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gebruiker_aanmelden_nieuwe_gebruiker);

        // create ViewModels
        Log.v(TAG, "CreateViewModelUsers");
        createViewModelUsers();

        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Assign fields
        Button aanmeldenMelderButton = findViewById(R.id.buttonAanmeldenMelder);
        Button aanmeldenBehandelaarButton = findViewById(R.id.buttonAanmeldenBehandelaar);
        Button aanmeldenAnnulerenButton = findViewById(R.id.buttonAnnuleren);

        // initialiseer schermvelden
        voornaamTextView = findViewById(R.id.editText_voornaam);
        achternaamTextView = findViewById(R.id.editText_achternaam);
        telefoonnummerTextView = findViewById(R.id.editText_telefoonnummer);
        foutmeldingView = findViewById(R.id.textView_foutmelding);
        foutmeldingView.setText("");


        // Set click listeners
        aanmeldenMelderButton.setOnClickListener(this);
        aanmeldenBehandelaarButton.setOnClickListener(this);
        aanmeldenAnnulerenButton.setOnClickListener(this);

        // bepaal uid
        gebruikerUidUitIntent = null;
        if (getIntent().hasExtra("gebruiker_uid")) {
            //noinspection ConstantConditions
            gebruikerUidUitIntent = (String) getIntent().getExtras().get("gebruiker_uid");
        }

        // uid moet in intent voorkomen
        if (gebruikerUidUitIntent == null ){
            throw new RuntimeException(getString(R.string.errorIntent));
        }

    }

    /*
    Handle click on signIn Button
     */
    @Override
    public void onClick(View v) {
        /*
        Afhandeling click Button
         */

        switch (v.getId()) {
            case R.id.buttonAnnuleren:
                mFirebaseAuth.signOut();
                setResult(RESULT_OK, null);
                finish();
                break;
            case R.id.buttonAanmeldenMelder:
                Rol gebruikerGevraagdeRol = Rol.MELDER;
                verwerkButton(gebruikerGevraagdeRol);
                break;
            case R.id.buttonAanmeldenBehandelaar:
                gebruikerGevraagdeRol = Rol.BEHANDELAAR;
                verwerkButton(gebruikerGevraagdeRol);
                break;
            default:
                throw new RuntimeException(getString(R.string.unkownButtonID));
        }
    }

    private void verwerkButton(Rol rol) {
        Log.v(TAG, "SignInUser");

        String gebruikerVoornaam = voornaamTextView.getText().toString();
        String gebruikerAchternaam = achternaamTextView.getText().toString();
        String gebruikerTelefoonnummer = telefoonnummerTextView.getText().toString();

        // default waarden voor notification en deviceToken
        boolean gebruikerNotification = false;
        String gebruikerDeviceToken = null;

        @SuppressWarnings("ConstantConditions") GebruikerInfo nieuwGebruikerInfo = new GebruikerInfo(gebruikerVoornaam, gebruikerAchternaam, "",
                gebruikerUidUitIntent,GebruikerStatus.NIEUW,
                gebruikerTelefoonnummer, rol,
                gebruikerNotification, gebruikerDeviceToken);

        try {
            gebruikerBeheerViewModel.voegUserToe(nieuwGebruikerInfo);
        }
        catch (MeldAppException ex) {
            foutmeldingView.setText(ExceptionVertaler.vertaalException(ex.getMessage()));
            return;
        }

        // Start Main
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /*
    Methods for viewModels
     */
    private void createViewModelUsers() {
        Log.v(TAG, "InitViewModelUsers");

        gebruikerBeheerViewModel = ViewModelProviders.of(this).get(GebruikersBeheerViewModel.class);

        // attach an observer to the viewmodel methodview
        Log.v(TAG, "RegisterObserverHaalUsers");
        //noinspection Convert2Lambda
        gebruikerBeheerViewModel.haalGebruikers().observe(this, new Observer<List<Gebruiker>>() {
            @Override
            public void onChanged(@Nullable List<Gebruiker> gebruikersLijst) {
                Log.v(TAG, "onChangeUsers");
            }
        });

    }
}
