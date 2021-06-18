package it.polimi.ingsw.utils.exceptions;

/**
 * Exception thrown when a column has an impossible value
 */
public class InvalidColumnException extends Exception{

    @Override
    public String getMessage() {
        return "Error! The specified column doesn't exist";
    }
}
