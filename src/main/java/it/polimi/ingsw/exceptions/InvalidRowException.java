package it.polimi.ingsw.exceptions;

public class InvalidRowException extends Exception{

    @Override
    public String getMessage() {
        return "Error! The specified row doesn't exist";
    }
}
