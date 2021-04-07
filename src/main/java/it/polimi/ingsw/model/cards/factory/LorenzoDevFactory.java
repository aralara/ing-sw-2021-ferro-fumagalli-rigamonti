package it.polimi.ingsw.model.cards.factory;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.cards.card.LorenzoDev;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LorenzoDevFactory implements CardFactory {

    public LorenzoDevFactory(){

    }


    @Override
    public List<LorenzoDev> loadCardFromFile(String fileName) {
        Gson gson = new Gson();
        List<LorenzoDev> lorenzoDevDeck = new ArrayList<>();

        try {
            LorenzoDev[] jsonLorenzoDevs;
            JsonReader reader = new JsonReader(new FileReader(fileName));
            jsonLorenzoDevs = gson.fromJson(reader, LorenzoDev[].class);

            for (LorenzoDev lorenzoDev : jsonLorenzoDevs) {
                lorenzoDevDeck.add(new LorenzoDev(lorenzoDev.getColor(), lorenzoDev.getQuantity()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return lorenzoDevDeck;
    }
}
