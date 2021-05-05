package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.boards.*;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.utils.messages.Message;

import java.util.List;

public class PlayerBoardSetupMessage implements Message {

    private String nickname;
    private List<Deck> developmentBSpaces;
    private Deck leaderBBoard;
    private Deck leaderBHand;
    private int faithBFaith;
    private boolean[] faithBPope;
    private List<Shelf> warehouse;
    private List<Resource> strongbox;
    private boolean inkwell;


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
}
