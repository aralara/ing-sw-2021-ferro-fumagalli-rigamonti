package it.polimi.ingsw.server.model.cards.factory;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DevelopmentCardFactory implements CardFactory {

    public DevelopmentCardFactory(){

    }


    @Override
    public List<DevelopmentCard> loadCardFromFile(String fileName){
        Gson gson = new Gson();
        List<DevelopmentCard> developmentCardList = new ArrayList<>();

        try {
            DevelopmentCard[] jsonDevelopmentCard;
            JsonReader reader = new JsonReader(new FileReader(fileName));
            jsonDevelopmentCard = gson.fromJson(reader, DevelopmentCard[].class);

            for (DevelopmentCard developmentCard : jsonDevelopmentCard) {
                developmentCardList.add(new DevelopmentCard(developmentCard.getVP(), developmentCard.getColor(),
                        developmentCard.getLevel(), developmentCard.getProduction(), developmentCard.getCost()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return developmentCardList;
    }
}
