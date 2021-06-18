package it.polimi.ingsw.utils.exceptions;

/**
 * Exception thrown when an unknown message has been received
 */
public class UnknownMessageException extends Exception{
    @Override
    public String getMessage() {
        return "Error! An unknown message has been received";
    }
}
