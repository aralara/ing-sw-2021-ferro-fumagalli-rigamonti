package it.polimi.ingsw.model.cards.factory;

import it.polimi.ingsw.model.cards.card.DevelopmentCard;

import java.util.List;

public class DevelopmentCardFactory implements CardFactory {

    public DevelopmentCardFactory(){

    }


    @Override
    public List<DevelopmentCard> loadCardFromFile(String fileName){
        return null;
    }
}
