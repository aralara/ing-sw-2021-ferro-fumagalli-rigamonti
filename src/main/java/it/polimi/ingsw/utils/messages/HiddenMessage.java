package it.polimi.ingsw.utils.messages;

/**
 * Generic message that contains information that must be hidden to the player
 */
public interface HiddenMessage extends Message {
    void hide();
}
