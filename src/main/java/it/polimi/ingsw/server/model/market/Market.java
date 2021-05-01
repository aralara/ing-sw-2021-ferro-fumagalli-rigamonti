package it.polimi.ingsw.server.model.market;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.model.storage.Storage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Market {

    private static final int ROWS = 3;
    private static final int COLUMNS = 4;

    private final Marble[][] marbleMatrix;
    private Marble floatingMarble;


    public Market() {
        marbleMatrix = new Marble [ROWS][COLUMNS];
        floatingMarble = null;
    }


    /**
     * Loads marbles from the file given by parameter, then calls randomizeMarbles
     * @param fileName Path of the file that contains the information
     */
    public void loadMarket(String fileName) {
        Gson gson = new Gson();
        Stack<Marble> buffer = new Stack<>();

        try {
            Marble[] marbles;
            JsonReader reader = new JsonReader(new FileReader(fileName));
            marbles = gson.fromJson(reader, Marble[].class);

            for (Marble marble : marbles) {
                buffer.add(new Marble(marble.getColor(), marble.getResourceType()));
            }

            randomizeMarbles(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

        if(row>=0){
            for(int resColumn=0; resColumn<COLUMNS; resColumn++) {
                marbleResources.add(new Resource(marbleMatrix[row][resColumn].getResourceType(), 1));
            }
        }
        else {
            for(int resRow=0; resRow<ROWS; resRow++) {
                marbleResources.add(new Resource(marbleMatrix[resRow][column].getResourceType(), 1));
            }
        }

        Storage.aggregateResources(marbleResources);

        moveFloatingMarble(row, column);
        return marbleResources;
    }

    /**
     * Takes the floating marble and moves at the end/bottom of the chosen row/column and shifts the other
     * marbles setting the marble at the start/top of the row/column as the new floatingMarble
     * @param row Row chosen by the player, if the player chose a column it is -1
     * @param column Column chosen by the player, if the player chose a row it is -1
     */
    private void moveFloatingMarble(int row, int column) {
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
     * Gets the marbleMatrix attribute
     * @return Returns marbleMatrix
     */
    public Marble[][] getMarbleMatrix() {
        return marbleMatrix;
    }

    /**
     * Gets the floatingMarble attribute
     * @return Returns floatingMarble
     */
    public Marble getFloatingMarble() {
        return floatingMarble;
    }
}
