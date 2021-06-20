package it.polimi.ingsw.client.gui.controllers;

/**
 * Handles quitting game when a disconnection occurs
 */
public class ErrorController extends GenericController {

    /**
     * Quits the game closing the program
     */
    public void quit() {
        System.exit(0);
    }
}
