package nl.ou.applabdemo.domain;

import java.util.ArrayList;
import java.util.List;

import nl.ou.applabdemo.R;
import nl.ou.applabdemo.util.AppContext;
import nl.ou.applabdemo.util.MeldAppException;

import static nl.ou.applabdemo.util.Date.getDateTimeStamp;

/**
 * Klasse verantwoordelijk voor het beheer van de meldingen.
 */
@SuppressWarnings("SpellCheckingInspection")
public class MeldingBeheer {

    private List<Melding> meldingen;

    /**
     * Constructor voor Meldingbeheer op basis van een lijst met meldingen.
     *
     * @param meldingen lijst met meldingen
     */
    public MeldingBeheer(List<Melding> meldingen) {

        this.meldingen = meldingen;
    }

     /**
     * Haalt een lijst met meldingen op basis van de lijst met uid's van melders en de lijst
     * van statussen.
     *
     * @param uidMelders Lijst met uid's van melders waaraan de melder van een melding
     *                        moet voldoen.
     * @param statussen Lijst met statussen waaraan de status van een melding moet voldoen.
     * @return Lijst met meldingInfo
     */
    public List<MeldingInfo> getMeldingenMelders(List<String> uidMelders, List<Status> statussen) {

        return getMeldingenGebruiker(uidMelders, Rol.MELDER, statussen);
    }

    /**
     * Haalt een lijst met meldingen op basis van de lijst met uid's van behandelaars en de lijst
     * van statussen.
     *
     * @param uidBehandelaars Lijst met uid's van behandelaars waaraan de behandelaar van een melding
     *                        moet voldoen.
     * @param statussen Lijst met statussen waaraan de status van een melding moet voldoen.
     * @return Lijst met meldingInfo
     */
    public List<MeldingInfo> getMeldingenBehandelaars(List<String> uidBehandelaars, List<Status> statussen) {

        return getMeldingenGebruiker(uidBehandelaars, Rol.BEHANDELAAR, statussen);
    }

    /**
     * Voegt een aanvulling achteraan aan de inhoud van de melding toe. Hierbij wordt ook een timestamp opgenomen.
     *
     * @param inhoud De inhoud van de melding.
     * @param aanvulling de aanvulling aan de inhoud.
     * @return de nieuwe inhoud van de melding.
     */
    public String addAanvullingAanInhoud(String inhoud, String aanvulling) throws MeldAppException {

        String inhoudMetAanvulling = inhoud;

        if (aanvulling == null || aanvulling.isEmpty()) {
            throw new MeldAppException(AppContext.getContext().getResources().getString(R.string.meldingBeheer_add_aanvulling));
        }

        // Voeg de aanvulling achteraan aan de inhoud toe.
        // Plaats een lege regel achteraan in de inhoud.
        inhoudMetAanvulling = inhoudMetAanvulling +
                System.getProperty("line.separator") +
                System.getProperty("line.separator");

        // Voeg een tijdstempel toe.
        inhoudMetAanvulling = inhoudMetAanvulling +
                "Aanvulling " +
                getDateTimeStamp() +
                System.getProperty("line.separator");

        // Voeg de aanvulling zelf toe.
        inhoudMetAanvulling = inhoudMetAanvulling +
                aanvulling.trim();

        return inhoudMetAanvulling;
    }

    /**
     * Haalt een lijst met meldingen voor de gegeven gebruiker identificatie, rol en lijst met statussen.
     *
     * @param uidGebruikers De lijst met uid's van gebruikers waaraan of de melder of de behandelaar van
     *                      de melding moet voldoen.
     * @param rol De rol melder of behandelaar.
     * @param statussen De lijst met statussen waaraan de status van de melding moet voldoen.
     * @return Lijst met meldingInfo.
     */
    private List<MeldingInfo> getMeldingenGebruiker(List<String> uidGebruikers, Rol rol, List<Status> statussen) {

        List<MeldingInfo> meldingenInfo = new ArrayList<>();

        // Doorloop alle meldingen en verzamel hieruit de meldingen met de juiste gebruiker en status.
        for(Melding melding: meldingen) {

            for(String uidGebruiker: uidGebruikers) {

                String uidGebruikerMelding;

                switch (rol) {
                    case MELDER:
                        uidGebruikerMelding = melding.getUidGebruikerMelder();
                        if (uidGebruikerMelding != null && uidGebruikerMelding.equals(uidGebruiker)) {
                            geldigeStatusMelding(meldingenInfo, melding, statussen);
                        }
                        break;
                    case BEHANDELAAR:
                        uidGebruikerMelding = melding.getUidGebruikerBehandelaar();
                        if (uidGebruikerMelding != null && uidGebruikerMelding.equals(uidGebruiker)) {
                            geldigeStatusMelding(meldingenInfo, melding, statussen);
                        }
                        break;
                }

            }

        }
        return meldingenInfo;
    }

    /**
     * Retourneert de melding met de gegeven gebruiker uid.
     *
     * @return melding info
     */
    public MeldingInfo getMelding(String uidMelding) {

        MeldingInfo meldingInfo = null;

        // Doorloop alle meldingen en haal hieruit de melding met de uid van de melding.
        for(Melding melding: meldingen) {

            // Komen de uidMelding overeen.
            if(melding.getUidMelding().equals(uidMelding)) {
                // uidMeldingen zijn gelijk.
                meldingInfo = getMeldingInfo(melding);
                break;
             }
        }

        return meldingInfo;
    }

    /**
     * Verifieert of status van de melding gelijk is en voegt deze toe aan lijst meldingenInfo
     *
     * @param meldingenInfo lijst met meldingInfo's
     * @param melding de Melding
     * @param statussen lijst met Statussen
     */
    private void geldigeStatusMelding(List<MeldingInfo> meldingenInfo, Melding melding, List<Status> statussen) {

        // Doorloop alle statussen en selecteer de melding als de status overeenkomt.
        for(Status status: statussen) {
            if(melding.getStatus().equals(status)){
                // Statussen zijn gelijk. Voeg melding aan groep met meldingen toe.
                MeldingInfo meldingInfo = getMeldingInfo(melding);
                meldingenInfo.add(meldingInfo);
            }
        }
    }

    /**
     * Plaatst de inhoud van melding in meldinginfo en retourneert deze.
     *
     * @param melding de melding die in meldinginfo wordt overgenomen.
     * @return de melding info met gegevens uit melding.
     */
    private MeldingInfo getMeldingInfo(Melding melding){

        MeldingInfo meldingInfo = null;

        if (melding != null)
        meldingInfo = new MeldingInfo(melding);

        return meldingInfo;
    }

}
