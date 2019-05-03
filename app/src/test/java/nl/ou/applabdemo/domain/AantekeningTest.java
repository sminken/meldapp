package nl.ou.applabdemo.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
@SuppressWarnings("SpellCheckingInspection")
public class AantekeningTest {
    Aantekening a,b;

    @Before
    public void setUp() throws Exception {
        a = new Aantekening("00000","ABcd","201809121800","The quick brown fox");
        b = new Aantekening("","");
    }

    @Test
    public void getAantekeningId() {
        assertEquals("goed","00000",a.getAantekeningId());
    }

    @Test
    public void setAantekeningId() {
        b.setAantekeningId("11111");
        assertEquals("11111",b.getAantekeningId());
    }

    @Test
    public void getMeldingId() {
        assertEquals("ABcd",a.getMeldingId());
    }

    @Test
    public void setMeldingId() {
        b.setMeldingId("12345");
        assertEquals("12345",b.getMeldingId());
    }

    @Test
    public void getDatumTijd() {
        assertEquals("201809121800",a.getDatumTijd());
    }

    @Test
    public void setDatumTijd() {
        b.setDatumTijd("201809121800");
        assertEquals("201809121800",b.getDatumTijd());
    }

    @Test
    public void getTekst() {
        assertEquals("The quick brown fox",a.getTekst());
    }

    @Test
    public void setTekst() {
        b.setTekst("The lazy dog");
        assertEquals("The lazy dog",b.getTekst());
    }
}