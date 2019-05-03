package nl.ou.applabdemo.util;

/**
 * Verantwoordelijk voor het beheer van de excepties van de
 */
public class MeldAppException extends Exception {

    private static final long serialVersionUID = 1L;

    public MeldAppException() {
        super();
    }

    public MeldAppException(String s) {
        super(s);
    }

}
