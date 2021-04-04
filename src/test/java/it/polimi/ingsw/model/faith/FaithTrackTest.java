package it.polimi.ingsw.model.faith;

import org.junit.Test;

import static org.junit.Assert.*;

public class FaithTrackTest {

    @Test
    public void testLoadTrack() {
        FaithTrack faithTrack = new FaithTrack();

        faithTrack.loadTrack();
        assertEquals(3, faithTrack.getVaticanReports().size());
        assertEquals(5, faithTrack.getVaticanReports().get(0).getMin());
        assertEquals(12, faithTrack.getVaticanReports().get(1).getMin());
        assertEquals(19, faithTrack.getVaticanReports().get(2).getMin());
        assertEquals(8, faithTrack.getVaticanReports().get(0).getMax());
        assertEquals(16, faithTrack.getVaticanReports().get(1).getMax());
        assertEquals(24, faithTrack.getVaticanReports().get(2).getMax());
        assertEquals(2, faithTrack.getVaticanReports().get(0).getPopeValue());
        assertEquals(3, faithTrack.getVaticanReports().get(1).getPopeValue());
        assertEquals(4, faithTrack.getVaticanReports().get(2).getPopeValue());

        assertEquals(8, faithTrack.getFaithSpaces().size());
        assertEquals(1, faithTrack.getFaithSpaces().get(0).getVP());
        assertEquals(3, faithTrack.getFaithSpaces().get(0).getPosition());
        assertEquals(2, faithTrack.getFaithSpaces().get(1).getVP());
        assertEquals(6, faithTrack.getFaithSpaces().get(1).getPosition());
        assertEquals(4, faithTrack.getFaithSpaces().get(2).getVP());
        assertEquals(9, faithTrack.getFaithSpaces().get(2).getPosition());
        assertEquals(6, faithTrack.getFaithSpaces().get(3).getVP());
        assertEquals(12, faithTrack.getFaithSpaces().get(3).getPosition());
        assertEquals(9, faithTrack.getFaithSpaces().get(4).getVP());
        assertEquals(15, faithTrack.getFaithSpaces().get(4).getPosition());
        assertEquals(12, faithTrack.getFaithSpaces().get(5).getVP());
        assertEquals(18, faithTrack.getFaithSpaces().get(5).getPosition());
        assertEquals(16, faithTrack.getFaithSpaces().get(6).getVP());
        assertEquals(21, faithTrack.getFaithSpaces().get(6).getPosition());
        assertEquals(20, faithTrack.getFaithSpaces().get(7).getVP());
        assertEquals(24, faithTrack.getFaithSpaces().get(7).getPosition());
    }

    @Test
    public void testCheckReportActivation() {
    }

    @Test
    public void testCheckPlayerReportPosition() {
    }

    @Test
    public void testCalculateVP() {
        //should call calculateFaithSpaceVP and calculateReportVP
    }
}