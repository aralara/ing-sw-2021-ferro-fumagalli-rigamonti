package it.polimi.ingsw.utils.exceptions;

public class InvalidColumnException extends Exception{

    @Override
    public String getMessage() {
        return "Error! The specified column doesn't exist";
    }
}
