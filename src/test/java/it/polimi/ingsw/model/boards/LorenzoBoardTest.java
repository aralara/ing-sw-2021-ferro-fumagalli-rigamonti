package it.polimi.ingsw.model.boards;

import it.polimi.ingsw.model.FileNames;
import it.polimi.ingsw.model.cards.card.LorenzoCard;
import it.polimi.ingsw.model.cards.card.LorenzoDev;
import it.polimi.ingsw.model.cards.card.LorenzoFaith;
import it.polimi.ingsw.model.games.SingleGame;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LorenzoBoardTest {

    @Test   //TODO: cambiato costruttore game
    public void testAddFaith() {/*
        SingleGame game = new SingleGame("singleUser");
        LorenzoBoard lorenzoBoard = new LorenzoBoard(game);

        assertEquals(0, lorenzoBoard.getFaith());
        lorenzoBoard.addFaith(1);
        assertEquals(1, lorenzoBoard.getFaith());
        lorenzoBoard.addFaith(3);
        assertEquals(4, lorenzoBoard.getFaith());*/
    }

    @Test
    public void testTakeDevCard() {
    }

    @Test   //TODO: cambiato costruttore game
    public void testInitLorenzoDeck_and_pickLorenzoCard() {/*
        SingleGame game = new SingleGame("singleUser");
        LorenzoBoard lorenzoBoard = new LorenzoBoard(game);
        lorenzoBoard.initLorenzoDeck(FileNames.LORENZO_DEV_FILE.value(), FileNames.LORENZO_FAITH_FILE.value());
        int[] lorenzoCards = new int[2];
        int numOfCards = 7;

        for(int i=0; i<7; i++){
            LorenzoCard picked = lorenzoBoard.pickLorenzoCard();
            assertNotNull(picked);
            if(picked instanceof LorenzoDev)
                lorenzoCards[0]++;
            else if(picked instanceof LorenzoFaith)
                lorenzoCards[1]++;
        }
        assertEquals(4, lorenzoCards[0]);
        assertEquals(3, lorenzoCards[1]);*/
    }

    @Test   //TODO: cambiato costruttore game
    public void testRefreshDeck() {/*
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

        assertTrue(preRefresh.containsAll(postRefresh));
        assertTrue(postRefresh.containsAll(preRefresh));*/
    }
}