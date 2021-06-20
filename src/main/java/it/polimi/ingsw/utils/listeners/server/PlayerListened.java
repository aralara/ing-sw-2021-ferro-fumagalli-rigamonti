package it.polimi.ingsw.utils.listeners.server;

import it.polimi.ingsw.utils.listeners.Listened;

/**
 * Listened class for a PlayerBoard object
 */
public abstract class PlayerListened extends Listened {

    private String playerNickname = "";

    /**
     * Sets the playerNickname attribute
     * @param playerNickname New attribute value
     */
    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    @Override
    public void fireUpdate(String propertyName, Object newValue) {
        super.fireUpdate(propertyName, new PlayerProperty(playerNickname, newValue));
    }
}
