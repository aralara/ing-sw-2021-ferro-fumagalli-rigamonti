package it.polimi.ingsw.utils.messages;

/**
 * Generic message that contains information that must be hidden to the player
 */
public interface HiddenMessage extends Message {

    /**
     * Hides some contents to the player, depending on the message
     */
    void hide();

}
