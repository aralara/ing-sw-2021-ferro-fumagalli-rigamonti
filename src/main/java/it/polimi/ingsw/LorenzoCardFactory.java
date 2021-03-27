package it.polimi.ingsw;

import java.util.List;

public interface LorenzoCardFactory extends CardFactory{

   @Override
   List<Card> loadCardFromFile(String fileName);
}
