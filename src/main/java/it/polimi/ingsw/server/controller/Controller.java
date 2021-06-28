package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.cards.card.*;
import it.polimi.ingsw.server.model.games.Game;
import it.polimi.ingsw.server.model.games.MultiGame;
import it.polimi.ingsw.server.model.games.SingleGame;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.view.VirtualView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handles the game's flow by calling the macro-methods of the model
 */
public class Controller {

    private final Game game;

    /**
     * Public constructor with playerNumber parameter
     * @param playerNumber Int that determinate if the game is a SingleGame or a MultiGame
     */
    public Controller(int playerNumber) {
        if(playerNumber == 1)
            game = new SingleGame();
        else
            game = new MultiGame();
    }

    /**
     * Public constructor with game parameter
     * @param game Game to be set
     */
    public Controller(Game game) {
        this.game = game;
    }


    /**
     * Gets the game attribute
     * @return Returns game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gets the current player nickname
     * @return Returns the current player nickname
     */
    public String getPlayingNickname() {
        return game.getPlayingNickname();
    }

    /**
     * Gets the current player playerBoard
     * @return Returns the current player playerBoard
     */
    public PlayerBoard getPlayerBoard(String nickname) {
        return game.getPlayerBoards().get(game.getPlayerIndexOf(nickname));
    }

    /**
     * Method that initialize a new game
     * @param views List of player's VirtualView
     */
    public void initNewGame(List<VirtualView> views) {
        game.initGame(views.stream().map(VirtualView::getNickname).collect(Collectors.toList()));
        game.addListeners(views);
    }

    /**
     * Method that initialize a saved game
     * @param views List of player's VirtualView
     */
    public void initSavedGame(List<VirtualView> views) {
        game.addListeners(views);
    }

    /**
     * Invokes a method to discard leader cards
     * @param player Index of the player
     * @param leaderCards Leader cards to be discarded
     * @param setup Indicates if the action is played during the initial setup ot not
     * @return Return true if the action is correctly completed, false otherwise
     */
    public boolean discardLeaders(String player, List<LeaderCard> leaderCards, boolean setup) {
        return game.discardLeaders(game.getPlayerIndexOf(player), leaderCards, setup);
    }

    /**
     * Invokes a method to add resources to warehouse
     * @param player Index of the player
     * @param shelves New shelves to be set up in the warehouse
     * @param extra List of resources to be discarded, of faith
     * @return Return true if the action is correctly completed, false otherwise
     */
    public boolean addResourcesToWarehouse(String player, List<Shelf> shelves, List<Resource> extra) {
        return game.addResourcesToWarehouse(game.getPlayerIndexOf(player), shelves, extra);
    }

    /**
     * Invokes a method to load next turn
     * @return Returns 2 if it's the last round, 3 if the game is ended, 1 otherwise
     */
    public int loadNextTurn(){
        return game.loadNextTurn();
    }

    /**
     * Invokes a method to get resources from market
     * @param player Index of the player
     * @param row Row chosen by the player, if the player chose a column it is -1
     * @param column Column chosen by the player, if the player chose a row it is -1
     * @return Returns a list of resources corresponding to the marbles contained in the market
     */
    public List<Resource> getFromMarket(String player, int row, int column) {
        return game.getFromMarket(game.getPlayerIndexOf(player), row, column);
    }

    /**
     * Invokes a method to check if a DevelopmentCard can be bought and placed on the board for the player
     * @param player Index of the player to check
     * @param card The development card to be added
     * @param space The space where card will be added
     * @return Returns true if the card can be added, false otherwise
     */
    public boolean canBuyDevCard(String player, DevelopmentCard card, int space) {
        return game.canBuyDevCard(game.getPlayerIndexOf(player), card, space);
    }

    /**
     * Invokes a method to buy a developmentCard
     * @param player Index of the player to buy for
     * @param card The development card to buy
     * @param space The position where the card will be placed
     * @param requests List of requests containing resource quantity and location
     * @return Returns true if the card is bought, false otherwise
     */
    public boolean buyDevCard(String player, DevelopmentCard card, int space, List<RequestResources> requests) {
        return game.buyDevCard(game.getPlayerIndexOf(player), card, space, requests);
    }

    /**
     * Invokes a methods to checks if a list of productions can be activated
     * @param player Index of the player to check
     * @param consumed The list of resources that will be consumed
     * @return Returns true if the productions can be activated, false otherwise
     */
    public boolean canActivateProductions(String player, List<Resource> consumed) {
        return game.canActivateProductions(game.getPlayerIndexOf(player), consumed);
    }

    /**
     * Invokes a method to activates the productions
     * @param player Index of the player to activate productions for
     * @param productions List of productions to activate
     * @param requests List of requests containing resource quantity and location for the spent resources
     * @param consumed The list of resources that will be consumed
     * @param produced The list of resources that will be produced
     * @return Returns true if the productions can be activated, false otherwise
     */
    public boolean activateProductions(String player, List<Production> productions, List<RequestResources> requests,
                                       List<Resource> consumed, List<Resource> produced) {
        return game.activateProductions(game.getPlayerIndexOf(player), productions, requests, consumed, produced);
    }

    /**
     * Invokes a method to equalize the resources in the setup phase
     * @return Returns a map of lists of resources using the nickname of the player as a key
     */
    public Map<String, List<Resource>> getResourcesToEqualize() {
        return game.getResourcesToEqualize();
    }

    /**
     * Invokes a methods to play leader cards
     * @param player Index of the player to play the Leader card
     * @param cards LeaderCards to be played
     * @return Returns true if the cards are played, false otherwise
     */
    public boolean playLeaderCard(String player, List<LeaderCard> cards) {
        for(LeaderCard card : cards)
            if(!game.playLeaderCard(game.getPlayerIndexOf(player), card))
                return false;
        return true;
    }

    /**
     * Checks if it's the turn of a given player
     * @param nickname Nickname of the player
     * @return Returns true if it's the turn of the player, false otherwise
     */
    public boolean checkTurnPlayer(String nickname) {
        return game.getPlayingNickname().equals(nickname);
    }
}
