package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.GraphicalCLI;

public class NewPlayerMessage implements ServerActionMessage {

    private final String playerNickname;


    public NewPlayerMessage(String playerNickname) {
        this.playerNickname = playerNickname;
    }


    public String getPlayerNickname() {
        return playerNickname;
    }

    @Override
    public void doAction(ClientController client) {
        GraphicalCLI graphicalCLI = ((CLI) client).getGraphicalCLI();   //TODO: CAST A CLI ORRENDI, BRUTTI E ASSOLUTAMENTE TEMPORANEI IN TUTTO IL METODO
        if(!client.getNickname().equals(playerNickname)) {
            graphicalCLI.printString("The player " + playerNickname + " has joined the game!\n");
        }else{
            graphicalCLI.printString("You have been added to the game!\n");
        }
    }
}
