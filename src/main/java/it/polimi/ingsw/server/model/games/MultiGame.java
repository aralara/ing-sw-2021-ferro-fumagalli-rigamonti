package it.polimi.ingsw.server.model.games;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.TurnStatus;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class MultiGame extends Game {

    private boolean lastTurn;
    private int currentPlayer;


    public MultiGame() { }


    @Override
    public void initGame(List<VirtualView> views) {
        super.initGame(views);
        lastTurn = false;
    }

    @Override
    public String getPlayingNickname() {
        return getPlayerBoards().get(currentPlayer).getPlayer().getNickname();
    }
    
    @Override
    public Map<String, List<Resource>> getResourcesToEqualize() {    //TODO: hardcoded resources
        Map<String, List<Resource>> equalizeRes = new HashMap<>();
        List<List<Resource>> resources = new ArrayList<>();
        resources.add(new ArrayList<>());
        resources.add(Arrays.asList(new Resource(ResourceType.WILDCARD, 1)));
        resources.add(Arrays.asList(
                new Resource(ResourceType.WILDCARD, 1),
                new Resource(ResourceType.FAITH, 1)));
        resources.add(Arrays.asList(
                new Resource(ResourceType.WILDCARD, 2),
                new Resource(ResourceType.FAITH, 1)));
        for(int i = 0; i < getPlayerNumber(); i++)
            equalizeRes.put(getPlayerBoards().get(i).getPlayer().getNickname(), resources.get(i));
        return equalizeRes;
    }

    @Override
    public int loadNextTurn() {
        int actionToDo = TurnStatus.LOAD_TURN_NORMAL.value();
        if(getPlayerBoards().get(currentPlayer).isTurnPlayed()) {
            getPlayerBoards().get(currentPlayer).setTurnPlayed(false);
            currentPlayer = ++currentPlayer % getPlayerNumber();
            checkFaith();
            if (checkEndGame()) {
                if (lastTurn)
                    actionToDo = TurnStatus.LOAD_TURN_LAST_ROUND.value();
                lastTurn = true;
            }
            if (lastTurn && getPlayerBoards().get(currentPlayer).isFirstPlayer()) {
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
}
