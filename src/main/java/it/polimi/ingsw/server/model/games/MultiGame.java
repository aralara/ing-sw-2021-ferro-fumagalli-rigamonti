package it.polimi.ingsw.server.model.games;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.utils.TurnStatus;

import java.util.*;

public class MultiGame extends Game {

    private boolean lastTurn;
    private int currentPlayer;


    /**
     * Default constructor for MultiGame
     */
    public MultiGame() { }


    @Override
    public void initGame(List<String> players) {
        super.initGame(players);
        lastTurn = false;
    }

    @Override
    public int loadNextTurn() {
        int actionToDo = TurnStatus.LOAD_TURN_NORMAL.value();
        if(getPlayerBoards().get(currentPlayer).isTurnPlayed()) {
            getPlayerBoards().get(currentPlayer).setTurnPlayed(false);
            //Calculates the index for the next player
            currentPlayer = ++currentPlayer % getPlayerNumber();
            checkFaith();
            //Checks if the game is ending (if it's the last round)
            if (checkEndGame()) {
                if (lastTurn)
                    actionToDo = TurnStatus.LOAD_TURN_LAST_ROUND.value();
                lastTurn = true;
            }
            //Checks if the game has ended
            if (lastTurn && getPlayerBoards().get(currentPlayer).isFirstPlayer()) {
                //Updates the player boards with the results
                calculateTotalVP();
                calculateFinalPositions();
                setFinished(true);
                actionToDo = TurnStatus.LOAD_TURN_END_GAME.value();
            }
        }
        else
            actionToDo = TurnStatus.INVALID.value();
        return actionToDo;
    }

    @Override
    public void addFaithAll(int playerEx, int quantity) {
        List<PlayerBoard> playerBoards = getPlayerBoards();
        for(int i = 0; i < getPlayerNumber(); i++){
            if(i != playerEx)
                playerBoards.get(i).getFaithBoard().addFaith(quantity);
        }
    }

    @Override
    public String getPlayingNickname() {
        return getPlayerBoards().get(currentPlayer).getPlayer().getNickname();
    }
}
