package nl.ou.applabdemo.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.ou.applabdemo.comparator.MeldingInfoBehandelaarGeslotenComparator;
import nl.ou.applabdemo.comparator.MeldingInfoBehandelaarInBehandelingComparator;
import nl.ou.applabdemo.comparator.MeldingInfoBehandelaarOpenstaandComparator;
import nl.ou.applabdemo.comparator.MeldingInfoMelderGeslotenComparator;
import nl.ou.applabdemo.comparator.MeldingInfoMelderOpenstaandComparator;
import nl.ou.applabdemo.comparator.MeldingInfoMelderOpgelostComparator;

import static org.junit.Assert.*;

public class MeldingBeheerTest {

    private List<Melding> meldingen = null;
    private int meldingUid = 0;

    private MeldingBeheer meldingBeheer;

    @Before
    public void setUp() throws Exception {

        // Creëer meldingenlijst.
        meldingen = new ArrayList<Melding>();

        // Voeg meldingen aan meldingenlijst toe, voor iedere status één.
        addMeldingen();

        // Voeg nogmaals meldingen aan meldingenlijst toe, voor iedere status één.
        addMeldingen();

        // Creëer meldingenbeheer met alle meldingen.
        meldingBeheer = new MeldingBeheer(meldingen);

    }

    @After
    public void tearDown() {

        meldingen = null;
        meldingUid = 0;
    }

    @Test
    public void getMeldingenMeldersGroepOpenstaand() {

        // Lijst met uids van melders
        List<String> uidMelders = new ArrayList<String>();
        uidMelders.add("uidGM");

        // Lijst met statussen.
        List<Status> statussen = new ArrayList<Status>();
        statussen.add(Status.NIEUW);
        statussen.add(Status.INBEHANDELING);
        statussen.add(Status.HEROPEND);

        // Haal meldingen voor deze melder en status.
        List<MeldingInfo> meldingenInfo = meldingBeheer.getMeldingenMelders(uidMelders, statussen);

        // Comparator voor meldingen.
        Comparator<MeldingInfo> meldingInfoComparator = new MeldingInfoMelderOpenstaandComparator();
        // Sorteer de meldingen met de gegeven comparator.
        Collections.sort(meldingenInfo, meldingInfoComparator);

        // Haal voor de controle één voor één de verwachte meldingen uit de meldingenlijst. Haal de melding
        // uit de meldinglijst op basis van de meldingUid, en voeg deze toe aan een lijst met meldingen voor
        // de controle.
        List<Melding> meldingenControle = new ArrayList<Melding>();
        searchMelding(meldingenControle,"9");
        searchMelding(meldingenControle,"2");
        searchMelding(meldingenControle,"10");
        searchMelding(meldingenControle,"3");
        searchMelding(meldingenControle,"8");
        searchMelding(meldingenControle,"1");

        // Vergelijk de inhoud van beide lijsten met meldingen.
        compareMeldingen(meldingenInfo, meldingenControle);
    }

    @Test
    public void getMeldingenMeldersGroepOpgelost() {

        // Lijst met uids van melders
        List<String> uidMelders = new ArrayList<String>();
        uidMelders.add("uidGM");

        // Lijst met statussen.
        List<Status> statussen = new ArrayList<Status>();
        statussen.add(Status.OPGELOST);

        // Haal meldingen voor deze melder en status.
        List<MeldingInfo> meldingenInfo = meldingBeheer.getMeldingenMelders(uidMelders, statussen);

        // Comparator voor meldingen.
        Comparator<MeldingInfo> meldingInfoComparator = new MeldingInfoMelderOpgelostComparator();
        // Sorteer de meldingen met de gegeven comparator.
        Collections.sort(meldingenInfo, meldingInfoComparator);

        // Haal voor de controle één voor één de verwachte meldingen uit de meldingenlijst. Haal de melding
        // uit de meldinglijst op basis van de meldingUid, en voeg deze toe aan een lijst met meldingen voor
        // de controle.
        List<Melding> meldingenControle = new ArrayList<Melding>();
        searchMelding(meldingenControle,"11");
        searchMelding(meldingenControle,"4");

        // Vergelijk de inhoud van beide lijsten met meldingen.
        compareMeldingen(meldingenInfo, meldingenControle);
    }

    @Test
    public void getMeldingenMeldersGroepGesloten() {

        // Lijst met uids van melders
        List<String> uidMelders = new ArrayList<String>();
        uidMelders.add("uidGM");

        // Lijst met statussen.
        List<Status> statussen = new ArrayList<Status>();
        statussen.add(Status.GESLOTEN);

        // Haal meldingen voor alle melders en statussen.
        List<MeldingInfo> meldingenInfo = meldingBeheer.getMeldingenMelders(uidMelders, statussen);

        // Comparator voor meldingen.
        Comparator<MeldingInfo> meldingInfoComparator = new MeldingInfoMelderGeslotenComparator();
        // Sorteer de meldingen met de gegeven comparator.
        Collections.sort(meldingenInfo, meldingInfoComparator);

        // Haal voor de controle één voor één de verwachte meldingen uit de meldingenlijst. Haal de melding
        // uit de meldinglijst op basis van de meldingUid, en voeg deze toe aan een lijst met meldingen voor
        // de controle.
        List<Melding> meldingenControle = new ArrayList<Melding>();
        searchMelding(meldingenControle,"12");
        searchMelding(meldingenControle,"5");

        // Vergelijk de inhoud van beide lijsten met meldingen.
        compareMeldingen(meldingenInfo, meldingenControle);
    }

