package it.polimi.ingsw.utils.listeners.server;

/**
 * Player property that can be listened
 */
public class PlayerProperty {

    private final String nickname;
    private final Object property;


    /**
     * PlayerProperty constructor given a property and its player
     * @param nickname Nickname of the player who owns the property
     * @param property Property that can be listened
     */
    public PlayerProperty(String nickname, Object property) {
        this.nickname = nickname;
        this.property = property;
    }


    /**
     * Gets the nickname attribute
     * @return Returns nickname value
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Gets the property attribute
     * @return Returns property value
     */
    public Object getProperty() {
        return property;
    }
}
