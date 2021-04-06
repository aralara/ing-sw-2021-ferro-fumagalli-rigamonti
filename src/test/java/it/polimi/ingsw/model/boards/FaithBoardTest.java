package it.polimi.ingsw.model.boards;

import it.polimi.ingsw.model.FileNames;
import it.polimi.ingsw.model.faith.FaithTrack;
import org.junit.Test;

import static org.junit.Assert.*;

public class FaithBoardTest {

    @Test
    public void testAddFaith() {
        FaithBoard faithBoard = new FaithBoard();

        assertEquals(0, faithBoard.getFaith());
        faithBoard.addFaith(1);
        assertEquals(1, faithBoard.getFaith());
        faithBoard.addFaith(3);
        assertEquals(4, faithBoard.getFaith());
    }

    @Test
    public void testTurnCard() {
        FaithBoard faithBoard = new FaithBoard();
        for(boolean popeProgressionValue : faithBoard.getPopeProgression()){
            assertFalse(popeProgressionValue);
        }

        faithBoard.turnCard(0,true);
        assertTrue(faithBoard.getPopeProgression()[0]);
        assertFalse(faithBoard.getPopeProgression()[1]);
        assertFalse(faithBoard.getPopeProgression()[2]);

        faithBoard.turnCard(1,false);
        assertTrue(faithBoard.getPopeProgression()[0]);
        assertFalse(faithBoard.getPopeProgression()[1]);
        assertFalse(faithBoard.getPopeProgression()[2]);

        faithBoard.turnCard(2,true);
        assertTrue(faithBoard.getPopeProgression()[0]);
        assertFalse(faithBoard.getPopeProgression()[1]);
        assertTrue(faithBoard.getPopeProgression()[2]);
    }

    @Test
    public void testGetFaith() {
    }

    @Test
    public void testGetPopeProgression() {
    }

    @Test
    public void testCalculateVP() {
        FaithBoard faithBoard = new FaithBoard();
        FaithTrack faithTrack = new FaithTrack();
        faithTrack.loadTrack(FileNames.VATICAN_REPORT_FILE.value(), FileNames.FAITH_SPACE_FILE.value());

        assertEquals(0, faithBoard.calculateVP(faithTrack));
        faithBoard.addFaith(3);
        assertEquals(1, faithBoard.calculateVP(faithTrack));
        faithBoard.addFaith(1);
        assertEquals(1, faithBoard.calculateVP(faithTrack));
        faithBoard.addFaith(4);
        assertEquals(2, faithBoard.calculateVP(faithTrack));
        faithBoard.addFaith(1);
        assertEquals(4, faithBoard.calculateVP(faithTrack));
        faithBoard.turnCard(0, true);
        assertEquals(6, faithBoard.calculateVP(faithTrack));
        faithBoard.addFaith(9);
        faithBoard.turnCard(1, false);
        assertEquals(14, faithBoard.calculateVP(faithTrack));
        faithBoard.turnCard(1, true);
        assertEquals(17, faithBoard.calculateVP(faithTrack));
        faithBoard.addFaith(4);
        assertEquals(21, faithBoard.calculateVP(faithTrack));
        faithBoard.addFaith(3);
        faithBoard.turnCard(2, true);
        assertEquals(29, faithBoard.calculateVP(faithTrack));

    }
}