    @Test
    public void getMeldingenBehandelaarsInBehandeling() {

         // Lijst met uids van behandelaar
        List<String> uidBehandelaars = new ArrayList<String>();
        uidBehandelaars.add("uidGB");

        // Lijst met statussen.
        List<Status> statussen = new ArrayList<Status>();
        statussen.add(Status.INBEHANDELING);
        statussen.add(Status.HEROPEND);

        // Haal meldingen voor deze behandelaar en statussen.
        List<MeldingInfo> meldingenInfo = meldingBeheer.getMeldingenBehandelaars(uidBehandelaars, statussen);

        // Comparator voor meldingen.
        Comparator<MeldingInfo> meldingInfoComparator = new MeldingInfoBehandelaarInBehandelingComparator();
        // Sorteer de meldingen met de gegeven comparator.
        Collections.sort(meldingenInfo, meldingInfoComparator);

        // Haal voor de controle één voor één de verwachte meldingen uit de meldingenlijst. Haal de melding
        // uit de meldinglijst op basis van de meldingUid, en voeg deze toe aan een lijst met meldingen voor
        // de controle.
        List<Melding> meldingenControle = new ArrayList<Melding>();
        searchMelding(meldingenControle,"2");
        searchMelding(meldingenControle,"9");
        searchMelding(meldingenControle,"3");
        searchMelding(meldingenControle,"10");

        // Vergelijk de inhoud van beide lijsten met meldingen.
        compareMeldingen(meldingenInfo, meldingenControle);
    }

    @Test
    public void getMeldingenBehandelaarsOpenstaand() {

        Melding melding = null;

        // Lijst met uids van alle melders
        List<String> uidMelders = new ArrayList<String>();
        uidMelders.add("uidGM");

        // Lijst met statussen.
        List<Status> statussen = new ArrayList<Status>();
        statussen.add(Status.NIEUW);

        // Haal meldingen voor deze melder en status.
        List<MeldingInfo> meldingenInfo = meldingBeheer.getMeldingenMelders(uidMelders, statussen);

        // Comparator voor meldingen.
        Comparator<MeldingInfo> meldingInfoComparator = new MeldingInfoBehandelaarOpenstaandComparator();
        // Sorteer de meldingen met de gegeven comparator.
        Collections.sort(meldingenInfo, meldingInfoComparator);

        // Haal voor de controle één voor één de verwachte meldingen uit de meldingenlijst. Haal de melding
        // uit de meldinglijst op basis van de meldingUid, en voeg deze toe aan een lijst met meldingen voor
        // de controle.
        List<Melding> meldingenControle = new ArrayList<Melding>();
        searchMelding(meldingenControle,"1");
        searchMelding(meldingenControle,"8");

        // Vergelijk de inhoud van beide lijsten met meldingen.
        compareMeldingen(meldingenInfo, meldingenControle);
    }

    @Test
    public void getMeldingenBehandelaarsGesloten() {

        Melding melding = null;

        // Lijst met uids van alle melders
        List<String> uidMelders = new ArrayList<String>();
        uidMelders.add("uidGM");

        // Lijst met statussen.
        List<Status> statussen = new ArrayList<Status>();
        statussen.add(Status.GESLOTEN);
        statussen.add(Status.OPGELOST);

        // Haal meldingen voor deze melder en status.
        List<MeldingInfo> meldingenInfo = meldingBeheer.getMeldingenMelders(uidMelders, statussen);

        // Comparator voor meldingen.
        Comparator<MeldingInfo> meldingInfoComparator = new MeldingInfoBehandelaarGeslotenComparator();
        // Sorteer de meldingen met de gegeven comparator.
        Collections.sort(meldingenInfo, meldingInfoComparator);

        // Haal voor de controle één voor één de verwachte meldingen uit de meldingenlijst. Haal de melding
        // uit de meldinglijst op basis van de meldingUid, en voeg deze toe aan een lijst met meldingen voor
        // de controle.
        List<Melding> meldingenControle = new ArrayList<Melding>();
        searchMelding(meldingenControle,"11");
        searchMelding(meldingenControle,"4");
        searchMelding(meldingenControle,"12");
        searchMelding(meldingenControle,"5");

        // Vergelijk de inhoud van beide lijsten met meldingen.
        compareMeldingen(meldingenInfo, meldingenControle);
    }

    @Test
    public void addAanvullingAanInhoud() {


    }

