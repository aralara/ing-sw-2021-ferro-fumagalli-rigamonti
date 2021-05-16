package it.polimi.ingsw.exceptions;

public class UnknownMessageException extends Exception{
    @Override
    public String getMessage() {
        return "Error! An unknown message has been received";
    }
}
