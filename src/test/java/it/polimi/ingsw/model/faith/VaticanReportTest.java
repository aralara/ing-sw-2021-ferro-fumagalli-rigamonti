package it.polimi.ingsw.model.faith;

import org.junit.Test;

import static org.junit.Assert.*;

public class VaticanReportTest {

    @Test
    public void testInRange() {
        VaticanReport[] vaticanReports = new VaticanReport[3];
        vaticanReports[0] = new VaticanReport(5,8,2);
        vaticanReports[1] = new VaticanReport(12,16,3);
        vaticanReports[2] = new VaticanReport(19,24,4);

        assertFalse(vaticanReports[0].inRange(4));
        assertFalse(vaticanReports[0].inRange(0));
        assertTrue(vaticanReports[0].inRange(5));
        assertTrue(vaticanReports[0].inRange(8));
        assertTrue(vaticanReports[0].inRange(11));
        assertTrue(vaticanReports[0].inRange(12));

        assertFalse(vaticanReports[1].inRange(5));
        assertFalse(vaticanReports[1].inRange(11));
        assertTrue(vaticanReports[1].inRange(12));
        assertTrue(vaticanReports[1].inRange(16));
        assertTrue(vaticanReports[1].inRange(18));
        assertTrue(vaticanReports[1].inRange(19));

        assertFalse(vaticanReports[2].inRange(5));
        assertFalse(vaticanReports[2].inRange(12));
        assertFalse(vaticanReports[2].inRange(18));
        assertTrue(vaticanReports[2].inRange(19));
        assertTrue(vaticanReports[2].inRange(24));
        assertTrue(vaticanReports[2].inRange(29)); //out of the faith track range, but still valid
    }

    @Test
    public void testSetTriggered() {
        VaticanReport[] vaticanReports = new VaticanReport[3];
        vaticanReports[0] = new VaticanReport(5,8,2);
        vaticanReports[1] = new VaticanReport(12,16,3);
        vaticanReports[2] = new VaticanReport(19,24,4);

        for(VaticanReport vaticanReport : vaticanReports)
            assertFalse(vaticanReport.getTriggered());

        vaticanReports[0].setTriggered(true);
        assertTrue(vaticanReports[0].getTriggered());
        assertFalse(vaticanReports[1].getTriggered());
        assertFalse(vaticanReports[2].getTriggered());

        vaticanReports[1].setTriggered(true);
        assertTrue(vaticanReports[0].getTriggered());
        assertTrue(vaticanReports[1].getTriggered());
        assertFalse(vaticanReports[2].getTriggered());

        vaticanReports[0].setTriggered(false);
        vaticanReports[2].setTriggered(true);
        assertFalse(vaticanReports[0].getTriggered());
        assertTrue(vaticanReports[1].getTriggered());
        assertTrue(vaticanReports[2].getTriggered());
    }
}