    @Test
    public void getMelding() {

        List<MeldingInfo> meldingenInfo = new ArrayList<MeldingInfo>();
        MeldingInfo meldingInfo = meldingBeheer.getMelding("2");
        meldingenInfo.add(meldingInfo);

        List<Melding> meldingenControle = new ArrayList<Melding>();
        searchMelding(meldingenControle,"2");

        // Vergelijk de inhoud van beide lijsten met meldingen.
        compareMeldingen(meldingenInfo, meldingenControle);
    }

    /**
     * Vergelijkt de inhoud van individuele elementen van iedere regel in twee lijsten, als het aantal
     * regels in de beide lijsten exact gelijk zijn. Een ongelijk aantal regels is altijd fout.
     * @param meldingenInfo Meldingen ter controle.
     * @param meldingenControle  Meldingen waarmee gecontroleerd moet worden.
     */
    private void compareMeldingen(List<MeldingInfo> meldingenInfo, List<Melding> meldingenControle) {
        // Aantal regels gelijk.
        assertEquals(meldingenInfo.size(), meldingenControle.size());

        // Het aantal regels in beide lijsten is gelijk. Vergelijk nu per regel de inhoud van de
        // individuele elementen.
        for(int i=0; i < meldingenInfo.size(); i++) {
            MeldingInfo meldingInfo = meldingenInfo.get(i);
            Melding melding = meldingenControle.get(i);
            compareMelding(meldingInfo,melding);
        }

    }

    /**
     * Vergelijkt de individuele elementen in de MeldingInfo en de Melding.
     * @param meldingInfo De meldingInfo
     * @param melding de melding
     */
    private void compareMelding(MeldingInfo meldingInfo, Melding melding) {
        assertEquals(meldingInfo.getUidMelding(), melding.getUidMelding());
        assertEquals(meldingInfo.getUidGebruikerMelder(), melding.getUidGebruikerMelder());
        assertEquals(meldingInfo.getUidGebruikerBehandelaar(), melding.getUidGebruikerBehandelaar());
        assertEquals(meldingInfo.getOnderwerp(), melding.getOnderwerp());
        assertEquals(meldingInfo.getInhoud(), melding.getInhoud());
        assertEquals(meldingInfo.getDatumTijd(), melding.getDatumTijd());
        assertEquals(meldingInfo.getDatum(), getDatum(melding.getDatumTijd()));
        assertEquals(meldingInfo.getTijd(), getTijd(melding.getDatumTijd()));
        assertEquals(meldingInfo.getStatus(), melding.getStatus());
        String MeldingInfoStatusTekst = meldingInfo.getStatusTekst().toLowerCase().replaceAll(" ", "");
        String MeldingStatusTekst = melding.getStatus().toString().toLowerCase();
        assertEquals(MeldingInfoStatusTekst, MeldingStatusTekst);
        assertEquals(meldingInfo.getNotificatieVerstuurd(), melding.getNotificatieVerstuurd());
    }

    /**
     * Zoekt de gevraagde melding uit de lijst met meldingen, voegt deze toe aan een lijst
     * met meldingen voor controle.
     * @param meldingenControle lijst met meldingen die moeten worden gecontroleerd.
     * @param uidMelding De uid van de gezochte melding in de lijst met meldingen.
     */
    private void searchMelding(List<Melding> meldingenControle, String uidMelding) {

        for(Melding melding: meldingen) {
            if (melding.getUidMelding().equals(uidMelding)) {
                meldingenControle.add(melding);
            }
        }
    }

    private void addMeldingen() throws Exception {

        for(Status status: Status.values()) {

            String uidMelding = "" + meldingUid;
            String uidGebruikerMelder = "uidGM";
            String uidGebruikerBehandelaar = "uidGB";
            String onderwerp = "Ond" + meldingUid;
            String inhoud = "uidInh" + meldingUid;
            String datumTijd = getDatumTijd(meldingUid);
            boolean notificatieVerstuurd = false;

            // Creeer een nieuwe melding met deze status.
            Melding melding = new Melding(uidMelding, uidGebruikerMelder, uidGebruikerBehandelaar,
                    onderwerp, inhoud, datumTijd, status.toString(), notificatieVerstuurd);

            // Voeg de melding toe aan meldingen.
            meldingen.add(melding);

            meldingUid++;

        }
    }

    /**
     * Geeft de huidige datum en tijd, en telt daarbij het aantal gegeven minuten bij op.
     * @param minuten Aantal minuten
     * @return Huidige datum en tijd, plus het aantal gegeven minuten.
     */
    private String getDatumTijd(int minuten) {
        // Get calendar
        Calendar calendar = Calendar.getInstance();

        // Voeg het aantal minuten toe aan de huidige tijd.
        calendar.add(Calendar.MINUTE, minuten);
        Date date = calendar.getTime();

        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
    }

    private String getDatum(String datumTijd) {
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).parse(datumTijd);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            return dateFormat.format(date);
        }
        catch (ParseException e) {
            return null;
        }
    }

    private String getTijd(String datumTijd) {

        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).parse(datumTijd);
            DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
            return dateFormat.format(date);
        }
        catch (ParseException e) {
            return null;
        }
    }

}