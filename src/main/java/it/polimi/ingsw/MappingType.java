package it.polimi.ingsw;

import java.util.Map;

public class MappingType {

    private Map<SpecialAbility,AbilityType> mapAbility;
    private Map<Requirement,RequirementType> mapRequirement;
    private Map<LorenzoCard,LorenzoType> mapLorenzo;


    public MappingType() {

    }


    public void addAbility(SpecialAbility ability, AbilityType type) {

    }

    public void addRequirement(Requirement requirement, RequirementType type){

    }

    public void addLorenzo(LorenzoCard lorenzoCard, LorenzoType type){

    }

    public AbilityType returnAbilityType(SpecialAbility ability){
        return null;
    }

    public RequirementType returnRequirementType(Requirement requirement){
        return null;
    }

    public LorenzoType returnLorenzoType(LorenzoCard lorenzoCard){
        return null;
    }
}
