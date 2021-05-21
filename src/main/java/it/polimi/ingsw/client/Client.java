package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.gui.GUIApplication;


public class Client {

    public static void main(String[] args) {

        //TODO: gestire il lancio di diverse tipologie di gioco

        if(args[0].equals("1")) {
            System.out.println("CLI version starting...");
            CLI cli = new CLI();
            cli.setup();
            cli.run();
            //System.exit(0); TODO: anche qua?
        }
        else if(args[0].equals("2")) {
            System.out.println("GUI version starting...");
            GUIApplication.main(null);
            System.exit(0);
        }
        else {
            System.out.println("Error in the choice of the game modality occurred!");
        }
    }
}