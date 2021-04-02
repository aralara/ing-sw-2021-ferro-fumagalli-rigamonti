package it.polimi.ingsw.model.boards;

public class FaithBoard {

    private int faith;
    private boolean[] popeProgression;


    public FaithBoard() {
    }


    /**
     * Adds a set amount of faith to the current player
     * @param faith Faith quantity to be added
     */
    public void addFaith(int faith){

    }

    /**
     * Method invoked to set a Pope progression value based on the position of the faith marker
     * @param position Position of the Pope's favor tile to set
     * @param value Value to give to the Pope's favor tile
     */
    public void turnCard(int position, boolean value){

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
}
