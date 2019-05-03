package nl.ou.applabdemo.util;

import nl.ou.applabdemo.R;

/**
 * Retourneert de juiste vertaling van de status uit de string resources
 */
public class StatusVertaler {

    public static String vertaalStatus(String status) {

        String vertaaldeStatus;

        switch (status) {
            case "Nieuw":
                vertaaldeStatus = AppContext.getContext().getResources().getString(R.string.status_nieuw);
                break;
            case "Heropend":
                vertaaldeStatus = AppContext.getContext().getResources().getString(R.string.status_heropend);
                break;
            case "In behandeling":
                vertaaldeStatus = AppContext.getContext().getResources().getString(R.string.status_in_behandeling);
                break;
            case "Opgelost":
                vertaaldeStatus = AppContext.getContext().getResources().getString(R.string.status_opgelost);
                break;
            case "Gesloten":
                vertaaldeStatus = AppContext.getContext().getResources().getString(R.string.status_gesloten);
                break;
            default:
                vertaaldeStatus = AppContext.getContext().getResources().getString(R.string.status_onbekend);
                break;
        }
        return vertaaldeStatus;
    }
}
