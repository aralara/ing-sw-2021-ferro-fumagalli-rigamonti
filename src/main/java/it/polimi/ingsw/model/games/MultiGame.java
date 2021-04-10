package it.polimi.ingsw.model.games;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.storage.*;

import java.util.*;

public class MultiGame extends Game{

    private boolean lastTurn;   //TODO: lastTurn andrebbe spostato in Game


    public MultiGame(String ... players){
        initGame(players);
    }


    @Override
    public void initGame(String ... players){
        super.initGame(players);
        lastTurn = false;
        randomizeStartingPlayer();
    }

    /**
     * Shuffles the players and appoints the first one as the starting player
     */
    public void randomizeStartingPlayer(){
        //TODO: Questo metodo potrebbe essere private
        Collections.shuffle(getPlayerBoards());
        getPlayerBoards().get(0).firstPlayer();
    }

    /**
     * Gets a list that contains a list of resources (wildcards and fatih) for each player that need to be equalized
     * in the same playing order
     * @return Returns a list of lists of resources
     */
    public List<List<Resource>> getResourcesToEqualize(){
        List<List<Resource>> equalizeRes = new ArrayList<>();
        equalizeRes.add(null);
        equalizeRes.add(new ArrayList<>(){{
            add(new Resource(ResourceType.FAITH, 1));
        }});
        equalizeRes.add(new ArrayList<>(){{
            add(new Resource(ResourceType.WILDCARD, 1));
            add(new Resource(ResourceType.FAITH, 1));
        }});
        equalizeRes.add(new ArrayList<>(){{
            add(new Resource(ResourceType.WILDCARD, 2));
            add(new Resource(ResourceType.FAITH, 1));
        }});
        return equalizeRes;
    }

    @Override
    public void loadNextTurn(){
        checkFaith();
        if(checkEndGame()) {
            calculateTotalVP();
            calculateFinalPositions();
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
