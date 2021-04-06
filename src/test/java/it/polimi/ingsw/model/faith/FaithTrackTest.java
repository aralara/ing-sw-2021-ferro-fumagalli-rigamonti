package it.polimi.ingsw.model.faith;

import it.polimi.ingsw.model.FileNames;
import org.junit.Test;

import static org.junit.Assert.*;

public class FaithTrackTest {

    @Test
    public void testLoadTrack() {
        FaithTrack faithTrack = new FaithTrack();

        faithTrack.loadTrack(FileNames.VATICAN_REPORT_FILE.value(), FileNames.FAITH_SPACE_FILE.value());
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
        FaithTrack faithTrackCell = new FaithTrack(); //to check the exact activation cell
        faithTrackCell.loadTrack(FileNames.VATICAN_REPORT_FILE.value(), FileNames.FAITH_SPACE_FILE.value());

        FaithTrack faithTrackBeyond = new FaithTrack(); //to check the activation beyond the cell
        faithTrackBeyond.loadTrack(FileNames.VATICAN_REPORT_FILE.value(), FileNames.FAITH_SPACE_FILE.value());

        assertEquals(-1, faithTrackCell.getLastReportTriggered());
        assertEquals(-1, faithTrackBeyond.getLastReportTriggered());

        //first report
        assertFalse(faithTrackCell.checkReportActivation(0));
        assertFalse(faithTrackBeyond.checkReportActivation(4));
        assertTrue(faithTrackCell.checkReportActivation(8));
        assertTrue(faithTrackBeyond.checkReportActivation(15));

        assertEquals(0, faithTrackCell.getLastReportTriggered());
        assertEquals(0, faithTrackBeyond.getLastReportTriggered());

        //second report
        assertFalse(faithTrackCell.checkReportActivation(8));
        assertFalse(faithTrackBeyond.checkReportActivation(15));
        assertTrue(faithTrackCell.checkReportActivation(16));
        assertTrue(faithTrackBeyond.checkReportActivation(23));

        assertEquals(1, faithTrackCell.getLastReportTriggered());
        assertEquals(1, faithTrackBeyond.getLastReportTriggered());

        //third report
        assertFalse(faithTrackCell.checkReportActivation(16));
        assertFalse(faithTrackBeyond.checkReportActivation(23));
        assertTrue(faithTrackCell.checkReportActivation(24));
        assertTrue(faithTrackBeyond.checkReportActivation(27)); //out of the faith track range, but still valid

        assertEquals(2, faithTrackCell.getLastReportTriggered());
        assertEquals(2, faithTrackBeyond.getLastReportTriggered());
    }

    @Test
    public void testCheckPlayerReportPosition() {
        FaithTrack faithTrack = new FaithTrack();
        faithTrack.loadTrack(FileNames.VATICAN_REPORT_FILE.value(), FileNames.FAITH_SPACE_FILE.value());

        faithTrack.checkReportActivation(8);
        assertFalse(faithTrack.checkPlayerReportPosition(4));
        assertTrue(faithTrack.checkPlayerReportPosition(5));
        assertTrue(faithTrack.checkPlayerReportPosition(8));
        assertTrue(faithTrack.checkPlayerReportPosition(11));

        faithTrack.checkReportActivation(16);
        assertFalse(faithTrack.checkPlayerReportPosition(4));
        assertFalse(faithTrack.checkPlayerReportPosition(5));
        assertFalse(faithTrack.checkPlayerReportPosition(8));
        assertFalse(faithTrack.checkPlayerReportPosition(11));
        assertTrue(faithTrack.checkPlayerReportPosition(12));
        assertTrue(faithTrack.checkPlayerReportPosition(16));
        assertTrue(faithTrack.checkPlayerReportPosition(18));

        faithTrack.checkReportActivation(24);
        assertFalse(faithTrack.checkPlayerReportPosition(4));
        assertFalse(faithTrack.checkPlayerReportPosition(5));
        assertFalse(faithTrack.checkPlayerReportPosition(8));
        assertFalse(faithTrack.checkPlayerReportPosition(11));
        assertFalse(faithTrack.checkPlayerReportPosition(12));
        assertFalse(faithTrack.checkPlayerReportPosition(16));
        assertFalse(faithTrack.checkPlayerReportPosition(18));
        assertTrue(faithTrack.checkPlayerReportPosition(19));
        assertTrue(faithTrack.checkPlayerReportPosition(24));
        assertTrue(faithTrack.checkPlayerReportPosition(25)); //out of the faith track range, but still valid
    }

