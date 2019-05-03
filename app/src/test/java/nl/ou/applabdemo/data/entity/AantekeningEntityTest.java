package nl.ou.applabdemo.data.entity;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
@SuppressWarnings("SpellCheckingInspection")
public class AantekeningEntityTest {
    AantekeningEntity   e1,e2;

    @Before
    public void setUp() throws Exception {
        e1 = new AantekeningEntity("12345","abcd","201809130900","The quick brown fox");
        e2 = new AantekeningEntity();
    }

    @Test
    public void getAantekeningId() {
        assertEquals("12345",e1.getAantekeningId());
    }

    @Test
    public void setAantekeningId() {
        e2.setAantekeningId("99999");
        assertEquals("99999",e2.getAantekeningId());
    }

    @Test
    public void getMeldingId() {
        assertEquals("abcd",e1.getMeldingId());
    }

    @Test
    public void setMeldingId() {
        e2.setMeldingId("8888");
        assertEquals("8888",e2.getMeldingId());
    }

    @Test
    public void getDatumTijd() {
        assertEquals("201809130900",e1.getDatumTijd());
    }

    @Test
    public void setDatumTijd() {
        e2.setDatumTijd("201809130901");
        assertEquals("201809130901",e2.getDatumTijd());
    }

    @Test
    public void getTekst() {
        assertEquals("The quick brown fox", e1.getTekst());
    }

    @Test
    public void setTekst() {
        e2.setTekst("The lazy dog");
        assertEquals("The lazy dog",e2.getTekst());
    }
}