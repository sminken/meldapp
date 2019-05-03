package nl.ou.applabdemo.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RolTest {
    private Rol rolAdmin;
    private Rol rolMelder;
    private Rol rolBehandelaar;

    @Before
    public void setUp()  {
        rolAdmin = Rol.ADMIN;
        rolBehandelaar = Rol.BEHANDELAAR;
        rolMelder = Rol.MELDER;
    }


    @Test
    public void toStringTest() {
        assertEquals(rolAdmin.toString(), "ADMIN");
        assertEquals(rolMelder.toString(), "MELDER");
        assertEquals(rolBehandelaar.toString(), "BEHANDELAAR");
    }



    @After
    public void tearDown() {
        rolMelder = null;
        rolBehandelaar = null;
        rolAdmin = null;
    }
}