    @Test
    public void testCalculateVP() {
        FaithTrack faithTrack = new FaithTrack();
        faithTrack.loadTrack(FileNames.VATICAN_REPORT_FILE.value(), FileNames.FAITH_SPACE_FILE.value());

        assertEquals(0, faithTrack.calculateVP(0, new boolean[] {false, false, false}));
        assertEquals(2, faithTrack.calculateVP(0, new boolean[] {true, false, false}));
        assertEquals(3, faithTrack.calculateVP(0, new boolean[] {false, true, false}));
        assertEquals(4, faithTrack.calculateVP(0, new boolean[] {false, false, true}));
        assertEquals(5, faithTrack.calculateVP(0, new boolean[] {true, true, false}));
        assertEquals(6, faithTrack.calculateVP(0, new boolean[] {true, false, true}));
        assertEquals(7, faithTrack.calculateVP(0, new boolean[] {false, true, true}));
        assertEquals(9, faithTrack.calculateVP(0, new boolean[] {true, true, true}));


        assertEquals(0, faithTrack.calculateVP(2, new boolean[] {false, false, false}));
        assertEquals(1, faithTrack.calculateVP(3, new boolean[] {false, false, false}));
        assertEquals(1, faithTrack.calculateVP(4, new boolean[] {false, false, false}));
        assertEquals(3, faithTrack.calculateVP(4, new boolean[] {true, false, false}));
        assertEquals(4, faithTrack.calculateVP(4, new boolean[] {false, true, false}));
        assertEquals(5, faithTrack.calculateVP(4, new boolean[] {false, false, true}));
        assertEquals(6, faithTrack.calculateVP(4, new boolean[] {true, true, false}));
        assertEquals(7, faithTrack.calculateVP(4, new boolean[] {true, false, true}));
        assertEquals(8, faithTrack.calculateVP(4, new boolean[] {false, true, true}));
        assertEquals(10, faithTrack.calculateVP(4, new boolean[] {true, true, true}));


        assertEquals(1, faithTrack.calculateVP(5, new boolean[] {false, false, false}));
        assertEquals(2, faithTrack.calculateVP(6, new boolean[] {false, false, false}));
        assertEquals(2, faithTrack.calculateVP(7, new boolean[] {false, false, false}));
        assertEquals(4, faithTrack.calculateVP(7, new boolean[] {true, false, false}));
        assertEquals(5, faithTrack.calculateVP(7, new boolean[] {false, true, false}));
        assertEquals(6, faithTrack.calculateVP(7, new boolean[] {false, false, true}));
        assertEquals(7, faithTrack.calculateVP(7, new boolean[] {true, true, false}));
        assertEquals(8, faithTrack.calculateVP(7, new boolean[] {true, false, true}));
        assertEquals(9, faithTrack.calculateVP(7, new boolean[] {false, true, true}));
        assertEquals(11, faithTrack.calculateVP(7, new boolean[] {true, true, true}));


        assertEquals(2, faithTrack.calculateVP(8, new boolean[] {false, false, false}));
        assertEquals(4, faithTrack.calculateVP(9, new boolean[] {false, false, false}));
        assertEquals(4, faithTrack.calculateVP(10, new boolean[] {false, false, false}));
        assertEquals(6, faithTrack.calculateVP(10, new boolean[] {true, false, false}));
        assertEquals(7, faithTrack.calculateVP(10, new boolean[] {false, true, false}));
        assertEquals(8, faithTrack.calculateVP(10, new boolean[] {false, false, true}));
        assertEquals(9, faithTrack.calculateVP(10, new boolean[] {true, true, false}));
        assertEquals(10, faithTrack.calculateVP(10, new boolean[] {true, false, true}));
        assertEquals(11, faithTrack.calculateVP(10, new boolean[] {false, true, true}));
        assertEquals(13, faithTrack.calculateVP(10, new boolean[] {true, true, true}));

        assertEquals(4, faithTrack.calculateVP(11, new boolean[] {false, false, false}));
        assertEquals(6, faithTrack.calculateVP(12, new boolean[] {false, false, false}));
        assertEquals(6, faithTrack.calculateVP(13, new boolean[] {false, false, false}));
        assertEquals(8, faithTrack.calculateVP(13, new boolean[] {true, false, false}));
        assertEquals(9, faithTrack.calculateVP(13, new boolean[] {false, true, false}));
        assertEquals(10, faithTrack.calculateVP(13, new boolean[] {false, false, true}));
        assertEquals(11, faithTrack.calculateVP(13, new boolean[] {true, true, false}));
        assertEquals(12, faithTrack.calculateVP(13, new boolean[] {true, false, true}));
        assertEquals(13, faithTrack.calculateVP(13, new boolean[] {false, true, true}));
        assertEquals(15, faithTrack.calculateVP(13, new boolean[] {true, true, true}));

        assertEquals(6, faithTrack.calculateVP(14, new boolean[] {false, false, false}));
        assertEquals(9, faithTrack.calculateVP(15, new boolean[] {false, false, false}));
        assertEquals(9, faithTrack.calculateVP(16, new boolean[] {false, false, false}));
        assertEquals(11, faithTrack.calculateVP(16, new boolean[] {true, false, false}));
        assertEquals(12, faithTrack.calculateVP(16, new boolean[] {false, true, false}));
        assertEquals(13, faithTrack.calculateVP(16, new boolean[] {false, false, true}));
        assertEquals(14, faithTrack.calculateVP(16, new boolean[] {true, true, false}));
        assertEquals(15, faithTrack.calculateVP(16, new boolean[] {true, false, true}));
        assertEquals(16, faithTrack.calculateVP(16, new boolean[] {false, true, true}));
        assertEquals(18, faithTrack.calculateVP(16, new boolean[] {true, true, true}));

        assertEquals(9, faithTrack.calculateVP(17, new boolean[] {false, false, false}));
        assertEquals(12, faithTrack.calculateVP(18, new boolean[] {false, false, false}));
        assertEquals(12, faithTrack.calculateVP(19, new boolean[] {false, false, false}));
        assertEquals(14, faithTrack.calculateVP(19, new boolean[] {true, false, false}));
        assertEquals(15, faithTrack.calculateVP(19, new boolean[] {false, true, false}));
        assertEquals(16, faithTrack.calculateVP(19, new boolean[] {false, false, true}));
        assertEquals(17, faithTrack.calculateVP(19, new boolean[] {true, true, false}));
        assertEquals(18, faithTrack.calculateVP(19, new boolean[] {true, false, true}));
        assertEquals(19, faithTrack.calculateVP(19, new boolean[] {false, true, true}));
        assertEquals(21, faithTrack.calculateVP(19, new boolean[] {true, true, true}));

        assertEquals(12, faithTrack.calculateVP(20, new boolean[] {false, false, false}));
        assertEquals(16, faithTrack.calculateVP(21, new boolean[] {false, false, false}));
        assertEquals(16, faithTrack.calculateVP(22, new boolean[] {false, false, false}));
        assertEquals(18, faithTrack.calculateVP(22, new boolean[] {true, false, false}));
        assertEquals(19, faithTrack.calculateVP(22, new boolean[] {false, true, false}));
        assertEquals(20, faithTrack.calculateVP(22, new boolean[] {false, false, true}));
        assertEquals(21, faithTrack.calculateVP(22, new boolean[] {true, true, false}));
        assertEquals(22, faithTrack.calculateVP(22, new boolean[] {true, false, true}));
        assertEquals(23, faithTrack.calculateVP(22, new boolean[] {false, true, true}));
        assertEquals(25, faithTrack.calculateVP(22, new boolean[] {true, true, true}));

        assertEquals(16, faithTrack.calculateVP(23, new boolean[] {false, false, false}));
        assertEquals(20, faithTrack.calculateVP(24, new boolean[] {false, false, false}));
        assertEquals(22, faithTrack.calculateVP(24, new boolean[] {true, false, false}));
        assertEquals(23, faithTrack.calculateVP(24, new boolean[] {false, true, false}));
        assertEquals(24, faithTrack.calculateVP(24, new boolean[] {false, false, true}));
        assertEquals(25, faithTrack.calculateVP(24, new boolean[] {true, true, false}));
        assertEquals(26, faithTrack.calculateVP(24, new boolean[] {true, false, true}));
        assertEquals(27, faithTrack.calculateVP(24, new boolean[] {false, true, true}));
        assertEquals(29, faithTrack.calculateVP(24, new boolean[] {true, true, true}));
    }
}