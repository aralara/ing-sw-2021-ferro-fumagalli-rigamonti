package it.polimi.ingsw;

import java.util.List;

public class LeaderCard implements Card{

    private int VP;
    private List<Requirement> req;
    private SpecialAbility ability;


    public LeaderCard(){

    }


    public int getVP(){
        return VP;
    }

    public List<Requirement> getReq(){
        return req;
    }

    public SpecialAbility getAbility(){
        return ability;
    }
}
