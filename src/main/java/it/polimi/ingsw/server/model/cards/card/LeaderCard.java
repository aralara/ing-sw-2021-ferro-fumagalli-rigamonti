package it.polimi.ingsw.server.model.cards.card;

import it.polimi.ingsw.server.model.cards.ability.SpecialAbility;
import it.polimi.ingsw.server.model.cards.requirement.Requirement;

import java.util.List;

public class LeaderCard implements Card {

    private final int VP, ID;
    private final List<Requirement> requirements;
    private final SpecialAbility ability;


    public LeaderCard(int ID, int VP, List<Requirement> requirements, SpecialAbility ability) {
        this.ID = ID;
        this.VP = VP;
        this.requirements = requirements;
        this.ability = ability;
    }

    /**
     * Gets the ID value
     * @return Returns ID
     */
    public int getID() {
        return ID;
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
