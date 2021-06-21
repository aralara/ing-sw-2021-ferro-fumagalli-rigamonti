package it.polimi.ingsw.server.model.games;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.cards.card.Card;
import it.polimi.ingsw.server.model.cards.card.CardColors;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.TurnStatus;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Tests methods of Game class
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
        MultiGame game = new MultiGame();
        List<String> players = new ArrayList<>();
        int playerID;
        players.add("user1");
        players.add("user2");
        game.initGame(players);

        // Try to take resource before and after the action for the turn
        assertEquals(TurnStatus.INVALID.value(), game.loadNextTurn());
        playerID = updatePlayer(game);

        assertFalse(getPlayerboard(game, playerID).isTurnPlayed());

        List<Resource> marketResources1 = game.getFromMarket(playerID, 1, -1);

        assertTrue(getPlayerboard(game, playerID).isTurnPlayed());
        assertNotNull(marketResources1);
        assertEquals(marketResources1.stream().mapToInt(Resource::getQuantity).sum(), 4);

        getPlayerboard(game, playerID).setTurnPlayed(true);
        List<Resource> marketResources2 = game.getFromMarket(playerID, 1, -1);

        assertTrue(getPlayerboard(game, playerID).isTurnPlayed());
        assertNull(marketResources2);
    }

    /**
     * Tests the removal of a development card
     */
    @Test
    public void testRemoveDevCard() {
        // The test is unrealistic since the method would never be called in a MultiGame
        // but works for the sake of testing the method
        MultiGame game = new MultiGame();
        List<String> players = new ArrayList<>();
        boolean success;
        players.add("user1");
        players.add("user2");
        game.initGame(players);

        DevelopmentDeck developmentDeck1 = game.getDevelopmentDecks().stream().filter(dd ->
                dd.getDeckColor() == CardColors.PURPLE && dd.getDeckLevel() == 1).findFirst().orElse(null);
        DevelopmentDeck developmentDeck2 = game.getDevelopmentDecks().stream().filter(dd ->
                dd.getDeckColor() == CardColors.PURPLE && dd.getDeckLevel() == 2).findFirst().orElse(null);
        DevelopmentDeck developmentDeck3 = game.getDevelopmentDecks().stream().filter(dd ->
                dd.getDeckColor() == CardColors.PURPLE && dd.getDeckLevel() == 3).findFirst().orElse(null);

        assertNotNull(developmentDeck1);
        assertNotNull(developmentDeck2);
        assertNotNull(developmentDeck3);

        DevelopmentCard firstCard1 = (DevelopmentCard)developmentDeck1.getDeck().get(0);
        DevelopmentCard firstCard2 = (DevelopmentCard)developmentDeck2.getDeck().get(0);
        DevelopmentCard firstCard3 = (DevelopmentCard)developmentDeck3.getDeck().get(0);

        assertEquals(developmentDeck1.getDeck().size(), 4);
        assertEquals(developmentDeck2.getDeck().size(), 4);
        assertEquals(developmentDeck3.getDeck().size(), 4);

        success = game.removeDevCard(CardColors.PURPLE, 1);

        assertTrue(success);
        assertTrue(developmentDeck1.getDeck().getCards().stream().noneMatch(c -> c.equals(firstCard1)));
        assertEquals(developmentDeck1.getDeck().size(), 3);
        assertEquals(developmentDeck2.getDeck().size(), 4);
        assertEquals(developmentDeck3.getDeck().size(), 4);

        game.removeDevCard(CardColors.PURPLE, 0);
        game.removeDevCard(CardColors.PURPLE, 0);
        game.removeDevCard(CardColors.PURPLE, 0);

        assertTrue(developmentDeck1.getDeck().isEmpty());
        assertEquals(developmentDeck2.getDeck().size(), 4);
        assertEquals(developmentDeck3.getDeck().size(), 4);

        success = game.removeDevCard(CardColors.PURPLE, 0);

        assertTrue(success);
        assertTrue(developmentDeck2.getDeck().getCards().stream().noneMatch(c -> c.equals(firstCard2)));
        assertTrue(developmentDeck1.getDeck().isEmpty());
        assertEquals(developmentDeck2.getDeck().size(), 3);
        assertEquals(developmentDeck3.getDeck().size(), 4);

        game.removeDevCard(CardColors.PURPLE, 2);
        game.removeDevCard(CardColors.PURPLE, 2);
        game.removeDevCard(CardColors.PURPLE, 2);

        assertTrue(developmentDeck1.getDeck().isEmpty());
        assertTrue(developmentDeck2.getDeck().isEmpty());
        assertEquals(developmentDeck3.getDeck().size(), 4);

        success = game.removeDevCard(CardColors.PURPLE, 0);

        assertTrue(success);
        assertTrue(developmentDeck3.getDeck().getCards().stream().noneMatch(c -> c.equals(firstCard3)));
        assertTrue(developmentDeck1.getDeck().isEmpty());
        assertTrue(developmentDeck2.getDeck().isEmpty());
        assertEquals(developmentDeck3.getDeck().size(), 3);

        game.removeDevCard(CardColors.PURPLE, 3);
        game.removeDevCard(CardColors.PURPLE, 3);
        game.removeDevCard(CardColors.PURPLE, 3);

        assertTrue(developmentDeck1.getDeck().isEmpty());
        assertTrue(developmentDeck2.getDeck().isEmpty());
        assertTrue(developmentDeck3.getDeck().isEmpty());

        success = game.removeDevCard(CardColors.PURPLE, 1) || game.removeDevCard(CardColors.PURPLE, 2) ||
                game.removeDevCard(CardColors.PURPLE, 3) || game.removeDevCard(CardColors.PURPLE, 0);
        assertFalse(success);
    }

    /**
     * Tests buying a development card
     */
    @Test
    public void testBuyDevCard() {
        MultiGame game = new MultiGame();
        List<String> players = new ArrayList<>();
        int playerID;
        boolean success;
        players.add("user1");
        players.add("user2");
        game.initGame(players);

        DevelopmentDeck developmentDeck = game.getDevelopmentDecks().stream().filter(dd ->
                dd.getDeckColor() == CardColors.PURPLE && dd.getDeckLevel() == 1).findFirst().orElse(null);

        assertNotNull(developmentDeck);
        assertEquals(TurnStatus.INVALID.value(), game.loadNextTurn());
        playerID = updatePlayer(game);

        DevelopmentCard card1 = (DevelopmentCard) developmentDeck.getDeck().getCards().get(0);
        DevelopmentCard card2 = (DevelopmentCard) developmentDeck.getDeck().getCards().get(1);

        // Trying to buy a card without resources
        success = game.buyDevCard(playerID, card1, 1,
                List.of(new RequestResources(card1.getCost(), StorageType.STRONGBOX)));

        assertFalse(success);

        getPlayerboard(game, playerID).getStrongbox().addResources(
                List.of(new Resource(ResourceType.SHIELD, 100),
                        new Resource(ResourceType.COIN, 100),
                        new Resource(ResourceType.SERVANT, 100),
                        new Resource(ResourceType.STONE, 100)));

        // Trying to buy a card with resources but wrong requests
        success = game.buyDevCard(playerID, card1, 1,
                List.of(new RequestResources(card1.getCost(), StorageType.WAREHOUSE)));
        success |= game.buyDevCard(playerID, card1, 1,
                List.of(new RequestResources(new ArrayList<>(), StorageType.STRONGBOX)));

        assertFalse(success);

        // Trying to buy a card that cannot be taken
        success = game.buyDevCard(playerID, card2, 1,
                List.of(new RequestResources(card2.getCost(), StorageType.STRONGBOX)));

        assertFalse(success);

        // Buying the card
        success = game.buyDevCard(playerID, card1, 1,
                List.of(new RequestResources(card1.getCost(), StorageType.STRONGBOX)));

        assertTrue(success);

        assertEquals(TurnStatus.LOAD_TURN_NORMAL.value(), game.loadNextTurn());
        playerID = updatePlayer(game);
        getPlayerboard(game, playerID).setTurnPlayed(true);

        assertEquals(TurnStatus.LOAD_TURN_NORMAL.value(), game.loadNextTurn());
        playerID = updatePlayer(game);

        // Trying to put the second card in the wrong place
        success = game.buyDevCard(playerID, card2, 1,
                List.of(new RequestResources(card2.getCost(), StorageType.STRONGBOX)));
        success |= game.buyDevCard(playerID, card2, 5,
                List.of(new RequestResources(card2.getCost(), StorageType.STRONGBOX)));

        assertFalse(success);

        // Buying the second card
        success = game.buyDevCard(playerID, card2, 2,
                List.of(new RequestResources(card2.getCost(), StorageType.STRONGBOX)));

        assertTrue(success);
    }

    /**
     * Tests activating a set of productions
     */
    @Test
    public void testActivateProductions() {
        MultiGame game = new MultiGame();
        List<String> players = new ArrayList<>();
        int playerID;
        boolean success;
        players.add("user1");
        players.add("user2");
        game.initGame(players);

        assertEquals(TurnStatus.INVALID.value(), game.loadNextTurn());
        playerID = updatePlayer(game);

        List<Resource> basicProductionConsumed = new ArrayList<>();
        basicProductionConsumed.add(new Resource(ResourceType.SERVANT, 2));
        List<Resource> basicProductionProduced  = new ArrayList<>();
        basicProductionProduced.add(new Resource(ResourceType.STONE, 1));
        Production basicProduction = new Production(basicProductionConsumed, basicProductionProduced);

        // Trying to activate a production without resources
        success = game.activateProductions(playerID, List.of(basicProduction), List.of(
                new RequestResources(basicProductionConsumed, StorageType.STRONGBOX)),
                basicProductionConsumed, basicProductionProduced);

        assertFalse(success);

        getPlayerboard(game, playerID).getStrongbox().addResources(
                List.of(new Resource(ResourceType.SHIELD, 100),
                        new Resource(ResourceType.COIN, 100),
                        new Resource(ResourceType.SERVANT, 100),
                        new Resource(ResourceType.STONE, 100)));

        // Trying to activate a production with resources but wrong requests
        success = game.activateProductions(playerID, List.of(basicProduction), List.of(
                new RequestResources(basicProductionConsumed, StorageType.WAREHOUSE)),
                basicProductionConsumed, basicProductionProduced);
        success |= game.activateProductions(playerID, List.of(basicProduction), List.of(
                new RequestResources(new ArrayList<>(), StorageType.STRONGBOX)),
                basicProductionConsumed, basicProductionProduced);

        assertFalse(success);

        // Trying to activate a production not available to the player
        List<Resource> pConsumed = new ArrayList<>();
        pConsumed.add(new Resource(ResourceType.COIN, 1));
        List<Resource> pProduced = new ArrayList<>();
        pProduced.add(new Resource(ResourceType.SHIELD, 1));
        Production p = new Production(pConsumed, pProduced);

        success = game.activateProductions(playerID, List.of(basicProduction, p), List.of(
                new RequestResources(basicProductionConsumed, StorageType.STRONGBOX)),
                Stream.concat(basicProductionConsumed.stream(), pConsumed.stream()).collect(Collectors.toList()),
                Stream.concat(basicProductionProduced.stream(), pProduced.stream()).collect(Collectors.toList()));

        assertFalse(success);

        // Trying to activate a production with wrong specification/wildcards
        success = game.activateProductions(playerID, List.of(basicProduction), List.of(
                new RequestResources(new ArrayList<>(), StorageType.STRONGBOX)),
                pConsumed, pProduced);

        assertFalse(success);

        // Activating the production
        success = game.activateProductions(playerID, List.of(basicProduction), List.of(
                new RequestResources(basicProductionConsumed, StorageType.STRONGBOX)),
                basicProductionConsumed, basicProductionProduced);

        assertTrue(success);
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
        MultiGame game = new MultiGame();
        List<String> players = new ArrayList<>();
        players.add("user1");
        players.add("user2");
        game.initGame(players);

        List<VirtualView> virtualViews = new ArrayList<>();
        players.forEach(p -> virtualViews.add(new VirtualView(null, null, null, p)));
        game.addListeners(virtualViews);

        for(DevelopmentDeck dDeck : game.getDevelopmentDecks())
            assertTrue(dDeck.hasListeners());

        assertTrue(game.getMarket().hasListeners());

        for(PlayerBoard pBoard : game.getPlayerBoards()) {
            assertTrue(pBoard.hasListeners());
            assertTrue(pBoard.getDevelopmentBoard().hasListeners());
            assertTrue(pBoard.getFaithBoard().hasListeners());
            assertTrue(pBoard.getLeaderBoard().hasListeners());
            assertTrue(pBoard.getStrongbox().hasListeners());
            assertTrue(pBoard.getWarehouse().hasListeners());
        }
    }


    @Test
    public void testIsFinished() {
        MultiGame game = new MultiGame();
        List<String> players = new ArrayList<>();
        int playerID;
        players.add("user1");
        players.add("user2");
        game.initGame(players);

        assertFalse(game.isFinished());

        assertEquals(TurnStatus.INVALID.value(), game.loadNextTurn());
        playerID = updatePlayer(game);
        game.getPlayerBoards().get(game.getPlayerIndexOf(game.getPlayingNickname())).getFaithBoard().addFaith(25);
        getPlayerboard(game, playerID).setTurnPlayed(true);

        assertEquals(TurnStatus.LOAD_TURN_LAST_ROUND.value(), game.loadNextTurn());
        playerID = updatePlayer(game);
        getPlayerboard(game, playerID).setTurnPlayed(true);

        assertEquals(TurnStatus.LOAD_TURN_END_GAME.value(), game.loadNextTurn());

        assertTrue(game.isFinished());
    }
}