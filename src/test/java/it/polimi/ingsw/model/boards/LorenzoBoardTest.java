package it.polimi.ingsw.model.boards;

import it.polimi.ingsw.model.games.SingleGame;
import org.junit.Test;

import static org.junit.Assert.*;

public class LorenzoBoardTest {

    @Test
    public void testGetFaith() {
    }

    @Test
    public void testAddFaith() {
        SingleGame game = new SingleGame("singleUser");
        LorenzoBoard lorenzoBoard = new LorenzoBoard(game);

        assertEquals(0, lorenzoBoard.getFaith());
        lorenzoBoard.addFaith(1);
        assertEquals(1, lorenzoBoard.getFaith());
        lorenzoBoard.addFaith(3);
        assertEquals(4, lorenzoBoard.getFaith());
    }

    @Test
    public void testTakeDevCard() {
    }

    @Test
    public void testInitLorenzoDeck() {
    }

    @Test
    public void testPickLorenzoCard() {
    }

    @Test
    public void testRefreshDeck() {
    }
}