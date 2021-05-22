package it.polimi.ingsw.exceptions;

public class LibraryNotLoadedException extends Exception{
    @Override
    public String getMessage() {
        return "Error! Game library hasn't been loaded";
    }
}
