package nl.ou.applabdemo.data.repository;


import com.google.firebase.database.DatabaseReference;

import nl.ou.applabdemo.data.entity.GebruikerEntity;
import nl.ou.applabdemo.data.mapper.GebruikerMapper;
import nl.ou.applabdemo.domain.Gebruiker;
import nl.ou.applabdemo.domain.GebruikerInfo;
import nl.ou.applabdemo.util.MeldAppException;

/**
 * Deze klasse beheert instanties van de repository voor de domein klasse gebruiker
 *
 */
@SuppressWarnings("SpellCheckingInspection")
public class GebruikerRepository extends FirebaseDatabaseRepository<Gebruiker> {

    /**
     * Constructor waarbij de mapper voor Gebruikers wordt aangemaakt en vastgelegd
     */
    public GebruikerRepository() {
        super(new GebruikerMapper());
    }

    /**
     * Methode voor het toevoegen van een domein instantie Melding in de database
     * @param gebruikerInfo - melding die aan de database moet worden toegevoegd
     */
    public void voegGebruikerToeAanDatabase(GebruikerInfo gebruikerInfo) throws MeldAppException{

        Gebruiker gebruiker = new Gebruiker (
                gebruikerInfo.getVoornaam(),
                gebruikerInfo.getAchternaam(),
                gebruikerInfo.getOrganisatie(),
                gebruikerInfo.getUid(),
                gebruikerInfo.getGebruikerStatus(),
                gebruikerInfo.getTelefoonnummer(),
                gebruikerInfo.getRol(),
                gebruikerInfo.getNotification(),
                gebruikerInfo.getDeviceToken());

        GebruikerEntity gebruikerEntity = GebruikerMapper.demap(gebruiker);

        /*
        In oorspronkelijke opzet werd "sleutelwaarde" node automatisch toegekend
        In gewijzigde opzet wordt uid als sleutelwaarde gekozen, dit om autorisatie in database
        te vereenvoudigen
         */
//        databaseReference.push().setValue(gebruikerEntity);
        databaseReference.child(gebruikerEntity.getUid()).setValue(gebruikerEntity);
    }

    /**
     * Deze methode wijzigt de notificatie van een gegeven gebruiker
     * @param uid - uid van gegeven gebruiker
     * @param notification - nieuwe waarde notification
     * @throws MeldAppException - als uid null waarde heeft of als gebruiker met uid niet voorkomt
     */
    public void wijzigNotificatieGebruiker(String uid, boolean notification) throws MeldAppException {

        if ( uid == null) {
            throw new MeldAppException("Uid gebruiker is niet gevuld");
        }
        DatabaseReference reference = databaseReference.child(uid).getRef();

        if ( reference == null) {
            throw new MeldAppException("Gebruiker komt niet voor");
        }
        reference.child("notification").setValue(notification);

    }
    /**
     * Deze methode wijzigt het deviceToken van een gegeven gebruiker
     * @param uid - uid van gegeven gebruiker
     * @param deviceToken - nieuwe waarde deviceToken
     * @throws MeldAppException - als uid null waarde heeft of als gebruiker met uid niet voorkomt
     */
    public void updateDeviceTokenGebruiker(String uid, String deviceToken) throws MeldAppException {

        if ( uid == null) {
            throw new MeldAppException("Uid gebruiker is niet gevuld");
        }
        DatabaseReference reference = databaseReference.child(uid).getRef();
        if ( reference == null) {
            throw new MeldAppException("Gebruiker komt niet voor");
        }
        reference.child("deviceToken").setValue(deviceToken);

    }


    /**
     * Definitie van de naam van de collectie voor Gebruiker in de database
     * @return - de naam van die collectie
     */
    @Override
    protected String getRootNode() {
        return "gebruikers";
    }
}
