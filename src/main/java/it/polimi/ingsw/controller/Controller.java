package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cards.card.CardColors;
import it.polimi.ingsw.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.model.cards.card.LeaderCard;
import it.polimi.ingsw.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.model.games.Game;
import it.polimi.ingsw.model.storage.Resource;
import it.polimi.ingsw.model.storage.Shelf;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class Controller {

    Game game;


    Controller(){
    }


    public void initController(String ... players) {
        game.initGame(players);
    }

    public void discardLeaders(int player, List<LeaderCard> cards) {
        game.discardLeaders(player, cards);
    }

    public void addResourcesToWarehouse(int player, List<Shelf> shelves, List<Resource> extra) {
        game.addResourcesToWarehouse(player, shelves, extra);
    }

    public abstract void loadNextTurn();

    public List<Resource> getFromMarket(int row, int column) {
        return game.getFromMarket(row, column);
    }

    public boolean removeDevCard(CardColors color, int level) {
        return game.removeDevCard(color, level);
    }

    public boolean canBuyDevCard(int player, DevelopmentCard card) {
        return game.canBuyDevCard(player, card);
    }
}
