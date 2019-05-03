package nl.ou.applabdemo.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import nl.ou.applabdemo.R;
import nl.ou.applabdemo.domain.Melding;
import nl.ou.applabdemo.util.MeldAppException;
import nl.ou.applabdemo.viewmodel.AantekeningBeheerViewModel;

@SuppressWarnings("SpellCheckingInspection")
/**
 * Activity voor het toevoegen van een nieuwe aantekeningen
 */
public class AantekeningToevoegenActivity extends AppCompatActivity {
    private final String TAG = "AantekeningToevgn";
    private String meldingId;
    private Button buttonVoegToeNieuweAantekeningOk;
    private Button buttonAnnulerenToevoegen;
    private EditText aantekeningInvoerVeld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aantekening_toevoegen);
        AantekeningBeheerViewModel viewModelAantekening = ViewModelProviders.of(this).get(AantekeningBeheerViewModel.class);
        meldingId = getIncomingIntent();
        Log.d(TAG, "Creer viewModel. meldingId = " + meldingId );

        buttonVoegToeNieuweAantekeningOk = (Button) findViewById(R.id.buttonVoegToeNieuweAantekeningOk);
        aantekeningInvoerVeld = (EditText) findViewById(R.id.AantekeningInvoerVeld);
        buttonVoegToeNieuweAantekeningOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    viewModelAantekening.voegNieuweAantekeningToe( meldingId ,aantekeningInvoerVeld.getText().toString());
                    finish(); // Terug naar het aantekeningenoverzicht
                } catch (MeldAppException e) {
                    Toast.makeText(AantekeningToevoegenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonAnnulerenToevoegen = (Button) findViewById(R.id.buttonAnnulerenToevoegen);
        buttonAnnulerenToevoegen.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        meldingId = null;
        buttonVoegToeNieuweAantekeningOk = null;
        aantekeningInvoerVeld = null;
    }

    /**
     * Ophalen melding identifier van parent intent.
     * @return Melding identifier
     */
    private String getIncomingIntent() {
        Melding melding = null;
        String mId = null;
        Intent intent = getIntent();
        if ( intent.hasExtra("melding_id") ) {
            mId = intent.getStringExtra("melding_id");
            Log.d(TAG, "meldingId=" + mId + " geselecteerd.");
        } else {
            Log.d(TAG, "Geen meldingId doorgegegeven");
            return "ffffffffff";
        }
        return mId;
    }
}
