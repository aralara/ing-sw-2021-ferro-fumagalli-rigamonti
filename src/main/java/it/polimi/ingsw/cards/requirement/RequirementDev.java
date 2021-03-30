package it.polimi.ingsw.cards.requirement;

import it.polimi.ingsw.cards.card.CardColors;

public class RequirementDev implements Requirement {

    private CardColors devColor;
    private int devLevel;
    private int number;


    public RequirementDev(){

    }


    /**
     * Gets the devColor attribute
     * @return Returns devColor value
     */
    public CardColors getDevColor() {
        return devColor;
    }

    /**
     * Gets the devLevel attribute
     * @return Returns devLevel value
     */
    public int getDevLevel() {
        return devLevel;
    }

    /**
     * Gets the number attribute
     * @return Returns number value
     */
    public int getNumber() {
        return number;
    }
}
