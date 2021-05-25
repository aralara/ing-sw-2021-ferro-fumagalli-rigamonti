package it.polimi.ingsw.utils.listeners.server;

import it.polimi.ingsw.server.model.cards.card.LorenzoCard;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.action.LorenzoCardMessage;

import java.beans.PropertyChangeEvent;

public class LorenzoCardPlayListener extends ModelChangeListener {

    public LorenzoCardPlayListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getVirtualView().sendMessage(new LorenzoCardMessage((LorenzoCard) evt.getNewValue()));
    }
}
