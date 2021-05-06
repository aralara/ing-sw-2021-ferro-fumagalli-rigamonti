package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.cards.card.*;
import it.polimi.ingsw.server.model.games.Game;
import it.polimi.ingsw.server.model.games.MultiGame;
import it.polimi.ingsw.server.model.games.SingleGame;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.view.VirtualView;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    private final Game game;
    private final int playerNumber;
    private final List<String> players;


    public Controller(int playerNumber) {
        if(playerNumber == 1)
            game = new SingleGame();
        else
            game = new MultiGame();
        this.playerNumber = playerNumber;
        players = new ArrayList<>();
    }


    public Game getGame() {
        return game;
    }

    public void addPlayer(String player) {
        players.add(player);
    }

    public void initGame(List<VirtualView> views) {
        game.initGame(views);
    }

    public void discardLeaders(String player, List<LeaderCard> cards) {
        game.discardLeaders(game.getPlayerIndexOf(player), cards);
    }

    public boolean addResourcesToWarehouse(String player, List<Shelf> shelves, List<Resource> extra) {
        return game.addResourcesToWarehouse(game.getPlayerIndexOf(player), shelves, extra);
    }

    public void loadNextTurn(){
        game.loadNextTurn();
    }

    public List<Resource> getFromMarket(String player, int row, int column) {
        return game.getFromMarket(game.getPlayerIndexOf(player), row, column);
    }

    public boolean removeDevCard(CardColors color, int level) {
        return game.removeDevCard(color, level);
    }

    public boolean canBuyDevCard(String player, DevelopmentCard card) {
        return game.canBuyDevCard(game.getPlayerIndexOf(player), card);
    }

    public boolean buyDevCard(String player, DevelopmentCard card, int space, List<RequestResources> requests) {
        return game.buyDevCard(game.getPlayerIndexOf(player), card, space, requests);
    }

    public boolean canActivateProductions(String player, List<Resource> consumed) {
        return game.canActivateProductions(game.getPlayerIndexOf(player), consumed);
    }

    public boolean activateProductions(String player, List<Resource> produced, List<RequestResources> requests) {
        return game.activateProductions(game.getPlayerIndexOf(player), produced, requests);
    }

    public void discardExtraLeader(String player, LeaderCard card) {
        game.discardExtraLeader(game.getPlayerIndexOf(player), card);
    }

    public boolean playLeaderCard(String player, LeaderCard card) {
        return game.playLeaderCard(game.getPlayerIndexOf(player), card);
    }
}
