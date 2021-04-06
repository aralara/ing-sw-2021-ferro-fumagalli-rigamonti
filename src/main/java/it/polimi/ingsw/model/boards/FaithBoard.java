package it.polimi.ingsw.model.boards;

import it.polimi.ingsw.model.cards.card.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;

public class FaithBoard {
    private static final int popeProgressionSize = 3;

    private int faith;
    private boolean[] popeProgression;


    public FaithBoard() {
        faith = 0;
        popeProgression = new boolean[popeProgressionSize];
    }


    /**
     * Adds a set amount of faith to the current player
     * @param faith Faith quantity to be added
     */
    public void addFaith(int faith){
        this.faith += faith;
    }

    /**
     * Sets a Pope progression value based on the position of the faith marker
     * @param position Position of the Pope's favor tile to set
     * @param value Value to give to the Pope's favor tile
     */
    public void turnCard(int position, boolean value){
        popeProgression[position] = value;
    }

    /**
     * Gets the faith attribute
     * @return Returns faith
     */
    public int getFaith(){
        return this.faith;
    }

    /**
     * Gets the popeProgression attribute
     * @return Returns popeProgression
     */
    public boolean[] getPopeProgression(){
        return this.popeProgression;
    }

    /**
     * Calculates total VPs given by the faithBoard for a player
     * @param faithTrack FaithTrack utilized to calculate total VPs
     * @return Returns VP amount
     */
    public int calculateVP(FaithTrack faithTrack){
        return faithTrack.calculateVP(faith, popeProgression);
    }

    /**
     * Checks if a player completes his faith track
     * @return Returns true if it's completed, false otherwise
     */
    public boolean isCompleted(){
        return faith >= 24;
    }
}
