package it.polimi.ingsw.utils.listeners.server;

public class PlayerProperty {

    private final String nickname;
    private final Object property;


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
