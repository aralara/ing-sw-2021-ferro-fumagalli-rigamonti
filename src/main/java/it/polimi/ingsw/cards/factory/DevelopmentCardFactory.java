package it.polimi.ingsw.cards.factory;

import it.polimi.ingsw.cards.card.Card;

import java.util.List;

public class DevelopmentCardFactory implements CardFactory {

    public DevelopmentCardFactory(){

    }


    @Override
    public List<Card> loadCardFromFile(String fileName){
        return null;
    }
}
