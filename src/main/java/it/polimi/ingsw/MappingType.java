package it.polimi.ingsw;

import java.util.Map;

public class MappingType {

    private Map<SpecialAbility,AbilityType> mapAbility;
    private Map<Requirement,RequirementType> mapRequirement;
    private Map<LorenzoCard,LorenzoType> mapLorenzo;


    public MappingType() {

    }


    /**
     * Adds an entry to the ability map
     * @param ability SpecialAbility representing the key of the entry
     * @param type AbilityType representing the value of the entry
     */
    public void addAbility(SpecialAbility ability, AbilityType type) {

    }

    /**
     * Adds an entry to the requirement map
     * @param requirement Requirement representing the key of the entry
     * @param type RequirementType representing the value of the entry
     */
    public void addRequirement(Requirement requirement, RequirementType type) {

    }

    /**
     * Adds an entry to the Lorenzo map
     * @param lorenzoCard LorenzoCard representing the key of the entry
     * @param type LorenzoType representing the value of the entry
     */
    public void addLorenzo(LorenzoCard lorenzoCard, LorenzoType type) {

    }

    /**
     * Gets an ability map entry value from the key
     * @param ability SpecialAbility representing the key of the entry
     * @return Returns the corresponding AbilityType representing the value of the entry
     */
    public AbilityType returnAbilityType(SpecialAbility ability) {
        return null;
    }

    /**
     * Gets a requirement map entry value from the key
     * @param requirement Requirement representing the key of the entry
     * @return Returns the corresponding RequirementType representing the value of the entry
     */
    public RequirementType returnRequirementType(Requirement requirement) {
        return null;
    }

    /**
     * Gets a Lorenzo map entry value from the key
     * @param lorenzoCard LorenzoCard representing the key of the entry
     * @return Returns the corresponding LorenzoType representing the value of the entry
     */
    public LorenzoType returnLorenzoType(LorenzoCard lorenzoCard) {
        return null;
    }
}
