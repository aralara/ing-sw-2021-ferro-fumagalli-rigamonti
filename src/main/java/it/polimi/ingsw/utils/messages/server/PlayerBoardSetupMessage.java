package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.server.model.boards.*;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.utils.messages.HiddenMessage;

import java.util.ArrayList;
import java.util.List;

public class PlayerBoardSetupMessage implements HiddenMessage, ServerActionMessage {

    private final String nickname;
    private final List<Deck> developmentBSpaces;
    private final Deck leaderBBoard;
    private Deck leaderBHand;
    private final int faithBFaith;
    private final boolean[] faithBPope;
    private final List<Shelf> warehouse;
    private final List<Resource> strongbox;
    private final boolean inkwell;


    public PlayerBoardSetupMessage(PlayerBoard playerBoard) {
        this.nickname = playerBoard.getPlayer().getNickname();
        this.developmentBSpaces = playerBoard.getDevelopmentBoard().getSpaces();
        this.leaderBBoard = playerBoard.getLeaderBoard().getBoard();
        this.leaderBHand = playerBoard.getLeaderBoard().getHand();
        this.faithBFaith = playerBoard.getFaithBoard().getFaith();
        this.faithBPope = playerBoard.getFaithBoard().getPopeProgression();
        this.warehouse = playerBoard.getWarehouse().getShelves();
        this.strongbox = playerBoard.getStrongbox().getList();
        this.inkwell = playerBoard.isFirstPlayer();
    }


    public String getNickname() {
        return nickname;
    }

    public List<Deck> getDevelopmentBSpaces() {
        return developmentBSpaces;
    }

    public Deck getLeaderBBoard() {
        return leaderBBoard;
    }

    public Deck getLeaderBHand() {
        return leaderBHand;
    }

    public int getFaithBFaith() {
        return faithBFaith;
    }

    public boolean[] getFaithBPope() {
        return faithBPope;
    }

    public List<Shelf> getWarehouse() {
        return warehouse;
    }

    public List<Resource> getStrongbox() {
        return strongbox;
    }

    public boolean isFirstPlayer() {
        return inkwell;
    }

    @Override
    public void hide() {
        List<LeaderCard> newList = new ArrayList<>();
        leaderBHand.getCards().forEach(c -> newList.add(new LeaderCard()));
        leaderBHand = new Deck(newList);
    }

    @Override
    public void doAction(CLI client) {
        client.playerBoardSetup(this);
    }
}
