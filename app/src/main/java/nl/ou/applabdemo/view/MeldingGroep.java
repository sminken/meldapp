package nl.ou.applabdemo.view;

/**
 * Enum ten behoeve van een groep meldingen op het overzicht met meldingen, die is samengesteld uit een
 * meldingen met een bepaalde status.
 */
public enum MeldingGroep {

    OPENSTAAND {
        public String toString() {
            return "OPENSTAAND";
        }
    },
    INBEHANDELING {
        public String toString() {
            return "INBEHANDELING";
        }
    },
    OPGELOST {
        public String toString() {
            return "OPGELOST";
        }
    },
    GESLOTEN {
        public String toString() {
            return "GESLOTEN";
        }
    }
}
