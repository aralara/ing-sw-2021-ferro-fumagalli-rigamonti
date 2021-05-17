package it.polimi.ingsw.server.model.games;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.view.VirtualView;

import java.util.*;

public class MultiGame extends Game{

    private boolean lastTurn;
    private int currentPlayer;


    public MultiGame(){
    }


    @Override
    public void initGame(List<VirtualView> views){
        super.initGame(views);
        lastTurn = false;
    }

    @Override
    public String getPlayingNickname() {
        return getPlayerBoards().get(currentPlayer).getPlayer().getNickname();
    }

    /**
     * Gets a list that contains a list of resources (wildcards and fatih) for each player that need to be equalized
     * in the same playing order
     * @return Returns a map of lists of resources using the nickname of the player as a key
     */
    @SuppressWarnings("")
    public Map<String, List<Resource>> getResourcesToEqualize(){    //TODO: hardcoded resources
        Map<String, List<Resource>> equalizeRes = new HashMap<>();
        List<List<Resource>> resources = new ArrayList<>();
        resources.add(Arrays.asList(new Resource(ResourceType.WILDCARD, 1)));
        resources.add(Arrays.asList(
                new Resource(ResourceType.WILDCARD, 1),
                new Resource(ResourceType.FAITH, 1)));
        resources.add(Arrays.asList(
                new Resource(ResourceType.WILDCARD, 2),
                new Resource(ResourceType.FAITH, 1)));
        for(int i = 1; i < getPlayerNumber(); i++)
            equalizeRes.put(getPlayerBoards().get(i).getPlayer().getNickname(), resources.get(i - 1));
        return equalizeRes;
    }

    @Override
    public void loadNextTurn(){
        getPlayerBoards().get(currentPlayer).setTurnPlayed(false);
        currentPlayer = ++currentPlayer % getPlayerNumber();
        checkFaith();
        if(checkEndGame())
            lastTurn = true;
        if(lastTurn && getPlayerBoards().get(currentPlayer).isFirstPlayer()){
            calculateTotalVP();
            calculateFinalPositions();
            finished = true;
        }
    }

    @Override
    public void addFaithAll(int playerEx, int quantity){
        List<PlayerBoard> playerBoards = getPlayerBoards();
        for(int i = 0; i < getPlayerNumber(); i++){
            if(i != playerEx)
                playerBoards.get(i).getFaithBoard().addFaith(quantity);
        }
    }
}
