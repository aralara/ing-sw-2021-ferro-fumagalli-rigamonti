package it.polimi.ingsw.model.games;

import it.polimi.ingsw.model.storage.*;

import java.util.List;

public class MultiGame extends Game{

    private boolean lastTurn;


    public MultiGame(){

    }


    /**
     * Gets the nickname of the player at a specified position
     * @param position Position of the player
     * @return Returns a String containing the nickname of the player
     */
    public String getPlayerNameAt(int position){
        return null;
    }

    /**
     * Sets the number of players that will play the game
     * @param playerNumber Number of players
     */
    public void setPlayerNumber(int playerNumber){

    }

    @Override
    public void initGame(){

    }

    /**
     * Chooses a random player that will start the game
     */
    public void randomizeStartingPlayer(){

    }

    /**
     * Gets a list that contains a list of resources (wildcards and fatih) for each player that need to be equalized
     * in the same playing order
     * @return Returns a list of lists of resources
     */
    public List<List<Resource>> getResourcesToEqualize(){
        return null;
    }

    @Override
    public void loadNextTurn(){

    }

    @Override
    public void addFaithAll(int quantity){}
}
