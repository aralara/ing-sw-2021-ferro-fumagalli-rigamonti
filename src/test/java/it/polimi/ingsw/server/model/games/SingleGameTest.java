package it.polimi.ingsw.server.model.games;

import it.polimi.ingsw.utils.TurnStatus;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests methods of SingleGame class
 */
public class SingleGameTest {

    /**
     * Tests if turn is loaded correctly
     */
    @Test
    public void testLoadNextTurn() {
        SingleGame game = new SingleGame();
        List<String> players = new ArrayList<>();
        players.add("user");
        game.initGame(players);
        assertEquals("user", game.getPlayingNickname());
        assertNotNull(game.getLorenzoBoard());
        assertEquals("Lorenzo il Magnifico", game.getLorenzoBoard().getPlayer().getNickname());
        game.getPlayerBoards().get(game.getPlayerIndexOf(game.getPlayingNickname())).setTurnPlayed(true);
        assertEquals(TurnStatus.LOAD_TURN_NORMAL.value(), game.loadNextTurn());
        game.getPlayerBoards().get(game.getPlayerIndexOf(game.getPlayingNickname())).getFaithBoard().addFaith(25);
        game.getPlayerBoards().get(game.getPlayerIndexOf(game.getPlayingNickname())).setTurnPlayed(true);
        assertEquals(TurnStatus.LOAD_TURN_END_GAME.value(), game.loadNextTurn());
        assertEquals(1, game.getEndPlayerList().get(0).getFinalPosition());

        game.initGame(players);
        game.addFaithAll(0, 25);
        assertEquals(0, game.getPlayerBoards().get(0).getFaithBoard().getFaith());
        assertEquals(TurnStatus.LOAD_TURN_END_GAME.value(), game.loadNextTurn());
        assertEquals(2, game.getEndPlayerList().get(0).getFinalPosition());

        game.initGame(players);
        game.getDevelopmentDecks().get(0).getDeck().getCards().clear();
        game.getDevelopmentDecks().get(4).getDeck().getCards().clear();
        game.getDevelopmentDecks().get(8).getDeck().getCards().clear();
        assertEquals(TurnStatus.LOAD_TURN_END_GAME.value(), game.loadNextTurn());
        assertEquals(2, game.getEndPlayerList().get(0).getFinalPosition());
    }
}