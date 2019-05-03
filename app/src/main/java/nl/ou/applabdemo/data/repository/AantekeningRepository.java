package nl.ou.applabdemo.data.repository;

import nl.ou.applabdemo.data.entity.AantekeningEntity;
import nl.ou.applabdemo.data.mapper.AantekeningMapper;
import nl.ou.applabdemo.domain.Aantekening;

/**
 * Klasse voor het beheer van de Aantekeningen in de database.
 *
 */
public class AantekeningRepository extends FirebaseDatabaseRepository<Aantekening> {

    /**
     * Constructor voor de mapper van Aantekeningen.
     */
    public AantekeningRepository() {
        super(new AantekeningMapper());
    }

    /**
     * Methode voor het toevoegen van een Aantekening in de database.
     * @param aantekening - de Aantekening die aan de database moet worden toegevoegd.
     */
    public void voegAantekeningToeAanDatabase(Aantekening aantekening) {
        AantekeningEntity aantekeningEntity = AantekeningMapper.demap(aantekening);
        databaseReference.push().setValue(aantekeningEntity);
    }

    /**
     * Definitie van de naam van de collectie voor Aantekeningen in de database.
     * @return - de naam van die collectie
     */
    @Override
    protected String getRootNode() {
        return "aantekeningen";
    }
}
