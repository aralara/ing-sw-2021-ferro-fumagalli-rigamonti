package it.polimi.ingsw.utils.listeners.server;

import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.update.LorenzoFaithMessage;

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
