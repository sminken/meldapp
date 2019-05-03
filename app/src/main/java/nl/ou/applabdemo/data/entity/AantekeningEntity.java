package nl.ou.applabdemo.data.entity;

/**
 * Klasse voor het beheren van een entiteit aantekening in de database.
 * Iedere instantie van deze klasse komt overeen met een entiteit aantekening in de collectie aantekeningen in de database.
 *
 */

public class AantekeningEntity {

    private String aantekeningId;
    private String meldingId;
    private String datumTijd;
    private String tekst;

    public AantekeningEntity(){};

    /**
     * Attributen van een entiteit Aantekening in de database.
     * @param aantekeningId Unieke identificatie van de aantekening.
     * @param meldingId Unieke identificatie van de melding waartoe de aamtekening behoort.
     * @param datumTijd Datum en tijdstip waarop de aantekening is geplaatst.
     * @param tekst De tekst van de aantekening.
     */
    public AantekeningEntity(String aantekeningId, String meldingId, String datumTijd, String tekst) {
        this.aantekeningId = aantekeningId;
        this.meldingId = meldingId;
        this.datumTijd = datumTijd;
        this.tekst = tekst;
    }

    public String getAantekeningId() {
        return aantekeningId;
    }

    public void setAantekeningId(String aantekeningId) {
        this.aantekeningId = aantekeningId;
    }

    public String getMeldingId() {
        return meldingId;
    }

    public void setMeldingId(String meldingId) {
        this.meldingId = meldingId;
    }

    public String getDatumTijd() {
        return datumTijd;
    }

    public void setDatumTijd(String datumTijd) {
        this.datumTijd = datumTijd;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }
}
