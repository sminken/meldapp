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
import android.support.design.widget.FloatingActionButton;
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
import nl.ou.applabdemo.comparator.MeldingInfoMelderGeslotenComparator;
import nl.ou.applabdemo.comparator.MeldingInfoMelderOpenstaandComparator;
import nl.ou.applabdemo.comparator.MeldingInfoMelderOpgelostComparator;
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
public class OverzichtMeldingenMelderActivity extends AppCompatActivity {

    // logging
    private static final String TAG = "OverzichtMelder";

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
        setContentView(R.layout.activity_overzicht_meldingen_melder);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overzicht_meldingen_melder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.item_afmelden:
                menuItemAfmelden();
                return true;
            case R.id.item_instellingen:
                menuItemInstellingen();
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
        Intent intent = new Intent(OverzichtMeldingenMelderActivity.this, MainActivity.class);
        intent.putExtra("gebruiker_afmelden", "true");
        startActivity(intent);
        finishAffinity();
    }

    /**
     * Afhandelen van het menuitem instellingen.
     */
    private void menuItemInstellingen() {
        // Afmelden via mainactivity.
        Intent intent = new Intent(OverzichtMeldingenMelderActivity.this, InstellingenMelderActivity.class);
        startActivity(intent);
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

                    initButtons();
                    initRecyclerView(meldingenInfo);
                }
            }
        });
    }

    /**
     * Initialiseert de buttons van de view.
     */
    private void initButtons() {
        // Knop voor aanmaken van een nieuwe melding.
        final FloatingActionButton buttonMeldingNieuw = findViewById(R.id.button_nieuw);
        //noinspection Convert2Lambda
        buttonMeldingNieuw.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Verwijder observers.
                removeObservers();

                // Start activity nieuwe melding.
                Intent intent = new Intent(OverzichtMeldingenMelderActivity.this, MeldingNieuwActivity.class);
                intent.putExtra("melding_groep", meldingGroep.name());
                startActivity(intent);
                finish();
            }
        });

        // Knop voor overzicht met alle openstaande meldingen.
        final Button buttonOverzichtOpenstaand = findViewById(R.id.button_openstaand);
        //noinspection Convert2Lambda
        buttonOverzichtOpenstaand.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Verwijder observers.
                removeObservers();

                // Start activity overzicht openstaande meldingen.
                Intent intent = new Intent(OverzichtMeldingenMelderActivity.this, OverzichtMeldingenMelderActivity.class);
                intent.putExtra("melding_groep", MeldingGroep.OPENSTAAND.toString());
                startActivity(intent);
                finish();
            }
        });

        // Knop voor overzicht met alle opgeloste meldingen.
        final Button buttonOverzichtOpgelost = findViewById(R.id.button_opgelost);
        //noinspection Convert2Lambda
        buttonOverzichtOpgelost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Verwijder observers.
                removeObservers();

                // Start activity overzicht opgeloste meldingen.
                Intent intent = new Intent(OverzichtMeldingenMelderActivity.this, OverzichtMeldingenMelderActivity.class);
                intent.putExtra("melding_groep", MeldingGroep.OPGELOST.toString());
                startActivity(intent);
                finish();
            }
        });

        // Knop voor overzicht met alle gesloten meldingen.
        final Button buttonOverzichtGesloten = findViewById(R.id.button_gesloten);
        //noinspection Convert2Lambda
        buttonOverzichtGesloten.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Verwijder observers.
                removeObservers();

                // Start activity overzicht gesloten meldingen.
                Intent intent = new Intent(OverzichtMeldingenMelderActivity.this, OverzichtMeldingenMelderActivity.class);
                intent.putExtra("melding_groep", MeldingGroep.GESLOTEN.toString());
                startActivity(intent);
                finish();
            }
        });

        final View dividerOverzichtOpenstaand = findViewById(R.id.divider_overzicht_openstaand);
        final View dividerOverzichtOpgelost = findViewById(R.id.divider_overzicht_opgelost);
        final View dividerOverzichtGesloten = findViewById(R.id.divider_overzicht_gesloten);

        // Definieer de settings van de knop voor nieuwe melding.
        buttonMeldingNieuw.setBackgroundColor(Color.RED);

        // Definieer de settings van de knoppen openstaande, opgeloste en gesloten meldingen.
        // Activeer en deactiveer op basis van de geselecteerde meldinggroep de overeenkomstige knoppen.
        switch (meldingGroep) {
            case OPENSTAAND:
                // Button Openstaand
                buttonOverzichtOpenstaand.setEnabled(false);
                buttonOverzichtOpenstaand.setTextColor(Color.RED);
                dividerOverzichtOpenstaand.setBackgroundColor(Color.RED);
                // Button Opgelost
                buttonOverzichtOpgelost.setEnabled(true);
                buttonOverzichtOpgelost.setTextColor(Color.LTGRAY);
                dividerOverzichtOpgelost.setBackgroundColor(Color.TRANSPARENT);
                // Button Gesloten
                buttonOverzichtGesloten.setEnabled(true);
                buttonOverzichtGesloten.setTextColor(Color.LTGRAY);
                dividerOverzichtGesloten.setBackgroundColor(Color.TRANSPARENT);
                break;
            case OPGELOST:
                // Button Openstaand
                buttonOverzichtOpenstaand.setEnabled(true);
                buttonOverzichtOpenstaand.setTextColor(Color.LTGRAY);
                dividerOverzichtOpenstaand.setBackgroundColor(Color.TRANSPARENT);
                // Button Opgelost
                buttonOverzichtOpgelost.setEnabled(false);
                buttonOverzichtOpgelost.setTextColor(Color.RED);
                dividerOverzichtOpgelost.setBackgroundColor(Color.RED);
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
                // Button Opgelost
                buttonOverzichtOpgelost.setEnabled(true);
                buttonOverzichtOpgelost.setTextColor(Color.LTGRAY);
                dividerOverzichtOpgelost.setBackgroundColor(Color.TRANSPARENT);
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
    private void initRecyclerView(List<MeldingInfo> meldingenInfo) {

        // Initialiseert de recycler view.
        RecyclerView mRecyclerView = findViewById(R.id.view_meldingen);

        // Initialiseert de layout manager.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //  Verbetert de performance.
        mRecyclerView.setHasFixedSize(true);

        // Initialiseert de adapter en geeft ArrayLijsten mee
        // Referenties naar RecyclerView en Adapter
        OverzichtMeldingenMelderRecyclerViewAdapter mAdapter = new OverzichtMeldingenMelderRecyclerViewAdapter(meldingenInfo, meldingGroep.toString(), this);
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
            case OPGELOST:
            case GESLOTEN:
                meldingenInfo = getMeldingenMelder(statussen);
                break;
        }

        // Sorteer de meldingen.
        sortMeldingen(meldingenInfo);

        return meldingenInfo;
    }

    /**
     * Geeft op basis van de statusgroep een lijst met statussen.
     *
     * @return lijst met statussen
     */
    private List<Status> getStatussen() {

        // Lijst met statussen waaraan de status van de melding voor deze groep moet voldoen.
        List<Status> statussen = new ArrayList<>();

        // Verzamel de groep met meldingen.
        switch (meldingGroep) {
            case OPENSTAAND:
                statussen.add(Status.HEROPEND);
                statussen.add(Status.NIEUW);
                statussen.add(Status.INBEHANDELING);
                break;
            case OPGELOST:
                statussen.add(Status.OPGELOST);
                break;
            case GESLOTEN:
                statussen.add(Status.GESLOTEN);
                break;
            default:
                statussen.add(Status.NIEUW);
                break;
        }

        return statussen;
    }


    /**
     * Geeft op basis van de meldinggroep en statussen de lijst met meldingen van de
     * melder.
     *
     * @return lijst met meldingen.
     */
    private List<MeldingInfo> getMeldingenMelder(List<Status> statussen) {

        // Lijst met alle gevraagde meldingen.
        List<MeldingInfo> meldingenInfo;

        // Alleen de uid van de aangemelde gebruiker, zijnde de melder.
        GebruikerInfo gebruikerInfo = gebruikerBeheer.haalAangemeldeGebruiker();
        String uidMelder = gebruikerInfo.getUid();

        // Lijst met alle uid's van behandelaars.
        List<String> uidMelders = new ArrayList<>();
        uidMelders.add(uidMelder);

        // Haal de lijst met alle meldingen van de gegeven melders en statussen.
        meldingenInfo = meldingBeheer.getMeldingenMelders(uidMelders, statussen);

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
                meldingInfoComparator = new MeldingInfoMelderOpenstaandComparator();
                break;
            case OPGELOST:
                meldingInfoComparator = new MeldingInfoMelderOpgelostComparator();
                break;
            case GESLOTEN:
                meldingInfoComparator = new MeldingInfoMelderGeslotenComparator();
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
