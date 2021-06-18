package it.polimi.ingsw.utils.exceptions;

/**
 * Exception thrown when a space has an impossible value
 */
public class InvalidSpaceException extends Exception{

    @Override
    public String getMessage() {
        return "Error! The specified space doesn't exist";
    }
}
