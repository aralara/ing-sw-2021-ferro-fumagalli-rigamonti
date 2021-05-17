package it.polimi.ingsw.server.model.boards;

import java.io.Serializable;

public class Player implements Serializable {

    private final String nickname;
    private int totalVP, finalPosition;


    public Player(String nickname){
        this.nickname = nickname;
        totalVP = 0;
        finalPosition = 0;
    }


    /**
     * Gets the nickname attribute
     * @return Returns nickname
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * Gets the totalVp attribute
     * @return Returns totalVP
     */
    public int getTotalVP(){
        return totalVP;
    }

    /**
     * Sets the totalVP attribute
     * @param totalVP New attribute value
     */
    public void setTotalVP(int totalVP){
        this.totalVP = totalVP;
    }

    /**
     * Gets the finalPosition attribute
     * @return Returns finalPosition
     */
    public int getFinalPosition(){
        return finalPosition;
    }

    /**
     * Sets the finalPosition attribute
     * @param finalPosition New attribute value
     */
    public void setFinalPosition(int finalPosition){
        this.finalPosition = finalPosition;
    }
}
