package nl.ou.applabdemo.domain;

import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Klasse voor het beheer van Melding Info.
 */
@SuppressWarnings("SpellCheckingInspection")
public class MeldingInfo {

    private final String uidMelding;
    private final String uidGebruikerMelder;
    private final String uidGebruikerBehandelaar;
    private final String onderwerp;
    private final String inhoud;
    private final Status status;
    private final String datumTijd;
    private final boolean notificatieVerstuurd;

    public MeldingInfo(Melding melding) {

        this.uidMelding = melding.getUidMelding();
        this.uidGebruikerMelder = melding.getUidGebruikerMelder();
        this.uidGebruikerBehandelaar = melding.getUidGebruikerBehandelaar();
        this.onderwerp = melding.getOnderwerp();
        this.inhoud = melding.getInhoud();
        this.status = melding.getStatus();
        this.datumTijd = melding.getDatumTijd();
        this.notificatieVerstuurd = melding.getNotificatieVerstuurd();
    }

    public String getUidMelding() {
        return uidMelding;
    }

    public String getUidGebruikerMelder() {
        return uidGebruikerMelder;
    }

    public String getUidGebruikerBehandelaar() { return uidGebruikerBehandelaar;
    }

    public String getOnderwerp() {
        return onderwerp;
    }

    public String getInhoud() {
        return inhoud;
    }

    public Status getStatus() {
        return status;
    }

    public boolean getNotificatieVerstuurd() {
        return notificatieVerstuurd;
    }

    public String getStatusTekst() {

        String statusTekst;

        switch(status) {
            case NIEUW:
                statusTekst = "Nieuw";
                break;
            case HEROPEND:
                statusTekst = "Heropend";
                break;
            case INBEHANDELING:
                statusTekst = "In behandeling";
                break;
            case OPGELOST:
                statusTekst = "Opgelost";
                break;
            case GESLOTEN:
                statusTekst = "Gesloten";
                break;
            default:
                statusTekst = "Onbekend";
                break;
        }

        return statusTekst;
    }

    public String getDatumTijd() {
        return datumTijd;
    }

    public String getDatum() {
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).parse(datumTijd);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            return dateFormat.format(date);
        }
        catch (ParseException e) {
            return null;
        }
    }

    public String getTijd() {

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
