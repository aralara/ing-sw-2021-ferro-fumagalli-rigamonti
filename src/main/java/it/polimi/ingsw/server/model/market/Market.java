package it.polimi.ingsw.server.model.market;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.exceptions.InvalidColumnException;
import it.polimi.ingsw.exceptions.InvalidRowException;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.utils.listeners.Listened;
import it.polimi.ingsw.utils.listeners.Listeners;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.utils.Constants.MARKET_COLUMN_SIZE;
import static it.polimi.ingsw.utils.Constants.MARKET_ROW_SIZE;

public class Market extends Listened implements Serializable {

    private final Marble[][] marbleMatrix;
    private Marble floatingMarble;


    /**
     * Default constructor for Market that initializes the marble matrix with its default dimensions
     */
    public Market() {
        marbleMatrix = new Marble [MARKET_ROW_SIZE.value()][MARKET_COLUMN_SIZE.value()];
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
        for(int row = 0; row < MARKET_ROW_SIZE.value(); row++)
            for(int column = 0; column < MARKET_COLUMN_SIZE.value(); column++)
                marbleMatrix[row][column] = buffer.pop();
        floatingMarble = buffer.pop();
    }

    /**
     * Method invoked to take resources from a row/column of the marble matrix then calls moveFloatingMarble
     * @param row Row chosen by the player, if the player chose a column it is -1
     * @param column Column chosen by the player, if the player chose a row it is -1
     * @return Returns a list of resources corresponding to the marbles contained in the chosen matrix coordinates
     * @throws InvalidRowException when the selected row is out of the maximum ROWS value
     * @throws InvalidColumnException when the selected column is out of the maximum COLUMNS value
     */
    public List<Resource> chooseCoordinates(int row, int column) throws InvalidRowException, InvalidColumnException {
        List<Resource> marbleResources = new ArrayList<>();

        //Row chosen
        if(row >= 0) {
            if (row >= MARKET_ROW_SIZE.value()) throw new InvalidRowException();
            for(int resColumn = 0; resColumn < MARKET_COLUMN_SIZE.value(); resColumn++)
                marbleResources.add(new Resource(marbleMatrix[row][resColumn].getResourceType(), 1));
        }
        //Column chosen
        else {
            if (column >= MARKET_COLUMN_SIZE.value()) throw new InvalidColumnException();
            for(int resRow = 0; resRow < MARKET_ROW_SIZE.value(); resRow++) {
                marbleResources.add(new Resource(marbleMatrix[resRow][column].getResourceType(), 1));
            }
        }

        Storage.aggregateResources(marbleResources);

        moveFloatingMarble(row, column);
        fireUpdate(Listeners.GAME_MARKET.value(), this);
        return marbleResources;
    }

    /**
     * Takes the floating marble and moves at the end/bottom of the chosen row/column and shifts the other
     * marbles setting the marble at the start/top of the row/column as the new floatingMarble
     * @param row Row chosen by the player, if the player chose a column it is -1
     * @param column Column chosen by the player, if the player chose a row it is -1
     */
    @SuppressWarnings("ManualArrayCopy")
    private void moveFloatingMarble(int row, int column) {
        Marble newFloatingMarble;
        //Row chosen
        if(row >= 0){
            newFloatingMarble = marbleMatrix[row][0];
            for(int resColumn = 0; resColumn < MARKET_COLUMN_SIZE.value() - 1; resColumn++)
                marbleMatrix[row][resColumn] = marbleMatrix[row][resColumn + 1];
            marbleMatrix[row][MARKET_COLUMN_SIZE.value() - 1] = floatingMarble;
        }
        //Column chosen
        else {
            newFloatingMarble = marbleMatrix[0][column];
            for(int resRow = 0; resRow < MARKET_ROW_SIZE.value() - 1; resRow++)
                marbleMatrix[resRow][column] = marbleMatrix[resRow + 1][column];
            marbleMatrix[MARKET_ROW_SIZE.value() - 1][column] = floatingMarble;
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
