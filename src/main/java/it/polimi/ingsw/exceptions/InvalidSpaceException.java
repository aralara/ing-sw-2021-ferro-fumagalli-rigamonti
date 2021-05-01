package it.polimi.ingsw.exceptions;

public class InvalidSpaceException extends Exception{

    @Override
    public String getMessage() {
        return "Error! The specified space doesn't exist";
    }
}
