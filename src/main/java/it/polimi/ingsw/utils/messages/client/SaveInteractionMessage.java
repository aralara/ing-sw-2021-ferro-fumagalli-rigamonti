package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.exceptions.LibraryNotLoadedException;
import it.polimi.ingsw.server.saves.GameLibrary;
import it.polimi.ingsw.server.saves.GameSave;
import it.polimi.ingsw.server.saves.SaveInteractions;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;
import it.polimi.ingsw.utils.messages.server.action.GameSavesMessage;

/**
 * Client message that operates on a saved game
 */
public class SaveInteractionMessage extends ClientActionMessage {

    private final GameSave save;
    private final SaveInteractions interaction;


    /**
     * Constructor for a SaveInteractionMessage give the save and an interaction
     * @param save Save subject of the interaction
     * @param interaction Interaction that will be applied to the save
     */
    public SaveInteractionMessage(GameSave save, SaveInteractions interaction) {
        this.save = save;
        this.interaction = interaction;
    }


    @Override
    public void doAction(VirtualView view) {
        try {
            switch (interaction) {
                case DELETE_SAVE:
                    GameLibrary gameLibrary = GameLibrary.getInstance();
                    boolean success = gameLibrary.deleteSave(save);
                    view.sendMessage(new ServerAckMessage(getUuid(), success));
                    view.sendMessage(
                            new GameSavesMessage(gameLibrary.getSaves(view.getGameHandler().getAllNicknames())));
                    break;
                case OPEN_SAVE:
                    view.sendMessage(new ServerAckMessage(getUuid(), true));
                    view.getGameHandler().startFromSave(save);
                    break;
                case NO_ACTION:
                    view.sendMessage(new ServerAckMessage(getUuid(), true));
                    view.getGameHandler().startNewGame();
            }
        } catch(LibraryNotLoadedException e) {
            view.sendMessage(new ServerAckMessage(getUuid(), false));
        }
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        String message = "";
        switch (interaction) {
            case DELETE_SAVE:
                message = "Save deleted successfully";
                break;
            case OPEN_SAVE:
                message = "Save loaded successfully";
                break;
            case NO_ACTION:
                message = "Proceeding to create a game";
        }
        client.ackNotification(message, true);
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.ackNotification("Unable to delete the selected save", true);
    }
}
