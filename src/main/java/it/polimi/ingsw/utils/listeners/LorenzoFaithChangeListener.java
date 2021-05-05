package it.polimi.ingsw.utils.listeners;

import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.LorenzoFaithMessage;

import java.beans.PropertyChangeEvent;

public class LorenzoFaithChangeListener extends ModelChangeListener {

    public LorenzoFaithChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getVirtualView().sendMessage(new LorenzoFaithMessage((int) evt.getNewValue()));
    }
}
