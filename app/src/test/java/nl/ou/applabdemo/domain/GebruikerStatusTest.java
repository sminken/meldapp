package nl.ou.applabdemo.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
@SuppressWarnings("SpellCheckingInspection")
public class GebruikerStatusTest {
    private GebruikerStatus statusNIEUW;
    private GebruikerStatus statusACTIEF;
    private GebruikerStatus statusINACTIEF;

    @Before
    public void setUp() {
        statusNIEUW = GebruikerStatus.NIEUW;
        statusACTIEF = GebruikerStatus.ACTIEF;
        statusINACTIEF = GebruikerStatus.INACTIEF;
    }

    @Test
    public void toStringTest() {
        assertEquals(statusNIEUW.toString(),"NIEUW");
        assertEquals(statusACTIEF.toString(),"ACTIEF");
        assertEquals(statusINACTIEF.toString(),"INACTIEF");


    }

    @After
    public void tearDown() {
        statusINACTIEF = null;
        statusACTIEF = null;
        statusNIEUW = null;
    }
}