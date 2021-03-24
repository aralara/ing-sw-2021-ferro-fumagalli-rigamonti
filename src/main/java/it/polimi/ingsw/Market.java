package it.polimi.ingsw;


import java.util.List;

public class Market {

    final public int ROWS = 3;
    final public int COLUMN = 4;

    private Marble[][] marbleMatrix;
    private Marble floatingMarble;


    public Market() {

    }


    /**
     * Loads marbles from the file given by parameter
     * @param fileName Path of the file that contains the information
     */
    public void loadMarket(String fileName){

    }

    /**
     * Randomizes the order of the marbles contained in the matrix and the floating marble
     */
    private void randomizeMarbles() {

    }

    /**
     * Method invoked to take resources from a row/column of the marble matrix then calls moveFloatingMarble
     * @param row Row chosen by the player, if the player chose a column it is -1
     * @param column Column chosen by the player, if the player chose a row it is -1
     * @return Returns a list of resources corresponding to the marbles contained in the chosen matrix coordinates
     */
    public List<Resource> chooseCoordinates(int row, int column) {
        return null;
    }

    /**
     * Takes the floating marble and moves at the end/bottom of the chosen row/column and shifts the other
     * marbles setting the marble at the start/top of the row/column as the new floatingMarble
     * @param row Row chosen by the player, if the player chose a column it is -1
     * @param column Column chosen by the player, if the player chose a row it is -1
     */
    public void moveFloatingMarble(int row, int column) {

    }

    /**
     * Gets a marble from the matrix at the given coordinates
     * @param row Row of the chosen marble
     * @param column Column of the chosen marble
     * @return Returns the requested Marble
     */
    public Marble getMarbleAt(int row, int column){
        return null;
    }

    /**
     * Gets the floatingMarble attribute
     * @return Returns floatingMarble
     */
    public Marble getFloatingMarble(){
        return floatingMarble;
    }
}
