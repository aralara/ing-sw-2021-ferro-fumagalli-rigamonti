package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cards.card.*;
import it.polimi.ingsw.model.games.Game;
import it.polimi.ingsw.model.games.MultiGame;
import it.polimi.ingsw.model.games.SingleGame;
import it.polimi.ingsw.model.storage.*;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    Game game;
    int playerNumber;
    List<String> players;


    Controller(int playerNumber) {
        if(playerNumber == 1)
            game = new SingleGame();
        else
            game = new MultiGame();
        this.playerNumber = playerNumber;
        players = new ArrayList<>();
    }


    public boolean addPlayer(String player) {
        players.add(player);
        if(players.stream().anyMatch(s -> s.equals(player)))
            return false;
        if(players.size() >= playerNumber)
            initGame(players);
        return true;
    }

    public void initGame(List<String> players) {
        game.initGame(players);
    }

    public void discardLeaders(String player, List<LeaderCard> cards) {
        game.discardLeaders(game.getPlayerIndexOf(player), cards);
    }

    public void addResourcesToWarehouse(String player, List<Shelf> shelves, List<Resource> extra) {
        game.addResourcesToWarehouse(game.getPlayerIndexOf(player), shelves, extra);
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
