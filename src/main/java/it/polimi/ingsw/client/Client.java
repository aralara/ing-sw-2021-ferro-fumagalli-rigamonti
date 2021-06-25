package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.GraphicalCLI;
import it.polimi.ingsw.client.gui.GUIApplication;

/**
 * Handles client version to be started
 */
public class Client {

    /**
     * Calls CLI or GUI according to the given parameter: 1 to start CLI, 2 to start GUI
     * @param args List of arguments
     */
    public static void main(String[] args) {
        if(args[0].equals("1")) {
            GraphicalCLI.printlnString("Master of Renaissance: CLI version");
            CLI cli = new CLI();
            cli.setup();
            cli.run();
            System.exit(0);
        }
        else if(args[0].equals("2")) {
            GraphicalCLI.printlnString("Master of Renaissance: GUI version");
            GUIApplication.main(null);
            System.exit(0);
        }
        else
            GraphicalCLI.printlnString("Client version not supported");
    }
}