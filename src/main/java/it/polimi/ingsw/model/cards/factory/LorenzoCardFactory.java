package it.polimi.ingsw.model.cards.factory;

import it.polimi.ingsw.model.cards.card.LorenzoCard;

import java.util.List;

public interface LorenzoCardFactory extends CardFactory {

   @Override
   List<? extends LorenzoCard> loadCardFromFile(String fileName);
}
