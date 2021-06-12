package it.polimi.ingsw.server.model.boards;

import java.io.Serializable;

public class Player implements Serializable {

    private final String nickname;
    private int totalVP, finalPosition;


    /**
     * Player constructor given the nickname of a player
     * @param nickname Nickname of the player
     */
    public Player(String nickname) {
        this.nickname = nickname;
        totalVP = -1;
        finalPosition = 0;
    }


    /**
     * Gets the nickname attribute
     * @return Returns nickname value
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Gets the totalVp attribute
     * @return Returns totalVP amount
     */
    public int getTotalVP() {
        return totalVP;
    }

    /**
     * Sets the totalVP attribute
     * @param totalVP New attribute value
     */
    public void setTotalVP(int totalVP) {
        this.totalVP = totalVP;
    }

    /**
     * Gets the finalPosition attribute
     * @return Returns finalPosition value
     */
    public int getFinalPosition() {
        return finalPosition;
    }

    /**
     * Sets the finalPosition attribute
     * @param finalPosition New attribute value
     */
    public void setFinalPosition(int finalPosition) {
        this.finalPosition = finalPosition;
    }
}
