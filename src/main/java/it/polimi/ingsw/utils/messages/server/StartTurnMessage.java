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
        //TODO: serve una stringa che inserita in qualsiasi modo ci faccia tornare indietro
        // (es. se provo a comprare una carta ma non ho risorse se no si blocca il gioco)

        if (playingNickname.equals(client.getNickname())) {
            graphicalCLI.printString("\nNOW IT'S YOUR TURN!\n\n");
            client.setMainActionPlayed(false);
            ((CLI) client).turnMenu(true);
        }
        else {
            graphicalCLI.printString("Now is " + playingNickname + "'s turn\n");
            ((CLI) client).turnMenu(false);
        }
    }
}
