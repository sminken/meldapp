package nl.ou.applabdemo.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.ou.applabdemo.R;
import nl.ou.applabdemo.comparator.MeldingInfoBehandelaarGeslotenComparator;
import nl.ou.applabdemo.comparator.MeldingInfoBehandelaarInBehandelingComparator;
import nl.ou.applabdemo.comparator.MeldingInfoBehandelaarOpenstaandComparator;
import nl.ou.applabdemo.domain.Gebruiker;
import nl.ou.applabdemo.domain.GebruikerBeheer;
import nl.ou.applabdemo.domain.GebruikerInfo;
import nl.ou.applabdemo.domain.Melding;
import nl.ou.applabdemo.domain.MeldingBeheer;
import nl.ou.applabdemo.domain.MeldingInfo;
import nl.ou.applabdemo.domain.Status;
import nl.ou.applabdemo.viewmodel.GebruikersBeheerViewModel;
import nl.ou.applabdemo.viewmodel.MeldingBeheerViewModel;

/**
 * Actvity voor het overzicht van meldingen van de behandelaar.
 */
@SuppressWarnings("SpellCheckingInspection")
public class OverzichtMeldingenBehandelaarActivity extends AppCompatActivity {

    // logging
    private static final String TAG = "OverzichtBehandelaar";

    // Viewmodel voor het beheer van meldingen.
    @SuppressWarnings("FieldCanBeLocal")
    private MeldingBeheerViewModel meldingBeheerViewModel = null;
    private MeldingBeheer meldingBeheer = null;

    // Viewmodel voor het beheer van gebruikers.
    @SuppressWarnings("FieldCanBeLocal")
    private GebruikersBeheerViewModel gebruikerBeheerViewModel = null;
    private GebruikerBeheer gebruikerBeheer = null;

