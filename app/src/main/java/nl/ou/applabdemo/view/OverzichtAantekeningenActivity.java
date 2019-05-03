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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Iterator;
import java.util.List;
import nl.ou.applabdemo.R;
import nl.ou.applabdemo.domain.Aantekening;
import nl.ou.applabdemo.util.MeldAppException;
import nl.ou.applabdemo.viewmodel.AantekeningBeheerViewModel;
@SuppressWarnings("SpellCheckingInspection")
/**
 * Activity voor het tonen en toevoegen van aantekeningen.
 */
public class OverzichtAantekeningenActivity extends AppCompatActivity {

    private static final String TAG = "VerwerkenMeldActivity";
    private String meldingId = null;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AantekeningBeheerViewModel viewModel;
    private Button toevoegenAantekeningButton;
    private Button buttonAantekeningToevoegenAnnuleren;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overzicht_aantekeningen);
        if ( meldingId == null ) {
            meldingId = getIncomingIntent();
        }

        Log.d(TAG, "Creer viewModel. meldingId = " + meldingId );
        viewModel = ViewModelProviders.of(this).get(AantekeningBeheerViewModel.class);

        //        Verbind een Observer aan het viewModel
        Log.d(TAG, "Verbind een Observer aan methode viewModel.haalAantekeningenLijst( " + meldingId + " )");
        try {
            viewModel.haalAantekeningenLijst( meldingId ).observe(this, new Observer<List<Aantekening>>() {

                @Override
                public void onChanged(@Nullable List<Aantekening> aantekenings) {
                    Log.d(TAG,"onCreate.onChanged aangeroepen");
                    Iterator iterator = aantekenings.iterator();
                    while ( iterator.hasNext() ){
                        Aantekening a = (Aantekening) iterator.next();
                        Log.d(TAG,"RecyclerView meldingId=" + a.getMeldingId() );
                    }

                    initAantekeningRecyclerView(aantekenings);
                }
            });
        } catch (MeldAppException e) {
            e.printStackTrace();
        }

        toevoegenAantekeningButton = findViewById(R.id.toevoegen_button);
        // Zet listener op knop "Toevoiegen"
        toevoegenAantekeningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OverzichtAantekeningenActivity.this, AantekeningToevoegenActivity.class);
                intent.putExtra("melding_id", meldingId);
                startActivity(intent);
                Log.d(TAG,"Nieuwe aantekening toegevoegen met melding_id " + meldingId );
            }
        });

        buttonAantekeningToevoegenAnnuleren = findViewById(R.id.buttonAantekeningToevoegenAnnuleren);
        // Zet listener op de knop "Annuleren"
        buttonAantekeningToevoegenAnnuleren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
            });
    }

    /**
     * Ininialiseert de RecyclerView. Wijst een LayoutManager hieraan toe en verbindt een adapter aan de RecyclerView
     * @param lijstAantekeningen de lijst met aantekeningen van een melding
     */
    private void initAantekeningRecyclerView(List<Aantekening> lijstAantekeningen) {
        Log.d(TAG,lijstAantekeningen.toString());

        //       Initialiseert de RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.aantekeningen_recyclerview);

        //        Initialiseert een LinearLayoutManager ern wijst deze toe aan de RecyclerView. Standaard zorgt dit
        //        voor een verticaal geordende lijst.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //        Initialiseert Adapter en geeft ArrayLijsten mee
        Log.d(TAG,"Adapter geinitialiseerd");
        mAdapter = new AantekeningRecyclerViewAdapter(lijstAantekeningen, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void annulerenButtonAction (View view) {
        finish();
    }

    public void opgelostButton (View view) {
        finish();
    }


    /**
     * Haalt door parent doorgegeven melding identifier op.
     */
    private String getIncomingIntent() {

        String uidMelding = null;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            uidMelding = bundle.getString("uid_melding");
        }

        return uidMelding;

    }


}
