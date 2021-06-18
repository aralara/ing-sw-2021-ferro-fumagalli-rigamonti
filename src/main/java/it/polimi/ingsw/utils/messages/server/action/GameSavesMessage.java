package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.saves.GameSave;

import java.util.List;

/**
 * Server message that offers the available saved games to the client
 */
public class GameSavesMessage implements ServerActionMessage {

    private final List<GameSave> saves;


    public GameSavesMessage(List<GameSave> saves) {
        this.saves = saves;
    }


    @Override
    public void doAction(ClientController client) {
        client.displaySaves(saves);
    }
}