    // Groep met meldingen in het overzicht. Een groep met meldingen bevat een verzameling met
    // meldingen met een bepaalde status.
    private MeldingGroep meldingGroep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overzicht_meldingen_behandelaar);

        // Haal input parameters op.
        getIncomingIntent();

        // Creëer de viewmodels voor gebruikers en meldingen.
        createViewModelGebruikerBeheer();
        createViewModelMeldingBeheer();
    }

    @Override
    public void onDestroy () {
        Log.v(TAG, "onDestroy");

        super.onDestroy();

        Log.v(TAG, "Remove gebruikers");
        gebruikerBeheerViewModel.haalGebruikers().removeObservers(this);

        Log.v(TAG, "Remove meldingen");
        meldingBeheerViewModel.haalMeldingen().removeObservers(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overzicht_meldingen_behandelaar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.item_afmelden:
                menuItemAfmelden();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Afhandelen van het menuitem afmelden.
     */
    private void menuItemAfmelden() {
        // Verwijder observers.
        removeObservers();

        // Start activity main.
        Intent intent = new Intent(OverzichtMeldingenBehandelaarActivity.this, MainActivity.class);
        intent.putExtra("gebruiker_afmelden", "true");
        startActivity(intent);
        finishAffinity();
    }

    /**
     * Bepaal de waarde van de binnengekomen parameters.
     */
    private void getIncomingIntent() {

        // Default groep met meldingen is alle openstaande meldingen.
        this.meldingGroep = MeldingGroep.OPENSTAAND;

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String groep = bundle.getString("melding_groep");

            // Bepaal op basis van de groep de meldinggroep.
            for (MeldingGroep meldingGroep : MeldingGroep.values()) {
                if(meldingGroep.toString().equals(groep)) {
                    this.meldingGroep = meldingGroep;
                    break;
                }
            }
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
                if (meldingen != null) {
                    meldingBeheer = new MeldingBeheer(meldingen);

                    // Geef alle meldingen voor de melding groep.
                    List<MeldingInfo> meldingenInfo = getMeldingen();

                    // Geef een lijst van alle melders.
                    List<GebruikerInfo> meldersInfo = gebruikerBeheer.geefAlleMelders();

                    initButtons();
                    initRecyclerView(meldingenInfo, meldersInfo);
                }
            }
        });
    }

    /**
     * Initialiseert de buttons van de view.
     */
    private void initButtons() {

        // Knop voor overzicht met alle openstaande meldingen.
        final Button buttonOverzichtOpenstaand = findViewById(R.id.button_openstaand);
        //noinspection Convert2Lambda
        buttonOverzichtOpenstaand.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Verwijder observers.
                removeObservers();

                // Start activity overzicht openstaande meldingen.
                Intent intent = new Intent(OverzichtMeldingenBehandelaarActivity.this, OverzichtMeldingenBehandelaarActivity.class);
                intent.putExtra("melding_groep", MeldingGroep.OPENSTAAND.toString());
                startActivity(intent);
                finish();
            }
        });

        // Knop voor overzicht met in behandeling zijnde meldingen.
        final Button buttonOverzichtInBehandeling = findViewById(R.id.button_in_behandeling);
        //noinspection Convert2Lambda
        buttonOverzichtInBehandeling.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Verwijder observers.
                removeObservers();

                // Start activity overzicht in behandeling zijnde meldingen.
                Intent intent = new Intent(OverzichtMeldingenBehandelaarActivity.this, OverzichtMeldingenBehandelaarActivity.class);
                intent.putExtra("melding_groep", MeldingGroep.INBEHANDELING.toString());
                startActivity(intent);
                finish();
            }
        });

        // Knop voor overzicht met alle opgeloste meldingen.
        final Button buttonOverzichtGesloten = findViewById(R.id.button_gesloten);
        //noinspection Convert2Lambda
        buttonOverzichtGesloten.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Verwijder observers.
                removeObservers();

                // Start activity overzicht gesloten meldingen.
                Intent intent = new Intent(OverzichtMeldingenBehandelaarActivity.this, OverzichtMeldingenBehandelaarActivity.class);
                intent.putExtra("melding_groep", MeldingGroep.GESLOTEN.toString());
                startActivity(intent);
                finish();
            }
        });

        final View dividerOverzichtOpenstaand = findViewById(R.id.divider_overzicht_openstaand);
        final View dividerOverzichtInBehandeling = findViewById(R.id.divider_overzicht_in_behandeling);
        final View dividerOverzichtGesloten = findViewById(R.id.divider_overzicht_gesloten);

        // Definieer de settings van de knoppen openstaande, opgeloste en gesloten meldingen.
        // Activeer en deactiveer op basis van de geselecteerde meldinggroep de overeenkomstige knoppen.
        switch (meldingGroep) {
            case OPENSTAAND:
                // Button Openstaand
                buttonOverzichtOpenstaand.setEnabled(false);
                buttonOverzichtOpenstaand.setTextColor(Color.RED);
                dividerOverzichtOpenstaand.setBackgroundColor(Color.RED);
                // Button InBehandeling
                buttonOverzichtInBehandeling.setEnabled(true);
                buttonOverzichtInBehandeling.setTextColor(Color.LTGRAY);
                dividerOverzichtInBehandeling.setBackgroundColor(Color.TRANSPARENT);
                // Button Gesloten
                buttonOverzichtGesloten.setEnabled(true);
                buttonOverzichtGesloten.setTextColor(Color.LTGRAY);
                dividerOverzichtGesloten.setBackgroundColor(Color.TRANSPARENT);
                break;
            case INBEHANDELING:
                // Button Openstaand
                buttonOverzichtOpenstaand.setEnabled(true);
                buttonOverzichtOpenstaand.setTextColor(Color.LTGRAY);
                dividerOverzichtOpenstaand.setBackgroundColor(Color.TRANSPARENT);
                // Button InBehandeling
                buttonOverzichtInBehandeling.setEnabled(false);
                buttonOverzichtInBehandeling.setTextColor(Color.RED);
                dividerOverzichtInBehandeling.setBackgroundColor(Color.RED);
                // Button Gesloten
                buttonOverzichtGesloten.setEnabled(true);
                buttonOverzichtGesloten.setTextColor(Color.LTGRAY);
                dividerOverzichtGesloten.setBackgroundColor(Color.TRANSPARENT);
                break;
            case GESLOTEN:
                // Button Openstaand
                buttonOverzichtOpenstaand.setEnabled(true);
                buttonOverzichtOpenstaand.setTextColor(Color.LTGRAY);
                dividerOverzichtOpenstaand.setBackgroundColor(Color.TRANSPARENT);
                // Button InBehandeling
                buttonOverzichtInBehandeling.setEnabled(true);
                buttonOverzichtInBehandeling.setTextColor(Color.LTGRAY);
                dividerOverzichtInBehandeling.setBackgroundColor(Color.TRANSPARENT);
                // Button Gesloten
                buttonOverzichtGesloten.setEnabled(false);
                buttonOverzichtGesloten.setTextColor(Color.RED);
                dividerOverzichtGesloten.setBackgroundColor(Color.RED);
                break;
        }

    }

    /**
     * Initialiseert de recycler view met het overzicht van melding.
     *
     * @param meldingenInfo lijst met meldingeninfo.
     */
    private void initRecyclerView(List<MeldingInfo> meldingenInfo,
                                  List<GebruikerInfo> meldersInfo) {

        // Initialiseert de recycler view.
        RecyclerView mRecyclerView = findViewById(R.id.view_meldingen);

        // Initialiseert de layout manager.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //  Verbetert de performance.
        mRecyclerView.setHasFixedSize(true);

        // Initialiseert de adapter en geeft ArrayLijsten mee
        // Referenties naar RecyclerView en Adapter
        OverzichtMeldingenBehandelaarRecyclerViewAdapter mAdapter = new OverzichtMeldingenBehandelaarRecyclerViewAdapter(meldingenInfo, meldersInfo, meldingGroep.toString(), this);
        mRecyclerView.setAdapter(mAdapter);

    }

    /**
     * Geeft op basis van de meldinggroep en statussen de lijst met meldingen.
     *
     * @return lijst met meldingen.
     */
    private List<MeldingInfo> getMeldingen() {

        // Lijst met alle gevraagde meldingen.
        List<MeldingInfo> meldingenInfo = null;

        // Haal op basis van de meldinggroep de statussen van gevraagde meldingen.
        List<Status> statussen = getStatussen();

        // Haal op basis van de meldinggroep en lijst met statussen de lijst
        // met gevraagde meldingen.
        switch (meldingGroep) {
            case OPENSTAAND:
            case GESLOTEN:
                meldingenInfo = getMeldingenMelders(statussen);
                break;
            case INBEHANDELING:
                meldingenInfo = getMeldingenBehandelaar(statussen);
                break;
        }

        // Sorteer de meldingen.
        sortMeldingen(meldingenInfo);

        return meldingenInfo;
    }

    /**
     * Geeft op basis van de meldinggroep de lijst met statussen.
     *
     * @return lijst met statussen.
     */
    private List<Status> getStatussen() {

        // Lijst met statussen waaraan de status van de melding voor deze groep moet voldoen.
        List<Status> statussen = new ArrayList<>();

        // Bepaal de lijst met statussen op basis van de melding groep.
        switch (meldingGroep) {
            case OPENSTAAND:
                statussen.add(Status.NIEUW);
                break;
            case INBEHANDELING:
                statussen.add(Status.HEROPEND);
                statussen.add(Status.INBEHANDELING);
                break;
            case GESLOTEN:
                statussen.add(Status.OPGELOST);
                statussen.add(Status.GESLOTEN);
                break;
            default:
                statussen.add(Status.NIEUW);
                break;
        }

        return statussen;
    }

    /**
     * Geeft op basis van de meldinggroep en statussen de lijst met meldingen van melders.
     *
     * @return lijst met meldingen.
     */
    private List<MeldingInfo> getMeldingenMelders(List<Status> statussen) {

        // Lijst met alle gevraagde meldingen.
        List<MeldingInfo> meldingenInfo;

        // Lijst met alle melders.
        List<GebruikerInfo> gebruikersInfo = gebruikerBeheer.geefAlleMelders();

        // Lijst met alle uid's van melders.
        List<String> uidMelders = new ArrayList<>();
        for(GebruikerInfo gebruikerInfo: gebruikersInfo) {
            String uidMelder = gebruikerInfo.getUid();
            uidMelders.add(uidMelder);
        }

        // Haal de lijst met alle meldingen van de gegeven melders en statussen.
        meldingenInfo = meldingBeheer.getMeldingenMelders(uidMelders, statussen);

        return meldingenInfo;
    }

    /**
     * Geeft op basis van de meldinggroep en statussen de lijst met meldingen van de
     * behandelaar.
     *
     * @return lijst met meldingen.
     */
    private List<MeldingInfo> getMeldingenBehandelaar(List<Status> statussen) {

        // Lijst met alle gevraagde meldingen.
        List<MeldingInfo> meldingenInfo;

        // Alleen de uid van de aangemelde gebruiker, zijnde de behandelaar.
        GebruikerInfo gebruikerInfo = gebruikerBeheer.haalAangemeldeGebruiker();
        String uidBehandelaar = gebruikerInfo.getUid();

        // Lijst met alle uid's van behandelaars.
        List<String> uidBehandelaars = new ArrayList<>();
        uidBehandelaars.add(uidBehandelaar);

        // Haal de lijst met alle meldingen van de gegeven behandelaars en statussen.
        meldingenInfo = meldingBeheer.getMeldingenBehandelaars(uidBehandelaars, statussen);

        return meldingenInfo;
    }

    /**
     * Sorteer de meldingen op basis van de meldinggroep.
     *
     * @param meldingenInfo Meldingen die moeten worden gesorteerd.
     */
    private void sortMeldingen(List<MeldingInfo> meldingenInfo) {

        // Comparator voor meldingen.
        Comparator<MeldingInfo> meldingInfoComparator;

        // Bepaal op basis van de melding groep de comparator.
        switch (meldingGroep) {
            case OPENSTAAND:
                meldingInfoComparator = new MeldingInfoBehandelaarOpenstaandComparator();
                break;
            case INBEHANDELING:
                meldingInfoComparator = new MeldingInfoBehandelaarInBehandelingComparator();
                break;
            case GESLOTEN:
                meldingInfoComparator = new MeldingInfoBehandelaarGeslotenComparator();
                break;
            default:
                return;
        }

        // Sorteer de meldingen met de gegeven comparator.
        Collections.sort(meldingenInfo, meldingInfoComparator);

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
