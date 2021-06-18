package it.polimi.ingsw.utils.exceptions;

/**
 * Exception thrown when a library hasn't been loaded
 */
public class LibraryNotLoadedException extends Exception{
    @Override
    public String getMessage() {
        return "Error! Game library hasn't been loaded";
    }
}
