package it.polimi.ingsw.server.model.games;

import it.polimi.ingsw.utils.TurnStatus;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests methods of MultiGame class
 */
public class MultiGameTest {

    /**
     * Tests if turn is loaded correctly
     */
    @Test
    public void testLoadNextTurn() {
        MultiGame game = new MultiGame();
        List<String> players = new ArrayList<>();
        players.add("user1");
        players.add("user2");
        game.initGame(players);
        assertEquals(TurnStatus.INVALID.value(), game.loadNextTurn());
        game.getPlayerBoards().get(game.getPlayerIndexOf(game.getPlayingNickname())).getFaithBoard().addFaith(9);
        game.getPlayerBoards().get(game.getPlayerIndexOf(game.getPlayingNickname())).setTurnPlayed(true);
        assertEquals(TurnStatus.LOAD_TURN_NORMAL.value(), game.loadNextTurn()); //first player
        game.getPlayerBoards().get(game.getPlayerIndexOf(game.getPlayingNickname())).setTurnPlayed(true);
        assertEquals(TurnStatus.LOAD_TURN_NORMAL.value(), game.loadNextTurn()); //second player
        game.getPlayerBoards().get(game.getPlayerIndexOf(game.getPlayingNickname())).getFaithBoard().addFaith(16);
        game.getPlayerBoards().get(game.getPlayerIndexOf(game.getPlayingNickname())).setTurnPlayed(true);
        assertEquals(TurnStatus.LOAD_TURN_LAST_ROUND.value(), game.loadNextTurn()); //first player
        game.getPlayerBoards().get(game.getPlayerIndexOf(game.getPlayingNickname())).setTurnPlayed(true);
        assertEquals(TurnStatus.LOAD_TURN_END_GAME.value(), game.loadNextTurn()); //second player
    }

    /**
     * Tests if faith is added correctly to every player except the specified one
     */
    @Test
    public void testAddFaithAll() {
        MultiGame game = new MultiGame();
        List<String> players = new ArrayList<>();
        players.add("user1");
        players.add("user2");
        players.add("user3");
        game.initGame(players);
        assertEquals(0, game.getPlayerBoards().get(0).getFaithBoard().getFaith());
        assertEquals(0, game.getPlayerBoards().get(1).getFaithBoard().getFaith());
        assertEquals(0, game.getPlayerBoards().get(2).getFaithBoard().getFaith());
        game.addFaithAll(1, 4);
        assertEquals(4, game.getPlayerBoards().get(0).getFaithBoard().getFaith());
        assertEquals(0, game.getPlayerBoards().get(1).getFaithBoard().getFaith());
        assertEquals(4, game.getPlayerBoards().get(2).getFaithBoard().getFaith());
    }

    /**
     * Tests if the current player nickname is not null and if it's one of the nicknames of the players
     */
    @Test
    public void testGetPlayingNickname() {
        MultiGame game = new MultiGame();
        List<String> players = new ArrayList<>();
        players.add("user1");
        players.add("user2");
        game.initGame(players);
        assertNotNull(game.getPlayingNickname());
        assertTrue(game.getPlayingNickname().equals("user1") || game.getPlayingNickname().equals("user2"));
    }
}