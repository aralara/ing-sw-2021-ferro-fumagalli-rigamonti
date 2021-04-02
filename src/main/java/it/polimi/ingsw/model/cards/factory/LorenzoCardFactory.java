package it.polimi.ingsw.model.cards.factory;

import it.polimi.ingsw.model.cards.card.Card;

import java.util.List;

public interface LorenzoCardFactory extends CardFactory {

   @Override
   List<Card> loadCardFromFile(String fileName);
}
