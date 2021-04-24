package it.polimi.ingsw.server.model.cards.factory;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.server.model.cards.card.LorenzoFaith;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LorenzoFaithFactory implements CardFactory {

    public LorenzoFaithFactory(){

    }


    @Override
    public List<LorenzoFaith> loadCardFromFile(String fileName) {
        Gson gson = new Gson();
        List<LorenzoFaith> lorenzoFaithDeck = new ArrayList<>();

        try {
            LorenzoFaith[] jsonLorenzoFaiths;
            JsonReader reader = new JsonReader(new FileReader(fileName));
            jsonLorenzoFaiths = gson.fromJson(reader, LorenzoFaith[].class);

            for (LorenzoFaith lorenzoFaith : jsonLorenzoFaiths) {
                lorenzoFaithDeck.add(new LorenzoFaith(lorenzoFaith.isRefresh(), lorenzoFaith.getAmount()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return lorenzoFaithDeck;
    }
}
