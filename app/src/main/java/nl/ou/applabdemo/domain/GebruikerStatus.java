package nl.ou.applabdemo.domain;

/**
 * Deze klasse beheert de lijst met statussen die een gebruiker kan hebben
 *
 */
@SuppressWarnings("SpellCheckingInspection")
public enum GebruikerStatus {

    /*
    NIEUW: Gebruiker heeft zich aangemeld in de meldApp
           maar is nog niet gevalideerd door de administrator

           Een dergelijke gebruiker kan de app pas gaan gebruiken na validatie
     */
    NIEUW {
        public String toString() {
            return "NIEUW";
        }
    },

    /*
    ACTIEF: Een, door de administrator, gevalideerde en dus actieve gebruiker van de meldApp

            Een dergelijke gebruiker kan de functionaliteit van de app gebruiken
     */
    ACTIEF {
        public String toString() {
            return "ACTIEF";
        }
    },


    /*
    INACTIEF: Een gevalideerde gebruiker van de meldApp die door de administrator is gedeactiveerd

              Een dergelijke gebruiker kan de functionaliteit van de app pas weer gaan gebruiken
              nadat deze weer actief gemaakt is
     */
    INACTIEF {
        public String toString() {
            return "INACTIEF";
        }
    }
}
