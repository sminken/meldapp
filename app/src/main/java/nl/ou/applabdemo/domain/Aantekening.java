package nl.ou.applabdemo.domain;

import android.support.annotation.NonNull;

import nl.ou.applabdemo.util.Date;
import nl.ou.applabdemo.util.Id;

/**
 * Klasse voor het domein aantekening.
 *
 */
@SuppressWarnings("SpellCheckingInspection")
public class Aantekening {

    private String aantekeningId;
    private String meldingId;
    private String datumTijd;
    private String tekst;

    /**
     * Constructor voor een nieuw domein Aantekening.
     * @param meldingId Unieke identificatie van de melding waartoe de aamtekening behoort.
     * @param tekst De tekst van de aantekening.
     */
    public Aantekening(String meldingId, String tekst) {
        this.aantekeningId = Id.getGeneratedId();
        this.meldingId = meldingId;
        this.datumTijd = Date.getDateTimeStamp();
        this.tekst = tekst;
    }

    /**
     * Constructor voor een volledig domein Aantekening.
     * @param aantekeningId Unieke identificatie van de aantekening.
     * @param meldingId Unieke identificatie van de melding waartoe de aamtekening behoort.
     * @param datumTijd Datum en tijdstip waarop de aantekening is geplaatst.
     * @param tekst De tekst van de aantekening.
     */
    public Aantekening(String aantekeningId, String meldingId, String datumTijd, String tekst) {
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
