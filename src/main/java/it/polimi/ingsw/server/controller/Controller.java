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

public class Controller {

    private final int playerNumber;
    private final Game game;


    public Controller(int playerNumber) {
        this.playerNumber = playerNumber;
        if(playerNumber == 1)
            game = new SingleGame();
        else
            game = new MultiGame();
    }


    public Game getGame() {
        return game;
    }

    public String getPlayingNickname() {
        return game.getPlayingNickname();
    }

    public PlayerBoard getPlayerBoard(String nickname) {
        return game.getPlayerBoards().get(game.getPlayerIndexOf(nickname));
    }

    public void initGame(List<VirtualView> views) {
        game.initGame(views);
    }

    public void discardLeaders(String player, List<LeaderCard> leaderCards, boolean setup) {
        game.discardLeaders(game.getPlayerIndexOf(player), leaderCards, setup);
    }

    public boolean addResourcesToWarehouse(String player, List<Shelf> shelves, List<Resource> extra) {
        return game.addResourcesToWarehouse(game.getPlayerIndexOf(player), shelves, extra);
    }

    public int loadNextTurn(){
        return game.loadNextTurn();
    }

    public List<Resource> getFromMarket(String player, int row, int column) {
        return game.getFromMarket(game.getPlayerIndexOf(player), row, column);
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

    public Map<String, List<Resource>> getResourcesToEqualize() {
        if(playerNumber == 1)
            return null;
        else
            return ((MultiGame) game).getResourcesToEqualize();
    }

    public boolean playLeaderCard(String player, List<LeaderCard> cards) { //TODO: da controllare
        for(LeaderCard card : cards)
            if(!game.playLeaderCard(game.getPlayerIndexOf(player), card))
                return false;
        return true;
    }
}
