package nl.ou.applabdemo.domain;
@SuppressWarnings("SpellCheckingInspection")
public enum Rol {

    ADMIN {
        public String toString() {

            return "ADMIN";
        }
    },
    MELDER {
        public String toString() {

            return "MELDER";
        }
    },
    BEHANDELAAR {
        public String toString() {

            return "BEHANDELAAR";
        }
    }
}
