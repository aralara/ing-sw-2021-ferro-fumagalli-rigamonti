package it.polimi.ingsw.server.model.games;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.cards.card.Card;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.utils.TurnStatus;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Tests methods of Game class TODO: fare javadoc
 */
public class GameTest {

    /**
     * Helper method that returns the index of the player that is currently playing
     * @param game Referenced game
     * @return Returns an integer value containing the index
     */
    private int updatePlayer(Game game) {
        return game.getPlayerIndexOf(game.getPlayingNickname());
    }

    /**
     * Returns a PlayerBoard from the game given its index
     * @param game Referenced game
     * @param n Index of the PlayerBoard
     * @return Returns the corresponding PlayerBoard object
     */
    private PlayerBoard getPlayerboard(Game game, int n) {
        return game.getPlayerBoards().get(n);
    }

    /**
     * Tests the discard leaders method
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDiscardLeaders() {
        MultiGame game = new MultiGame();
        List<String> players = new ArrayList<>();
        int playerID;
        boolean discardValue;
        players.add("user1");
        players.add("user2");
        game.initGame(players);

        // Discards the first two leader cards during "setup"
        assertEquals(TurnStatus.INVALID.value(), game.loadNextTurn());
        playerID = updatePlayer(game);

        assertEquals(getPlayerboard(game, playerID).getLeaderBoard().getHand().size(), 4);
        assertTrue(getPlayerboard(game, playerID).getLeaderBoard().getBoard().isEmpty());

        discardValue = game.discardLeaders(playerID,
                (List<LeaderCard>)(List<? extends Card>) getPlayerboard(game, playerID).getLeaderBoard().getHand()
                        .getCards().stream().limit(2).collect(Collectors.toList()),
                true);
        getPlayerboard(game, playerID).setTurnPlayed(true);

        assertEquals(getPlayerboard(game, playerID).getLeaderBoard().getHand().size(), 2);
        assertEquals(getPlayerboard(game, playerID).getFaithBoard().getFaith(), 0);
        assertTrue(getPlayerboard(game, playerID).getLeaderBoard().getBoard().isEmpty());
        assertTrue(discardValue);

        assertEquals(TurnStatus.LOAD_TURN_NORMAL.value(), game.loadNextTurn());
        playerID = updatePlayer(game);
        getPlayerboard(game, playerID).setTurnPlayed(true);

        // Discards a single leader card
        assertEquals(TurnStatus.LOAD_TURN_NORMAL.value(), game.loadNextTurn());
        playerID = updatePlayer(game);
        discardValue = game.discardLeaders(playerID,
                List.of((LeaderCard)getPlayerboard(game, playerID).getLeaderBoard().getHand().get(0)), false);
        getPlayerboard(game, playerID).setTurnPlayed(true);

        assertEquals(getPlayerboard(game, playerID).getLeaderBoard().getHand().size(), 1);
        assertEquals(getPlayerboard(game, playerID).getFaithBoard().getFaith(), 1);
        assertTrue(getPlayerboard(game, playerID).getLeaderBoard().getBoard().isEmpty());
        assertTrue(discardValue);

        assertEquals(TurnStatus.LOAD_TURN_NORMAL.value(), game.loadNextTurn());
        playerID = updatePlayer(game);
        getPlayerboard(game, playerID).setTurnPlayed(true);

        // Try to discard a card in "setup" while the game isn't in setup
        assertEquals(TurnStatus.LOAD_TURN_NORMAL.value(), game.loadNextTurn());
        playerID = updatePlayer(game);
        discardValue = game.discardLeaders(playerID,
                List.of((LeaderCard)getPlayerboard(game, playerID).getLeaderBoard().getHand().get(0)), true);
        getPlayerboard(game, playerID).setTurnPlayed(true);

        assertEquals(getPlayerboard(game, playerID).getLeaderBoard().getHand().size(), 1);
        assertEquals(getPlayerboard(game, playerID).getFaithBoard().getFaith(), 1);
        assertTrue(getPlayerboard(game, playerID).getLeaderBoard().getBoard().isEmpty());
        assertFalse(discardValue);
    }

    /**
     * Tests the resource equalization method
     */
    @Test
    public void testGetResourcesToEqualize() {
        MultiGame game = new MultiGame();
        List<String> players = List.of("user1", "user2", "user3", "user4");
        List<String> orderedPlayers = new ArrayList<>();
        final List<List<Resource>> equalizedResourcesTest = List.of(
                new ArrayList<>(),
                Collections.singletonList(new Resource(ResourceType.WILDCARD, 1)),
                Arrays.asList(
                        new Resource(ResourceType.WILDCARD, 1),
                        new Resource(ResourceType.FAITH, 1)),
                Arrays.asList(
                        new Resource(ResourceType.WILDCARD, 2),
                        new Resource(ResourceType.FAITH, 1))
        );

        game.initGame(players);

        List<PlayerBoard> playerBoards = game.getPlayerBoards();
        playerBoards.forEach(pb -> orderedPlayers.add(pb.getPlayer().getNickname()));

        Map<String, List<Resource>> equalize = game.getResourcesToEqualize();

        for(int i = 0; i < equalizedResourcesTest.size(); i++) {
            final int index = i;
            assertTrue(equalize.get(orderedPlayers.get(index)).stream().allMatch(r1 ->
                    equalizedResourcesTest.get(index).stream().anyMatch(r2 ->
                            r1.getResourceType() == r2.getResourceType() && r1.getQuantity() == r2.getQuantity())));
            assertTrue(equalizedResourcesTest.get(index).stream().allMatch(r1 ->
                    equalize.get(orderedPlayers.get(index)).stream().anyMatch(r2 ->
                            r1.getResourceType() == r2.getResourceType() && r1.getQuantity() == r2.getQuantity())));
        }
    }

