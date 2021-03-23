package it.polimi.ingsw;

import java.util.List;

public class DevelopmentCard implements Card{

    private int VP;
    private CardColors color;
    private int level;
    private Production prod;
    private List<Resource> cost;


    public DevelopmentCard(){

    }


    public int getVP() {
        return VP;
    }

    public List<Resource> getCost(){
        return cost;
    }

    public int getLevel(){
        return level;
    }

    public CardColors getColor(){
        return color;
    }

}
