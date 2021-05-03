package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.boards.*;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.utils.messages.Message;

public class PlayerBoardSetupMessage implements Message {

    private Player player;
    private DevelopmentBoard developmentBoard;
    private LeaderBoard leaderBoard;
    private FaithBoard faithBoard;
    private Warehouse warehouse;
    private Strongbox strongbox;
    private boolean inkwell;


    public PlayerBoardSetupMessage(PlayerBoard playerBoard) {
        this.player = playerBoard.getPlayer();
        this.developmentBoard = playerBoard.getDevelopmentBoard();
        this.leaderBoard = playerBoard.getLeaderBoard();
        this.faithBoard = playerBoard.getFaithBoard();
        this.warehouse = playerBoard.getWarehouse();
        this.strongbox = playerBoard.getStrongbox();
        this.inkwell = playerBoard.isFirstPlayer();
    }


    public Player getPlayer() {
        return player;
    }

    public DevelopmentBoard getDevelopmentBoard() {
        return developmentBoard;
    }

    public LeaderBoard getLeaderBoard() {
        return leaderBoard;
    }

    public FaithBoard getFaithBoard() {
        return faithBoard;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public Strongbox getStrongbox() {
        return strongbox;
    }

    public boolean isFirstPlayer() {
        return inkwell;
    }
}
