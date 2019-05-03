package nl.ou.applabdemo.domain;

/**
 * Enum ten behoeve van het vastleggen van de status van een melding
 */
public enum Status {

    OPEN {
        public String toString() {
            return "OPEN";
        }
    },
    NIEUW {
        public String toString() {
            return "NIEUW";
        }
    },
    HEROPEND {
        public String toString() {
            return "HEROPEND";
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
    },
    ONBEKEND {
        public String toString() {
            return "ONBEKEND";
        }
    }
}
