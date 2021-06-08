package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.server.model.FileNames;
import it.polimi.ingsw.server.model.cards.card.LorenzoCard;
import it.polimi.ingsw.server.model.cards.card.LorenzoDev;
import it.polimi.ingsw.server.model.cards.card.LorenzoFaith;
import it.polimi.ingsw.server.model.games.SingleGame;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LorenzoBoardTest {

    /**
     * Tests if Lorenzo's faith is added correctly
     */
    @Test
    public void testAddFaith() {
        SingleGame game = new SingleGame();
        LorenzoBoard lorenzoBoard = new LorenzoBoard(game);

        assertEquals(0, lorenzoBoard.getFaith());
        lorenzoBoard.addFaith(1);
        assertEquals(1, lorenzoBoard.getFaith());
        lorenzoBoard.addFaith(3);
        assertEquals(4, lorenzoBoard.getFaith());
    }

    /**
     * Tests Lorenzo's cards are loaded from file correctly and if the picked card is not null
     */
    @Test
    public void testInitLorenzoDeckAndPickLorenzoCard() {
        SingleGame game = new SingleGame();
        LorenzoBoard lorenzoBoard = new LorenzoBoard(game);
        lorenzoBoard.initLorenzoDeck(FileNames.LORENZO_DEV_FILE.value(), FileNames.LORENZO_FAITH_FILE.value());
        assertEquals(7, lorenzoBoard.getDeck().size());
        int[] lorenzoCards = new int[2];

        for(int i=0; i<7; i++){
            LorenzoCard picked = lorenzoBoard.pickLorenzoCard();
            assertEquals(7, lorenzoBoard.getDeck().size());
            assertNotNull(picked);
            if(picked instanceof LorenzoDev)
                lorenzoCards[0]++;
            else if(picked instanceof LorenzoFaith)
                lorenzoCards[1]++;
        }
        assertEquals(4, lorenzoCards[0]);
        assertEquals(3, lorenzoCards[1]);
    }

    /**
     * Tests if the size of the Lorenzo's cards are always the same before and after the shuffle action
     */
    @Test
    public void testRefreshDeck() {
        SingleGame game = new SingleGame();
        LorenzoBoard lorenzoBoard = new LorenzoBoard(game);
        lorenzoBoard.initLorenzoDeck(FileNames.LORENZO_DEV_FILE.value(), FileNames.LORENZO_FAITH_FILE.value());
        List<LorenzoCard> preRefresh = new ArrayList<>();
        List<LorenzoCard> postRefresh = new ArrayList<>();
        int numOfCards = 7;
        while (preRefresh.size() < numOfCards){
            LorenzoCard picked = lorenzoBoard.pickLorenzoCard();
            assertNotNull(picked);
            preRefresh.add(picked);
        }
        lorenzoBoard.refreshDeck();
        while (postRefresh.size() < numOfCards){
            LorenzoCard picked = lorenzoBoard.pickLorenzoCard();
            assertNotNull(picked);
            postRefresh.add(picked);
        }

        assertTrue(preRefresh.containsAll(postRefresh));
        assertTrue(postRefresh.containsAll(preRefresh));
    }
}