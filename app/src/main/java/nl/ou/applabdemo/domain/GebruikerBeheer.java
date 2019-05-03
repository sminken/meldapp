package nl.ou.applabdemo.domain;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Deze klasse beheert instanties van de domein klasse GebruikerBeheer
 *
 */

@SuppressWarnings("SpellCheckingInspection")
public class GebruikerBeheer {

    private final List<Gebruiker> gebruikerLijst;

    /**
     * Constructor voor GebruikerBeheer
     *
     * @param gebruikers - lijst met gebruikers
     */
    public GebruikerBeheer(List<Gebruiker> gebruikers) {
        this.gebruikerLijst = gebruikers;
    }

    /**
     * Deze methode haal de gebruiker op uit de lijst van gebruikers aan de hand van de uid
     * @param uid - uid waarvan de gebruiker moet worden opgehaald
     * @return - gebruikerInfo indien de uid voorkomt in de lijst met gebruikers, anders null
     */
    public GebruikerInfo haalGebruikerOp(String uid) {

        if (uid == null || gebruikerLijst == null) {
            return null;
        }

        for (Gebruiker gebruiker : gebruikerLijst) {
            if (gebruiker != null && gebruiker.getUid() != null &&
                    gebruiker.getUid().equals(uid)) {
               return gebruiker.geefGebruikerInfoInstantie();
            }
        }
        return null;
    }

    /**
     * Deze methode haal de gegevens op van de, in Firebase database, aangemelde gebruiker
     * @return  gebruikerInfo indien de Firebase database uid van deze gebruiker voorkomt
     *          in de lijst met gebruikers, anders null
     */
    public GebruikerInfo haalAangemeldeGebruiker (){

        // Initialiseer Firebase Auth
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if ( mFirebaseUser == null ) {
            return null;
        }

        return haalGebruikerOp(mFirebaseUser.getUid());
    }

    /**
     * Deze methode geeft een lijst van alle Melders terug, ongeacht hun status
     * @return Lijst met alle melders, leeg indien er geen melders voorkomen
     */
    public List<GebruikerInfo> geefAlleMelders () {
        @SuppressWarnings("Convert2Diamond") List<GebruikerInfo> resultaatLijst = new ArrayList<GebruikerInfo>();

        if (gebruikerLijst == null) {
            return resultaatLijst;
        }

        for (Gebruiker gebruiker : gebruikerLijst) {
            if (gebruiker != null && gebruiker.getRol() == Rol.MELDER) {
                resultaatLijst.add(gebruiker.geefGebruikerInfoInstantie());           }
        }

        return resultaatLijst;
    }
}
