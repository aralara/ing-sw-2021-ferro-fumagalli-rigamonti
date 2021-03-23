package it.polimi.ingsw;

public class RequirementDev implements Requirement {

    private CardColors devColor;
    private int devLevel;
    private int number;


    public RequirementDev(){

    }


    public CardColors getDevColor(){
        return devColor;
    }

    public int getDevLevel(){
        return devLevel;
    }

    public int getNumber(){
        return number;
    }
}
