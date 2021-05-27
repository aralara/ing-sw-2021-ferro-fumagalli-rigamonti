package it.polimi.ingsw.server.model.cards.card;

import it.polimi.ingsw.server.model.cards.ability.SpecialAbility;
import it.polimi.ingsw.server.model.cards.requirement.Requirement;

import java.util.List;

public class LeaderCard extends Card {

    private final int VP;
    private final List<Requirement> requirements;
    private final SpecialAbility ability;


    /**
     * Default constructor for a LeaderCard having ID = -1 which is used to denote a hidden card
     */
    public LeaderCard() {
        setID(-1);
        this.VP = 0;
        this.requirements = null;
        this.ability = null;
    }

    /**
     * Constructor for a LeaderCard card
     * @param ID Unique ID reference for the card
     * @param VP Victory points given by the card
     * @param requirements Requirements to play the card
     * @param ability Ability of the card
     */
    public LeaderCard(int ID, int VP, List<Requirement> requirements, SpecialAbility ability) {
        setID(ID);
        this.VP = VP;
        this.requirements = requirements;
        this.ability = ability;
    }


    @Override
    public String cardToString(){
        StringBuilder toPrint;
        boolean first = true;
        if(getID() != -1) {
            toPrint = new StringBuilder(" LEADER CARD \n • Requirements: ");
            for (Requirement req : requirements) {
                toPrint.append((!first) ? ", " : "").append(req.requirementToString());
                first = false;
            }
            toPrint.append("\n • Victory points: ").append(VP).append("\n").append(ability.abilityToString());
        }
        else
            toPrint = new StringBuilder(" LEADER CARD \n• The leader card is covered, you can't see it!");
        return toPrint.toString();
    }

    /**
     * Gets the VP amount
     * @return Returns VP
     */
    public int getVP() {
        return VP;
    }

    /**
     * Gets the requirements attribute
     * @return Returns requirements value
     */
    public List<Requirement> getRequirements() {
        return requirements;
    }

    /**
     * Gets the ability attribute
     * @return Returns ability value
     */
    public SpecialAbility getAbility() {
        return ability;
    }
}
