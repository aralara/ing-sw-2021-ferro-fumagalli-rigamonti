package it.polimi.ingsw.exceptions;

public class NotExistingNickname extends Exception{

    @Override
    public String getMessage() {
        return "Error! The specified nickname doesn't exist";
    }
}
