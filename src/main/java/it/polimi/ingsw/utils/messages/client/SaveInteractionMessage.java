package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.exceptions.LibraryNotLoadedException;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.games.Game;
import it.polimi.ingsw.server.saves.GameLibrary;
import it.polimi.ingsw.server.saves.GameSave;
import it.polimi.ingsw.server.saves.SaveInteractions;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

public class SaveInteractionMessage extends ClientActionMessage {

    private final GameSave save;
    private final SaveInteractions interaction;


    public SaveInteractionMessage(GameSave save, SaveInteractions interaction) {
        this.save = save;
        this.interaction = interaction;
    }


    @Override
    public void doAction(VirtualView view, Controller controller) {
        try {
            switch (interaction) {
                case DELETE_SAVE:
                    boolean success = GameLibrary.getInstance().deleteSave(save);
                    view.sendMessage(new ServerAckMessage(getUuid(), success));
                    break;
                case OPEN_SAVE:
                    view.sendMessage(new ServerAckMessage(getUuid(), true));
                    view.getGameHandler().startingSequenceFromSave(save);
                    break;
                case NO_ACTION:
                    view.sendMessage(new ServerAckMessage(getUuid(), true));
                    view.getGameHandler().startingSequenceNewGame();
            }
        } catch(LibraryNotLoadedException e) {
            view.sendMessage(new ServerAckMessage(getUuid(), false));
        }
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        switch (interaction) {
            case DELETE_SAVE:
                client.ackNotification("Save deleted successfully");
                break;
            case OPEN_SAVE:
                client.ackNotification("Save loaded successfully");
                break;
            case NO_ACTION:
                client.ackNotification("Proceeding to create a game");
        }
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.ackNotification("Unable to delete the selected save");
    }
}
