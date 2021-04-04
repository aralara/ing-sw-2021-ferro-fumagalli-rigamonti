package it.polimi.ingsw.model.boards;

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
}