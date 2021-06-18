package it.polimi.ingsw.utils.exceptions;

/**
 * Exception thrown when the nickname doesn't exist
 */
public class NotExistingNicknameException extends Exception{

    @Override
    public String getMessage() {
        return "Error! The specified nickname doesn't exist";
    }
}
