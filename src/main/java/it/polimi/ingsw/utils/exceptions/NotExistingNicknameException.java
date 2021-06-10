package it.polimi.ingsw.utils.exceptions;

public class NotExistingNicknameException extends Exception{

    @Override
    public String getMessage() {
        return "Error! The specified nickname doesn't exist";
    }
}
