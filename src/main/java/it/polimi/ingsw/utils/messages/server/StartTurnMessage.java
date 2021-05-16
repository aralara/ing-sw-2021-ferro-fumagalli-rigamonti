package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.GraphicalCLI;

public class StartTurnMessage implements ServerActionMessage {

    private final String playingNickname;


    public StartTurnMessage(String playingNickname) {
        this.playingNickname = playingNickname;
    }


    public String getPlayingNickname() {
        return playingNickname;
    }

    @Override
    public void doAction(ClientController client) {
        GraphicalCLI graphicalCLI = ((CLI) client).getGraphicalCLI();   //TODO: CAST A CLI ORRENDI, BRUTTI E ASSOLUTAMENTE TEMPORANEI IN TUTTO IL METODO
        if (playingNickname.equals(client.getNickname())) {
            graphicalCLI.printString("\nNOW IT'S YOUR TURN!\n\n");
            client.setMainActionPlayed(false);
            client.setPlayerTurn(true);
            ((CLI) client).turnMenu();
        }
        else {
            graphicalCLI.printString("Now is " + playingNickname + "'s turn\n");
            client.setPlayerTurn(false);
            ((CLI) client).turnMenu();
        }
    }
}
