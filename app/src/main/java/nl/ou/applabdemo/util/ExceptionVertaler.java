package nl.ou.applabdemo.util;

import nl.ou.applabdemo.R;

/**
 * Retourneert vertaalde exception
 * Opmerking: Benodigd om vertaling van excepties in Melding en Gebruiker te krijgen.
 * De excepties worden namelijk in de junit test gecontroleerd.
 */
public class ExceptionVertaler {

    public static String vertaalException(String message) {

        String vertaaldException;

        switch (message) {
            case "Achternaam moet ingevuld zijn.":
                vertaaldException = AppContext.getContext().getResources().getString(R.string.gebruiker_achternaam_exceptie);
                break;
            case "Organisatie-waarde niet toegestaan.":
                vertaaldException = AppContext.getContext().getResources().getString(R.string.gebruiker_organisatie_exceptie);
                break;
            case "Uid moet ingevuld zijn.":
                vertaaldException = AppContext.getContext().getResources().getString(R.string.gebruiker_uid_exceptie);
                break;
            case "Alleen getallen en - zijn toegestaan":
                vertaaldException = AppContext.getContext().getResources().getString(R.string.gebruiker_getallen_exceptie);
                break;
            case "Geef een geldig telefoonnummer op bestaande uit 10 cijfers":
                vertaaldException = AppContext.getContext().getResources().getString(R.string.gebruiker_telefoon_exceptie);
                break;
            case "Onderwerp mag niet leeg zijn.":
                vertaaldException = AppContext.getContext().getResources().getString(R.string.melding_onderwerp_exceptie);
                break;
            case "Inhoud mag niet leeg zijn.":
                vertaaldException = AppContext.getContext().getResources().getString(R.string.melding_inhoud_exceptie);
                break;
            case "Datum en tijd mogen mogen niet leeg zijn.":
                vertaaldException = AppContext.getContext().getResources().getString(R.string.melding_datum_en_tijd_exceptie);
                break;
            case "Identificatie van de melder mag niet leeg zijn.":
                vertaaldException = AppContext.getContext().getResources().getString(R.string.melding_identificatie_exceptie);
                break;
            case "Status mag niet leeg zijn.":
                vertaaldException = AppContext.getContext().getResources().getString(R.string.melding_status_exceptie);
                break;
            default:
                vertaaldException = AppContext.getContext().getResources().getString(R.string.default_exceptie);
                break;
        }
        return vertaaldException;
    }
}
