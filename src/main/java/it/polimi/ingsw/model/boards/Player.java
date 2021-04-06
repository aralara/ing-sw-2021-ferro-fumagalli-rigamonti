package it.polimi.ingsw.model.boards;

public class Player {

    private String nickname;
    private int totalVp, finalPosition;


    public Player(String nickname){
        this.nickname = nickname;
        totalVp = 0;
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
     * @return Returns totalVp
     */
    public int getTotalVp(){
        return totalVp;
    }

    /**
     * Sets the totalVp attribute
     * @param totalVp New attribute value
     */
    public void setTotalVp(int totalVp){
        this.totalVp = totalVp;
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
