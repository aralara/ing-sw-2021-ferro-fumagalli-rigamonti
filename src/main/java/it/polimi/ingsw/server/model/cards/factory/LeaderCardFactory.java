package it.polimi.ingsw.server.model.cards.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import it.polimi.ingsw.server.model.cards.ability.*;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.requirement.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LeaderCardFactory implements CardFactory {

    public LeaderCardFactory() {
    }


    @Override
    public List<LeaderCard> loadCardFromFile(String fileName) {
        int i = 1;
        RuntimeTypeAdapterFactory<Requirement> requirementAdapterFactory
                = RuntimeTypeAdapterFactory.of(Requirement.class, "RequirementType");
        RuntimeTypeAdapterFactory<SpecialAbility> abilityAdapterFactory
                = RuntimeTypeAdapterFactory.of(SpecialAbility.class, "AbilityType");

        requirementAdapterFactory.registerSubtype(RequirementDev.class, "Development");
        requirementAdapterFactory.registerSubtype(RequirementRes.class, "Resource");
        abilityAdapterFactory.registerSubtype(AbilityDiscount.class, "Discount");
        abilityAdapterFactory.registerSubtype(AbilityWarehouse.class, "Warehouse");
        abilityAdapterFactory.registerSubtype(AbilityMarble.class, "Marble");
        abilityAdapterFactory.registerSubtype(AbilityProduction.class, "Production");

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(requirementAdapterFactory)
                .registerTypeAdapterFactory(abilityAdapterFactory)
                .create();

        List<LeaderCard> leadList = new ArrayList<>();

        try {
            LeaderCard[] jsonLeaderCard;
            JsonReader reader = new JsonReader(new FileReader(fileName));
            jsonLeaderCard = gson.fromJson(reader, LeaderCard[].class);

            for (LeaderCard leaderCard : jsonLeaderCard) {
                leadList.add(new LeaderCard(i, leaderCard.getVP(), leaderCard.getRequirements(), leaderCard.getAbility()));
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return leadList;
    }
}
