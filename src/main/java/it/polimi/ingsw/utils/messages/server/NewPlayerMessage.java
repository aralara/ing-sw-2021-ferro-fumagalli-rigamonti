package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.cli.CLI;

public class NewPlayerMessage implements ServerActionMessage {

    private final String playerNickname;


    public NewPlayerMessage(String playerNickname) {
        this.playerNickname = playerNickname;
    }


    public String getPlayerNickname() {
        return playerNickname;
    }

    @Override
    public void doAction(CLI client) {                                                  //TODO: print nel messaggio
        if(!client.getNickname().equals(playerNickname)) {
            System.out.println("The player " + playerNickname + " has joined the game!");
        }else{
            System.out.println("You have been added to the game!");
        }
    }
}