    /**
     * Tests the method that adds resources to a specific warehouse
     */
    @Test
    public void testAddResourcesToWarehouse() {
        //TODO
    }

    /**
     * Tests taking resources from the market
     */
    @Test
    public void testGetFromMarket() {
        //TODO
    }

    /**
     * Tests the removal of a development card
     */
    @Test
    public void testRemoveDevCard() {
        //TODO
    }

    /**
     * Tests if a development card can be bought
     */
    @Test
    public void testCanBuyDevCard() {
        //TODO
    }

    /**
     * Tests buying a development card
     */
    @Test
    public void testBuyDevCard() {
        //TODO
    }

    /**
     * Tests if a productions can be activated
     */
    @Test
    public void testCanActivateProductions() {
        //TODO
    }

    /**
     * Tests activating a set of productions
     */
    @Test
    public void testActivateProductions() {
        //TODO
    }

    /**
     * Tests playing a leader card
     */
    @Test
    public void testPlayLeaderCard() {
        //TODO
    }

    /**
     * Tests if the listeners are correctly added
     */
    @Test
    public void testAddListeners() {
    }

    //TODO: TEST CON COVERAGE MA SENZA TEST

    @Test
    public void testInitGame() {
    }

    @Test
    public void testInitPlayerBoards() {
    }

    @Test
    public void testInitMarket() {
    }

    @Test
    public void testInitDevelopment() {
    }

    @Test
    public void testInitFaithTrack() {
    }

    @Test
    public void testInitLeaders() {
    }

    @Test
    public void testCheckFaith() {
    }

    @Test
    public void testCheckEndGame() {
    }

    @Test
    public void testRandomizeStartingPlayer() {
    }

    @Test
    public void testCalculateFinalPositions() {
    }

    //TODO: TEST PER SETTER E GETTER

    @Test
    public void testGetPlayerIndexOf() {
    }

    @Test
    public void testGetEndPlayerList() {
    }

    @Test
    public void testGetPlayerBoards() {
    }

    @Test
    public void testGetMarket() {
    }

    @Test
    public void testGetDevelopmentDecks() {
    }

    @Test
    public void testGetFaithTrack() {
    }

    @Test
    public void testIsFinished() {
    }

    @Test
    public void testSerFinished() {
    }

    @Test
    public void testGetPlayerNumber() {
    }
}