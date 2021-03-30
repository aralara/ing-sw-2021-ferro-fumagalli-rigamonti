package it.polimi.ingsw.cards.factory;

import it.polimi.ingsw.cards.card.Card;

import java.util.List;

public interface LorenzoCardFactory extends CardFactory {

   @Override
   List<Card> loadCardFromFile(String fileName);
}
