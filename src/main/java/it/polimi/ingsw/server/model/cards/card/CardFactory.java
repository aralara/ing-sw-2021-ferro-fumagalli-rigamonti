package it.polimi.ingsw.server.model.cards.card;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.server.model.cards.ability.*;
import it.polimi.ingsw.server.model.cards.requirement.Requirement;
import it.polimi.ingsw.server.model.cards.requirement.RequirementDev;
import it.polimi.ingsw.server.model.cards.requirement.RequirementRes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Loads cards from file
 */
public class CardFactory {

    private static CardFactory cardFactory;

    /**
     * Default constructor for CardFactory
     */
    private CardFactory() { }

    /**
     * Gets singleton instance
     * @return Returns a unique CardFactory object
     */
    public static CardFactory getInstance() {
        if(cardFactory == null)
            cardFactory = new CardFactory();
        return cardFactory;
    }

    /**
     * Loads all the development cards present in a specified file
     * @param fileName Name of the file
     * @return Returns a list containing all the development cards
     */
    public List<DevelopmentCard> loadDevelopmentCardsFromFile(String fileName){
        Gson gson = new GsonBuilder().create();
        List<DevelopmentCard> developmentCardList = new ArrayList<>();

        DevelopmentCard[] jsonDevelopmentCard;
        JsonReader reader = new JsonReader(new InputStreamReader(this.getClass().getResourceAsStream(fileName)));
        jsonDevelopmentCard = gson.fromJson(reader, DevelopmentCard[].class);
        for (DevelopmentCard developmentCard : jsonDevelopmentCard)
            developmentCardList.add(new DevelopmentCard(developmentCard));
        return developmentCardList;
    }

    /**
     * Loads all the leader cards present in a specified file
     * @param fileName Name of the file
     * @return Returns a list containing all the leader cards
     */
    public List<LeaderCard> loadLeaderCardsFromFile(String fileName) {
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

        LeaderCard[] jsonLeaderCard;
        JsonReader reader = new JsonReader(new InputStreamReader(this.getClass().getResourceAsStream(fileName)));
        jsonLeaderCard = gson.fromJson(reader, LeaderCard[].class);
        for (LeaderCard leaderCard : jsonLeaderCard)
            leadList.add(new LeaderCard(leaderCard));

        return leadList;
    }

    /**
     * Loads all the LorenzoDev cards present in a specified file
     * @param fileName Name of the file
     * @return Returns a list containing all the LorenzoDev cards
     */
    public List<LorenzoDev> loadLorenzoDevCardsFromFile(String fileName) {
        Gson gson = new Gson();
        List<LorenzoDev> lorenzoDevDeck = new ArrayList<>();

        LorenzoDev[] jsonLorenzoDevs;
        JsonReader reader = new JsonReader(new InputStreamReader(this.getClass().getResourceAsStream(fileName)));
        jsonLorenzoDevs = gson.fromJson(reader, LorenzoDev[].class);
        for (LorenzoDev lorenzoDev : jsonLorenzoDevs)
            lorenzoDevDeck.add(new LorenzoDev(lorenzoDev));
        return lorenzoDevDeck;
    }

    /**
     * Loads all the LorenzoFaith cards present in a specified file
     * @param fileName Name of the file
     * @return Returns a list containing all the LorenzoFaith cards
     */
    public List<LorenzoFaith> loadLorenzoFaithCardsFromFile(String fileName) {
        Gson gson = new Gson();
        List<LorenzoFaith> lorenzoFaithDeck = new ArrayList<>();

        LorenzoFaith[] jsonLorenzoFaiths;
        JsonReader reader = new JsonReader(new InputStreamReader(this.getClass().getResourceAsStream(fileName)));
        jsonLorenzoFaiths = gson.fromJson(reader, LorenzoFaith[].class);
        for (LorenzoFaith lorenzoFaith : jsonLorenzoFaiths)
            lorenzoFaithDeck.add(new LorenzoFaith(lorenzoFaith));
        return lorenzoFaithDeck;
    }
}
