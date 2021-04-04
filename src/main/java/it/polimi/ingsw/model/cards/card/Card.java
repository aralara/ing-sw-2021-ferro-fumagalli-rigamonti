package it.polimi.ingsw.model.cards.card;

public interface Card extends Cloneable{

    /**
     * Method to return a cloned instance for a Card
     * @return Returns Card cloned object
     */
    Card makeClone();
}
