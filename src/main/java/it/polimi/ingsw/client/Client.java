package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.GraphicalCLI;
import it.polimi.ingsw.client.gui.GUIApplication;

public class Client {

    public static void main(String[] args) {
        if(args[0].equals("1")) {

            GraphicalCLI.printlnString("Master of Renaissance: CLI version");

            do {
                CLI cli = new CLI();
                cli.setup();
                cli.run();
                GraphicalCLI.printString("Do you want to play again? ");
            } while(GraphicalCLI.isAnswerYes());

            System.exit(0);
        }
        else if(args[0].equals("2")) {

            GraphicalCLI.printlnString("Master of Renaissance: GUI version");

            GUIApplication.main(null);

            System.exit(0);
        }
        else
            System.out.println("Error in the choice of the game modality occurred!");
    }
}