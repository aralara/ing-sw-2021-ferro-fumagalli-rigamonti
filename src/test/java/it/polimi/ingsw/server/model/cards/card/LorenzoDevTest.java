package it.polimi.ingsw.server.model.cards.card;

import it.polimi.ingsw.server.model.FileNames;
import it.polimi.ingsw.server.model.boards.LorenzoBoard;
import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.server.model.games.SingleGame;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests methods of LorenzoDev class TODO: fare javadoc
 */
public class LorenzoDevTest {

    @Test
    public void testActivateLorenzo() {
        SingleGame game = new SingleGame();
        List<String> players = new ArrayList<>();
        players.add("userTest");
        game.initGame(players);
        LorenzoBoard lorenzoBoard = new LorenzoBoard(game);
        lorenzoBoard.initLorenzoDeck(FileNames.LORENZO_DEV_FILE.value(), FileNames.LORENZO_FAITH_FILE.value());
        List<DevelopmentDeck> greenDecks = new ArrayList<>();
        for(DevelopmentDeck deck : game.getDevelopmentDecks())
            if(deck.getDeckColor()==CardColors.GREEN)
                greenDecks.add(deck);
        for(DevelopmentDeck deck : greenDecks)
            assertEquals(4, deck.getDeck().size());
        LorenzoDev lorenzoDev = new LorenzoDev(1, CardColors.GREEN, 2);
        assertEquals("Lorenzo removes 2 GREEN development cards from the development decks \n",
                lorenzoDev.toString());
        lorenzoDev.activateLorenzo(lorenzoBoard);
        assertEquals(2, greenDecks.get(0).getDeck().size());
        assertEquals(4, greenDecks.get(1).getDeck().size());
        assertEquals(4, greenDecks.get(2).getDeck().size());
        for(DevelopmentDeck deck : game.getDevelopmentDecks())
            if(deck.getDeckColor()!=CardColors.GREEN)
                assertEquals(4, deck.getDeck().size());
    }
}