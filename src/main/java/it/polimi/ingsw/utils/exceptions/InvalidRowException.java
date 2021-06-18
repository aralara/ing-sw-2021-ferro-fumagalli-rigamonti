package it.polimi.ingsw.utils.exceptions;

/**
 * Exception thrown when a row has an impossible value
 */
public class InvalidRowException extends Exception{

    @Override
    public String getMessage() {
        return "Error! The specified row doesn't exist";
    }
}
