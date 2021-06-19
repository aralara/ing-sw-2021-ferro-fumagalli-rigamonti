package it.polimi.ingsw.server.model.cards.card;

import it.polimi.ingsw.server.model.FileNames;
import it.polimi.ingsw.server.model.boards.LorenzoBoard;
import it.polimi.ingsw.server.model.games.SingleGame;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests methods of LorenzoFaith class
 */
public class LorenzoFaithTest {

    /**
     * Tests Lorenzo card activation
     */
    @Test
    public void testActivateLorenzo() {
        SingleGame game = new SingleGame();
        LorenzoBoard lorenzoBoard = new LorenzoBoard(game);
        lorenzoBoard.initLorenzoDeck(FileNames.LORENZO_DEV_FILE.value(), FileNames.LORENZO_FAITH_FILE.value());
        LorenzoFaith lorenzoFaith = new LorenzoFaith(1, true, 1);
        assertEquals("Lorenzo gains 1 faith and shuffles his deck\n",
                lorenzoFaith.toString());
        assertEquals(0, lorenzoBoard.getFaith());
        lorenzoFaith.activateLorenzo(lorenzoBoard);
        assertEquals(1, lorenzoBoard.getFaith());
        lorenzoFaith = new LorenzoFaith(1, false, 2);
        assertEquals("Lorenzo gains 2 faith\n",
                lorenzoFaith.toString());
        lorenzoFaith.activateLorenzo(lorenzoBoard);
        assertEquals(3, lorenzoBoard.getFaith());
    }
}