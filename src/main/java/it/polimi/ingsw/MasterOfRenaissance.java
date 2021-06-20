package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.cli.GraphicalCLI;
import it.polimi.ingsw.server.Server;

import java.util.List;

/**
 * The main class of Master of Renaissance
 */
public class MasterOfRenaissance {

    /**
     * Lets the user choose which version of Master of Renaissance start
     * @param args Unused command line arguments
     */
    public static void main(String[] args){

        List<String> versions = List.of("Server", "Client - CLI version", "Client - GUI version");

        int input = versions.indexOf(GraphicalCLI.objectOptionSelector(versions,
                GraphicalCLI::printlnString,
                () -> GraphicalCLI.printlnString("This is Master of Renaissance!"),
                () -> GraphicalCLI.printString("What version do you want to launch? "),
                null,
                false, -1 ,-1, -1).get(0));

        String[] arguments = new String[]{Integer.toString(input)};

        if (input == 0)
            Server.main(null);
        else
            Client.main(arguments);
    }
}
