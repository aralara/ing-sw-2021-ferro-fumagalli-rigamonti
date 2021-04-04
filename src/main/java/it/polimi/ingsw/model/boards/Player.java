package it.polimi.ingsw.model.boards;

public class Player {

    private String nickname;


    public Player(String nickname){
        this.nickname = nickname;
    }


    /**
     * Gets the nickname attribute
     * @return Returns nickname
     */
    public String getNickname(){
        return nickname;
    }
}
