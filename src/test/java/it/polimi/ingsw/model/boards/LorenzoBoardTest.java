package it.polimi.ingsw.model.boards;

import it.polimi.ingsw.model.FileNames;
import it.polimi.ingsw.model.cards.card.LorenzoCard;
import it.polimi.ingsw.model.games.SingleGame;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
    public void testInitLorenzoDeck_and_pickLorenzoCard() {
        SingleGame game = new SingleGame("singleUser");
        LorenzoBoard lorenzoBoard = new LorenzoBoard(game);
        lorenzoBoard.initLorenzoDeck(FileNames.LORENZO_DEV_FILE.value(), FileNames.LORENZO_FAITH_FILE.value());
        List<LorenzoCard> found = new ArrayList<>();
        int numOfCards = 7;
        while (found.size() < numOfCards){
            LorenzoCard picked = lorenzoBoard.pickLorenzoCard();
            assertNotNull(picked);
            if(!found.contains(picked))
                found.add(picked);
        }
        for(int i=0; i<numOfCards; i++)
            assertEquals(found.get(i), lorenzoBoard.pickLorenzoCard());
    }

    @Test
    public void testRefreshDeck() {
        SingleGame game = new SingleGame("singleUser");
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

        boolean different = false;
        for (int i=0; i<numOfCards && !different; i++){
            if(preRefresh.get(i)!=postRefresh.get(i))
                different=true;
        }
        assertTrue(different);
    }
}