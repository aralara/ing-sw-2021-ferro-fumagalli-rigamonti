package it.polimi.ingsw.utils.exceptions;

public class InvalidSpaceException extends Exception{

    @Override
    public String getMessage() {
        return "Error! The specified space doesn't exist";
    }
}
