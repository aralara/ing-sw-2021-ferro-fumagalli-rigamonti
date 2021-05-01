package it.polimi.ingsw.server.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class VirtualView implements PropertyChangeListener {

    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt.getNewValue());  //TODO
    }
}
