package it.polimi.ingsw.model.market;

import it.polimi.ingsw.model.storage.Resource;
import it.polimi.ingsw.model.storage.ResourceType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Market {

    final public int ROWS = 3;
    final public int COLUMNS = 4;

    private Marble[][] marbleMatrix;
    private Marble floatingMarble;


    public Market() {
        marbleMatrix = new Marble [ROWS][COLUMNS];
        floatingMarble = null;
    }


    /**
     * Loads marbles from the file given by parameter
     * @param fileName Path of the file that contains the information
     */
    public void loadMarket(String fileName) {
        try {
            File marbleFile = new File(fileName);
            Scanner fileReader = new Scanner(marbleFile);
            while (fileReader.hasNextLine()) {
                System.out.println("DA CAMBIARE");
                //%%parsing
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //potrebbe chiamare il randomize creando il parametro da passare dal parsing del file
    }

    /**
     * Randomizes the order of the marbles contained in the matrix and the floating marble
     * @param buffer Stack of marbles to shuffle and get randomly them from
     */
    private void randomizeMarbles(Stack<Marble> buffer) {
        Collections.shuffle(buffer);
        for(int row=0; row<ROWS; row++)
            for(int column=0; column<COLUMNS; column++)
                marbleMatrix[row][column] = buffer.pop();
        floatingMarble = buffer.pop();
    }

    /**
     * Method invoked to take resources from a row/column of the marble matrix then calls moveFloatingMarble
     * @param row Row chosen by the player, if the player chose a column it is -1
     * @param column Column chosen by the player, if the player chose a row it is -1
     * @return Returns a list of resources corresponding to the marbles contained in the chosen matrix coordinates
     */
    public List<Resource> chooseCoordinates(int row, int column) {
        List<Resource> marbleResources = new ArrayList<>();
        int resTypeNum = ResourceType.values().length;
        int[] added = new int[resTypeNum]; //[coin qt, shield qt, servant qt, stone qt, faith qt, wildcard qt]

        if(row>=0){
            for(int resColumn=0; resColumn<COLUMNS; resColumn++) {
                added[marbleMatrix[row][resColumn].getResourceType().ordinal()]++;
            }
        }
        else {
            for(int resRow=0; resRow<ROWS; resRow++) {
                added[marbleMatrix[resRow][column].getResourceType().ordinal()]++;
            }
        }

        for(int i=0; i<resTypeNum; i++)
            if(added[i]>0) {
                //%%marbleResources.add(new Resource(ResourceType.values()[i], added[i]));
                System.out.println("DA CAMBIARE");
            }

        moveFloatingMarble(row, column);
        return marbleResources;
    }

    /**
     * Takes the floating marble and moves at the end/bottom of the chosen row/column and shifts the other
     * marbles setting the marble at the start/top of the row/column as the new floatingMarble
     * @param row Row chosen by the player, if the player chose a column it is -1
     * @param column Column chosen by the player, if the player chose a row it is -1
     */
    public void moveFloatingMarble(int row, int column) {
        Marble newFloatingMarble;

        if(row>=0){
            newFloatingMarble = marbleMatrix[row][0];
            for(int resColumn=0; resColumn<COLUMNS-1; resColumn++) {
                marbleMatrix[row][resColumn] = marbleMatrix[row][resColumn+1];
            }
            marbleMatrix[row][COLUMNS-1] = floatingMarble;
        }
        else {
            newFloatingMarble = marbleMatrix[0][column];
            for(int resRow=0; resRow<ROWS-1; resRow++) {
                marbleMatrix[resRow][column] = marbleMatrix[resRow+1][column];
            }
            marbleMatrix[ROWS-1][column] = floatingMarble;
        }

        floatingMarble = newFloatingMarble;
    }

    /**
     * Gets a marble from the matrix at the given coordinates
     * @param row Row of the chosen marble
     * @param column Column of the chosen marble
     * @return Returns the requested Marble
     */
    public Marble getMarbleAt(int row, int column) {
        return marbleMatrix[row][column];
    }

    /**
     * Gets the floatingMarble attribute
     * @return Returns floatingMarble
     */
    public Marble getFloatingMarble() {
        return floatingMarble;
    }
}
